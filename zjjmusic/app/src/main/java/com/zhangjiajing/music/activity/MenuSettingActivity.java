package com.zhangjiajing.music.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.zhangjiajing.music.R;
import com.zhangjiajing.music.fragment.LeftFragment;
import com.zhangjiajing.music.fragment.RightFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单设置
 */
public class MenuSettingActivity extends FragmentActivity {
	
	public ViewPager mViewPager;//ViewPager是android扩展包v4包中的类，这个类可以让用户左右切换当前的view。
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.menu_setting);//设置视图menu_setting
		mViewPager = (ViewPager) findViewById(R.id.viewPager);
		initViewPager();
	}

	/**
	 * 初始化viewpager
	 */
	private void initViewPager() {
		Fragment leftFragment = new LeftFragment();//左fragment
		Fragment rightFragment = new RightFragment();//右fragment

		mFragmentList.add(leftFragment);
		mFragmentList.add(rightFragment);

		mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragmentList));//viewpager适配
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());//viewpager事件监听
		mViewPager.setCurrentItem(1, true);
	}

	/**
	 * viewpager适配
	 */
	private class MyPagerAdapter extends FragmentPagerAdapter {

		List<Fragment> fragmentList;

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragmentList = fragments;
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}

	}

	/**
	 * viewpager事件监听
	 */
	private class MyOnPageChangeListener implements OnPageChangeListener {

		int onPageScrolled = -1;

		// 当滑动状态改变时调用
		@Override
		public void onPageScrollStateChanged(int arg0) {
			if ((onPageScrolled == 0 || onPageScrolled == 2) && arg0 == 0) {
				finish();
			}
		}

		// 当当前页面被滑动时调用
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			onPageScrolled = arg0;
		}

		// 当新的页面被选中时调用
		@Override
		public void onPageSelected(int arg0) {
		}
	}

	/**
	 * 返回键
	 */
	@Override
	public void onBackPressed() {
		if(mViewPager.isShown()) {
			mViewPager.setCurrentItem(0, true);
		}
	}
}
