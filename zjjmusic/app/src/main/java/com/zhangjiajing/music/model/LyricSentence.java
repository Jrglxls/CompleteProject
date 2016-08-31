package com.zhangjiajing.music.model;

/**
 * 歌词句子，是一个时间戳和一行歌词组成
 */
public class LyricSentence {

	/** 歌詞文本的开始时间戳转换为毫秒数的值 */
	private long startTime = 0;

	/**一句歌词的实现*/
	private long duringTime = 0;

	/** 每个时间戳对应的一行歌词文本 */
	private String contentText = "";

	public LyricSentence(long time, String text) {
		startTime = time;
		contentText = text;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public String getContentText() {
		return contentText;
	}

	public void setContentText(String contentText) {
		this.contentText = contentText;
	}

	public long getDuringTime() {
		return duringTime;
	}

	public void setDuringTime(long duringTime) {
		this.duringTime = duringTime;
	}
}
