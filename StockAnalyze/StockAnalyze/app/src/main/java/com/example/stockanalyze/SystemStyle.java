package com.example.stockanalyze;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SystemStyle {

    /**
     * 設定status bar 顏色，所有頁面
     * */
    @SuppressLint("ResourceAsColor")
    public void setTitleBarColor(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(R.color.background));
    }

    /**
     * 設定status bar 顏色，我的組合
     * */
    @SuppressLint("ResourceAsColor")
    public void setMyTitleBarColor(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(R.color.myBackground));
    }

    /**
     * 設定status bar 文字顏色-亮
     * */
    public void systemTextLight(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    /**
     * 設定status bar 文字顏色-暗
     * */
    public void systemTextDark(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public int Width;
    public int Height;
    public float Density;

    public void ScreenInfo(int h, int w, float density) {
        this.Width = w;
        this.Height = h;
        this.Density = density;
    }

    /**
     * 取得螢幕大小
     * */
    public void getScreenSize(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int screenHeight = dm.heightPixels;
        int screenWidth = dm.widthPixels;
        float density = dm.density;

        pxToDp(screenHeight, screenWidth, density);


    }

    private void pxToDp(int h, int w, float density) {
        ScreenInfo((int) (h / density),
                (int) (w / density),
                density);
    }

    public int getWidth() {
        return (int) (Width * Density);
    }

    public int getHeight() {
        return (int) (Height * Density);
    }
}
