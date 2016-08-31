package com.zhangjiajing.music.interfaces;

import com.zhangjiajing.music.model.MusicInfo;

import java.util.List;

public interface IQueryFinished {
	
	public void onFinished(List<MusicInfo> list);

}
