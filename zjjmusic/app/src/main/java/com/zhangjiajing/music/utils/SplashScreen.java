package com.zhangjiajing.music.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.zhangjiajing.music.R;

/**
 * 引导页图片，停留若干秒，然后自动消失。
 */
public class SplashScreen {
    
    public final static int SLIDE_LEFT = 1;
    public final static int SLIDE_UP = 2;
    public final static int FADE_OUT = 3;
    
    private Dialog splashDialog;
    
    private Activity activity;
    
    public SplashScreen(Activity activity){
        this.activity = activity;
    }
    
    /**
     * 显示。
     * @param imageResource 图片资源
     * @param animation 消失时的动画效果，取值可以是：SplashScreen.SLIDE_LEFT, SplashScreen.SLIDE_UP, SplashScreen.FADE
     */
    public void show(final int imageResource, final int animation){
        Runnable runnable = new Runnable() {
            public void run() {
                // 获取参考，以显示
            	DisplayMetrics metrics = new DisplayMetrics();
                // 创建对话框布局
                LinearLayout root = new LinearLayout(activity);
                root.setMinimumHeight(metrics.heightPixels);
                root.setMinimumWidth(metrics.widthPixels);
                root.setOrientation(LinearLayout.VERTICAL);
                root.setBackgroundColor(Color.BLACK);
                root.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT, 0.0F));
                root.setBackgroundResource(imageResource);

                // 创建和显示对话框
                splashDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
                // 检查是否启动画面应为全屏
                if ((activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
                    splashDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                
                Window window = splashDialog.getWindow();
                switch(animation){
                    case SLIDE_LEFT:
                        window.setWindowAnimations(R.style.dialog_anim_slide_left);
                        break;
                    case SLIDE_UP:
                        window.setWindowAnimations(R.style.dialog_anim_slide_up);
                        break;
                    case FADE_OUT:
                        window.setWindowAnimations(R.style.dialog_anim_fade_out);
                        break;
                }
            
                splashDialog.setContentView(root);
                splashDialog.setCancelable(false);
                splashDialog.show();
            }
        };
        activity.runOnUiThread(runnable);
    }
    
    public void removeSplashScreen(){
        if (splashDialog != null && splashDialog.isShowing()) {
            splashDialog.dismiss();
            splashDialog = null;
        }
    }

}
