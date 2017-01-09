package com.umeng.soexample;

import android.app.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


/**
 * 首页
 */
public class HomeActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.umeng_blue));

        }
        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.umeng_title)).setText(R.string.umeng_home_title);
        findViewById(R.id.share).setOnClickListener(this);
        findViewById(R.id.auth).setOnClickListener(this);
        findViewById(R.id.getinfo).setOnClickListener(this);
        findViewById(R.id.check).setOnClickListener(this);
        findViewById(R.id.umeng_menu).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //分享
            case R.id.share:
                Intent shareintent = new Intent(HomeActivity.this,ShareActivity.class);
                startActivity(shareintent);
                break;
            //授权
            case R.id.auth:
                Intent authintent = new Intent(HomeActivity.this,AuthActivity.class);
                startActivity(authintent);
                break;
            //获取用户信息
            case R.id.getinfo:
                Intent getintent = new Intent(HomeActivity.this,GetInfoActivity.class);
                startActivity(getintent);
                break;
            //自检工具
            case R.id.check:
                Intent checkintent = new Intent(HomeActivity.this,CheckActivity.class);
                startActivity(checkintent);
                break;
            //分享菜单模板
            case R.id.umeng_menu:
                Intent menuintent = new Intent(HomeActivity.this,ShareMenuActivity.class);
                startActivity(menuintent);
                break;
        }
    }
}
