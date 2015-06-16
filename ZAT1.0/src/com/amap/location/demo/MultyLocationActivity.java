package com.amap.location.demo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.baoan.baoan_submit;


/**
 * 娣峰悎瀹氫綅绀轰緥
 * */
public class MultyLocationActivity extends Activity implements LocationSource,
		AMapLocationListener, OnCheckedChangeListener {
	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private RadioGroup mGPSModeGroup;
	private String location;//保存当前位置
	private double geoLat,geoLng;//经纬度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 涓嶆樉绀虹▼搴忕殑鏍囬鏍�
		setContentView(R.layout.activity_multy_location);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 姝ゆ柟娉曞繀椤婚噸鍐�
		init();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
	    menu.add("我要报案");
		//getMenuInflater().inflate(R.menu.baoan_menu, menu);
		return true;
	}
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}
	
	@Override    
	 public boolean onOptionsItemSelected(MenuItem item) {  
	       // TODO Auto-generated method stub     
		switch (item.getItemId()) {  
	    	case 0: // do something here           
	    		     Toast toast = Toast.makeText(MultyLocationActivity.this, "填写报案信息", Toast.LENGTH_SHORT); 
	    		     toast.show();
	    		 	Intent forcastIntent=new Intent(MultyLocationActivity.this,baoan_submit.class);
	    		 	forcastIntent.putExtra("location_now", location);
	    		 	forcastIntent.putExtra("geoLat", geoLat);
	    		 	forcastIntent.putExtra("geoLng", geoLng);
	    			startActivity(forcastIntent);
	    			break;      
	    			}
		return true;
	 
	  }
	/**
	 * 鍒濆鍖�
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		mGPSModeGroup = (RadioGroup) findViewById(R.id.gps_radio_group);
		mGPSModeGroup.setOnCheckedChangeListener(this);
	}

	/**
	 * 璁剧疆涓�浜沘map鐨勫睘鎬�
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);// 璁剧疆瀹氫綅鐩戝惉
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 璁剧疆榛樿瀹氫綅鎸夐挳鏄惁鏄剧ず
		aMap.setMyLocationEnabled(true);// 璁剧疆涓簍rue琛ㄧず鏄剧ず瀹氫綅灞傚苟鍙Е鍙戝畾浣嶏紝false琛ㄧず闅愯棌瀹氫綅灞傚苟涓嶅彲瑙﹀彂瀹氫綅锛岄粯璁ゆ槸false
		// 璁剧疆瀹氫綅鐨勭被鍨嬩负瀹氫綅妯″紡 锛屽彲浠ョ敱瀹氫綅銆佽窡闅忔垨鍦板浘鏍规嵁闈㈠悜鏂瑰悜鏃嬭浆鍑犵
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.gps_locate_button:
			// 璁剧疆瀹氫綅鐨勭被鍨嬩负瀹氫綅妯″紡
			aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
			break;
		case R.id.gps_follow_button:
			// 璁剧疆瀹氫綅鐨勭被鍨嬩负 璺熼殢妯″紡
			aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
			break;
		case R.id.gps_rotate_button:
			// 璁剧疆瀹氫綅鐨勭被鍨嬩负鏍规嵁鍦板浘闈㈠悜鏂瑰悜鏃嬭浆
			aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
			break;
		}

	}

	/**
	 * 鏂规硶蹇呴』閲嶅啓
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 鏂规硶蹇呴』閲嶅啓
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 鏂规硶蹇呴』閲嶅啓
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 鏂规硶蹇呴』閲嶅啓
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
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
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getAMapException().getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 鏄剧ず绯荤粺灏忚摑鐐�
				//获取位置信息
				Bundle locBundle = amapLocation.getExtras();
				if (locBundle != null) {
				    location = locBundle.getString("desc");
				}
				geoLat = amapLocation.getLatitude();
	            geoLng = amapLocation.getLongitude();  
			} else {
				Log.e("AmapErr","Location ERR:" + amapLocation.getAMapException().getErrorCode());
			}
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
			// 姝ゆ柟娉曚负姣忛殧鍥哄畾鏃堕棿浼氬彂璧蜂竴娆″畾浣嶈姹傦紝涓轰簡鍑忓皯鐢甸噺娑堣�楁垨缃戠粶娴侀噺娑堣�楋紝
			// 娉ㄦ剰璁剧疆鍚堥�傜殑瀹氫綅鏃堕棿鐨勯棿闅旓紙鏈�灏忛棿闅旀敮鎸佷负2000ms锛夛紝骞朵笖鍦ㄥ悎閫傛椂闂磋皟鐢╮emoveUpdates()鏂规硶鏉ュ彇娑堝畾浣嶈姹�
			// 鍦ㄥ畾浣嶇粨鏉熷悗锛屽湪鍚堥�傜殑鐢熷懡鍛ㄦ湡璋冪敤destroy()鏂规硶
			// 鍏朵腑濡傛灉闂撮殧鏃堕棿涓�-1锛屽垯瀹氫綅鍙畾涓�娆�
			// 鍦ㄥ崟娆″畾浣嶆儏鍐典笅锛屽畾浣嶆棤璁烘垚鍔熶笌鍚︼紝閮芥棤闇�璋冪敤removeUpdates()鏂规硶绉婚櫎璇锋眰锛屽畾浣峴dk鍐呴儴浼氱Щ闄�
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 60 * 1000, 10, this);
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
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
	}

}
