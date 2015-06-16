package com.jingyuan;

import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.amap.location.demo.MultyLocationActivity;
import com.amap.location.demo.R;
import com.baoan.baoan_submit;
import com.gongyong.Case;
import com.route.RouteActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 案件详情页
 * @author 思宁
 *
 */
public class Event_description_avtivity extends Activity{
	private Button daohang; // 帐号编辑框
	private int caseId;
	private Case case1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_event_description);
		
		daohang = (Button)findViewById(R.id.daohang);
		caseId = getIntent().getIntExtra("caseId", 0);//获取ID
		getCase(caseId);
		
		//事件处理完成
		((Button)findViewById(R.id.dealed)).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try{
					HttpPost request = new HttpPost(com.gongyong.Constants.SERVER_URL+"/caseDone"); 
					request.addHeader("Content-Type", "application/json; charset=utf-8"); 
					JSONObject jsonParams = new JSONObject();
					jsonParams.put("id", caseId);
					HttpEntity bodyEntity = new StringEntity(jsonParams.toString());
					request.setEntity(bodyEntity);
					HttpResponse httpResponse = new DefaultHttpClient().execute(request);
					if (httpResponse.getStatusLine().getStatusCode() != 404) {
						String result = EntityUtils.toString(httpResponse.getEntity());
						JSONObject jsonObject = new JSONObject(result.toString());  //得到结果jsonObject.getString("d") 		
						//判断是否为true，然后toast就行
					}																
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	//从服务器获取案子详情
	private void getCase(int id){		
		try{
			HttpPost request = new HttpPost(com.gongyong.Constants.SERVER_URL+"/getCase"); 
			request.addHeader("Content-Type", "application/json; charset=utf-8"); 
			JSONObject jsonParams = new JSONObject();
			jsonParams.put("id", id);
			HttpEntity bodyEntity = new StringEntity(jsonParams.toString());
			request.setEntity(bodyEntity);
			HttpResponse httpResponse = new DefaultHttpClient().execute(request);
			if (httpResponse.getStatusLine().getStatusCode() != 404) {
				String result = EntityUtils.toString(httpResponse.getEntity());
				JSONObject jsonObject = new JSONObject(result.toString());  //得到结果jsonObject.getString("d") 		
				//根据得到的结果初始化页面-即下面的改到这里来
			}																
		}catch(Exception e){
			e.printStackTrace();
		}
		case1 = new Case(1, "name", "描述信息", "36.061, 103.834,武汉市武昌区武汉大学", new Date(), "d://img1.jpg");
		((TextView)findViewById(R.id.event_description_event)).setText(case1.getName());
		((TextView)findViewById(R.id.event__detail_description)).setText(case1.getDate()+"\r\n"+case1.getDes()); //日期要变下
		((TextView)findViewById(R.id.event_description_loc)).setText(case1.getLocation().split(",")[2]); 
		((Button)findViewById(R.id.jingyuan_loc)).setText(case1.getLocation().split(",")[2]); 
		//图片就获取路径，然后直接从服务器那边下载下来然后显示即可
	}
	
 	 public void daohang_click(View v) {     //标题栏 返回按钮
		 Intent intent = new Intent(Event_description_avtivity.this, RouteActivity.class); 
		 intent.putExtra("location", case1.getLocation().split(",")[2]);
	    	startActivity(intent);
	      }  
}
