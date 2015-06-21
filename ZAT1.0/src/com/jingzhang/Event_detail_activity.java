package com.jingzhang;

import com.amap.location.demo.R;
import com.gongyong.Case;

import android.app.Activity;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

//案件详情页-警长
//图片未搞定
public class Event_detail_activity extends Activity{
	private int caseId;
	private Case case1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_event_description);
				
		Bundle data = getIntent().getExtras();
		case1 =(Case)data.getSerializable("caseInfo");
		init();//初始化界面	
	}
	
	private void init(){
		caseId = case1.getId();
			((TextView)findViewById(R.id.event_description_event)).setText(case1.getName());
			String a = case1.getDate()+"\r\n"+case1.getDes();
			if(case1.getIsHandled() == 0){
				a += "\r\n"+"未处理";
			}else if(case1.getIsHandled() == 1){
				a += "\r\n"+"已结处理，处理警员为"+case1.getpId();
			}else if(case1.getIsHandled() == 2){
				a += "\r\n"+"正在处理，处理警员为"+case1.getpId();
			}
			((TextView)findViewById(R.id.event__detail_description)).setText(a); //日期要变下
			((TextView)findViewById(R.id.event_description_loc)).setText(case1.getLocation().split(",")[2] );  //case1.getLocation().split(",")[2] 
			((Button)findViewById(R.id.jingyuan_loc)).setVisibility(View.INVISIBLE);
			((Button)findViewById(R.id.dealed)).setVisibility(View.INVISIBLE);
			((Button)findViewById(R.id.daohang)).setVisibility(View.INVISIBLE);
			//图片就获取路径，然后直接从服务器那边下载下来然后显示即可
	}

}
