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
 * ��������ҳ
 * @author ˼��
 *
 */
public class Event_description_avtivity extends Activity{
	private Button daohang; // �ʺű༭��
	private int caseId;
	private Case case1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_event_description);
		
		daohang = (Button)findViewById(R.id.daohang);
		caseId = getIntent().getIntExtra("caseId", 0);//��ȡID
		getCase(caseId);
		
		//�¼��������
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
						JSONObject jsonObject = new JSONObject(result.toString());  //�õ����jsonObject.getString("d") 		
						//�ж��Ƿ�Ϊtrue��Ȼ��toast����
					}																
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	//�ӷ�������ȡ��������
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
				JSONObject jsonObject = new JSONObject(result.toString());  //�õ����jsonObject.getString("d") 		
				//���ݵõ��Ľ����ʼ��ҳ��-������ĸĵ�������
			}																
		}catch(Exception e){
			e.printStackTrace();
		}
		case1 = new Case(1, "name", "������Ϣ", "36.061, 103.834,�人��������人��ѧ", new Date(), "d://img1.jpg");
		((TextView)findViewById(R.id.event_description_event)).setText(case1.getName());
		((TextView)findViewById(R.id.event__detail_description)).setText(case1.getDate()+"\r\n"+case1.getDes()); //����Ҫ����
		((TextView)findViewById(R.id.event_description_loc)).setText(case1.getLocation().split(",")[2]); 
		((Button)findViewById(R.id.jingyuan_loc)).setText(case1.getLocation().split(",")[2]); 
		//ͼƬ�ͻ�ȡ·����Ȼ��ֱ�Ӵӷ������Ǳ���������Ȼ����ʾ����
	}
	
 	 public void daohang_click(View v) {     //������ ���ذ�ť
		 Intent intent = new Intent(Event_description_avtivity.this, RouteActivity.class); 
		 intent.putExtra("location", case1.getLocation().split(",")[2]);
	    	startActivity(intent);
	      }  
}
