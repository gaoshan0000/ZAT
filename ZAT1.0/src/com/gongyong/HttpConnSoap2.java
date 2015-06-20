package com.gongyong;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.util.Log;

/**
 * 连接webservice的类 
 * @author 思宁
 *
 */
public class HttpConnSoap2 {
	/** 
     * 获取返回的InputStream，为了增强通用性，在方法内不对其进行解析。 
     * 
     * @param methodName 
     *            webservice方法名 
     * @param Parameters 
     *            webservice方法对应的参数名 
     * @param ParValues 
     *            webservice方法中参数对应的值 
     * @return 未解析的InputStream 
     */  
    public InputStream GetWebServre (String methodName, ArrayList<String> Parameters, ArrayList<String> ParValues)  
    {  
  
        //指定URL地址，我这里使用的是常量。  
        //如：String ServerUrl = "http://10.0.2.2:11125/Service1.asmx";  
        String ServerUrl = "http://192.168.1.108/test_new/Service1.asmx";
  
        //soapAction = 命名空间 + 方法名  
        String soapAction = "http://tempuri.org/" + methodName;  
  
        //拼凑requestData  
        String soap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"  
                      + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"  
                      + "<soap:Body />";  
        String tps, vps, ts;  
        String mreakString = "";  
        mreakString = "<" + methodName + " xmlns=\"http://tempuri.org/\">";  
        for (int i = 0; i < Parameters.size(); i++)  
        {  
            tps = Parameters.get (i).toString();  
            //设置该方法的参数为.net webService中的参数名称  
            vps = ParValues.get (i).toString();  
            ts = "<" + tps + ">" + vps + "</" + tps + ">";  
            mreakString = mreakString + ts;  
        }  
        mreakString = mreakString + "</" + methodName + ">";  
        String soap2 = "</soap:Envelope>";  
        String requestData = soap + mreakString + soap2;  
        //其上所有的数据都是在拼凑requestData，即向服务器发送的数据  
  
        try  
        {  
            URL url = new URL (ServerUrl); //指定服务器地址  
            HttpURLConnection con = (HttpURLConnection) url.openConnection();//打开链接  
            byte[] bytes = requestData.getBytes ("utf-8"); //指定编码格式，可以解决中文乱码问题  
            con.setDoInput (true); //指定该链接是否可以输入  
            con.setDoOutput (true); //指定该链接是否可以输出  
            con.setUseCaches (false); //指定该链接是否只用caches  
            con.setConnectTimeout (6000); // 设置超时时间  
            con.setRequestMethod ("POST"); //指定发送方法名，包括Post和Get。  
            con.setRequestProperty ("Content-Type", "text/xml;charset=utf-8"); //设置（发送的）内容类型  
            con.setRequestProperty ("SOAPAction", soapAction); //指定soapAction  
            con.setRequestProperty ("Content-Length", "" + bytes.length); //指定内容长度  
  
            //发送数据  
            OutputStream outStream = con.getOutputStream();  
            outStream.write (bytes);  
            outStream.flush();  
            outStream.close();  
  
            //获取数据  
            InputStream inputStream = con.getInputStream();  
            return inputStream;  
  
            /** 
             * 此类到此结束了，比原来的HttpConnSoap还短，因为这里没有对返回的数据做解析。数据完全都保存在了inputStream中。 
             * 而原来的类是将数据解析成了ArrayList 
             * <String>格式返回。显然，这样无法解决我们上面的需求（返回值是复杂类型的List） 
             */  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
            return null;  
        }  
    }  
}
