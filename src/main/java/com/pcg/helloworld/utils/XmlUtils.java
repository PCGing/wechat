package com.pcg.helloworld.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pcg.helloworld.entity.WxReceive;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;

@Slf4j
@Controller
public class XmlUtils {

	private  final Logger logger = LoggerFactory.getLogger( XmlUtils.class);
	public static void main(String[] args) throws Exception {
		XmlUtils xmlUtils = new XmlUtils();
		WxReceive wxReceive = new WxReceive();
		
		wxReceive.setContent("jihu222");
		xmlUtils.bean2XmlStr(wxReceive);
	}

	/**
	 * 将bean对象转为xml字符串
	 * @param <T>
	 * @return
	 * @throws IntrospectionException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public <T> String bean2XmlStr(T t) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Document document = DocumentHelper.createDocument();
		
        //添加注解
        List<String> strList = new LinkedList<String>();
        strList.add("ToUserName");
        strList.add("FromUserName");
        strList.add("MsgType");
        strList.add("Content");
        strList.add("MsgId");
        strList.add("MEDIAID");
        strList.add("TITLE");
        strList.add("DESCRIPTION");
        strList.add("MUSICURL");
        strList.add("HQMUSICURL");
        strList.add("THUMBMEDIAID");
        strList.add("ARTICLECOUNT");
        strList.add("PICURL");
        strList.add("URL");
        
		//获取属性调用read方法
		Element rootElement = document.addElement("xml");

        Field[] fields = t.getClass().getDeclaredFields();
        //属性名称
        String objName = "";
        Object objValue = new Object();
        Method method = null;
        Element element = null;
        for(int i=0;i<fields.length;i++) {
        	objName = fields[i].getName();
        	PropertyDescriptor pd = new PropertyDescriptor(objName, t.getClass());
        	method = pd.getReadMethod();
        	objValue = "";
        	objValue = method.invoke(t);
        	element = rootElement.addElement(objName);

        	if(ObjectUtils.isEmpty(objValue)) {
        		objValue = "";
        	}
        	if(strList.contains(objName)) {
        		element.addText("<![CDATA["+objValue+"]]>");
        	}else {
        		element.addText(objValue.toString());
        	}
        }
       System.out.println(document.asXML());
       String removeStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
       String s1 = document.asXML().replace(removeStr, "");
       String s2 = s1.replace("&lt;", "<").replace("&gt;", ">");
       return s2;
	}
	
	
	/**
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	public Document string2Xmls(String xml) throws DocumentException {

		Document document = DocumentHelper.parseText(xml);

		return document;
	}


	public <T> T xmlString2Bean(String xmlStr, Class<T> t)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException, IntrospectionException, DocumentException {
		LinkedList<String> defaultRemovePropertiesList = new LinkedList<String>();
		defaultRemovePropertiesList.add("serialVersionUID");

		T myObj = t.newInstance();
		Field[] fs = t.getDeclaredFields();

		Document doc = null;
		// 将字符串转为XML
		doc = DocumentHelper.parseText(xmlStr);
		// 获取根节点
		Element rootElt = doc.getRootElement();
		// 获取根节点下的子节点head
		Iterator<Element> iter = rootElt.elementIterator();
		Element element = null;
		Map<String, String> dataMap = new HashMap<String, String>();
		while (iter.hasNext()) {
			element = iter.next();
			String elementName = element.getName();
			String elementValue = element.getStringValue();
//			System.out.println(elementName + ":" + elementValue);
			dataMap.put(elementName.toUpperCase(), elementValue);
		}

		List<Field> fieldList = new LinkedList<Field>();
		for (int i = 0; i < fs.length; i++) {
			if (!ObjectUtils.isEmpty(defaultRemovePropertiesList)) {
				if (!defaultRemovePropertiesList.contains(fs[i].getName())) {
					fieldList.add(fs[i]);
				}
			} else {
				fieldList.add(fs[i]);
			}
		}
		Method method = null;
		// 导出属性
		for (int i = 0; i < fieldList.size(); i++) {
			// 调用方法,设置属性
			PropertyDescriptor pd = new PropertyDescriptor(fieldList.get(i).getName(), t);
			String displayName = pd.getDisplayName().toUpperCase();
			if (!ObjectUtils.isEmpty(dataMap.get(displayName))) {
				method = pd.getWriteMethod();
				method.invoke(myObj, dataMap.get(displayName).toString());
			}
		}
		return myObj;
	}
}
