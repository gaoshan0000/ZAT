package com.baoan;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.amap.api.maps.MapView;
import com.amap.location.demo.MultyLocationActivity;
import com.amap.location.demo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class baoan_submit extends Activity{
	String location_now;
	double lat,lng;
	String fileName;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.baoan);
		
		location_now = this.getIntent().getStringExtra("location_now");
		lat = this.getIntent().getDoubleExtra("geoLat", 0);
		lng = this.getIntent().getDoubleExtra("geoLng", 0);
		((Button)findViewById(R.id.baoaner_loc)).setText(location_now);
		((TextView)findViewById(R.id.baoan_loc_edit)).setText(lat+","+lng);
		
		//确认
		((Button)findViewById(R.id.baoan_ok)).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				new Thread(){
					@Override
					public void run() {
						try{
							HttpPost request = new HttpPost(com.gongyong.Constants.SERVER_URL+"/BaoAn"); 
							request.addHeader("Content-Type", "application/json; charset=utf-8"); 
							JSONObject jsonParams = new JSONObject();
							fileName = "asdf";
							jsonParams.put("caseName", ((TextView)findViewById(R.id.baoan_event_edit)).getText());
							jsonParams.put("caseDes", ((TextView)findViewById(R.id.baoan_description_edit)).getText());
							jsonParams.put("img", fileName); //这个因为拍照还没搞好，所以fileName还不是最后要存的东西
							jsonParams.put("caseLocation", ((TextView)findViewById(R.id.baoan_loc_edit)).getText()+","+location_now);//经,纬,汉字
							HttpEntity bodyEntity = new StringEntity(jsonParams.toString());
							request.setEntity(bodyEntity);
							HttpResponse httpResponse = new DefaultHttpClient().execute(request);
							if (httpResponse.getStatusLine().getStatusCode() != 404) {
								String result = EntityUtils.toString(httpResponse.getEntity());
								JSONObject jsonObject = new JSONObject(result.toString());  //得到结果jsonObject.getString("d") 
								if(jsonObject.getString("d").equals("false")){ 
									//添加失败
									Looper.prepare();
									Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();			
									Looper.loop();																
								}else{ 
									//添加成功			Toast		
									Looper.prepare();
									Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();			
									Looper.loop();
									Intent intent = new Intent(baoan_submit.this,MultyLocationActivity.class);
									startActivity(intent);									
								}
							}																
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}.start();
			}
		});
	}

}
