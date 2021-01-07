package com.example.project1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;


public class Test extends AppCompatActivity {

    // opencv 사용하기 위해 라이브러리 로드
    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }

    // opencv 사용하는 cpp 함수
    public native void image_processing(long img_inputNativeObjAddr, long img_input2NativeObjAddr, long img_input3NativeObjAddr, long img_input4NativeObjAddr, long img_outputNativeObjAddr, long addr, long objAddr, long nativeObjAddr, long inputImage, long inputImage2, long outputImage, long grayNativeObjAddr);


    ImageView fullbody, hatimage, upperimage, bottomimage, result;
    private Mat img_input;  // 전신
    private Mat img_input2; // upper
    private Mat img_input3; // bottom
    private Mat img_input4; // hat
    private Mat img_output;
    private Mat mask, mask_inv, res, res2, dst, roi, gray;
    private static final String TAG = "opencv";
    private final int IMAGE_FROM_GALLERY1 = 200;
    private final int IMAGE_FROM_UPPER_LIST = 400;
    private final int IMAGE_FROM_BOTTOM_LIST = 600;
    private final int IMAGE_FROM_HAT_LIST = 800;
    boolean isReady = false;
    private Intent intent;
    private ImageView iv;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        fullbody = (ImageView) findViewById(R.id.fullbody);
        hatimage = (ImageView) findViewById(R.id.hat_image);
        upperimage = (ImageView) findViewById(R.id.upper_image);
        bottomimage = (ImageView) findViewById(R.id.bottom_image);
        result = (ImageView) findViewById(R.id.result);


        // 버튼을 누르면 합성된 이미지 결과가 나온다.
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                show_result();
            }
        });



        /* 전신사진, 상의, 하의 사진 가져오기*/

        // 전신사진은 갤러리에서 가져옴
        fullbody.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent, IMAGE_FROM_GALLERY1);

            }
        });

        /* startActivityForResult를 사용하면, 카테고리 페이지로부터 정보를 넘겨받을 수 있다.
        * 아이콘 클릭 시 카테고리 페이지로 이동 */
        hatimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "모자 카테고리입니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), HatCategory.class);
                startActivityForResult(intent, IMAGE_FROM_HAT_LIST);
            }
        });

        upperimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "상의 카테고리입니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UpperCategory.class);
                startActivityForResult(intent, IMAGE_FROM_UPPER_LIST);
            }
        });

        bottomimage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "하의 카테고리입니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), BottomCategory.class);
                startActivityForResult(intent, IMAGE_FROM_BOTTOM_LIST);

            }
        });

        /* 새로고침 */
        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        /* 옷장버튼 누르면 옷장으로 이동 */
        findViewById(R.id.btn_closet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyClosetIntro.class);
                Toast.makeText(getApplicationContext(), "옷장으로 이동합니다.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        isReady = true;
    }

    private void show_result() {

        if (isReady == false) return;

        /* 합성에 필요한 변수들 */
        mask = new Mat();
        mask_inv = new Mat();
        roi = new Mat();
        res = new Mat();
        res2 = new Mat();
        dst = new Mat();
        gray = new Mat();
        if (img_output == null)
            img_output = new Mat();

        image_processing(img_input.getNativeObjAddr(), img_input2.getNativeObjAddr(), img_input3.getNativeObjAddr(),img_input4.getNativeObjAddr(),img_output.getNativeObjAddr(), mask.getNativeObjAddr(),
                mask_inv.getNativeObjAddr(), res.getNativeObjAddr(), res2.getNativeObjAddr(), roi.getNativeObjAddr(), dst.getNativeObjAddr(), gray.getNativeObjAddr());

        Bitmap bitmapOutput = Bitmap.createBitmap(img_output.cols(), img_output.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img_output, bitmapOutput);
        result.setImageBitmap(bitmapOutput);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_FROM_GALLERY1) {
            if (data.getData() != null) {
                Uri uri = data.getData();

                try {
                    String path = getRealPathFromURI(uri);
                    int orientation = getOrientationOfImage(path);
                    Bitmap temp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    Bitmap bitmap = getRotatedBitmap(temp, orientation);
                    fullbody.setImageBitmap(bitmap);

                    img_input = new Mat();
                    Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    Utils.bitmapToMat(bmp32, img_input);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

        if (requestCode == IMAGE_FROM_UPPER_LIST) {
            if(resultCode == Activity.RESULT_OK) {

                /* Extra에 담긴 정보를 가져오고 upperimage에 세팅 */
                upperimage.setImageResource(data.getIntExtra("imgRes",0));
                BitmapDrawable drawble = (BitmapDrawable)upperimage.getDrawable();
                Bitmap TT = drawble.getBitmap();

                try {
                    /* OpenCV로 합성하기 위해서는 비트맵 파일이어야 하므로 비트맵 설정 */
                    img_input2 = new Mat();
                    Bitmap bmp32 = TT.copy(Bitmap.Config.ARGB_8888, true);
                    Utils.bitmapToMat(bmp32, img_input2);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        if (requestCode == IMAGE_FROM_BOTTOM_LIST) {
            if(resultCode == Activity.RESULT_OK) {

                /* Extra에 담긴 정보를 가져오고 bottomimage에 세팅 */
                    bottomimage.setImageResource(data.getIntExtra("imgRes2",0));
                    BitmapDrawable drawble = (BitmapDrawable)bottomimage.getDrawable();
                    Bitmap TT = drawble.getBitmap();
//                    bottomimage.setImageBitmap(TT);

                    try {

                        /* OpenCV로 합성하기 위해서는 비트맵 파일이어야 하므로 비트맵 설정 */
                        img_input3 = new Mat();
                        Bitmap bmp32 = TT.copy(Bitmap.Config.ARGB_8888, true);
                        Utils.bitmapToMat(bmp32, img_input3);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }



            }
        }



        if (requestCode == IMAGE_FROM_HAT_LIST) {
            if(resultCode == Activity.RESULT_OK) {

                /* Extra에 담긴 정보를 가져오고 hatimage에 세팅 */
                hatimage.setImageResource(data.getIntExtra("imgRes3",0));
                BitmapDrawable drawble = (BitmapDrawable)hatimage.getDrawable();
                Bitmap TT = drawble.getBitmap();

                try {

                    /* OpenCV로 합성하기 위해서는 비트맵 파일이어야 하므로 비트맵 설정 */
                    img_input4 = new Mat();
                    Bitmap bmp32 = TT.copy(Bitmap.Config.ARGB_8888, true);
                    Utils.bitmapToMat(bmp32, img_input4);


                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        }

    }


    private String getRealPathFromURI(Uri contentUri) {

        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        return cursor.getString(column_index);
    }

    public int getOrientationOfImage(String filepath) {
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (IOException e) {
            Log.d("@@@", e.toString());
            return -1;
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

        if (orientation != -1) {
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }
        }

        return 0;
    }

    public Bitmap getRotatedBitmap(Bitmap bitmap, int degrees) throws Exception {
        if (bitmap == null) return null;
        if (degrees == 0) return bitmap;
        Matrix m = new Matrix();
        m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }


}
