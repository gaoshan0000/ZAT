package com.start;

import com.amap.location.demo.MultyLocationActivity;
import com.amap.location.demo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class flash_door_activity extends Activity{
	private ImageView mLeft;
	private ImageView mRight;
	private TextView mText;
	String string;
	//	int label;

    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.flash_door);
            
            Intent intent = this.getIntent();        //获取已有的intent对象   
            Bundle bundle = intent.getExtras();    //获取intent里面的bundle对象   
            string = bundle.getString("label");    //获取Bundle里面的字符串 
            
            mLeft = (ImageView)findViewById(R.id.imageLeft);
            mRight = (ImageView)findViewById(R.id.imageRight);
            mText = (TextView)findViewById(R.id.anim_text);
            
            AnimationSet anim = new AnimationSet(true);
    		TranslateAnimation mytranslateanim = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-1f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f);
    		mytranslateanim.setDuration(2000);
    		anim.setStartOffset(800);
    		anim.addAnimation(mytranslateanim);
    		anim.setFillAfter(true);
    		mLeft.startAnimation(anim);
    		
    		AnimationSet anim1 = new AnimationSet(true);
    		TranslateAnimation mytranslateanim1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,+1f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f);
    		mytranslateanim1.setDuration(1500);
    		anim1.addAnimation(mytranslateanim1);
    		anim1.setStartOffset(800);
    		anim1.setFillAfter(true);
    		mRight.startAnimation(anim1);
    		
    		AnimationSet anim2 = new AnimationSet(true);
    		ScaleAnimation myscaleanim = new ScaleAnimation(1f,3f,1f,3f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
    		myscaleanim.setDuration(1000);
    		AlphaAnimation myalphaanim = new AlphaAnimation(1,0.0001f);
    		myalphaanim.setDuration(1500);
    		anim2.addAnimation(myscaleanim);
    		anim2.addAnimation(myalphaanim);
    		anim2.setFillAfter(true);
    		mText.startAnimation(anim2);
    		
    		new Handler().postDelayed(new Runnable(){
    			@Override
    			public void run(){
    				 //Toast toast0 = Toast.makeText(flash_door_activity.this, string, Toast.LENGTH_SHORT); 
		    		 //toast0.show();
		    		 if (string.equals("0")){
		    			 Intent intent = new Intent (flash_door_activity.this,MultyLocationActivity.class);			
		    			 startActivity(intent);			
		    			 flash_door_activity.this.finish();
		    		 }
		    		 else if (string.equals("1")){
		    			 Intent intent = new Intent (flash_door_activity.this,login_activity.class);			
		    			 startActivity(intent);			
		    			 flash_door_activity.this.finish();
		    		 }
		    		 else if (string.equals("2")){
		    			 Intent intent = new Intent (flash_door_activity.this,login_activity_jingzhang.class);			
		    			 startActivity(intent);			
		    			 flash_door_activity.this.finish();
		    		 }
    			}
    		}, 2300);
        }
}
