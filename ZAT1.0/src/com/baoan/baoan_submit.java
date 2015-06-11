package com.baoan;

import com.amap.api.maps.MapView;
import com.amap.location.demo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class baoan_submit extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.baoan);
	}

}
