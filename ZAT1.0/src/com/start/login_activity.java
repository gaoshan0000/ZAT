package com.start;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;


import com.amap.location.demo.R;
import com.jingyuan.Event_review_activity;

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
	    	if("123".equals(mUser.getText().toString()) && "123".equals(mPassword.getText().toString()))   //�ж� �ʺź�����
	        {
	             Intent intent = new Intent();
	             intent.setClass(login_activity.this,Event_review_activity.class);
	             startActivity(intent);
	             //Toast.makeText(getApplicationContext(), "��¼�ɹ�", Toast.LENGTH_SHORT).show();
	          }
	        else if("".equals(mUser.getText().toString()) || "".equals(mPassword.getText().toString()))   //�ж� �ʺź�����
	        {
	        	new AlertDialog.Builder(login_activity.this)
				.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
				.setTitle("��¼����")
				.setMessage("�ʺŻ������벻��Ϊ�գ�\n��������ٵ�¼��")
				.create().show();
	         }
	        else{
	           
	        	new AlertDialog.Builder(login_activity.this)
				.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
				.setTitle("��¼ʧ��")
				.setMessage("�ʺŻ������벻��ȷ��\n������������룡")
				.create().show();
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
