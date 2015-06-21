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

//��Ա��¼
public class login_activity extends Activity{
	private EditText mUser; // �ʺű༭��
	private EditText mPassword; // ����༭��
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE); 
	        setContentView(R.layout.login);
	        
	        mUser = (EditText)findViewById(R.id.login_user_edit);
	        mPassword = (EditText)findViewById(R.id.login_passwd_edit);
	    }
	 
	 public void login_jingyuan(View v) {
	        if("".equals(mUser.getText().toString()) || "".equals(mPassword.getText().toString()))   //�ж� �ʺź�����
	        {
	        	new AlertDialog.Builder(login_activity.this)
				.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
				.setTitle("��¼����")
				.setMessage("�ʺŻ������벻��Ϊ�գ�\n��������ٵ�¼��")
				.create().show();
	         }
	        else{
	        	new Thread(){
	        		public void run() {
	        			try{
	        				HttpPost request = new HttpPost(com.gongyong.Constants.SERVER_URL+"/pLogin"); 
	        				request.addHeader("Content-Type", "application/json; charset=utf-8"); 
	        				JSONObject jsonParams = new JSONObject();
	        				jsonParams.put("pId", Integer.parseInt(mUser.getText().toString()));
	        				jsonParams.put("pPwd", mPassword.getText().toString());
	        				HttpEntity bodyEntity = new StringEntity(jsonParams.toString());
	        				request.setEntity(bodyEntity);
	        				HttpResponse httpResponse = new DefaultHttpClient().execute(request);
	        				if (httpResponse.getStatusLine().getStatusCode() != 404) {
	        				String result = EntityUtils.toString(httpResponse.getEntity());
	        				JSONObject jsonObject = new JSONObject(result.toString()); 
	        				if(jsonObject.getString("d").equals("true")){
	        					Looper.prepare();
	        					Toast.makeText(getApplicationContext(), "��¼�ɹ���", Toast.LENGTH_LONG).show();
	        					Looper.loop();
	        					Intent intent = new Intent();
	        		             intent.setClass(login_activity.this,Event_review_activity.class);
	        		             intent.putExtra("id", mUser.getText().toString());//��þ�Ա�ľ���
	        		             startActivity(intent);
	        				}else{	        				
	        					Looper.prepare();
	        					Toast.makeText(getApplicationContext(), "�ʺŻ������벻��ȷ, ������������룡", Toast.LENGTH_LONG).show();
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
	 public void login_back(View v) {     //������ ���ذ�ť
	      	this.finish();
	      }  
	    public void login_pw(View v) {     //�������밴ť
	    	Uri uri = Uri.parse("http://3g.qq.com"); 
	    	Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
	    	startActivity(intent);
	    	//Intent intent = new Intent();
	    	//intent.setClass(Login.this,Whatsnew.class);
	        //startActivity(intent);
	      }  

}
