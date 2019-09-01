package com.ewc.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.ab.image.AbImageCache;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.ewc.activity.ChangePasswordActivity;
import com.ewc.activity.HealthNewsActivity;
import com.ewc.activity.LoginActivity;
import com.ewc.activity.MainActivity;
import com.ewc.activity.MyHealthNewsCollectionActivity;
import com.ewc.base.App;
import com.ewc.bean.User;
import com.ewc.db.PreferenceMap;
import com.ewc.utils.Utils;
import com.example.environmentwithcar.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainMenuFragment extends Fragment {
	private List<HashMap<String, Object>> list = null;
	private ListView mAbPullListView = null;
	private SimpleAdapter adapter;
	private Button btn_quitLogin;
	private Button btn_clear_save;
	private TextView tv_name ;
	
	
	private TextView tv_authority ;
	
	private User curUser ;
	
	public static ImageLoader imageLoader = ImageLoader.getInstance();

	@SuppressLint("InflateParams") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.main_menu, null);
		
		curUser = new PreferenceMap(getActivity()).getUser();
		
		//获取ListView对象
		mAbPullListView = (ListView) view.findViewById(R.id.menu_list);
		btn_quitLogin = (Button) view.findViewById(R.id.quitLogin);
		btn_clear_save = (Button) view.findViewById(R.id.clear_save);
		tv_name = (TextView) view.findViewById(R.id.user_name);
		tv_authority = (TextView) view.findViewById(R.id.user_authority);
		tv_authority.setText(curUser.getPhone());
		tv_name.setText(curUser.getUserName());
		btn_quitLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
				.showCancelButton(true).setTitleText("是否确定退出登录？").setConfirmText("确定")
				.setCancelText("取消").setConfirmClickListener(new OnSweetClickListener() {
					@Override
					public void onClick(final SweetAlertDialog sweetAlertDialog) {
						// TODO Auto-generated method stub
					
								getActivity().runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										sweetAlertDialog.dismiss();
										User user = new User();
										new PreferenceMap(getActivity()).setUser(user);
										getActivity().finish();
										startActivity(new Intent(getActivity(), LoginActivity.class));

									}
								});
							}

				}).setCancelClickListener(new OnSweetClickListener() {
					
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						// TODO Auto-generated method stub
						sweetAlertDialog.dismiss();
					}
				}).show();
			}
		});
		
		btn_clear_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AbDialogUtil.showProgressDialog(getActivity(),0, "正在清空缓存...");
				AbTask task = new AbTask();
				// 定义异步执行的对象
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskListener() {

					@Override
					public void update() {
						AbDialogUtil.removeDialog(getActivity());
						Toast.makeText(getActivity(), "缓存已清空完成", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void get() {
						try {
							AbFileUtil.clearDownloadFile();
							AbImageCache.getInstance().clearBitmap();
						} catch (Exception e) {
							Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

						}
					};
				});
				task.execute(item);
			}
		});
		
		list = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("key", "用户");
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("key", "我的收藏");
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("key", "反馈意见");
		HashMap<String, Object> map5 = new HashMap<String, Object>(); 
		map5.put("key", "修改密码");
		HashMap<String, Object> map6 = new HashMap<String, Object>(); 
		map6.put("key", "健康资讯");
		HashMap<String, Object> map7 = new HashMap<String, Object>(); 
		map7.put("key", "我的位置");
		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map5);
		list.add(map6);
		list.add(map7);
		//使用自定义Adapter
		adapter = new SimpleAdapter(getActivity(), list, android.R.layout.simple_list_item_1, new String[]{"key"}, new int[]{android.R.id.text1});
		
		mAbPullListView.setAdapter(adapter);
		
		//item点击事件
		mAbPullListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position == 3){
					Utils.goActivity(getActivity(), ChangePasswordActivity.class);
				}else if (position == 4) {
					Utils.goActivity(getActivity(), HealthNewsActivity.class);
				}else if(position == 1){
					Utils.goActivity(getActivity(), MyHealthNewsCollectionActivity.class);
				}else if(position == 5){
					MainActivity.getInstance().isFirstLoc = true;
					MainActivity.getInstance().mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
							MainActivity.getInstance().mCurrentMode, true, MainActivity.getInstance().mCurrentMarker));
					// 开启定位图层
					MainActivity.getInstance().mBaiduMap.setMyLocationEnabled(true);
					// 定位初始化
					MainActivity.getInstance().mLocationClient.registerLocationListener(MainActivity.getInstance().myListener);
					LocationClientOption option = new LocationClientOption();
					option.setOpenGps(true);// 打开gps
					option.setCoorType("bd09ll"); // 设置坐标类型
					option.setScanSpan(1000);
					option.setAddrType("all");
					MainActivity.getInstance().mLocationClient.setLocOption(option);
					MainActivity.getInstance().mLocationClient.start();
				}
			}
		});
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		User curUser = new PreferenceMap(App.ctx).getUser();
		tv_name.setText(curUser.getUserName());
		System.out.println("curUser--->"+ curUser.getUserName());
	}
}
