package com.jingyuan;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

















import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

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
import com.amap.location.demo.MultyLocationActivity;
import com.amap.location.demo.R;
import com.baoan.baoan_submit;
import com.gongyong.Case;
import com.gongyong.Constants;
import com.gongyong.HttpConnSoap2;
import com.gongyong.ToastUtil;
import com.gongyong.XMLParase;

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
import android.widget.Toast;

//在地图上面显示案件-警员
public class Event_review_activity extends Activity implements 
	OnInfoWindowClickListener, OnMapLoadedListener,
	OnClickListener, InfoWindowAdapter,LocationSource,
	AMapLocationListener{
	//	private MarkerOptions markerOption;
	//	private TextView markerText;
	//private Button markerButton;
	//private RadioGroup radioOption;
	private String id;//警员警号
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
		setContentView(R.layout.jingyuan_view);
		id = getIntent().getStringExtra("id");//警员警号
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState); 
		init();
	}

	private void init() {
		//markerText = (TextView) findViewById(R.id.mark_listenter_text);
		//radioOption = (RadioGroup) findViewById(R.id.custom_info_window_options);
		//markerButton = (Button) findViewById(R.id.marker_button);
		//markerButton.setOnClickListener(this);		
		Button clearMap = (Button) findViewById(R.id.clearMap);
		clearMap.setOnClickListener(this);
		Button resetMap = (Button) findViewById(R.id.resetMap);
		resetMap.setOnClickListener(this);
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	private void setUpMap() {
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 璁剧疆灏忚摑鐐圭殑鍥炬爣
		myLocationStyle.strokeColor(Color.BLACK);// 璁剧疆鍦嗗舰鐨勮竟妗嗛鑹�
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 璁剧疆鍦嗗舰鐨勫～鍏呴鑹�
		// myLocationStyle.anchor(int,int)//璁剧疆灏忚摑鐐圭殑閿氱偣
		myLocationStyle.strokeWidth(1.0f);// 璁剧疆鍦嗗舰鐨勮竟妗嗙矖缁�
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 璁剧疆瀹氫綅鐩戝惉
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 璁剧疆榛樿瀹氫綅鎸夐挳鏄惁鏄剧ず
		aMap.setMyLocationEnabled(true);// 璁剧疆涓簍rue琛ㄧず鏄剧ず瀹氫綅灞傚苟鍙Е鍙戝畾浣嶏紝false琛ㄧず闅愯棌瀹氫綅灞傚苟涓嶅彲瑙﹀彂瀹氫綅锛岄粯璁ゆ槸false
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

	//有点问题，只显示一个
	//向地图上添加maker-要隔几秒就自动检测一次然后刷新一下
	private void addMarkersToMap() {
		cases = new ArrayList<Case>();
		new Thread(){
			public void run() {
				try{		
					HttpConnSoap2 webservice = new HttpConnSoap2();  
					String methodName = "getAnJian";//方法名  
					ArrayList<String> paramList = new ArrayList<String>();  
					ArrayList<String> parValueList = new ArrayList<String>();
					paramList.add ("id");//指定参数名  
					parValueList.add ("001");//指定参数值  
					InputStream inputStream = webservice.GetWebServre (methodName, paramList, parValueList);  
					cases = XMLParase.paraseCommentInfors (inputStream);
					//然后显示到地图上
					String loca = "";
					if(cases.size()>0){
						for(int i = 0;i<cases.size();i++){
							loca = cases.get(i).getLocation();
							final Marker marker1=aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
									.position(new LatLng(Double.parseDouble(loca.split(",")[0]), Double.parseDouble(loca.split(",")[1])))
									.title(cases.get(i).getName()+","+cases.get(i).getDes())
									.icon(BitmapDescriptorFactory
											.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
									.draggable(true));
							new Runnable() {								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									marker1.showInfoWindow();
								}
							};							
						}
					}									
				}catch(Exception e){
					e.printStackTrace();
				}
			};
		}.start();		
	}

//
//	public void drawMarkers() {
//		Marker marker = aMap.addMarker(new MarkerOptions()
//				.position(latlng)
//				.title("案件描述信息")
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
	
	//传入案子的ID
	protected void dialog(final int cId) {
		Log.i("check", "ok-click");
		  AlertDialog.Builder builder = new Builder(Event_review_activity.this);
		  builder.setMessage("是否确认处理该事件");
		  builder.setTitle("提示");
		  builder.setPositiveButton("取消",new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}});
		  builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// 任务正在进行中
					try{
						HttpPost request = new HttpPost(com.gongyong.Constants.SERVER_URL+"/caseDoing"); 
						request.addHeader("Content-Type", "application/json; charset=utf-8"); 
						JSONObject jsonParams = new JSONObject();
						jsonParams.put("id", cId);
						jsonParams.put("pId", Integer.parseInt(id));
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
					Intent intent = new Intent (Event_review_activity.this,Event_description_avtivity.class);	
					intent.putExtra("caseId", cId);					
					startActivity(intent);	
				}});
		  builder.create().show();
		 }
	
	//信息窗被点击，然后跳转到相应处理页面
	@Override
	public void onInfoWindowClick(Marker marker) {		
		String a = marker.getId();
		int index = Integer.parseInt(a.substring(6));
		Log.i("marker", cases.get(index-1).getId()+"");
		dialog(cases.get(index-1).getId());
//		Intent intent = new Intent (Event_review_activity.this,SuredealEvent.class);			
//		startActivity(intent);	
//		Intent intent = new Intent (Event_review_activity.this,Event_description_avtivity.class);			
//		startActivity(intent);	
	}
	
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

		case R.id.clearMap:
			if (aMap != null) {
				aMap.clear();
			}
			break;

		case R.id.resetMap:
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
	 * 姝ゆ柟娉曞凡缁忓簾寮�
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
	 * 瀹氫綅鎴愬姛鍚庡洖璋冨嚱鏁�
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// 鏄剧ず绯荤粺灏忚摑鐐�
		}
	}

	/**
	 * 婵�娲诲畾浣�
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2鐗堟湰鏂板鏂规硶锛岃缃畉rue琛ㄧず娣峰悎瀹氫綅涓寘鍚玤ps瀹氫綅锛宖alse琛ㄧず绾綉缁滃畾浣嶏紝榛樿鏄痶rue Location
			 * API瀹氫綅閲囩敤GPS鍜岀綉缁滄贩鍚堝畾浣嶆柟寮�
			 * 锛岀涓�涓弬鏁版槸瀹氫綅provider锛岀浜屼釜鍙傛暟鏃堕棿鏈�鐭槸2000姣锛岀涓変釜鍙傛暟璺濈闂撮殧鍗曚綅鏄背锛岀鍥涗釜鍙傛暟鏄畾浣嶇洃鍚��
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	/**
	 * 鍋滄瀹氫綅
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
