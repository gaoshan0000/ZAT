package com.gongyong;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

/**
 * ����xmlpull����xml
 * �����Ľ���
 * @author ˼��
 *
 */
public class XMLParase {
	 /** 
     * ����Case xml��Ϣ 
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
                case XmlPullParser.START_DOCUMENT:// �ĵ���ʼ�¼�,���Խ������ݳ�ʼ������  
                    break;  
                case XmlPullParser.START_TAG:// ��ʼԪ���¼�  
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
                        info.setDate (new Date() ); //ʱ���е�����  
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
                case XmlPullParser.END_TAG:// ����Ԫ���¼�  
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
