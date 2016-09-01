package jianke.jkanalytics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import jianke.library.JKAnalytics;

/**
 * 注释测试代码
 */
public class MainActivity extends AppCompatActivity {
     private JKAnalytics jkAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jkAnalytics = JKAnalytics.getInstance();

        jkAnalytics.startWithAppkey("ZJJappkey", this);
        jkAnalytics.getUserIdAndUserFlag("ZJJuserId", "ZJJuserFlag");
        jkAnalytics.beginLogPageView();
        jkAnalytics.paramsInfo("ZJJsomething");
    }

    public void doClick(View view) {
        Intent intent = new Intent(this,OtherActivity.class);
        startActivity(intent);
        jkAnalytics.endLogPageView();
        jkAnalytics.event("ZJJeventId", "ZJJpageId", this);
    }
}
