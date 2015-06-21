package com.jingzhang;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;








import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMap.OnMarkerDragListener;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.location.demo.R;
import com.gongyong.Case;
import com.gongyong.Constants;
import com.gongyong.HttpConnSoap2;
import com.gongyong.ToastUtil;
import com.gongyong.XMLParase;
import com.jingyuan.Event_description_avtivity;
import com.jingyuan.Event_review_activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Event_jingzhang extends Activity implements 
	OnInfoWindowClickListener, OnMapLoadedListener,
	OnClickListener, InfoWindowAdapter,LocationSource,
	AMapLocationListener{
//	private MarkerOptions markerOption;
//	private TextView markerText;
	//private Button markerButton;
	//private RadioGroup radioOption;
	private AMap aMap;
	private MapView mapView;
//	private Marker marker2;
	private LatLng latlng = new LatLng(36.061, 103.834);
	private List<Case> cases;//所有的事件
	
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.jingzhang_view);
		cases = new ArrayList<Case>();
		getData.start();  //得到数据
		mapView = (MapView) findViewById(R.id.map_jingzhang);
		mapView.onCreate(savedInstanceState); 
		init();
	}

	Thread getData =  new Thread(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{		
				HttpConnSoap2 webservice = new HttpConnSoap2();  
				String methodName = "getAnJianAll";//方法名  
				ArrayList<String> paramList = new ArrayList<String>();  
				ArrayList<String> parValueList = new ArrayList<String>();
				paramList.add ("id");//指定参数名  
				parValueList.add ("001");//指定参数值  
				InputStream inputStream = webservice.GetWebServre (methodName, paramList, parValueList);  
				cases= XMLParase.paraseCommentInfors (inputStream);
				//然后显示到地图上		
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	};
	
	private void init() {
		//markerText = (TextView) findViewById(R.id.mark_listenter_text);
		//radioOption = (RadioGroup) findViewById(R.id.custom_info_window_options);
		//markerButton = (Button) findViewById(R.id.marker_button);
		//markerButton.setOnClickListener(this);
		Button clearMap = (Button) findViewById(R.id.clearMap_jingzhang);
		clearMap.setOnClickListener(this);
		Button resetMap = (Button) findViewById(R.id.resetMap_jingzhang);
		resetMap.setOnClickListener(this);
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	private void setUpMap() {
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 鐠佸墽鐤嗙亸蹇氭憫閻愬湱娈戦崶鐐垼
		myLocationStyle.strokeColor(Color.BLACK);// 鐠佸墽鐤嗛崷鍡楄埌閻ㄥ嫯绔熷鍡涱杹閼癸拷
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 鐠佸墽鐤嗛崷鍡楄埌閻ㄥ嫬锝為崗鍛搭杹閼癸拷
		// myLocationStyle.anchor(int,int)//鐠佸墽鐤嗙亸蹇氭憫閻愬湱娈戦柨姘卞仯
		myLocationStyle.strokeWidth(1.0f);// 鐠佸墽鐤嗛崷鍡楄埌閻ㄥ嫯绔熷鍡欑煐缂侊拷
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 鐠佸墽鐤嗙�规矮缍呴惄鎴濇儔
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 鐠佸墽鐤嗘妯款吇鐎规矮缍呴幐澶愭尦閺勵垰鎯侀弰鍓с仛
		aMap.setMyLocationEnabled(true);// 鐠佸墽鐤嗘稉绨峳ue鐞涖劎銇氶弰鍓с仛鐎规矮缍呯仦鍌氳嫙閸欘垵袝閸欐垵鐣炬担宥忕礉false鐞涖劎銇氶梾鎰鐎规矮缍呯仦鍌氳嫙娑撳秴褰茬憴锕�褰傜�规矮缍呴敍宀勭帛鐠併倖妲竑alse
	   // aMap.setMyLocationType()
		
//		aMap.setOnMarkerDragListener(this);
		aMap.setOnMapLoadedListener(this);
//		aMap.setOnMarkerClickListener(this);
		aMap.setOnInfoWindowClickListener(this);
		aMap.setInfoWindowAdapter(this);
		addMarkersToMap();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}


	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	private void addMarkersToMap() {
		String loca = "";
		if(cases.size() >0){
			for(int i =0;i<cases.size();i++){
				loca = cases.get(i).getLocation();
				//Log.i("123","asfdfds:"+cases.get(i).getDes()+" "+cases.get(i).getId());
				MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 0.5f)
						.position(new LatLng(Double.parseDouble(loca.split(",")[0]), Double.parseDouble(loca.split(",")[1])))
						.title(cases.get(i).getName()+","+cases.get(i).getDes())						
						.draggable(true);				
				if(cases.get(i).getIsHandled() == 0){//未处理
					markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
				}else if(cases.get(i).getIsHandled() == 1){//已处理
					markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
				}else if(cases.get(i).getIsHandled() == 2){//正在处理
					markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
				}
				Marker marker = aMap.addMarker(markerOption);
				marker.showInfoWindow();		
			}
		}
	}

//
//	public void drawMarkers() {
//		Marker marker = aMap.addMarker(new MarkerOptions()
//				.position(latlng)
//				.title("妗堜欢鎻忚堪淇℃伅")
//				.icon(BitmapDescriptorFactory
//						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//				.draggable(true));
//		marker.showInfoWindow();
//	}

	
//	@Override
//	public boolean onMarkerClick(final Marker marker) {
//		if (marker.equals(marker2)) {
//			if (aMap != null) {
//				jumpPoint(marker);
//			}
//		}
//		markerText.setText("onMarkerClick" + marker.getTitle());
//		return false;
//	}


//	public void jumpPoint(final Marker marker) {
//		final Handler handler = new Handler();
//		final long start = SystemClock.uptimeMillis();
//		Projection proj = aMap.getProjection();
//		Point startPoint = proj.toScreenLocation(Constants.XIAN);
//		startPoint.offset(0, -100);
//		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
//		final long duration = 1500;
//
//		final Interpolator interpolator = new BounceInterpolator();
//		handler.post(new Runnable() {
//			@Override
//			public void run() {
//				long elapsed = SystemClock.uptimeMillis() - start;
//				float t = interpolator.getInterpolation((float) elapsed
//						/ duration);
//				double lng = t * Constants.XIAN.longitude + (1 - t)
//						* startLatLng.longitude;
//				double lat = t * Constants.XIAN.latitude + (1 - t)
//						* startLatLng.latitude;
//				marker.setPosition(new LatLng(lat, lng));
//				aMap.invalidate();
//				if (t < 1.0) {
//					handler.postDelayed(this, 16);
//				}
//			}
//		});
//
//	}
	
//	protected void dialog() {
//		  AlertDialog.Builder builder = new Builder(Event_jingzhang.this);
//		  builder.setMessage("鏄惁纭澶勭悊璇ヤ簨浠�");
//		  builder.setTitle("鎻愮ず");
//		  builder.setPositiveButton("鍙栨秷",new DialogInterface.OnClickListener(){
//
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				// TODO Auto-generated method stub
//				
//			}});
//		  builder.setPositiveButton("纭", new DialogInterface.OnClickListener(){
//
//				@Override
//				public void onClick(DialogInterface arg0, int arg1) {
//					// TODO Auto-generated method stub
//					Intent intent = new Intent (Event_jingzhang.this,Event_description_avtivity.class);			
//					startActivity(intent);	
//				}});
//		  builder.create().show();
//		 }
	
	//案件点击查看详情
	@Override
	public void onInfoWindowClick(Marker marker) {
		String a = marker.getId();
		int index = Integer.parseInt(a.substring(6));
		Log.i("marker", cases.get(index-1).getId()+"");
		Intent intent = new Intent (Event_jingzhang.this,Event_detail_activity.class);	
		Bundle data = new Bundle();
		data.putSerializable("caseInfo", cases.get(index-1));  
		intent.putExtras(data);  			
		startActivity(intent);	
	}
//
//	@Override
//	public void onMarkerDrag(Marker marker) {
//		String curDes = marker.getTitle() + "onMarkerDrag:(lat,lng)\n("
//				+ marker.getPosition().latitude + ","
//				+ marker.getPosition().longitude + ")";
//		markerText.setText(curDes);
//	}


	@Override
	public void onMapLoaded() {
		LatLngBounds bounds = new LatLngBounds.Builder()
				.include(Constants.XIAN).include(Constants.CHENGDU)
				.include(latlng).include(Constants.ZHENGZHOU).build();
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
	}


	@Override
	public View getInfoContents(Marker marker) {
//		if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_contents) {
//			return null;
//		}
		View infoContent = getLayoutInflater().inflate(
				R.layout.custom_info_contents, null);
		render(marker, infoContent);
		return infoContent;
	}


	@Override
	public View getInfoWindow(Marker marker) {
//		if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_window) {
//			return null;
//		}
		View infoWindow = getLayoutInflater().inflate(
				R.layout.custom_info_window, null);

		render(marker, infoWindow);
		return infoWindow;
	}


	public void render(Marker marker, View view) {
//		if (radioOption.getCheckedRadioButtonId() == R.id.custom_info_contents) {
//			((ImageView) view.findViewById(R.id.badge))
//					.setImageResource(R.drawable.badge_sa);
//		} else if (radioOption.getCheckedRadioButtonId() == R.id.custom_info_window) {
//			ImageView imageView = (ImageView) view.findViewById(R.id.badge);
//			imageView.setImageResource(R.drawable.badge_wa);
//		}
		String title = marker.getTitle();
		TextView titleUi = ((TextView) view.findViewById(R.id.title));
		if (title != null) {
			SpannableString titleText = new SpannableString(title);
			titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
					titleText.length(), 0);
			titleUi.setTextSize(15);
			titleUi.setText(titleText);

		} else {
			titleUi.setText("");
		}
		String snippet = marker.getSnippet();
		TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
		if (snippet != null) {
			SpannableString snippetText = new SpannableString(snippet);
			snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
					snippetText.length(), 0);
			snippetUi.setTextSize(20);
			snippetUi.setText(snippetText);
		} else {
			snippetUi.setText("");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.clearMap_jingzhang:
			if (aMap != null) {
				aMap.clear();
			}
			break;

		case R.id.resetMap_jingzhang:
			if (aMap != null) {
				aMap.clear();
				addMarkersToMap();
			}
			break;
//		case R.id.marker_button:
//			if (aMap != null) {
//				List<Marker> markers = aMap.getMapScreenMarkers();
//				if (markers == null || markers.size() == 0) {
//					ToastUtil.show(this, "marker_button=null");
//					return;
//				}
//				String tile = "marker_button";
//				for (Marker marker : markers) {
//					tile = tile + " " + marker.getTitle();
//
//				}
//				ToastUtil.show(this, tile);
//
//			}
//			break;
		default:
			break;
		}
	}

	/**
	 * 濮濄倖鏌熷▔鏇炲嚒缂佸繐绨惧锟�
	 */
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * 鐎规矮缍呴幋鎰閸氬骸娲栫拫鍐ㄥ毐閺侊拷
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// 閺勫墽銇氱化鑽ょ埠鐏忓繗鎽戦悙锟�
		}
	}

	/**
	 * 濠碉拷濞茶鐣炬担锟�
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2閻楀牊婀伴弬鏉款杻閺傝纭堕敍宀冾啎缂冪晧rue鐞涖劎銇氬ǎ宄版値鐎规矮缍呮稉顓炲瘶閸氱帳ps鐎规矮缍呴敍瀹朼lse鐞涖劎銇氱痪顖滅秹缂佹粌鐣炬担宥忕礉姒涙顓婚弰鐥秗ue Location
			 * API鐎规矮缍呴柌鍥╂暏GPS閸滃瞼缍夌紒婊勮穿閸氬牆鐣炬担宥嗘煙瀵拷
			 * 閿涘瞼顑囨稉锟芥稉顏勫棘閺佺増妲哥�规矮缍卲rovider閿涘瞼顑囨禍灞奸嚋閸欏倹鏆熼弮鍫曟？閺堬拷閻厽妲�2000濮ｎ偆顫楅敍宀�顑囨稉澶夐嚋閸欏倹鏆熺捄婵堫瀲闂傛挳娈ч崡鏇氱秴閺勵垳鑳岄敍宀�顑囬崶娑楅嚋閸欏倹鏆熼弰顖氱暰娴ｅ秶娲冮崥顒冿拷锟�
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	/**
	 * 閸嬫粍顒涚�规矮缍�
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}
}

