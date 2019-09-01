package com.ewc.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.ab.view.slidingmenu.SlidingMenu;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMyLocationClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.ewc.adapter.ChildAdapter;
import com.ewc.adapter.GroupAdapter;
import com.ewc.base.App;
import com.ewc.base.C;
import com.ewc.fragment.MainMenuFragment;
import com.ewc.https.WebService;
import com.ewc.poptitle.ActionItem;
import com.ewc.poptitle.TitlePopup;
import com.ewc.poptitle.TitlePopup.OnItemOnClickListener;
import com.ewc.utils.Utils;
import com.ewc.view.HeaderLayout;
import com.example.environmentwithcar.R;

public class MainActivity extends BaseActivity implements OnClickListener ,
OnGetPoiSearchResultListener, OnGetSuggestionResultListener {

	View showPupWindow = null; // ѡ�������view
	ProgressDialog dialog;
	
	static Context ctx;
	
	private double Lng;
	private double Lat;
	AutoCompleteTextView et_password;

	String address;
	int[] GroupNameArray = { R.array.province_item };
	Resources res;
	String[] province;
	String[] singlecity;
	String[][] citys;
	int[] city = { R.array.beijin_province_item, R.array.tianjin_province_item,
			R.array.heibei_province_item, R.array.shanxi1_province_item,
			R.array.neimenggu_province_item, R.array.liaoning_province_item,
			R.array.jilin_province_item, R.array.heilongjiang_province_item,
			R.array.shanghai_province_item, R.array.jiangsu_province_item,
			R.array.zhejiang_province_item, R.array.anhui_province_item,
			R.array.fujian_province_item, R.array.jiangxi_province_item,
			R.array.shandong_province_item, R.array.henan_province_item,
			R.array.hubei_province_item, R.array.hunan_province_item,
			R.array.guangdong_province_item, R.array.guangxi_province_item,
			R.array.hainan_province_item, R.array.chongqing_province_item,
			R.array.sichuan_province_item, R.array.guizhou_province_item,
			R.array.yunnan_province_item, R.array.xizang_province_item,
			R.array.shanxi2_province_item, R.array.gansu_province_item,
			R.array.qinghai_province_item, R.array.linxia_province_item,
			R.array.xinjiang_province_item, R.array.hongkong_province_item,
			R.array.aomen_province_item, R.array.taiwan_province_item }; // 34

	ListView groupListView = null;
	ListView childListView = null;
	GroupAdapter groupAdapter = null;
	ChildAdapter childAdapter = null;
	TranslateAnimation animation;// ���ֵĶ���Ч��
	// ��Ļ�Ŀ��
	public static int screen_width = 0;
	public static int screen_height = 0;
	PopupWindow mPopupWindow = null;

	private LinearLayout areaLayout, searchLayout;
	private boolean[] tabStateArr = new boolean[2];// ���tab��ѡ��״̬����������
	private ImageView areaImg, WageImg, classImg;
	private TextView areaText, wageText, classText;
	private TitlePopup titlePopup;
	
	public MapView mMapView;
	public BaiduMap mBaiduMap;
	public LocationClient mLocationClient;
	public LocationMode mCurrentMode;
	public BitmapDescriptor mCurrentMarker;
	public OnCheckedChangeListener radioButtonListener;
	public boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	public MyLocationListenner myListener = new MyLocationListenner();
	private SlidingMenu menu;

	Overlay overLay;
	public String curLng;
	public String curLat;
	private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;
	private ArrayAdapter<String> sugAdapter = null;
	private int load_Index = 0;
	LatLng latlng;
	public Button btn_curPoi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ctx = this;
		initView();
		mMapView = (MapView) findViewById(R.id.map_view);
		mBaiduMap = mMapView.getMap();
		mCurrentMode = LocationMode.NORMAL;
		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));
		// ������λͼ��
		mBaiduMap.setMyLocationEnabled(true);
		// ��λ��ʼ��
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);
		option.setAddrType("all");
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		initPoiSearch();
		initAction();
		init();
		setMenu();

	}


	public static void goMainActivity(Activity activity) {
		Intent intent = new Intent(activity, MainActivity.class);
		activity.startActivity(intent);
	}
	
	private void initPoiSearch(){
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		sugAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line);
		et_password.setAdapter(sugAdapter);
		
		/**
		 * ������ؼ��ֱ仯ʱ����̬���½����б�
		 */
		et_password.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.length() <= 0) {
					return;
				}
				String city = areaText.getText()
						.toString();
				/**
				 * ʹ�ý������������ȡ�����б������onSuggestionResult()�и���
				 */
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city(city));
			}
		});

	}
	
	private void init(){
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.addAction(new ActionItem(this, "�ڵ�һ"));
		titlePopup.addAction(new ActionItem(this, "�ڵ��"));
		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {
			
			@Override
			public void onItemClick(ActionItem item, int position) {
				// TODO Auto-generated method stub
				if(position == 0){
					startActivity(new Intent(MainActivity.this, ReceiveOneActivity.class));
				}else if (position == 1) {
					startActivity(new Intent(MainActivity.this, ReceiveTwoActivity.class));
				}
			}
		});
		
	}
	
	private void initView() {
		res = getResources();
		btn_curPoi = (Button) findViewById(R.id.customicon);
		DisplayMetrics dm = new DisplayMetrics();

		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("�����ѯ");
		headerLayout.showRightImageButton(R.drawable.ic_fifter, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				titlePopup.show(v);
			}
		});
		headerLayout.showLeftImageButton(R.drawable.menu_n, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (menu.isMenuShowing()) {
 					menu.showContent();
 				} else {
 					menu.showMenu();
 				}
			}
		});
		getWindowManager().getDefaultDisplay().getMetrics(dm); // ��ȡ�ֻ���Ļ�Ĵ�С
		screen_width = dm.widthPixels;
		screen_height = dm.heightPixels;

		areaLayout = (LinearLayout) findViewById(R.id.area_layout);
		searchLayout = (LinearLayout) findViewById(R.id.search_layout);
		areaImg = (ImageView) findViewById(R.id.area_img);
		areaText = (TextView) findViewById(R.id.area_textView);
		et_password = (AutoCompleteTextView) findViewById(R.id.et_password);
		areaLayout.setOnClickListener(this);
		searchLayout.setOnClickListener(this);

		int[] location = new int[2];
		areaLayout.getLocationOnScreen(location);// ��ȡ�ؼ�����Ļ�е�λ��,����չʾPopupwindow

		animation = new TranslateAnimation(0, 0, -700, location[1]);
		animation.setDuration(500);

	}

	/**
	 * 
	 */
	private void initAction() {
		province = res.getStringArray(R.array.province_item);
	    mBaiduMap.setOnMapClickListener(new OnMapClickListener() {  
	        public void onMapClick(LatLng point) {  
	            //�ڴ˴������¼�  
	        	
//	        	double lng = point.longitude;
//	        	double lat = point.latitude;
//	        	System.out.println("��ǰλ�����꣺"+"lng:"+lng+".."+"lat:"+lat);
	        }  
	        public boolean onMapPoiClick(MapPoi poi) {  
	            //�ڴ˴����ͼ��ע����¼�  
	        	LatLng ll = poi.getPosition();
	        	double lng = ll.longitude;
	        	double lat = ll.latitude;
	        	System.out.println("��ǰλ�����꣺"+"lng:"+lng+".."+"lat:"+lat);
	            return true;  
	        }  
	    });  
	    mBaiduMap.setOnMyLocationClickListener(new OnMyLocationClickListener() {
			
			@Override
			public boolean onMyLocationClick() {
				// TODO Auto-generated method stub
				Utils.showtoast(ctx, R.drawable.tips_success, address+" �������꣺"+ curLng+"γ�����꣺"+curLat);
				startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("Lat", Double.parseDouble(curLat)).putExtra("Lng",  Double.parseDouble(curLng)));
				System.out.println("��ǰλ�ã�"+"���ȣ�"+curLng+"γ�ȣ�"+curLat);
				return true;
			}
		});
	    btn_curPoi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isFirstLoc = true;
				mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
						mCurrentMode, true, mCurrentMarker));
				// ������λͼ��
				mBaiduMap.setMyLocationEnabled(true);
				// ��λ��ʼ��
				mLocationClient.registerLocationListener(myListener);
				LocationClientOption option = new LocationClientOption();
				option.setOpenGps(true);// ��gps
				option.setCoorType("bd09ll"); // ������������
				option.setScanSpan(1000);
				option.setAddrType("all");
				mLocationClient.setLocOption(option);
				mLocationClient.start();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.area_layout:
			tabStateArr[0] = !tabStateArr[0];
			setTabState(areaImg, areaText, tabStateArr[0]);
			if (tabStateArr[0]) {// �ж��Ƿ���Ҫ�رյ�����
				showPupupWindow();
			} else {
				mPopupWindow.dismiss();
			}
			break;
		case R.id.search_layout:
			tabStateArr[1] = !tabStateArr[1];
			et_password.setVisibility(View.VISIBLE);
			searchButtonProcess(v);
			break;
		}
	}

	/**
	 * ����tab��״̬
	 * 
	 * @param img
	 *            // ImageView����
	 * @param textView
	 *            // TextView����
	 * @param state
	 *            // ״̬
	 */
	private void setTabState(ImageView img, TextView textView, boolean state) {
		if (state) {// ѡ��״̬
			img.setBackgroundResource(R.drawable.up);
			textView.setTextColor(getResources().getColor(
					R.color.tab_text_pressed_color));
		} else {
			img.setBackgroundResource(R.drawable.down);
			textView.setTextColor(getResources().getColor(
					R.color.tab_text_color));
		}
	}

	public void initPopuWindow(View view) {
		/* ��һ������������ʾview �������Ǵ��ڴ�С */
		mPopupWindow = new PopupWindow(view, screen_width, screen_height);
		/* ���ñ�����ʾ */
		mPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.mypop_bg));
		/* ���ô�������ʱ��ʧ */
		// mPopupWindow.setOutsideTouchable(true);

		mPopupWindow.update();
		mPopupWindow.setTouchable(true);
		/* ���õ��menu���������ط��Լ����ؼ��˳� */
		mPopupWindow.setFocusable(true);

		/**
		 * 1.����ٴε��MENU���޷�Ӧ���� 2.sub_view��PopupWindow����View
		 */
		view.setFocusableInTouchMode(true);
	}

	/**
	 * չʾ����ѡ��ĶԻ���
	 */
	private void showPupupWindow() {
		if (mPopupWindow == null) {
			showPupWindow = LayoutInflater.from(this).inflate(
					R.layout.bottom_layout, null);
			initPopuWindow(showPupWindow);

			groupListView = (ListView) showPupWindow
					.findViewById(R.id.listView1);
			childListView = (ListView) showPupWindow
					.findViewById(R.id.listView2);

			groupAdapter = new GroupAdapter(this, province);
			groupListView.setAdapter(groupAdapter);
		}

		groupListView.setOnItemClickListener(new MyItemClick());

		childListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String content = childAdapter.getItem(position).toString();

				areaText.setText(content);
			}
		});

		showPupWindow.setAnimation(animation);
		showPupWindow.startAnimation(animation);

		mPopupWindow.showAsDropDown(areaLayout, -5, 10);

	}

	class MyItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			groupAdapter.setSelectedPosition(position);

			if (childAdapter == null) {
				childAdapter = new ChildAdapter(MainActivity.this);
				childListView.setAdapter(childAdapter);
			}

			Message msg = new Message();
			msg.what = 20;
			msg.arg1 = position;
			handler.sendMessage(msg);
		}

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 20:
				childAdapter.setChildData(res.getStringArray(city[msg.arg1]));
				childAdapter.notifyDataSetChanged();
				groupAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}

		};
	};

	@Override
	protected void onDestroy() {
		// �˳�ʱ���ٶ�λ
		mLocationClient.stop();
		// �رն�λͼ��
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		
		mPoiSearch.destroy();
		mSuggestionSearch.destroy();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				latlng = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latlng);
				mBaiduMap.animateMapStatus(u);
				Utils.showtoast(ctx, R.drawable.tips_success, "���ȣ�"+location.getLongitude()+"γ�ȣ�"+location.getLatitude());
				
				curLat = location.getLatitude()+"";
				curLng = location.getLongitude()+"";
				address = location.getAddrStr();
			}
			
		}

		
		public void onReceivePoi(BDLocation poiLocation) {
			Utils.showtoast(ctx, R.drawable.tips_success, "���ȣ�"+poiLocation.getLongitude()+"γ�ȣ�"+poiLocation.getLatitude());
		}
	}

	@Override
	public void onGetSuggestionResult(SuggestionResult result) {
		// TODO Auto-generated method stub
		if (result == null || result.getAllSuggestions() == null) {
			return;
		}
		sugAdapter.clear();
		for (SuggestionResult.SuggestionInfo info : result.getAllSuggestions()) {
			if (info.key != null)
				sugAdapter.add(info.key);
		}
		sugAdapter.notifyDataSetChanged();
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {
		// TODO Auto-generated method stub
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			
			Utils.showtoast(ctx, R.drawable.tips_error, "��Ǹ��δ�ҵ����");
		} else {
//			Toast.makeText(MainActivity.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
//			.show();
			
			Utils.showtoast(ctx, R.drawable.tips_success, result.getName() + ": " + result.getAddress());
			
			LatLng ll = result.getLocation();
			Lat = ll.latitude;
			Lng = ll.longitude;
			//getDate();
			startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("Lat", Lat).putExtra("Lng", Lng));
		}
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		// TODO Auto-generated method stub
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(MainActivity.this, "δ�ҵ����", Toast.LENGTH_LONG)
			.show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			mBaiduMap.clear();
			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();
			return;
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

			// ������ؼ����ڱ���û���ҵ����������������ҵ�ʱ�����ذ����ùؼ�����Ϣ�ĳ����б�
			String strInfo = "��";
			for (CityInfo cityInfo : result.getSuggestCityList()) {
				strInfo += cityInfo.city;
				strInfo += ",";
			}
			strInfo += "�ҵ����";
			Toast.makeText(MainActivity.this, strInfo, Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	/**
	 * Ӱ��������ť����¼�
	 * 
	 * @param v
	 */
	public void searchButtonProcess(View v) {
		mPoiSearch.searchInCity((new PoiCitySearchOption())
				.city(areaText.getText().toString())
				.keyword(et_password.getText().toString())
				.pageNum(load_Index));
	}
	public void goToNextPage(View v) {
		load_Index++;
		searchButtonProcess(null);
	}
	
	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			// if (poi.hasCaterDetails) {
				mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
						.poiUid(poi.uid));
			// }
			return true;
		}
	}
	
	
	
	private void getDate(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				param.put("Lng", Lng+"");  //����
				param.put("Lat", Lat+"");   //γ��
				String jsonStr = new WebService(C.GETPOINTDATA, param).getReturnInfo();
				System.out.println(jsonStr);
				System.out.println("Lng:"+Lng+"---"+"Lat:"+Lat);
				LatLng gtb = gpsToBaidu(latlng);
				double x = 2*Lng-gtb.longitude;
				double y  = 2*Lat-gtb.latitude;
				System.out.println("ת��֮��γ�ȣ�" + "���ȣ�" + x+ "γ�ȣ�"+ y);
			}
		}).start();
	}
	
    private LatLng gpsToBaidu(LatLng sourceLatLng ){
        // ��GPS�豸�ɼ���ԭʼGPS����ת���ɰٶ�����  
        CoordinateConverter converter  = new CoordinateConverter();  
        converter.from(CoordType.GPS);  
        // sourceLatLng��ת������  
        converter.coord(sourceLatLng);  
        return converter.convert(); 
}
    
    
    
 // ������Ӳ໬
 	private void setMenu() {
 		menu = new SlidingMenu(this);
 		menu.setMode(SlidingMenu.LEFT);

 		// slidingmenu���¼�ģʽ����������п��Ի���������TOUCHMODE_MARGIN
 		// �ɽ���¼���ͻ����
 		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

 		// menu.setShadowWidthRes(R.dimen.shadow_width);
 		menu.setShadowDrawable(R.drawable.shadow);
 		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
 		menu.setFadeDegree(0.35f);
 		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

 		// menu��ͼ��Fragment���
 		menu.setMenu(R.layout.sliding_menu_menu);
 		getSupportFragmentManager().beginTransaction()
 				.replace(R.id.menu_frame, new MainMenuFragment()).commit();

 	}
 	
 	public static MainActivity getInstance(){
 		if(null == ctx){
 			ctx = new MainActivity();
 		}
 		return (MainActivity) ctx;
 	}

}

