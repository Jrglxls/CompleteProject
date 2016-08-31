package com.zhangjiajing.music.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhangjiajing.music.R;
import com.zhangjiajing.music.model.LyricSentence;

import java.util.ArrayList;
import java.util.List;

/**
 * 歌词适配
 */
public class LyricAdapter extends BaseAdapter {
	private static final String TAG = LyricAdapter.class.getSimpleName();

	/** 歌词句子集合 */
	List<LyricSentence> mLyricSentences = null;

	Context mContext = null;

	/** 当前的句子索引号 */
	int mIndexOfCurrentSentence = 0;

	float mCurrentSize = 27;
	float mNotCurrentSize = 17;

	public LyricAdapter(Context context) {
		mContext = context;
		mLyricSentences = new ArrayList<LyricSentence>();
		mIndexOfCurrentSentence = 0;
	}

	/** 设置歌词，由外部调用， */
	public void setLyric(List<LyricSentence> lyric) {
		mLyricSentences.clear();
		if (lyric != null) {
			mLyricSentences.addAll(lyric);
		}
		mIndexOfCurrentSentence = 0;
	}

	/**歌词为空时，让ListView显示EmptyView */
	@Override
	public boolean isEmpty() {
		if (mLyricSentences == null) {
			Log.i(TAG, "isEmpty:null");
			return true;
		} else if (mLyricSentences.size() == 0) {
			Log.i(TAG, "isEmpty:size=0");
			return true;
		} else {
			Log.i(TAG, "isEmpty:not empty");
			return false;
		}
	}

	/** 禁止在列表条目上点击 */
	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public int getCount() {
		return mLyricSentences.size();
	}

	@Override
	public Object getItem(int position) {
		return mLyricSentences.get(position).getContentText();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 获取歌词视图
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.lyric_line, null);
			holder.lyric_line = (TextView) convertView
					.findViewById(R.id.lyric_line_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position >= 0 && position < mLyricSentences.size()) {
			holder.lyric_line.setText(mLyricSentences.get(position)
					.getContentText());
		}
		if (mIndexOfCurrentSentence == position) {
			// 当前播放到的句子设置为蓝色，字体大小更大
			holder.lyric_line.setTextColor(mContext.getResources().getColor(R.color.holo_orange_dark));
			holder.lyric_line.setTextSize(mCurrentSize);
			holder.lyric_line.setTypeface(Typeface.DEFAULT_BOLD);
		} else {
			// 其他的句子设置为暗色，字体大小较小
			holder.lyric_line.setTextColor(mContext.getResources().getColor(R.color.trans_white));
			holder.lyric_line.setTextSize(mNotCurrentSize);
		}
		return convertView;
	}

	public void setCurrentSentenceIndex(int index) {
		mIndexOfCurrentSentence = index;
	}

	static class ViewHolder {
		TextView lyric_line;
	}
}
