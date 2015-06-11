package com.route;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.overlay.BusRouteOverlay;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.location.demo.R;
import com.route.RouteSearchPoiDialog.OnListItemClick;
import com.gongyong.ToastUtil;



public class RouteActivity extends Activity implements OnMarkerClickListener,
OnMapClickListener, OnInfoWindowClickListener, InfoWindowAdapter,
OnPoiSearchListener, OnRouteSearchListener, OnClickListener {
private AMap aMap;
private MapView mapView;
private Button drivingButton;
private Button busButton;
private Button walkButton;

private ImageButton startImageButton;
private ImageButton endImageButton;
private ImageButton routeSearchImagebtn;

private EditText startTextView;
private EditText endTextView;
private ProgressDialog progDialog = null;
private int busMode = RouteSearch.BusDefault;
private int drivingMode = RouteSearch.DrivingDefault;
private int walkMode = RouteSearch.WalkDefault;
private BusRouteResult busRouteResult;
private DriveRouteResult driveRouteResult;
private WalkRouteResult walkRouteResult;// 姝ヨ妯″紡鏌ヨ缁撴灉
private int routeType = 1;// 1浠ｈ〃鍏氦妯″紡锛�2浠ｈ〃椹捐溅妯″紡锛�3浠ｈ〃姝ヨ妯″紡
private String strStart;
private String strEnd;
private LatLonPoint startPoint = null;
private LatLonPoint endPoint = null;
private PoiSearch.Query startSearchQuery;
private PoiSearch.Query endSearchQuery;

private boolean isClickStart = false;
private boolean isClickTarget = false;
private Marker startMk, targetMk;
private RouteSearch routeSearch;
public ArrayAdapter<String> aAdapter;

@Override
protected void onCreate(Bundle bundle) {
super.onCreate(bundle);
requestWindowFeature(Window.FEATURE_NO_TITLE); 
setContentView(R.layout.route_activity);
mapView = (MapView) findViewById(R.id.map);
mapView.onCreate(bundle);// 姝ゆ柟娉曞繀椤婚噸鍐�
init();
}

/**
* 鍒濆鍖朅Map瀵硅薄
*/
private void init() {
if (aMap == null) {
	aMap = mapView.getMap();
	registerListener();
}
routeSearch = new RouteSearch(this);
routeSearch.setRouteSearchListener(this);
startTextView = (EditText) findViewById(R.id.autotextview_roadsearch_start);
endTextView = (EditText) findViewById(R.id.autotextview_roadsearch_goals);
busButton = (Button) findViewById(R.id.imagebtn_roadsearch_tab_transit);
busButton.setOnClickListener(this);
drivingButton = (Button) findViewById(R.id.imagebtn_roadsearch_tab_driving);
drivingButton.setOnClickListener(this);
walkButton = (Button) findViewById(R.id.imagebtn_roadsearch_tab_walk);
walkButton.setOnClickListener(this);
startImageButton = (ImageButton) findViewById(R.id.imagebtn_roadsearch_startoption);
startImageButton.setOnClickListener(this);
endImageButton = (ImageButton) findViewById(R.id.imagebtn_roadsearch_endoption);
endImageButton.setOnClickListener(this);
routeSearchImagebtn = (ImageButton) findViewById(R.id.imagebtn_roadsearch_search);
routeSearchImagebtn.setOnClickListener(this);
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
* 閫夋嫨鍏氦妯″紡
*/
private void busRoute() {
routeType = 1;// 鏍囪瘑涓哄叕浜ゆā寮�
busMode = RouteSearch.BusDefault;
drivingButton.setBackgroundResource(R.drawable.mode_driving_off);
busButton.setBackgroundResource(R.drawable.mode_transit_on);
walkButton.setBackgroundResource(R.drawable.mode_walk_off);

}

/**
* 閫夋嫨椹捐溅妯″紡
*/
private void drivingRoute() {
routeType = 2;// 鏍囪瘑涓洪┚杞︽ā寮�
drivingButton.setBackgroundResource(R.drawable.mode_driving_on);
busButton.setBackgroundResource(R.drawable.mode_transit_off);
walkButton.setBackgroundResource(R.drawable.mode_walk_off);
}

/**
* 閫夋嫨姝ヨ妯″紡
*/
private void walkRoute() {
routeType = 3;// 鏍囪瘑涓烘琛屾ā寮�
walkMode = RouteSearch.WalkMultipath;
drivingButton.setBackgroundResource(R.drawable.mode_driving_off);
busButton.setBackgroundResource(R.drawable.mode_transit_off);
walkButton.setBackgroundResource(R.drawable.mode_walk_on);
}

/**
* 鍦ㄥ湴鍥句笂閫夊彇璧风偣
*/
private void startImagePoint() {
ToastUtil.show(RouteActivity.this, "选择起点");
isClickStart = true;
isClickTarget = false;
registerListener();
}

/**
* 鍦ㄥ湴鍥句笂閫夊彇缁堢偣
*/
private void endImagePoint() {
ToastUtil.show(RouteActivity.this, "选择终点");
isClickTarget = true;
isClickStart = false;
registerListener();
}

/**
* 鐐瑰嚮鎼滅储鎸夐挳寮�濮婻oute鎼滅储
*/
public void searchRoute() {
strStart = startTextView.getText().toString().trim();
strEnd = endTextView.getText().toString().trim();
if (strStart == null || strStart.length() == 0) {
	ToastUtil.show(RouteActivity.this, "请输入起点");
	return;
}
if (strEnd == null || strEnd.length() == 0) {
	ToastUtil.show(RouteActivity.this, "请输入终点");
	return;
}
if (strStart.equals(strEnd)) {
	ToastUtil.show(RouteActivity.this, "起点和终点相同");
	return;
}

startSearchResult();// 寮�濮嬫悳缁堢偣
}

@Override
public void onInfoWindowClick(Marker marker) {
isClickStart = false;
isClickTarget = false;
if (marker.equals(startMk)) {
	startTextView.setText("已选择起点");
	startPoint = AMapUtil.convertToLatLonPoint(startMk.getPosition());
	startMk.hideInfoWindow();
	startMk.remove();
} else if (marker.equals(targetMk)) {
	endTextView.setText("已选择终点");
	endPoint = AMapUtil.convertToLatLonPoint(targetMk.getPosition());
	targetMk.hideInfoWindow();
	targetMk.remove();
}
}

@Override
public boolean onMarkerClick(Marker marker) {
if (marker.isInfoWindowShown()) {
	marker.hideInfoWindow();
} else {
	marker.showInfoWindow();
}
return false;
}

@Override
public void onMapClick(LatLng latng) {
if (isClickStart) {
	startMk = aMap.addMarker(new MarkerOptions()
			.anchor(0.5f, 1)
			.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.point)).position(latng)
			.title("请选择起点"));
	startMk.showInfoWindow();
} else if (isClickTarget) {
	targetMk = aMap.addMarker(new MarkerOptions()
			.anchor(0.5f, 1)
			.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.point)).position(latng)
			.title("请选择重点"));
	targetMk.showInfoWindow();
}
}

