package com.yh;

import android.graphics.PixelFormat;
import android.os.Build;
import android.view.SurfaceView;

/**
 * 对surfaceView进行一些设置 这是一个单例
 * 
 * @author gary
 * 
 */
public class HolderSurfaceView {
	private HolderSurfaceView() {

	}

	private static HolderSurfaceView mHolderSurfaceView = null;

	public static HolderSurfaceView getInstance() {
		if (mHolderSurfaceView == null)
			mHolderSurfaceView = new HolderSurfaceView();
		return mHolderSurfaceView;
	}

	private SurfaceView mSurfaceView;

	/**
	 * 设置SurfaceView
	 * 
	 * @param view
	 */
	public void setSurfaceView(SurfaceView view) {
		mSurfaceView = view;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			mSurfaceView.setZOrderOnTop(true);
		}
		mSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
	}

	public SurfaceView getSurfaceView() {
		return mSurfaceView;
	}
}
