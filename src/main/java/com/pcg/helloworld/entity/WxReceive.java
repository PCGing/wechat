package com.pcg.helloworld.entity;

import lombok.Data;

/**
 * 本地接受微信消息的对象
 * @author jihu
 *
 */
@Data
public class WxReceive {
	
	//消息的目的地
	private String toUserName;
	//消息的出发地
	private String fromUserName;
	//消息的创建时间
	private String createTime;
	//消息类型
	private String msgType;
	//图片地址
	private String picUrl;
	//图片消息媒体id，可以调用获取临时素材接口拉取数据。
	private String MediaId;
	//语音格式，如amr，speex等
	private String format;
	//视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
	private String thumbMediaId;
	//消息内容
	private String content;
	//消息id
	private String location_X;
	//消息id
	private String location_Y;
	//消息id
	private String scale;
	//消息id
	private String label;
	//消息id
	private String msgId;
	//消息id
	private String event;
	//消息id
	private String eventKey;
	//消息id
	private String latitude;
	//消息id
	private String longitude;
	//消息id
	private String Precision;
	//消息id
	private String ticket;
	
}
