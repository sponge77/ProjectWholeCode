package com.example.project1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.project1.ScreenshotType.FULL;

public class Camera extends Activity implements View.OnTouchListener, View.OnClickListener {
    private Button fullPageScreenshot,button4;
    private FrameLayout rootContent;
    private ImageView imageView;

    /*
    안드로이드에서 확대, 축소, 드래그를 사용하기 위한 변수들을 설정한다.
     */
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    //3개의 상태가 가능하다.
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;

    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;

    private int PICK_IMAGE_REQUEST = 1;


    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cameramain);

        findViewById(R.id.take).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //클릭 리스너 이벤트를 설정한다.
                sendTakePhotoIntent(); //적힌 메서드를 실행한다.
            }
        });

        Button openimg = (Button)findViewById(R.id.uploadbutton2);
        openimg.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){ //클릭 리스너 이벤트를 설정한다.
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        ImageView img = (ImageView) findViewById(R.id.photo2);
        img.setOnTouchListener(this);

        //추가
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
        findViews();
        implementClickEvents();
    }

    //추가
    private void findViews() {
        fullPageScreenshot = (Button) findViewById(R.id.share3);
        button4 = (Button)findViewById(R.id.take);
        rootContent = (FrameLayout) findViewById(R.id.root_content3);
        imageView = (ImageView) findViewById(R.id.photo);

    }

    private void implementClickEvents() {
        fullPageScreenshot.setOnClickListener((View.OnClickListener) this);
    }

    public void onClick(View v) {
        if(v.getId() == R.id.share3){
            takeScreenshot(FULL);
        }
    }

    //스크린샷을 찍는다.
    private void takeScreenshot(ScreenshotType screenshotType) {
        Bitmap b = null;
        if(screenshotType == FULL){
            b = ScreenshotUtils.getScreenShot(rootContent);
        }

        if (b != null) { //비트맵이 null값이 아닐 경우에
            File saveFile = ScreenshotUtils.getMainDirectoryName(this);//스크린샷을 저장하기 위한 경로 설정
            File file = ScreenshotUtils.store(b, "screenshot" + screenshotType + ".jpg", saveFile);//선택된 경로에 저장한다.
            shareScreenshot(file);
        } else //비트맵이 null값일 경우에
            Toast.makeText(this, R.string.screenshot_take_failed, Toast.LENGTH_SHORT).show(); //토스트 메시지 띄우기
    }

    //공유하기 기능
    private void shareScreenshot(File file) {
        Uri uri = Uri.fromFile(file);//Convert file path into Uri for sharing
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);//pass uri here
        startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
    }

    //여기까지 추가함
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int exifOrientation;
            int exifDegree;
            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }
            ((ImageView)findViewById(R.id.photo)).setImageBitmap(rotate(bitmap, exifDegree));
        }

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageView = (ImageView) findViewById(R.id.photo2);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//사진 회전 각도를 다루는 코드
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
//사진을 비트맵 형태로 회전시키는 코드
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

//사진을 인텐트로 내보내는 코드
    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

//드래그, 확대, 축소 구현 부분
    /*
    드래그, 확대, 축소 구현한 부분
    MotionEvent 클래스에는 터치 관련 상수들이 정의되어 있다.
    여러 동작들을 구분해 터치 이벤트를 실행시킨다.
     */
    public boolean onTouch(View v, MotionEvent event) { //터치 이벤트를 작성하였다.
        ImageView view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: //처음 눌렸을 때의 동작을 의미한다.
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN: //멀티 터치
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP: //누른 것을 뗐을 때를 의미한다.
            case MotionEvent.ACTION_POINTER_UP: //멀티 터치
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE: //누르고 움직였을 때를 의미한다.
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;
                    matrix.postTranslate(dx, dy);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = (newDist / oldDist);
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                    if (lastEvent != null && event.getPointerCount() == 3) {
                        newRot = rotation(event);
                        float r = newRot - d;
                        float[] values = new float[9];
                        matrix.getValues(values);
                        float tx = values[2];
                        float ty = values[5];
                        float sx = values[0];
                        float xc = (view.getWidth() / 2) * sx;
                        float yc = (view.getHeight() / 2) * sx;
                        matrix.postRotate(r, tx + xc, ty + yc);
                    }
                }
                break;
        }
        view.setImageMatrix(matrix); //scaleType을 matrix로 설정하는 것이 중요하다
        return true;
    }

    //두 손가락 사이의 공간을 계산해서 리턴한다.
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }
    //두 손가락 사이의 midPoint를 계산한다.
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }
}