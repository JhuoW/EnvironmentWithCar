package com.ewc.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.ewc.adapter.HealthNewsAdapter;
import com.ewc.base.C;
import com.ewc.bean.GetObjectFromService;
import com.ewc.bean.PostModel;
import com.ewc.bean.User;
import com.ewc.db.PreferenceMap;
import com.ewc.https.WebService;
import com.ewc.tools.NetAsyncTask;
import com.ewc.tools.SimpleNetTask;
import com.ewc.utils.Utils;
import com.ewc.view.HeaderLayout;
import com.example.environmentwithcar.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class MyHealthNewsCollectionActivity extends BaseActivity {

	private List<PostModel> datas = new ArrayList<PostModel>();
	private HealthNewsAdapter adapter;
	ListView helthlist;
	User user ; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_healthnews);
		user = new PreferenceMap(ctx).getUser();
		initView();
		initAction();
		initData();
	}
	
	private void initView() {
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		helthlist = (ListView) findViewById(R.id.collection_list);
		headerLayout.showTitle("我的收藏");
		adapter=new HealthNewsAdapter(MyHealthNewsCollectionActivity.this, datas, R.layout.lay_post_lv_item);
		helthlist.setAdapter(adapter);
	}
	
	private void initAction() {
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyHealthNewsCollectionActivity.this.finish();
			}
		});

		helthlist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 Intent intent = new Intent(MyHealthNewsCollectionActivity.this,
				 PostDetailActivity.class);
				 intent.putExtra("post", datas.get(position-1));
				 startActivity(intent);
				 overridePendingTransition(R.anim.slide_in_right,
				 R.anim.slide_out_left);
			}
		});
		
		
		helthlist.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				new SweetAlertDialog(MyHealthNewsCollectionActivity.this, SweetAlertDialog.WARNING_TYPE)
				.setTitleText("是否取消收藏？").showCancelButton(true).setConfirmText("确定")
				.setCancelText("取消")
				.setConfirmClickListener(new OnSweetClickListener() {
					@Override
					public void onClick(final SweetAlertDialog sweetAlertDialog) {
							new NetAsyncTask(MyHealthNewsCollectionActivity.this,false) {
								boolean result;
									@Override
									protected void onPost(Exception e) {
										if(result){
											sweetAlertDialog.dismiss();
											datas.remove(position);
											adapter.notifyDataSetChanged();
												}else {
													sweetAlertDialog.dismiss();
													Toast.makeText(MyHealthNewsCollectionActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
												}
									}
									@Override
									protected void doInBack() throws Exception {
										Map<String, String> param = new HashMap<String, String>();
										param.put("phone", user.getPhone());
										param.put("healthNewsID",datas.get(position).getId());
										String jsonStr = new WebService(C.DELETEHEALTHNEWSCOLLECTION, param).getReturnInfo().toString();
										result = GetObjectFromService.getSimplyResult(jsonStr);
									}
								}.execute();
					}
				}).setCancelClickListener(new OnSweetClickListener() {
					
					@Override
					public void onClick(SweetAlertDialog sweetAlertDialog) {
						sweetAlertDialog.dismiss();
					}
				}).show();
				
				return true;
			}
		});
	}
		
	
	private void initData(){
		param.clear();
		param.put("phone",user.getPhone() );
		
		new SimpleNetTask(ctx) {
			List<PostModel> temp  = new ArrayList<PostModel>();
			@Override
			protected void onSucceed() {
				if(temp==null){
					Utils.toast("net wrong");
					return;
				}
				datas.clear();
				datas.addAll(temp);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			protected void doInBack() throws Exception {
				String jsonStr = new WebService(C.GETHEALTHNEWSCOLLECTIONLIST, param).getReturnInfo();
				temp = GetObjectFromService.getHealthNewsList(jsonStr);
			}
		}.execute();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initData();
	}
}
