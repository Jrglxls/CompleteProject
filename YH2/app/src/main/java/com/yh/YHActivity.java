package com.yh;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;

/**
 * Happy Birthday
 * @author pc
 *
 */
public class YHActivity extends Activity   {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//创建SurfaceView对象
		SurfaceView v = new SurfaceView(this);
		HolderSurfaceView.getInstance().setSurfaceView(v);
		//设置背景图
		v.setBackgroundResource(R.drawable.bg);
		this.setContentView(v);

		DrawYH yh=new DrawYH();
		v.setOnTouchListener(yh);
		yh.begin();
	}
	
}