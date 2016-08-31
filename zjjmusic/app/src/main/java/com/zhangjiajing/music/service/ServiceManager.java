package com.zhangjiajing.music.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;

import com.zhangjiajing.music.aidl.IMediaService;
import com.zhangjiajing.music.interfaces.IConstants;
import com.zhangjiajing.music.interfaces.IOnServiceConnectComplete;
import com.zhangjiajing.music.model.MusicInfo;

import java.util.List;

/**
 * 控制Service
 */
public class ServiceManager implements IConstants {

	public IMediaService mService;
	private Context mContext;
	private ServiceConnection mConn;
	private IOnServiceConnectComplete mIOnServiceConnectComplete;

	public ServiceManager(Context context) {
		this.mContext = context;
		initConn();
	}

	private void initConn() {
		mConn = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				mService = IMediaService.Stub.asInterface(service);
				if (mService != null) {
					mIOnServiceConnectComplete.onServiceConnectComplete(mService);
				}
			}
		};
	}

	public void connectService() {
		Intent intent = new Intent("com.ldw.music.service.MediaService");
		mContext.bindService(intent, mConn, Context.BIND_AUTO_CREATE);
	}
	
	public void disConnectService() {
		mContext.unbindService(mConn);
		mContext.stopService(new Intent("com.ldw.music.service.MediaService"));
	}
	
	public void refreshMusicList(List<MusicInfo> musicList) {
		if(musicList != null && mService != null) {
			try {
				mService.refreshMusicList(musicList);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
//	public List<MusicInfo> getMusicList() {
//		List<MusicInfo> musicList = new ArrayList<MusicInfo>();
//		try {
//			if(mService != null) {
//				mService.getMusicList(musicList);
//			}
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
//		return musicList;
//	}

	/**
	 * 播放
	 * @param id
	 * @return
	 */
	public boolean playById(int id) {
		if(mService != null) {
			try {
				return mService.playById(id);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 继续播放
	 * @return
	 */
	public boolean rePlay() {
		if(mService != null) {
			try {
				return mService.rePlay();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 暂停
	 * @return
	 */
	public boolean pause() {
		if(mService != null) {
			try {
				return mService.pause();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 上一首
	 * @return
	 */
	public boolean prev() {
		if(mService != null) {
			try {
				return mService.prev();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 下一首
	 * @return
	 */
	public boolean next() {
		if(mService != null) {
			try {
				return mService.next();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 改变进度
	 * @param progress
	 * @return
	 */
	public boolean seekTo(int progress) {
		if(mService != null) {
			try {
				return mService.seekTo(progress);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 位置
	 * @return
	 */
	public int position() {
		if(mService != null) {
			try {
				return mService.position();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 时长
	 * @return
	 */
	public int duration() {
		if(mService != null) {
			try {
				return mService.duration();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 获取播放状态
	 * @return
	 */
	public int getPlayState() {
		if(mService != null) {
			try {
				int mode = mService.getPlayState();
				return mService.getPlayState();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 设置循环模式
	 * @param mode
	 */
	public void setPlayMode(int mode) {
		if(mService != null) {
			try {
				mService.setPlayMode(mode);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取循环模式
	 * @return
	 */
	public int getPlayMode() {
		if(mService != null) {
			try {
				return mService.getPlayMode();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	public int getCurMusicId() {
		if(mService != null) {
			try {
				return mService.getCurMusicId();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	public MusicInfo getCurMusic() {
		if(mService != null) {
			try {
				return mService.getCurMusic();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void exit() {
		if(mService != null) {
			try {
				mService.exit();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		mContext.unbindService(mConn);
		mContext.stopService(new Intent(SERVICE_NAME));
	}
	
	public void updateNotification(Bitmap bitmap, String title, String name) {
		try {
			mService.updateNotification(bitmap, title, name);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void cancelNotification() {
		try {
			mService.cancelNotification();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void setOnServiceConnectComplete(
			IOnServiceConnectComplete IServiceConnect) {
		mIOnServiceConnectComplete = IServiceConnect;
	}

}
