package com.start;


import com.amap.location.demo.MultyLocationActivity;
import com.amap.location.demo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class select_identity_activity extends Activity{
	 CheckBox people=null; 
	 CheckBox police=null; 
	 CheckBox sheriff=null; 
	 int label;
//	 Intent intent=new Intent();
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.select_identity);
		people=(CheckBox)findViewById(R.id.public_check); 
		police=(CheckBox)findViewById(R.id.police_check);
		sheriff=(CheckBox)findViewById(R.id.sheriff_check);
		people.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
         @Override
         public void onCheckedChanged(CompoundButton buttonView, 
                 boolean isChecked) { 
             // TODO Auto-generated method stub 
        	 if(isChecked){
//            	 intent.setClass(select_identity_activity.this,flash_door_activity.class);
            	 label=0;
        			 
             }
         } 
        });
		police.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
	         @Override
	         public void onCheckedChanged(CompoundButton buttonView, 
	                 boolean isChecked) { 
	             // TODO Auto-generated method stub 
	             if(isChecked){
//	            	 intent.setClass(select_identity_activity.this,flash_door_activity.class);
	            	 label=1; 
	            	 
	             }
	         } 
	        });
		sheriff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
	         @Override
	         public void onCheckedChanged(CompoundButton buttonView, 
	                 boolean isChecked) { 
	             // TODO Auto-generated method stub 
	             if(isChecked){
//	            	 intent.setClass(select_identity_activity.this,flash_door_activity.class);
	            	 label=2;
	            	 	 
	             }
	         } 
	        });
	}
	 public void startbutton(View v) {
		 	Intent intent=new Intent();
		 	Bundle bundle = new Bundle();                           //����Bundle���� 
		 	switch(label){
		 		case 0:
		 			 Toast toast0 = Toast.makeText(select_identity_activity.this, "��ӭ���롪��Ⱥ��", Toast.LENGTH_SHORT); 
		    		 toast0.show();
		    		 bundle.putString("label", "0");     //װ������   
		    		 intent.putExtras(bundle);  
		 			 intent.setClass(select_identity_activity.this,flash_door_activity.class);
		 			break;
		 		case 1:
		 			Toast toast1 = Toast.makeText(select_identity_activity.this, "��ӭ���롪����Ա", Toast.LENGTH_SHORT); 
		    		toast1.show();
		    		bundle.putString("label", "1");     //װ������   
		    		 intent.putExtras(bundle);  
		    		intent.setClass(select_identity_activity.this,flash_door_activity.class);
		 			break;
		 		case 2:
		 			Toast toast2 = Toast.makeText(select_identity_activity.this, "��ӭ���롪������", Toast.LENGTH_SHORT); 
		    		 toast2.show();
		    		 bundle.putString("label", "2");     //װ������   
		    		 intent.putExtras(bundle);  
		    		 intent.setClass(select_identity_activity.this,flash_door_activity.class);
	 				break;
	 }
			startActivity(intent);
			this.finish();
	      }  

}
