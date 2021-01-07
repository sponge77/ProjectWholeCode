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
import java.nio.channels.AcceptPendingException;

import static com.example.project1.ScreenshotType.FULL;

public class HatDress extends Activity implements View.OnTouchListener,View.OnClickListener{
    private Button fullPageScreenshot,Urlbutton;
    private LinearLayout rootContent;

    // these matrices will be used to move and zoom image
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    // remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hat_dress);

        //View oParentView = (View)v.getParent(); // 부모의 View를 가져온다. 즉, 아이템 View임.
        //ImageView image = (ImageView) oParentView.findViewById(R.id.imageView1);
        //ImageView iv = (ImageView)findViewById(R.id.imageView1);



        ImageView view = (ImageView) findViewById(R.id.hatimageView1);

        Intent intent = getIntent();
        view.setImageResource(intent.getIntExtra("img", 0));

        view.setOnTouchListener(this);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
        findViews();
        implementClickEvents();
    }

    private void findViews() {
        fullPageScreenshot = (Button) findViewById(R.id.full_page_screenshot5);
        Urlbutton = (Button)findViewById(R.id.urlbutton5);
        //customPageScreenshot = (Button) findViewById(R.id.custom_page_screenshot);
        rootContent = (LinearLayout) findViewById(R.id.root_content5);
        //imageView = (ImageView) findViewById(R.id.mantopimageView1);

    }

    /*  Implement Click events over Buttons */
    private void implementClickEvents() {
        fullPageScreenshot.setOnClickListener((View.OnClickListener) this);
        //customPageScreenshot.setOnClickListener(this);
    }


    public void onClick(View v) {
        if(v.getId() == R.id.full_page_screenshot5){
            takeScreenshot(FULL);
        }

    }


    /*  Method which will take screenshot on Basis of Screenshot Type ENUM  */
    private void takeScreenshot(ScreenshotType screenshotType) {
        Bitmap b = null;
        if(screenshotType == FULL){
            b = ScreenshotUtils.getScreenShot(rootContent);
        }

        //If bitmap is not null
        if (b != null) {
            //showScreenShotImage(b);//show bitmap over imageview
            File saveFile = ScreenshotUtils.getMainDirectoryName(this);//get the path to save screenshot
            File file = ScreenshotUtils.store(b, "screenshot" + screenshotType + ".jpg", saveFile);//save the screenshot to selected path
            shareScreenshot(file);//finally share screenshot
        } else
            //If bitmap is null show toast message
            Toast.makeText(this, R.string.screenshot_take_failed, Toast.LENGTH_SHORT).show();
    }
    /*  Show screenshot Bitmap */
    /*private void showScreenShotImage(Bitmap b) {
        view.setImageBitmap(b);
    }*/
    /*  Share Screenshot  */
    private void shareScreenshot(File file) {
        Uri uri = Uri.fromFile(file);//Convert file path into Uri for sharing
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        //intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.sharing_text));
        intent.putExtra(Intent.EXTRA_STREAM, uri);//pass uri here
        startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
    }

    public void urlbutton5(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://store.musinsa.com/"));
        startActivity(intent);
    }


    public boolean onTouch(View v, MotionEvent event) {
        // handle touch events here
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

    /**
     * Determine the space between the first two fingers
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }
}

