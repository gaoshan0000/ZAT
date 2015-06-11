package com.start;

import com.amap.location.demo.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;


public class login_regist_activity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.login_regist);
    }
    public void welcome_login(View v) {  
      	Intent intent = new Intent();
		intent.setClass(login_regist_activity.this,login_activity.class);
		startActivity(intent);
		this.finish();
      }  
    public void welcome_register(View v) {  
//      	Intent intent = new Intent();
//		intent.setClass(Welcome.this,MainWeixin.class);
//		startActivity(intent);
		//this.finish();
      }  
}
