package com.ewc.activity;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.ewc.adapter.BaseListAdapter;
import com.ewc.adapter.GroupAdapter;
import com.ewc.adapter.ViewHolder;
import com.ewc.base.C;
import com.ewc.bean.Environment;
import com.ewc.db.DBHelper;
import com.ewc.db.EnvironmentTable;
import com.ewc.https.WebService;
import com.ewc.tools.SimpleNetTask;
import com.ewc.ui.xlist.XListView;
import com.ewc.utils.Utils;
import com.ewc.view.HeaderLayout;
import com.example.environmentwithcar.R;

public class DetailActivity extends BaseActivity implements
		OnItemClickListener, OnItemLongClickListener, OnClickListener,
		XListView.IXListViewListener {

	View showPupWindow = null; // 选择区域的view
	private XListView detailListView;
	private DetailAdapter adapter;
	double Lat;
	double Lng;
	DBHelper dbHelper;
	List<Environment> datas = new ArrayList<Environment>();
	String[] GroupNameArray = { "PM2.5", "PM10", "二氧化硫浓度", "湿度", "温度" };
	ListView groupListView = null;
	GroupAdapter groupAdapter = null;
	TranslateAnimation animation;// 出现的动画效果
	// 屏幕的宽高
	public static int screen_width = 0;
	public static int screen_height = 0;
	PopupWindow mPopupWindow = null;
	private LinearLayout areaLayout;
	private boolean[] tabStateArr = new boolean[1];// 标记tab的选中状态，方便设置
	private ImageView areaImg;
	private TextView areaText;

	String state;

	ProgressDialog dialog;

	LinearLayout linearLayout;
	LinearLayout linearLayout2;
	LinearLayout linearLayout3;
	LinearLayout linearLayout4;
	LinearLayout linearLayout5;
	// 用于存放每条折线的点数据
	private XYSeries line1, line2, line3, line4, line5;
	// 用于存放所有需要绘制的XYSeries
	private XYMultipleSeriesDataset mDataset, mDataset2, mDataset3, mDataset4,
			mDataset5;
	// 用于存放每条折线的风格
	private XYSeriesRenderer renderer1, renderer2, renderer3, renderer4,
			renderer5;
	// 用于存放所有需要绘制的折线的风格
	private XYMultipleSeriesRenderer mXYMultipleSeriesRenderer,
			mXYMultipleSeriesRenderer2, mXYMultipleSeriesRenderer3,
			mXYMultipleSeriesRenderer4, mXYMultipleSeriesRenderer5;
	private GraphicalView chart, chart2, chart3, chart4, chart5;
	LinearLayout[] mll ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		Intent intent = getIntent();
		Lat = intent.getDoubleExtra("Lat", 0);
		Lng = intent.getDoubleExtra("Lng", 0);
		dbHelper = new DBHelper(ctx);
		initCategory();
		initView();
		initHeader();
		getData();
	}

	private void initCategory() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
		screen_width = dm.widthPixels;
		screen_height = dm.heightPixels;
	}

	private void initView() {
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("详情");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DetailActivity.this.finish();
			}
		});
		headerLayout.showRightTextButton("查询", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(DetailActivity.this,
						TimeDetailsActivity.class).putExtra("Lng", Lng + "")
						.putExtra("Lat", Lat + ""));
			}
		});

		dialog = new ProgressDialog(ctx);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage("查询中......");
		areaLayout = (LinearLayout) findViewById(R.id.area_layout);
		areaImg = (ImageView) findViewById(R.id.area_img);
		areaLayout.setOnClickListener(this);
		int[] location = new int[2];
		areaLayout.getLocationOnScreen(location);// 获取控件在屏幕中的位置,方便展示Popupwindow

		animation = new TranslateAnimation(0, 0, -700, location[1]);
		animation.setDuration(500);

		detailListView = (XListView) findViewById(R.id.detail_listview);
		detailListView.setPullRefreshEnable(true);
		detailListView.setPullLoadEnable(false);
		detailListView.setXListViewListener(this);
		areaText = (TextView) findViewById(R.id.area_textView);
		state = areaText.getText().toString();

		// 初始化header
		LayoutInflater mInflater = LayoutInflater.from(ctx);
		LinearLayout headView = (LinearLayout) mInflater.inflate(
				R.layout.header_detail, null);
		linearLayout = (LinearLayout) headView.findViewById(R.id.guangzhexian);
		linearLayout2 = (LinearLayout) headView.findViewById(R.id.guangzhexian2);
		linearLayout3 = (LinearLayout) headView.findViewById(R.id.guangzhexian3);
		linearLayout4 = (LinearLayout) headView.findViewById(R.id.guangzhexian4);
		linearLayout5 = (LinearLayout) headView.findViewById(R.id.guangzhexian5);
		detailListView.addHeaderView(headView);
		adapter = new DetailAdapter(ctx, state, datas, R.layout.item_detail);
		mll =new LinearLayout[]{linearLayout,linearLayout2,linearLayout3,linearLayout4,linearLayout5};
		detailListView.setAdapter(adapter);
	}

	
	public void initHeader(){
		line1 = new XYSeries("PM2.5");
		line2 = new XYSeries("PM10");
		line3 = new XYSeries("SO2");
		line4 = new XYSeries("Hum");
		line5 = new XYSeries("Tem");

		renderer1 = new XYSeriesRenderer();
		renderer2 = new XYSeriesRenderer();
		renderer3 = new XYSeriesRenderer();
		renderer4 = new XYSeriesRenderer();
		renderer5 = new XYSeriesRenderer();
		mDataset = new XYMultipleSeriesDataset();
		mDataset2 = new XYMultipleSeriesDataset();
		mDataset3 = new XYMultipleSeriesDataset();
		mDataset4 = new XYMultipleSeriesDataset();
		mDataset5 = new XYMultipleSeriesDataset();

		mXYMultipleSeriesRenderer = new XYMultipleSeriesRenderer();
		mXYMultipleSeriesRenderer2 = new XYMultipleSeriesRenderer();
		mXYMultipleSeriesRenderer3 = new XYMultipleSeriesRenderer();
		mXYMultipleSeriesRenderer4 = new XYMultipleSeriesRenderer();
		mXYMultipleSeriesRenderer5 = new XYMultipleSeriesRenderer();
		
		
		initRenderer(renderer1, Color.RED,
				PointStyle.CIRCLE, true);
		initRenderer(renderer2, Color.RED,
				PointStyle.CIRCLE, true);
		initRenderer(renderer3, Color.RED,
				PointStyle.CIRCLE, true);
		initRenderer(renderer4, Color.RED,
				PointStyle.CIRCLE, true);
		initRenderer(renderer5, Color.RED,
				PointStyle.CIRCLE, true);
		
		
		for(int i = 0;i<5;i++){
			mll[i].setVisibility(View.GONE);
		}
		mll[0].setVisibility(View.VISIBLE);
	}
	
	private void getData() {
		dialog.show();
		new SimpleNetTask(ctx) {
			List<Environment> temp = new ArrayList<Environment>();

			@Override
			protected void onSucceed() {
				// TODO Auto-generated method stub
				dialog.dismiss();
				detailListView.stopRefresh();
				if (temp == null) {
					Utils.showtoast(ctx, R.drawable.tips_error, "获取数据失败");
				} else {
					datas.clear();
					datas.addAll(temp);
					adapter.notifyDataSetChanged();
					
					
					line1.clear();
					line2.clear();
					line3.clear();
					line4.clear();
					line5.clear();
					mDataset.clear();
					mDataset2.clear();
					mDataset3.clear();
					mDataset4.clear();
					mDataset5.clear();
					mXYMultipleSeriesRenderer.removeAllRenderers();
					mXYMultipleSeriesRenderer2.removeAllRenderers();
					mXYMultipleSeriesRenderer3.removeAllRenderers();
					mXYMultipleSeriesRenderer4.removeAllRenderers();
					mXYMultipleSeriesRenderer5.removeAllRenderers();

					
					for (int i = 0; i < datas.size(); i++) {
						line1.add(i, Double.parseDouble(datas.get(i)
								.getPm25()));
						line2.add(i, Double.parseDouble(datas.get(i).getPm10()));
						line3.add(i, Double.parseDouble(datas.get(i).getSo()));
						line4.add(i, Double.parseDouble(datas.get(i).getHum()));
						line5.add(i, Double.parseDouble(datas.get(i).getTem()));

					}
//					initRenderer(renderer1, Color.RED,
//							PointStyle.CIRCLE, true);
//					initRenderer(renderer2, Color.RED,
//							PointStyle.CIRCLE, true);
//					initRenderer(renderer3, Color.RED,
//							PointStyle.CIRCLE, true);
//					initRenderer(renderer4, Color.RED,
//							PointStyle.CIRCLE, true);
//					initRenderer(renderer5, Color.RED,
//							PointStyle.CIRCLE, true);
					mDataset.addSeries(line1);
					mDataset2.addSeries(line2);
					mDataset3.addSeries(line3);
					mDataset4.addSeries(line4);
					mDataset5.addSeries(line5);

					mXYMultipleSeriesRenderer
							.addSeriesRenderer(renderer1);
					mXYMultipleSeriesRenderer2.addSeriesRenderer(renderer2);
					mXYMultipleSeriesRenderer3.addSeriesRenderer(renderer3);
					mXYMultipleSeriesRenderer4.addSeriesRenderer(renderer4);
					mXYMultipleSeriesRenderer5.addSeriesRenderer(renderer5);

					// 配置chart参数
					setChartSettings("PM2.5",mXYMultipleSeriesRenderer, "X",
							"PM2.5",-1, datas.size()+2, 0, 120, Color.BLACK, Color.BLUE);
					setChartSettings("PM10",mXYMultipleSeriesRenderer2, "X",
							"PM10",-1, datas.size()+2, 0, 130, Color.BLACK, Color.BLUE);
					setChartSettings("SO2",mXYMultipleSeriesRenderer3, "X",
							"SO2",-1, datas.size()+2, 0, 150, Color.BLACK, Color.BLUE);
					setChartSettings("湿度",mXYMultipleSeriesRenderer4, "X",
							"湿度",-1, datas.size()+2, 0, 100, Color.BLACK, Color.BLUE);
					setChartSettings("温度",mXYMultipleSeriesRenderer5, "X",
							"温度",-1, datas.size()+2, -20, 50, Color.BLACK, Color.BLUE);
					// 通过该函数获取到一个View 对象
					chart = ChartFactory.getLineChartView(DetailActivity.this,  //不能用this
							mDataset, mXYMultipleSeriesRenderer);
					chart2 = ChartFactory.getLineChartView(DetailActivity.this,  //不能用this
							mDataset2, mXYMultipleSeriesRenderer2);
					chart3 = ChartFactory.getLineChartView(DetailActivity.this,  //不能用this
							mDataset3, mXYMultipleSeriesRenderer3);
					chart4 = ChartFactory.getLineChartView(DetailActivity.this,  //不能用this
							mDataset4, mXYMultipleSeriesRenderer4);
					chart5 = ChartFactory.getLineChartView(DetailActivity.this,  //不能用this
							mDataset5, mXYMultipleSeriesRenderer5);
					linearLayout.addView(chart, new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					linearLayout2.addView(chart2, new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					linearLayout3.addView(chart3, new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					linearLayout4.addView(chart4, new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					linearLayout5.addView(chart5, new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
					
				}

			}

			@Override
			protected void doInBack() throws Exception {
				// TODO Auto-generated method stub
				param.clear();
				param.put("Lng", Lng + ""); // 经度
				param.put("Lat", Lat + ""); // 纬度
				param.put("NeedCount", 20 + "");
				String jsonStr = new WebService(C.GETPOINTDATA, param)
						.getReturnInfo();
				JSONObject json = new JSONObject(jsonStr);
				JSONArray jsonarray = json.getJSONArray("Points");
				dbHelper.deleteAllDataFromTable(EnvironmentTable.TABLENAME);
				dbHelper.insertEnvironment(jsonarray);
				temp = dbHelper.getEnvironmentList();
			}
		}.execute();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.area_layout:
			tabStateArr[0] = !tabStateArr[0];
			setTabState(areaImg, areaText, tabStateArr[0]);
			if (tabStateArr[0]) {// 判断是否需要关闭弹出层
				showPupupWindow();
			} else {
				mPopupWindow.dismiss();
			}
			break;
		}
	}

	/**
	 * 初始化 PopupWindow
	 * 
	 * @param view
	 */
	public void initPopuWindow(View view) {
		/* 第一个参数弹出显示view 后两个是窗口大小 */
		mPopupWindow = new PopupWindow(view, screen_width, screen_height);
		/* 设置背景显示 */
		mPopupWindow.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.mypop_bg));
		/* 设置触摸外面时消失 */
		// mPopupWindow.setOutsideTouchable(true);

		mPopupWindow.update();
		mPopupWindow.setTouchable(true);
		/* 设置点击menu以外其他地方以及返回键退出 */
		mPopupWindow.setFocusable(true);

		view.setFocusableInTouchMode(true);
	}

	/**
	 * 展示区域选择的对话框
	 */
	@SuppressLint("InflateParams")
	private void showPupupWindow() {
		if (mPopupWindow == null) {
			showPupWindow = LayoutInflater.from(this).inflate(
					R.layout.bottom_layout2, null);
			initPopuWindow(showPupWindow);

			groupListView = (ListView) showPupWindow
					.findViewById(R.id.listView1);

			groupAdapter = new GroupAdapter(this, GroupNameArray);
			groupListView.setAdapter(groupAdapter);
		}

		groupListView.setOnItemClickListener(new MyItemClick());

		showPupWindow.setAnimation(animation);
		showPupWindow.startAnimation(animation);

		mPopupWindow.showAsDropDown(areaLayout, -5, 10);

	}

	class MyItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			groupAdapter.setSelectedPosition(position);
			groupAdapter.notifyDataSetChanged();
			state = groupAdapter.getItem(position).toString();
			areaText.setText(state);
			// { "PM2.5", "PM10", "二氧化硫浓度", "湿度", "温度" }
			if (state.equals("PM2.5")) {
				mll[0].setVisibility(View.VISIBLE);
				for(int i = 1;i<5;i++){
				mll[i].setVisibility(View.GONE);
				}
				adapter = new DetailAdapter(ctx, state, datas,
						R.layout.item_detail);
				detailListView.setAdapter(adapter);
			} else if (state.equals("PM10")) {
				// adapter.notifyDataSetChanged();
				mll[0].setVisibility(View.GONE);
				mll[1].setVisibility(View.VISIBLE);
				for(int i = 2;i<5;i++){
				mll[i].setVisibility(View.GONE);
				}
				adapter = new DetailAdapter(ctx, state, datas,
						R.layout.item_detail);
				detailListView.setAdapter(adapter);
			} else if (state.equals("二氧化硫浓度")) {
				mll[0].setVisibility(View.GONE);
				mll[1].setVisibility(View.GONE);
				mll[2].setVisibility(View.VISIBLE);
				for(int i = 3;i<5;i++){
				mll[i].setVisibility(View.GONE);
				}
				adapter = new DetailAdapter(ctx, state, datas,
						R.layout.item_detail);
				detailListView.setAdapter(adapter);
			} else if (state.equals("湿度")) {
				mll[0].setVisibility(View.GONE);
				mll[1].setVisibility(View.GONE);
				mll[2].setVisibility(View.GONE);
				mll[3].setVisibility(View.VISIBLE);
				mll[4].setVisibility(View.GONE);
				adapter = new DetailAdapter(ctx, state, datas,
						R.layout.item_detail);
				detailListView.setAdapter(adapter);
			} else if (state.equals("温度")) {
				mll[0].setVisibility(View.GONE);
				mll[1].setVisibility(View.GONE);
				mll[2].setVisibility(View.GONE);
				mll[3].setVisibility(View.GONE);
				mll[4].setVisibility(View.VISIBLE);
				adapter = new DetailAdapter(ctx, state, datas,
						R.layout.item_detail);
				detailListView.setAdapter(adapter);
			} else {
				adapter.notifyDataSetChanged();
			}

		}

	}

	private void setTabState(ImageView img, TextView textView, boolean state) {
		if (state) {// 选中状态
			img.setBackgroundResource(R.drawable.up);
			textView.setTextColor(getResources().getColor(
					R.color.tab_text_pressed_color));
		} else {
			img.setBackgroundResource(R.drawable.down);
			textView.setTextColor(getResources().getColor(
					R.color.tab_text_color));
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		getData();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	public class DetailAdapter extends BaseListAdapter<Environment> {

		String state;

		public DetailAdapter(Context ctx, String state,
				List<Environment> datas, int layoutId) {
			super(ctx, datas, layoutId);
			this.state = state;
			// TODO Auto-generated constructor stub
		}

		@Override
		public void conver(ViewHolder holder, int position, Environment t) {
			// TODO Auto-generated method stub
			holder.setText(R.id.time, t.getTime());
			// { "PM2.5", "PM10", "一氧化硫浓度", "湿度", "温度" }
			if (state.equals("PM2.5")) {
				holder.setText(R.id.state, t.getPm25());
			} else if (state.equals("PM10")) {
				holder.setText(R.id.state, t.getPm10());
			} else if (state.equals("二氧化硫浓度")) {
				holder.setText(R.id.state, t.getSo());
			} else if (state.equals("湿度")) {
				holder.setText(R.id.state, t.getHum());
			} else if (state.equals("温度")) {
				holder.setText(R.id.state, t.getTem());
			} else {
				holder.setText(R.id.state, "");
			}
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
	}
	
	
	private XYSeriesRenderer initRenderer(XYSeriesRenderer renderer, int color,
			PointStyle style, boolean fill) {
		// 设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
		renderer.setColor(color);
		renderer.setPointStyle(style);
		renderer.setFillPoints(fill);
		renderer.setLineWidth(1);
		renderer.setDisplayChartValues(true);
		renderer.setDisplayChartValuesDistance(15);
		renderer.setChartValuesTextSize(15);
		return renderer;
	}

	protected void setChartSettings(String ChartTitle,
			XYMultipleSeriesRenderer mXYMultipleSeriesRenderer, String xTitle,
			String yTitle, double xMin, double xMax, double yMin, double yMax,
			int axesColor, int labelsColor) {
		// 有关对图表的渲染可参看api文档
		mXYMultipleSeriesRenderer.setChartTitle(ChartTitle);
		mXYMultipleSeriesRenderer.setXTitle(xTitle);
		mXYMultipleSeriesRenderer.setYTitle(yTitle);
		mXYMultipleSeriesRenderer.setXAxisMin(xMin);
		mXYMultipleSeriesRenderer.setAxisTitleTextSize(16);
		mXYMultipleSeriesRenderer.setLabelsTextSize(20);
		mXYMultipleSeriesRenderer.setXAxisMax(xMax);
		mXYMultipleSeriesRenderer.setYAxisMin(yMin);
		mXYMultipleSeriesRenderer.setYAxisMax(yMax);
		mXYMultipleSeriesRenderer.setAxesColor(axesColor);
		mXYMultipleSeriesRenderer.setLabelsColor(labelsColor);
		mXYMultipleSeriesRenderer.setShowGrid(false);
		mXYMultipleSeriesRenderer.setGridColor(Color.GRAY);
		mXYMultipleSeriesRenderer.setXLabels(20);
		mXYMultipleSeriesRenderer.setYLabels(10);
		mXYMultipleSeriesRenderer.setXLabelsColor(Color.BLACK);
		mXYMultipleSeriesRenderer.setYLabelsColor(0, Color.BLACK);
		mXYMultipleSeriesRenderer.setXTitle("time");
		mXYMultipleSeriesRenderer.setYLabelsAlign(Align.RIGHT);
		mXYMultipleSeriesRenderer.setPointSize(5f);
		mXYMultipleSeriesRenderer.setShowLegend(true);
		mXYMultipleSeriesRenderer.setInScroll(true);
		mXYMultipleSeriesRenderer.setLegendTextSize(20);
		mXYMultipleSeriesRenderer.setPanEnabled(false, false);// 禁止报表的拖动
        //mXYMultipleSeriesRenderer.getSeriesRendererAt(0).setDisplayChartValues(true);
        mXYMultipleSeriesRenderer.setMargins(new int[] { 33, 33, 20, 24 }); // 设置图形四周的留白    上,左,下，右  
        mXYMultipleSeriesRenderer.setMarginsColor(Color.WHITE);
	}


}
