package com.example.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

public class ScreenshotUtils {
    //스크린샷을 찍는다.
    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true); //뷰가 업데이트 될 때마다 그 때의 뷰 이미지를 Drawing cache에 저장할지 여부를 결정해주는 역할을 한다.
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }


    public static File getMainDirectoryName(Context context) {
        File mainDir = new File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Demo");
        if (!mainDir.exists()) { //manDir가 존재하지 않는 경우
            if (mainDir.mkdir()) //새 디렉토리를 만든다.
                Log.e("Create Directory", "Main Directory Created : " + mainDir);
        }
        return mainDir;
    }


    public static File store(Bitmap bm, String fileName, File saveFilePath) {
        File dir = new File(saveFilePath.getAbsolutePath());
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(saveFilePath.getAbsolutePath(), fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);  //비트맵을 압축한다.
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;

    }
}