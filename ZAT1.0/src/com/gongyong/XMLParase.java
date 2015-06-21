package com.gongyong;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

/**
 * 利用xmlpull解析xml
 * 案件的解析
 * @author 思宁
 *
 */
public class XMLParase {
	 /** 
     * 解析Case xml信息 
     * @param inputStream 
     * @return 
     */  
    public static List<Case> paraseCommentInfors (InputStream inputStream)  
    {  
        List<Case> list = new ArrayList<Case>();  
        XmlPullParser parser = Xml.newPullParser();  
        try  
        {  
            parser.setInput (inputStream, "UTF-8");  
            int eventType = parser.getEventType();  
            Case info = new Case();  
  
            while (eventType != XmlPullParser.END_DOCUMENT)  
            {  
                switch (eventType)  
                {  
                case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理  
                    break;  
                case XmlPullParser.START_TAG:// 开始元素事件  
                    String name = parser.getName();  
                    if (name.equalsIgnoreCase ("casesInfo") )  
                    {  
                        info = new Case();  
                    }  
                    else if (name.equalsIgnoreCase ("caseId") )  
                    {  
                        eventType = parser.next();  
                        info.setId(Integer.parseInt(parser.getText()));
                    }  
                    else if (name.equalsIgnoreCase ("caseDes") )  
                    {  
                        eventType = parser.next();  
                        info.setDes(parser.getText() );  
                    }  
                    else if (name.equalsIgnoreCase ("caseName") )  
                    {  
                        eventType = parser.next();  
                        info.setName (parser.getText() );  
                    }  
                    else if (name.equalsIgnoreCase ("caseLocation") )  
                    {  
                        eventType = parser.next();  
                        info.setLocation (parser.getText() );  
                    }  
                   else if (name.equalsIgnoreCase ("caseDate") )  
                    {  
                        eventType = parser.next();  
                        info.setDate (new Date() ); //时间有点问题  
                    }  
                    else if (name.equalsIgnoreCase ("isHandled") )  
                    {  
                        eventType = parser.next();  
                        info.setIsHandled(Integer.parseInt(parser.getText() ));
                    }  
                    else if (name.equalsIgnoreCase ("pId") )  
                    {  
                        eventType = parser.next();  
                        info.setpId(Integer.parseInt(parser.getText()) );  
                    }  
                    else if (name.equalsIgnoreCase ("img") )  
                    {  
                        eventType = parser.next();  
                        info.setImg (parser.getText() );  
                    }  
                    break;  
                case XmlPullParser.END_TAG:// 结束元素事件  
                    if (parser.getName().equalsIgnoreCase ("casesInfo") )  
                    {  
                        list.add (info);  
                        info = null;  
                    }  
                    break;  
                }  
                eventType = parser.next();  
            }  
            inputStream.close();  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
        return list;  
    }  
}
