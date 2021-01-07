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

import java.io.File;

import static com.example.project1.ScreenshotType.FULL;

public class ManbottomsDress extends Activity implements View.OnTouchListener,View.OnClickListener {

    private Button fullPageScreenshot,Urlbutton;
    private LinearLayout rootContent;
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
        setContentView(R.layout.manbottoms_dress);



        ImageView view = (ImageView) findViewById(R.id.manbottomsimageView1);

        Intent intent = getIntent();
        view.setImageResource(intent.getIntExtra("img", 0));

        view.setOnTouchListener(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
        findViews();
        implementClickEvents();


    }

    private void findViews() {
        fullPageScreenshot = (Button) findViewById(R.id.full_page_screenshot1);
        Urlbutton = (Button)findViewById(R.id.urlbutton3);
        rootContent = (LinearLayout) findViewById(R.id.root_content1);
    }

    private void implementClickEvents() {
        fullPageScreenshot.setOnClickListener((View.OnClickListener) this);
    }


    public void onClick(View v) {
        if(v.getId() == R.id.full_page_screenshot1){
            takeScreenshot(FULL);
        }
    }


    private void takeScreenshot(ScreenshotType screenshotType) {
        Bitmap b = null;
        if(screenshotType == FULL){
            b = ScreenshotUtils.getScreenShot(rootContent);
        }


        if (b != null) {

            File saveFile = ScreenshotUtils.getMainDirectoryName(this);
            File file = ScreenshotUtils.store(b, "screenshot" + screenshotType + ".jpg", saveFile);
            shareScreenshot(file);
        } else
            Toast.makeText(this, R.string.screenshot_take_failed, Toast.LENGTH_SHORT).show();
    }

    private void shareScreenshot(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
    }

    public void urlbutton3(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://store.musinsa.com/"));
        startActivity(intent);
    }


    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
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
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
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

        view.setImageMatrix(matrix);
        return true;
    }


    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

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
