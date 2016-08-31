package com.yh;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * 需要返回值
 * 
 * @author gary
 * 
 */
public class GameSoundMedia {
	public static final int GAME_SOUND_MEDIA_COMPLETION = 1;
	private Context mContext;

	public GameSoundMedia(Context context) {
		mContext = context;
	}

	private List<MediaPlayer> mList = new ArrayList<MediaPlayer>();

	public void stopAll() {
		for (MediaPlayer i : mList) {
			if (i != null) {
				try {
					if (i.isPlaying()) {
						i.stop();
						i.release();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
		}
		mList.clear();
	}

	public MediaPlayer playInMediaPlayer(int resid, final GameSoundEvent event) {
		MediaPlayer player = MediaPlayer.create(mContext, resid);
		player.start();
		player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (event != null)
					event.onEvent(GAME_SOUND_MEDIA_COMPLETION);
				mp.release();
				mList.remove(mp);
			}
		});
		mList.add(player);
		return player;
	}

	public interface GameSoundEvent {
		public void onEvent(int what);
	}
}