@Override
public View getInfoContents(Marker marker) {
return null;
}

@Override
public View getInfoWindow(Marker marker) {
return null;
}

/**
* 娉ㄥ唽鐩戝惉
*/
private void registerListener() {
aMap.setOnMapClickListener(RouteActivity.this);
aMap.setOnMarkerClickListener(RouteActivity.this);
aMap.setOnInfoWindowClickListener(RouteActivity.this);
aMap.setInfoWindowAdapter(RouteActivity.this);
}

/**
* 鏄剧ず杩涘害妗�
*/
private void showProgressDialog() {
if (progDialog == null)
	progDialog = new ProgressDialog(this);
progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
progDialog.setIndeterminate(false);
progDialog.setCancelable(true);
progDialog.setMessage("正在搜索");
progDialog.show();
}

/**
* 闅愯棌杩涘害妗�
*/
private void dissmissProgressDialog() {
if (progDialog != null) {
	progDialog.dismiss();
}
}

/**
* 鏌ヨ璺緞瑙勫垝璧风偣
*/
public void startSearchResult() {
strStart = startTextView.getText().toString().trim();
if (startPoint != null && strStart.equals("startSearchResult")) {
	endSearchResult();
} else {
	showProgressDialog();
	startSearchQuery = new PoiSearch.Query(strStart, "", "010"); // 绗竴涓弬鏁拌〃绀烘煡璇㈠叧閿瓧锛岀浜屽弬鏁拌〃绀簆oi鎼滅储绫诲瀷锛岀涓変釜鍙傛暟琛ㄧず鍩庡競鍖哄彿鎴栬�呭煄甯傚悕
	startSearchQuery.setPageNum(0);// 璁剧疆鏌ヨ绗嚑椤碉紝绗竴椤典粠0寮�濮�
	startSearchQuery.setPageSize(20);// 璁剧疆姣忛〉杩斿洖澶氬皯鏉℃暟鎹�
	PoiSearch poiSearch = new PoiSearch(RouteActivity.this,
			startSearchQuery);
	poiSearch.setOnPoiSearchListener(this);
	poiSearch.searchPOIAsyn();// 寮傛poi鏌ヨ
}
}

