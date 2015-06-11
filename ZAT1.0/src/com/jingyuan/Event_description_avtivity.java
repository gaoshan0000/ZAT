package com.jingyuan;

import com.amap.location.demo.R;
import com.route.RouteActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class Event_description_avtivity extends Activity{
	private Button daohang; // ’ ∫≈±‡º≠øÚ
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_event_description);
		
		daohang = (Button)findViewById(R.id.daohang);
	}
	
	 public void daohang_click(View v) {     //±ÍÃ‚¿∏ ∑µªÿ∞¥≈•
		 Intent intent = new Intent(Event_description_avtivity.this, RouteActivity.class); 
	    	startActivity(intent);
	      }  
}
