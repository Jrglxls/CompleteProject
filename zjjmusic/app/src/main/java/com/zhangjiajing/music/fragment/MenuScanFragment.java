package com.zhangjiajing.music.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.zhangjiajing.music.R;
import com.zhangjiajing.music.interfaces.IConstants;
import com.zhangjiajing.music.activity.MenuSlideActivity;
import com.zhangjiajing.music.db.DatabaseHelper;
import com.zhangjiajing.music.utils.MusicUtils;

/**
 *扫描歌曲
 */
public class MenuScanFragment extends Fragment implements IConstants, OnClickListener {

	private Button mScanBtn;//开始扫描按钮
	private ImageButton mBackBtn;//返回按钮
	private Handler mHandler;
	private DatabaseHelper mHelper;//数据
	private ProgressDialog mProgress;//弹出框
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHelper = new DatabaseHelper(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_scan_fragment, container, false);//设置视图menu_scan_fragment

		mScanBtn = (Button) view.findViewById(R.id.scanBtn);//开始扫描按钮
		mBackBtn = (ImageButton) view.findViewById(R.id.backBtn);//返回按钮
		mScanBtn.setOnClickListener(this);
		mBackBtn.setOnClickListener(this);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				mProgress.dismiss();
				((MenuSlideActivity)getActivity()).mViewPager.setCurrentItem(0, true);
			}
		};

		return view;
	}

	/**
	 * 获得数据
	 */
	private void getData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				mHelper.deleteTables(getActivity());
				MusicUtils.queryMusic(getActivity(), START_FROM_LOCAL);
				MusicUtils.queryAlbums(getActivity());
				MusicUtils.queryArtist(getActivity());
				MusicUtils.queryFolder(getActivity());
				mHandler.sendEmptyMessage(1);
			}
		}).start();
	}

	/**
	 *点击扫描或退出
	 */
	@Override
	public void onClick(View v) {
		if(v == mScanBtn) {
			mProgress = new ProgressDialog(getActivity());
			mProgress.setMessage("正在扫描歌曲，请勿退出软件！");
			mProgress.setCancelable(false);
			mProgress.setCanceledOnTouchOutside(false);
			mProgress.show();
			getData();
		} else if(v == mBackBtn) {
			((MenuSlideActivity)getActivity()).mViewPager.setCurrentItem(0, true);
		}
	}
}
