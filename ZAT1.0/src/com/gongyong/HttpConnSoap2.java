package com.gongyong;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.util.Log;

/**
 * ����webservice���� 
 * @author ˼��
 *
 */
public class HttpConnSoap2 {
	/** 
     * ��ȡ���ص�InputStream��Ϊ����ǿͨ���ԣ��ڷ����ڲ�������н����� 
     * 
     * @param methodName 
     *            webservice������ 
     * @param Parameters 
     *            webservice������Ӧ�Ĳ����� 
     * @param ParValues 
     *            webservice�����в�����Ӧ��ֵ 
     * @return δ������InputStream 
     */  
    public InputStream GetWebServre (String methodName, ArrayList<String> Parameters, ArrayList<String> ParValues)  
    {  
  
        //ָ��URL��ַ��������ʹ�õ��ǳ�����  
        //�磺String ServerUrl = "http://10.0.2.2:11125/Service1.asmx";  
        String ServerUrl = "http://192.168.1.108/test_new/Service1.asmx";
  
        //soapAction = �����ռ� + ������  
        String soapAction = "http://tempuri.org/" + methodName;  
  
        //ƴ��requestData  
        String soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"  
                      + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"  
                      + "<soap:Body />";  
        String tps, vps, ts;  
        String mreakString = "";  
        mreakString = "<" + methodName + " xmlns=\"http://tempuri.org/\">";  
        for (int i = 0; i < Parameters.size(); i++)  
        {  
            tps = Parameters.get (i).toString();  
            //���ø÷����Ĳ���Ϊ.net webService�еĲ�������  
            vps = ParValues.get (i).toString();  
            ts = "<" + tps + ">" + vps + "</" + tps + ">";  
            mreakString = mreakString + ts;  
        }  
        mreakString = mreakString + "</" + methodName + ">";  
        String soap2 = "</soap:Envelope>";  
        String requestData = soap + mreakString + soap2;  
        //�������е����ݶ�����ƴ��requestData��������������͵�����  
  
        try  
        {  
            URL url = new URL (ServerUrl); //ָ����������ַ  
            HttpURLConnection con = (HttpURLConnection) url.openConnection();//������  
            byte[] bytes = requestData.getBytes ("utf-8"); //ָ�������ʽ�����Խ��������������  
            con.setDoInput (true); //ָ���������Ƿ��������  
            con.setDoOutput (true); //ָ���������Ƿ�������  
            con.setUseCaches (false); //ָ���������Ƿ�ֻ��caches  
            con.setConnectTimeout (6000); // ���ó�ʱʱ��  
            con.setRequestMethod ("POST"); //ָ�����ͷ�����������Post��Get��  
            con.setRequestProperty ("Content-Type", "text/xml;charset=utf-8"); //���ã����͵ģ���������  
            con.setRequestProperty ("SOAPAction", soapAction); //ָ��soapAction  
            con.setRequestProperty ("Content-Length", "" + bytes.length); //ָ�����ݳ���  
  
            //��������  
            OutputStream outStream = con.getOutputStream();  
            outStream.write (bytes);  
            outStream.flush();  
            outStream.close();  
  
            //��ȡ����  
            InputStream inputStream = con.getInputStream();  
            return inputStream;  
  
            /** 
             * ���ൽ�˽����ˣ���ԭ����HttpConnSoap���̣���Ϊ����û�жԷ��ص�������������������ȫ����������inputStream�С� 
             * ��ԭ�������ǽ����ݽ�������ArrayList 
             * <String>��ʽ���ء���Ȼ�������޷����������������󣨷���ֵ�Ǹ������͵�List�� 
             */  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
            return null;  
        }  
    }  
}