/**
* 鏌ヨ璺緞瑙勫垝缁堢偣
*/
public void endSearchResult() {
strEnd = endTextView.getText().toString().trim();
if (endPoint != null && strEnd.equals("endSearchResult")) {
	searchRouteResult(startPoint, endPoint);
} else {
	showProgressDialog();
	endSearchQuery = new PoiSearch.Query(strEnd, "", "010"); // 绗竴涓弬鏁拌〃绀烘煡璇㈠叧閿瓧锛岀浜屽弬鏁拌〃绀簆oi鎼滅储绫诲瀷锛岀涓変釜鍙傛暟琛ㄧず鍩庡競鍖哄彿鎴栬�呭煄甯傚悕
	endSearchQuery.setPageNum(0);// 璁剧疆鏌ヨ绗嚑椤碉紝绗竴椤典粠0寮�濮�
	endSearchQuery.setPageSize(20);// 璁剧疆姣忛〉杩斿洖澶氬皯鏉℃暟鎹�

	PoiSearch poiSearch = new PoiSearch(RouteActivity.this,
			endSearchQuery);
	poiSearch.setOnPoiSearchListener(this);
	poiSearch.searchPOIAsyn(); // 寮傛poi鏌ヨ
}
}

/**
* 寮�濮嬫悳绱㈣矾寰勮鍒掓柟妗�
*/
public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
showProgressDialog();
final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
		startPoint, endPoint);
if (routeType == 1) {// 鍏氦璺緞瑙勫垝
	BusRouteQuery query = new BusRouteQuery(fromAndTo, busMode, "鍖椾含", 0);// 绗竴涓弬鏁拌〃绀鸿矾寰勮鍒掔殑璧风偣鍜岀粓鐐癸紝绗簩涓弬鏁拌〃绀哄叕浜ゆ煡璇㈡ā寮忥紝绗笁涓弬鏁拌〃绀哄叕浜ゆ煡璇㈠煄甯傚尯鍙凤紝绗洓涓弬鏁拌〃绀烘槸鍚﹁绠楀鐝溅锛�0琛ㄧず涓嶈绠�
	routeSearch.calculateBusRouteAsyn(query);// 寮傛璺緞瑙勫垝鍏氦妯″紡鏌ヨ
} else if (routeType == 2) {// 椹捐溅璺緞瑙勫垝
	DriveRouteQuery query = new DriveRouteQuery(fromAndTo, drivingMode,
			null, null, "");// 绗竴涓弬鏁拌〃绀鸿矾寰勮鍒掔殑璧风偣鍜岀粓鐐癸紝绗簩涓弬鏁拌〃绀洪┚杞︽ā寮忥紝绗笁涓弬鏁拌〃绀洪�旂粡鐐癸紝绗洓涓弬鏁拌〃绀洪伩璁╁尯鍩燂紝绗簲涓弬鏁拌〃绀洪伩璁╅亾璺�
	routeSearch.calculateDriveRouteAsyn(query);// 寮傛璺緞瑙勫垝椹捐溅妯″紡鏌ヨ
} else if (routeType == 3) {// 姝ヨ璺緞瑙勫垝
	WalkRouteQuery query = new WalkRouteQuery(fromAndTo, walkMode);
	routeSearch.calculateWalkRouteAsyn(query);// 寮傛璺緞瑙勫垝姝ヨ妯″紡鏌ヨ
}
}

@Override
public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {

}

/**
* POI鎼滅储缁撴灉鍥炶皟
*/
@Override
public void onPoiSearched(PoiResult result, int rCode) {
	dissmissProgressDialog();
	if (rCode == 0) {// 杩斿洖鎴愬姛
		if (result != null && result.getQuery() != null
				&& result.getPois() != null && result.getPois().size() > 0) {// 鎼滅储poi鐨勭粨鏋�
			if (result.getQuery().equals(startSearchQuery)) {
				List<PoiItem> poiItems = result.getPois();// 鍙栧緱poiitem鏁版嵁
				RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(
						RouteActivity.this, poiItems);
				dialog.setTitle("请选择地点");
				dialog.show();
				dialog.setOnListClickListener(new OnListItemClick() {
					@Override
					public void onListItemClick(
							RouteSearchPoiDialog dialog,
							PoiItem startpoiItem) {
						startPoint = startpoiItem.getLatLonPoint();
						strStart = startpoiItem.getTitle();
						startTextView.setText(strStart);
						endSearchResult();// 寮�濮嬫悳缁堢偣
					}

				});
			} else if (result.getQuery().equals(endSearchQuery)) {
				List<PoiItem> poiItems = result.getPois();// 鍙栧緱poiitem鏁版嵁
				RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(
						RouteActivity.this, poiItems);
				dialog.setTitle("请选择地点");
				dialog.show();
				dialog.setOnListClickListener(new OnListItemClick() {
					@Override
					public void onListItemClick(
							RouteSearchPoiDialog dialog, PoiItem endpoiItem) {
						endPoint = endpoiItem.getLatLonPoint();
						strEnd = endpoiItem.getTitle();
						endTextView.setText(strEnd);
						searchRouteResult(startPoint, endPoint);// 杩涜璺緞瑙勫垝鎼滅储
					}

				});
			}
		} else {
			ToastUtil.show(RouteActivity.this, R.string.no_result);
		}
	} else if (rCode == 27) {
		ToastUtil.show(RouteActivity.this, R.string.error_network);
	} else if (rCode == 32) {
		ToastUtil.show(RouteActivity.this, R.string.error_key);
	} else {
		ToastUtil.show(RouteActivity.this, getString(R.string.error_other)
				+ rCode);
	}
}

