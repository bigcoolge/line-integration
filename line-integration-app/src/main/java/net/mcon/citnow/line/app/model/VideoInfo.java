package net.mcon.citnow.line.app.model;

import lombok.Data;

@Data
public class VideoInfo {
	private String channelId;
	private String userPhone;

	private String videoUrl;
	private String picUrl;
	private VideoType type;
	private String summary;
	private String description;


	public static enum VideoType {
		SALES, AFTERSALES;
	}
}
