package com.example.project1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import static com.example.project1.ScreenshotType.FULL;

public class MantopDress extends Activity implements View.OnTouchListener, View.OnClickListener{
    //share 기능 추가
    private Button fullPageScreenshot,Urlbutton;
    private LinearLayout rootContent;

    /*
    안드로이드에서 확대, 축소, 드래그를 사용하기 위한 변수들을 설정한다.
     */
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

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
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mantop_dress);

        ImageView view = (ImageView) findViewById(R.id.mantopimageView1);
        Intent intent = getIntent(); //인텐트를 설정한다.
        view.setImageResource(intent.getIntExtra("img", 0));

        view.setOnTouchListener(this); //이미지뷰 클릭 리스너를 설정한다.


        //추가
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
        findViews();
        implementClickEvents();
    }

    private void findViews() {
        fullPageScreenshot = (Button) findViewById(R.id.full_page_screenshot);
        Urlbutton = (Button)findViewById(R.id.urlbutton1);
        rootContent = (LinearLayout) findViewById(R.id.root_content);
    }

    private void implementClickEvents() {
        fullPageScreenshot.setOnClickListener((View.OnClickListener) this);
    }

    public void onClick(View v) {
        if(v.getId() == R.id.full_page_screenshot){
            takeScreenshot(FULL);
        }
    }
/*
스크린샷을 찍는 역할
 */
    private void takeScreenshot(ScreenshotType screenshotType) {
        Bitmap b = null;
        if(screenshotType == FULL){
            b = ScreenshotUtils.getScreenShot(rootContent);
        }

        if (b != null) { //비트맵이 null값이 아닐 경우에 해당됨
            File saveFile = ScreenshotUtils.getMainDirectoryName(this);
            File file = ScreenshotUtils.store(b, "screenshot" + screenshotType + ".jpg", saveFile);
            shareScreenshot(file);
        } else //비트맵이 null값인 경우
            Toast.makeText(this, R.string.screenshot_take_failed, Toast.LENGTH_SHORT).show();
    }
//공유시에는 인텐트를 사용하여 다른 앱으로 데이터를 보낼 수 있다.
    private void shareScreenshot(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(); //인텐트 객체 생성
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*"); //타입을 지정
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri); //다른 앱으로 데이터 보내기
        startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
    }

    public void urlbutton1(View view){ //사이트에 연결되는 코드이다.
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://store.musinsa.com/"));
        startActivity(intent);
    }
/*
터치 이벤트를 지정한다.
 */
    public boolean onTouch(View v, MotionEvent event) {
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
                if (mode == DRAG) { //드래그 구현
                    matrix.set(savedMatrix);
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;
                    matrix.postTranslate(dx, dy);
                } else if (mode == ZOOM) { //확대, 축소 구현
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

        view.setImageMatrix(matrix);
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

