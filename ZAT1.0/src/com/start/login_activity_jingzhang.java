package com.start;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.EditText;



import android.widget.Toast;

import com.amap.location.demo.R;
import com.jingyuan.Event_review_activity;
import com.jingzhang.Event_jingzhang;

/**
 * 警长登录
 * @author 思宁
 *
 */
public class login_activity_jingzhang extends Activity{
	private EditText mUser; // 帐号编辑框
	private EditText mPassword; // 密码编辑框
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE); 
	        setContentView(R.layout.login_jingzhang);
	        
	        mUser = (EditText)findViewById(R.id.login_user_edit_jingzhang);
	        mPassword = (EditText)findViewById(R.id.login_passwd_edit_jingzhang);
	    }
	 
	 public void login__jingzhang(View v) {
	    /*	if("123".equals(mUser.getText().toString()) && "123".equals(mPassword.getText().toString()))   //判断 帐号和密码
	        {
	             Intent intent = new Intent();
	             intent.setClass(login_activity_jingzhang.this,Event_jingzhang.class);
	             startActivity(intent);
	             //Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
	          }*/
		 if("".equals(mUser.getText().toString()) || "".equals(mPassword.getText().toString()))   //判断 帐号和密码
	        {
	        	new AlertDialog.Builder(login_activity_jingzhang.this)
				.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
				.setTitle("登录错误")
				.setMessage("帐号或者密码不能为空，\n请输入后再登录！")
				.create().show();
	         }
	        else{
	        	new Thread(){
	        		public void run() {
	        			try{
	        				HttpPost request = new HttpPost(com.gongyong.Constants.SERVER_URL+"/bLogin"); 
	        				request.addHeader("Content-Type", "application/json; charset=utf-8"); 
	        				JSONObject jsonParams = new JSONObject();
	        				jsonParams.put("bId", Integer.parseInt(mUser.getText().toString()));
	        				jsonParams.put("bPwd", mPassword.getText().toString());
	        				HttpEntity bodyEntity = new StringEntity(jsonParams.toString());
	        				request.setEntity(bodyEntity);
	        				HttpResponse httpResponse = new DefaultHttpClient().execute(request);
	        				if (httpResponse.getStatusLine().getStatusCode() != 404) {
	        				String result = EntityUtils.toString(httpResponse.getEntity());
	        				JSONObject jsonObject = new JSONObject(result.toString()); 
	        				if(jsonObject.getString("d").equals("true")){
	        					Looper.prepare();
								Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();			
								Looper.loop();
	        					  Intent intent = new Intent();
	        			             intent.setClass(login_activity_jingzhang.this,Event_jingzhang.class);
	        			             startActivity(intent);
	        				}else{	        	
	        					Looper.prepare();
								Toast.makeText(getApplicationContext(), "帐号或者密码不正确, 请检查后重新输入！", Toast.LENGTH_SHORT).show();			
								Looper.loop();
	        				}
	        				}
	        			}catch(Exception e){
	        				e.printStackTrace();
	        			}
	        		};
	        	}.start();	           	        	
	        }
	 }
	 public void login_back_jingzhang(View v) {     //标题栏 返回按钮
	      	this.finish();
	      }  
	    public void login_pw_jingzhang(View v) {     //忘记密码按钮
	    	Uri uri = Uri.parse("http://3g.qq.com"); 
	    	Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
	    	startActivity(intent);
	    	//Intent intent = new Intent();
	    	//intent.setClass(Login.this,Whatsnew.class);
	        //startActivity(intent);
	      }  

}

