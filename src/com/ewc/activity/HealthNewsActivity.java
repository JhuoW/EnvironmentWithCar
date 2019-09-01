package com.ewc.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.ewc.adapter.HealthNewsAdapter;
import com.ewc.base.C;
import com.ewc.bean.GetObjectFromService;
import com.ewc.bean.PostModel;
import com.ewc.bean.User;
import com.ewc.db.PreferenceMap;
import com.ewc.https.WebService;
import com.ewc.tools.SimpleNetTask;
import com.ewc.ui.xlist.XListView;
import com.ewc.view.HeaderLayout;
import com.example.environmentwithcar.R;

public class HealthNewsActivity extends BaseActivity implements XListView.IXListViewListener{

	private List<PostModel> datas = new ArrayList<PostModel>();
	private HealthNewsAdapter adapter;
	XListView helthlist;
	User user ; 
	ProgressDialog dialog;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_healthnews);
		user = new PreferenceMap(ctx).getUser();
		initView();
		initAction();
	}

	private void initView() {
		dialog = new ProgressDialog(ctx);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage("加载中......");
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		helthlist = (XListView) findViewById(R.id.collection_list);
		helthlist.setPullRefreshEnable(true);
		helthlist.setPullLoadEnable(false);
		helthlist.setXListViewListener(this);
		adapter=new HealthNewsAdapter(HealthNewsActivity.this, datas, R.layout.lay_post_lv_item);
		helthlist.setAdapter(adapter);
		headerLayout.showTitle("健康资讯");
	}

	private void initAction() {
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HealthNewsActivity.this.finish();
			}
		});

		helthlist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				 Intent intent = new Intent(HealthNewsActivity.this,
				 PostDetailActivity.class);
				 intent.putExtra("post", datas.get(position-1));
				 startActivity(intent);
				 overridePendingTransition(R.anim.slide_in_right,
				 R.anim.slide_out_left);
			}
		});
	}

	
	private void initData(){
//		adapter=new HealthNewsAdapter(HealthNewsActivity.this, datas, R.layout.lay_post_lv_item);
//		helthlist.setAdapter(adapter);
		dialog.show();
		param.clear();
		param.put("phone",user.getPhone());
		
		new SimpleNetTask(ctx) {
		   List<PostModel> temp = new ArrayList<PostModel>();
			@Override
			protected void onSucceed() {
				dialog.dismiss();
				//adapter=new HealthNewsAdapter(ctx, datas, R.layout.lay_post_lv_item);
				datas.clear();
				datas.addAll(temp);
				adapter.notifyDataSetChanged();
				
			}
			
			@Override
			protected void doInBack() throws Exception {
				String jsonStr = new WebService(C.GETHEALTHNEWSLIST, param).getReturnInfo();
				temp = GetObjectFromService.getHealthNewsList(jsonStr);
			}
		}.execute();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		param.clear();
		param.put("phone",user.getPhone());
		new SimpleNetTask(ctx) {
		   List<PostModel> temp = new ArrayList<PostModel>();
			@Override
			protected void onSucceed() {
				helthlist.stopRefresh();
				//adapter=new HealthNewsAdapter(ctx, datas, R.layout.lay_post_lv_item);
				datas.clear();
				datas.addAll(temp);
				adapter.notifyDataSetChanged();
			}
			
			@Override
			protected void doInBack() throws Exception {
				String jsonStr = new WebService(C.GETHEALTHNEWSLIST, param).getReturnInfo();
				temp = GetObjectFromService.getHealthNewsList(jsonStr);
			}
		}.execute();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
}
