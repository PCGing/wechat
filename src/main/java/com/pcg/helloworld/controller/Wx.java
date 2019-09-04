package com.pcg.helloworld.controller;

import java.beans.IntrospectionException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

import javax.annotation.security.RunAs;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.pcg.helloworld.entity.WxReceive;
import com.pcg.helloworld.entity.WxSend;
import com.pcg.helloworld.utils.SHA1;
import com.pcg.helloworld.utils.XmlUtils;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller("/Wx")
public class Wx extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private  final Logger logger = LoggerFactory.getLogger(Wx.class);

	private HttpServletRequest request;
	private HttpServletResponse response;

	@Value("${wx.token}")
	private String token;


    public Wx() {
        super();
    }
    @RequestMapping(method = RequestMethod.GET)
	protected void get(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(this.getClass().getName()+"---->"+Thread.currentThread().getStackTrace()[1].getMethodName()); 	
		String signature=request.getParameter("signature");
	        String timestamp=request.getParameter("timestamp");
	        String nonce=request.getParameter("nonce");
	        String echostr=request.getParameter("echostr");
	        String jiami="";
	         try {
				 //这里是对三个参数进行加密
	             jiami= SHA1.getSHA1(token, timestamp, nonce,"");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	         System.out.println("加密"+jiami);
	         System.out.println("本身"+signature);
	            PrintWriter out=response.getWriter();
	            if(jiami.equals(signature)) {
	            	out.print(echostr);
	            }
	}

	@RequestMapping(method = RequestMethod.POST)
	protected void post(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	this.request = request;

		System.out.println(this.getClass().getName()+"---->"+Thread.currentThread().getStackTrace()[1].getMethodName()); 	
		 XmlUtils xmlUtils = new XmlUtils();
		//处理接收数据
		request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        //获取服务器发送过来的信息，因为不是参数，得用输入流读取
        String sb = this.getReqData(request);
		//将用户发送得消息打印出来
        logger.info ("用户发送过来原始信息："+sb);
        WxReceive wxReceive = null;
		try {
			wxReceive = xmlUtils.xmlString2Bean(sb, WxReceive.class);
			logger.info("接收对象:"+wxReceive.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//返回数据
		PrintWriter out=response.getWriter();
		WxSend wxSend = new WxSend();
//		wxSend.setFromUserName(wxReceive.getToUserName());
//		wxSend.setToUserName(wxReceive.getFromUserName());
//		wxSend.setContent(wxReceive.getContent());
//		wxSend.setCreateTime(wxReceive.getCreateTime());
//		wxSend.setMsgId(wxReceive.getMsgId());
//		wxSend.setMsgType(wxReceive.getMsgType());

		wxSend.setFromUserName(wxReceive.getToUserName());
		wxSend.setToUserName(wxReceive.getFromUserName());
		wxSend.setContent(wxReceive.getContent());
		wxSend.setCreateTime(wxReceive.getCreateTime());
		wxSend.setMsgId("durG6aZNn_Y9NgzcID-zlGDSCDZbeTrLqo_6x2LGRN0");
		wxSend.setMsgType("image");
		
		String replyMsg = "";
		try {
			replyMsg = xmlUtils.bean2XmlStr(wxSend);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		logger.info("服务器发给公众号:"+replyMsg);
		//回复
		replyMsg = "<xml><ToUserName><![CDATA[otIMNt7Gq5KCYiHf3A9371Y2ph9M]]></ToUserName><FromUserName><![CDATA[gh_6428c0b0a8f2]]></FromUserName><CreateTime>1567431593</CreateTime><MsgType><![CDATA[image]]></MsgType><Image><MediaId><![CDATA[durG6aZNn_Y9NgzcID-zlGDSCDZbeTrLqo_6x2LGRN0]]></MediaId></Image></xml>";
		logger.info("服务器发给公众号:"+replyMsg);
		out.println(replyMsg);
	}
	private String getReqData(HttpServletRequest req) {
		 BufferedReader reader = null;
	        StringBuilder sb = new StringBuilder();
	        try{
	            reader = new BufferedReader(new InputStreamReader(req.getInputStream(), "utf-8"));
	            String line = null;
	            while ((line = reader.readLine()) != null){
	                sb.append(line);
	            }
	        } catch (IOException e){
	            e.printStackTrace();
	        } finally {
	            try{
	                if (null != reader){ 
	                	reader.close();
	                }
	            } catch (IOException e){
	                e.printStackTrace();
	            }
	        }
			return sb.toString();
	}


}
