package net.mcon.citnow.line.app.model;

import lombok.Data;

@Data
public class VideoInfo {
	/**
	 * Unique LINE ID of Channel
	 */
	private String channelId;
	/**
	 * The mobile phone of the end user
	 */
	private String userPhone;
	/**
	 * The type of the video which value can be either SALES or AFTERSALES
	 */
	private VideoType type;
	/**
	 * The link of video page on CitNOW website
	 */
	private String videoUrl;
	/**
	 * The preview of the image used in LINE message
	 */
	private String picUrl;
	/**
	 * A short description about the video
	 */
	private String summary;
	/**
	 * Detailed information about the video
	 */
	private String description;


	public static enum VideoType {
		SALES, AFTERSALES;
	}
}
