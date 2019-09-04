package com.pcg.helloworld.entity;

import lombok.Data;

/**
 * 本地返送到腾讯的对象
 * @author jihu
 *
 */
@Data
public class WxSend {

	private String ToUserName;
	private String FromUserName;
	private String CreateTime;
	private String MsgType;
	private String Content;
	private String MsgId;
}