/**
* 鍏氦璺嚎鏌ヨ鍥炶皟
*/
@Override
public void onBusRouteSearched(BusRouteResult result, int rCode) {
dissmissProgressDialog();
if (rCode == 0) {
	if (result != null && result.getPaths() != null
			&& result.getPaths().size() > 0) {
		busRouteResult = result;
		BusPath busPath = busRouteResult.getPaths().get(0);
		aMap.clear();// 娓呯悊鍦板浘涓婄殑鎵�鏈夎鐩栫墿
		BusRouteOverlay routeOverlay = new BusRouteOverlay(this, aMap,
				busPath, busRouteResult.getStartPos(),
				busRouteResult.getTargetPos());
		routeOverlay.removeFromMap();
		routeOverlay.addToMap();
		routeOverlay.zoomToSpan();
	} else {
		ToastUtil.show(RouteActivity.this, R.string.no_result);
	}
} else if (rCode == 27) {
	ToastUtil.show(RouteActivity.this, R.string.error_network);
} else if (rCode == 32) {
	ToastUtil.show(RouteActivity.this, R.string.error_key);
} else {
	ToastUtil.show(RouteActivity.this, getString(R.string.error_other)
			+ rCode);
}
}

/**
* 椹捐溅缁撴灉鍥炶皟
*/
@Override
public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
dissmissProgressDialog();
if (rCode == 0) {
	if (result != null && result.getPaths() != null
			&& result.getPaths().size() > 0) {
		driveRouteResult = result;
		DrivePath drivePath = driveRouteResult.getPaths().get(0);
		aMap.clear();// 娓呯悊鍦板浘涓婄殑鎵�鏈夎鐩栫墿
		DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
				this, aMap, drivePath, driveRouteResult.getStartPos(),
				driveRouteResult.getTargetPos());
		drivingRouteOverlay.removeFromMap();
		drivingRouteOverlay.addToMap();
		drivingRouteOverlay.zoomToSpan();
	} else {
		ToastUtil.show(RouteActivity.this, R.string.no_result);
	}
} else if (rCode == 27) {
	ToastUtil.show(RouteActivity.this, R.string.error_network);
} else if (rCode == 32) {
	ToastUtil.show(RouteActivity.this, R.string.error_key);
} else {
	ToastUtil.show(RouteActivity.this, getString(R.string.error_other)
			+ rCode);
}
}

/**
* 姝ヨ璺嚎缁撴灉鍥炶皟
*/
@Override
public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
dissmissProgressDialog();
if (rCode == 0) {
	if (result != null && result.getPaths() != null
			&& result.getPaths().size() > 0) {
		walkRouteResult = result;
		WalkPath walkPath = walkRouteResult.getPaths().get(0);
		aMap.clear();// 娓呯悊鍦板浘涓婄殑鎵�鏈夎鐩栫墿
		WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this,
				aMap, walkPath, walkRouteResult.getStartPos(),
				walkRouteResult.getTargetPos());
		walkRouteOverlay.removeFromMap();
		walkRouteOverlay.addToMap();
		walkRouteOverlay.zoomToSpan();
	} else {
		ToastUtil.show(RouteActivity.this, R.string.no_result);
	}
} else if (rCode == 27) {
	ToastUtil.show(RouteActivity.this, R.string.error_network);
} else if (rCode == 32) {
	ToastUtil.show(RouteActivity.this, R.string.error_key);
} else {
	ToastUtil.show(RouteActivity.this, getString(R.string.error_other)
			+ rCode);
}
}

@Override
public void onClick(View v) {
switch (v.getId()) {
case R.id.imagebtn_roadsearch_startoption:
	startImagePoint();
	break;
case R.id.imagebtn_roadsearch_endoption:
	endImagePoint();
	break;
case R.id.imagebtn_roadsearch_tab_transit:
	busRoute();
	break;
case R.id.imagebtn_roadsearch_tab_driving:
	drivingRoute();
	break;
case R.id.imagebtn_roadsearch_tab_walk:
	walkRoute();
	break;
case R.id.imagebtn_roadsearch_search:
	searchRoute();
	break;
default:
	break;
}
}
}
