package com.zhangjiajing.music.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhangjiajing.music.MusicApp;
import com.zhangjiajing.music.R;
import com.zhangjiajing.music.interfaces.IConstants;
import com.zhangjiajing.music.activity.MainContentActivity;
import com.zhangjiajing.music.activity.MenuSlideActivity;
import com.zhangjiajing.music.service.ServiceManager;
import com.zhangjiajing.music.slidemenu.SlidingMenu.OnOpenedListener;

/**
 * 侧滑Menu
 * 该类提供了软件的设置，歌曲的控制等几大功能
 */
public class MenuSlideFragment extends Fragment implements OnClickListener,
		IConstants, OnOpenedListener {

	private TextView mMediaCountTv;
	private TextView mScanTv, mPlayModeTv, mSleepTv, mExitTv;
	private MainContentActivity mMainActivity;

	private int mCurMode;
	private ServiceManager mServiceManager;
	private static final String modeName[] = { "列表循环", "顺序播放", "随机播放", "单曲循环" };
	private int modeDrawable[] = { R.drawable.icon_list_reapeat,
								   R.drawable.icon_sequence,
								   R.drawable.icon_shuffle,
			    			 	   R.drawable.icon_single_repeat };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.frame_menu1, container, false);
		initView(view);
		mServiceManager = MusicApp.mServiceManager;

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mMainActivity = (MainContentActivity) getActivity();
		mMainActivity.mSlidingMenu.setOnOpenedListener(this);
	}

	private void initView(View view) {
		mMediaCountTv = (TextView) view.findViewById(R.id.txt_media_count);
		mScanTv = (TextView) view.findViewById(R.id.txt_scan);
		mPlayModeTv = (TextView) view.findViewById(R.id.txt_play_mode);
		mSleepTv = (TextView) view.findViewById(R.id.txt_sleep);
		mExitTv = (TextView) view.findViewById(R.id.txt_exit);

		mMediaCountTv.setOnClickListener(this);
		mScanTv.setOnClickListener(this);
		mPlayModeTv.setOnClickListener(this);
		mSleepTv.setOnClickListener(this);
		mExitTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_scan:
			startActivityForResult(new Intent(getActivity(), MenuSlideActivity.class), 1);
			break;
		case R.id.txt_play_mode://循环模式
			changeMode();
			break;
		case R.id.txt_sleep:
			mMainActivity.showSleepDialog();
			break;
		case R.id.txt_exit:
			getActivity().finish();
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1) {
			//刷新主页内容
			((MainContentActivity)getActivity()).mMainFragment.refreshNum();
		}
	}

	/**
	 * 切换循环模式
	 */
	private void changeMode() {
		mCurMode++;
		if (mCurMode > MPM_SINGLE_LOOP_PLAY) {
			mCurMode = MPM_LIST_LOOP_PLAY;
		}
		mServiceManager.setPlayMode(mCurMode);
		initPlayMode();
	}

	/**
	 * 初始化播放模式
	 */
	private void initPlayMode() {
		mPlayModeTv.setText(modeName[mCurMode]);//更换循环模式名
		Drawable drawable = getResources().getDrawable(modeDrawable[mCurMode]);//更换循环模式图片
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		mPlayModeTv.setCompoundDrawables(drawable, null, null, null);
	}

	@Override
	public void onOpened() {
		mCurMode = mServiceManager.getPlayMode();
		initPlayMode();
	}
}
