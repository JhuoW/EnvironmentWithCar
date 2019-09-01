package com.ewc.activity;

import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ewc.base.C;
import com.ewc.bean.Environment;
import com.ewc.bean.GetObjectFromService;
import com.ewc.https.WebService;
import com.ewc.tools.SimpleNetTask;
import com.ewc.view.HeaderLayout;
import com.example.environmentwithcar.R;

/**
 * 数据格式：{"ret":"success", "Lng":"0","Lat":"0", "Tim":"2015-9-5 11:56:24",
 * "PM25":"39", "PM10":"50", "SO":"54", "Hum":"34", "Tem":"31"}
 * 
 * @author Administrator
 * 
 */
public class ReceiveOneActivity extends BaseActivity {

	private TextView tv_time;
	private TextView tv_lng;
	private TextView tv_lat;
	private TextView tv_pm25;
	private TextView tv_pm10;
	private TextView tv_so;
	private TextView tv_hum;
	private TextView tv_tem;
	private TextView tv_date;
	
	Spinner mSpinner;
	
	private Timer timer;

	private XYMultipleSeriesRenderer render;
	private GraphicalView chart;
	private Float addY;
	String[] x = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10" };// x轴缓冲
	Float[] ycache = new Float[10];
	private XYSeries series;// 用来清空第一个再加下一个
	private XYMultipleSeriesDataset dataset1;// xy轴数据源
	LinearLayout linearLayout;

	LinearLayout linearlayout2;
	private GraphicalView chart2;
	private XYMultipleSeriesDataset dataset2;// xy轴数据源
	private XYSeries series2;// 用来清空第一个再加下一个
	private Float addY2;
	Float[] ycache2 = new Float[10];
	String[] x2 = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10" };// x轴缓冲
	private XYMultipleSeriesRenderer render2;

	LinearLayout linearlayout3;
	private GraphicalView chart3;
	private XYMultipleSeriesDataset dataset3;// xy轴数据源
	private XYSeries series3;// 用来清空第一个再加下一个
	private Float addY3;
	Float[] ycache3 = new Float[10];
	String[] x3 = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10" };// x轴缓冲
	private XYMultipleSeriesRenderer render3;
	
	
	LinearLayout linearlayout4;
	private GraphicalView chart4;
	private XYMultipleSeriesDataset dataset4;// xy轴数据源
	private XYSeries series4;// 用来清空第一个再加下一个
	private Float addY4;
	Float[] ycache4 = new Float[10];
	String[] x4 = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10" };// x轴缓冲
	private XYMultipleSeriesRenderer render4;
	
	
	LinearLayout linearlayout5;
	private GraphicalView chart5;
	private XYMultipleSeriesDataset dataset5;// xy轴数据源
	private XYSeries series5;// 用来清空第一个再加下一个
	private Float addY5;
	Float[] ycache5 = new Float[10];
	String[] x5 = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10" };// x轴缓冲
	private XYMultipleSeriesRenderer render5;

	Environment environment;

	LinearLayout[] mll;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_one);
		timer = new Timer();
		findView();
		setTimerTask();
		initAction();
	}

	private void findView() {
		mSpinner = (Spinner) findViewById(R.id.spinner1);  
		String[] mItems = getResources().getStringArray(R.array.spinnername);  
		// 建立Adapter并且绑定数据源  
		ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
		mSpinner.setAdapter(_Adapter);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("节点一最新数据");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ReceiveOneActivity.this.finish();
			}
		});
		linearLayout = (LinearLayout) findViewById(R.id.guangzhexian);
		linearlayout2 = (LinearLayout) findViewById(R.id.guangzhexian2);
		linearlayout3 = (LinearLayout) findViewById(R.id.guangzhexian3);
		linearlayout4 = (LinearLayout) findViewById(R.id.guangzhexian4);
		linearlayout5 = (LinearLayout) findViewById(R.id.guangzhexian5);
		mll =new LinearLayout[]{linearLayout,linearlayout2,linearlayout3,linearlayout4,linearlayout5};
		// 制作PM2.5折线图
		chart = ChartFactory.getLineChartView(this, getdemodataset("PM2.5"),
				getdemorenderer("PM2.5"));
		linearLayout.removeAllViews();// 先remove再add可以实现统计图更新
		linearLayout.addView(chart, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		// 制作PM10折线图
		chart2 = ChartFactory.getLineChartView(this,
				getPM10demodataset("PM10"), getPM10demorenderer("PM10"));
		linearlayout2.removeAllViews();// 先remove再add可以实现统计图更新
		linearlayout2.addView(chart2, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		// 制作SO折线图
		chart3 = ChartFactory.getLineChartView(this, getSOdemodataset("SO2"),
				getSOdemorenderer("SO2"));
		linearlayout3.removeAllViews();// 先remove再add可以实现统计图更新
		linearlayout3.addView(chart3, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		//制作湿度折线图
		chart4 = ChartFactory.getLineChartView(this, getHumdemodataset("湿度"),
				getHumdemorenderer("湿度"));
		linearlayout4.removeAllViews();// 先remove再add可以实现统计图更新
		linearlayout4.addView(chart4, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		//制作温度折线图
		chart5 = ChartFactory.getLineChartView(this, getTemdemodataset("温度"),
				getTemdemorenderer("温度"));
		linearlayout5.removeAllViews();// 先remove再add可以实现统计图更新
		linearlayout5.addView(chart5, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_lng = (TextView) findViewById(R.id.tv_lng);
		tv_lat = (TextView) findViewById(R.id.tv_lat);
		tv_pm25 = (TextView) findViewById(R.id.tv_pm25);
		tv_pm10 = (TextView) findViewById(R.id.tv_pm10);
		tv_so = (TextView) findViewById(R.id.tv_so);
		tv_hum = (TextView) findViewById(R.id.tv_hum);
		tv_tem = (TextView) findViewById(R.id.tv_tem);
		tv_date = (TextView) findViewById(R.id.tv_date);
		
	}
	
	
	private void initAction() {
		// TODO Auto-generated method stub
			mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if(position == 0){
						mll[0].setVisibility(View.VISIBLE);
						for(int i = 1;i<5;i++){
						mll[i].setVisibility(View.GONE);
						}
					}else if(position == 1){
						mll[0].setVisibility(View.GONE);
						mll[1].setVisibility(View.VISIBLE);
						for(int i = 2;i<5;i++){
						mll[i].setVisibility(View.GONE);
						}
					}else if(position == 2){
						mll[0].setVisibility(View.GONE);
						mll[1].setVisibility(View.GONE);
						mll[2].setVisibility(View.VISIBLE);
						for(int i = 3;i<5;i++){
						mll[i].setVisibility(View.GONE);
						}
					}else if(position == 3){
						mll[0].setVisibility(View.GONE);
						mll[1].setVisibility(View.GONE);
						mll[2].setVisibility(View.GONE);
						mll[3].setVisibility(View.VISIBLE);
						mll[4].setVisibility(View.GONE);
					}else if(position == 4){
						mll[0].setVisibility(View.GONE);
						mll[1].setVisibility(View.GONE);
						mll[2].setVisibility(View.GONE);
						mll[3].setVisibility(View.GONE);
						mll[4].setVisibility(View.VISIBLE);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					for(int i = 0;i<4;i++){
						mll[i].setVisibility(View.VISIBLE);
					}
				}
			});
	}

	private void setTimerTask() {
		timer.schedule(new TimerTask() {
			Environment environment;

			@Override
			public void run() {
				param.clear();
				param.put("nodeID", "1");
				String jsonStr = new WebService(C.GETUPDATE, param)
						.getReturnInfo();
				environment = GetObjectFromService.getLoginResult(jsonStr);
				Message message = new Message();
				message.obj = environment;
				message.what = 1;
				doActionHandler.sendMessage(message);
			}
		}, 2000, 2000); /* 表示1000毫秒之后，每隔1000毫秒执行一次 */
	}

	private Handler doActionHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int msgId = msg.what;
			environment = (Environment) msg.obj;

			switch (msgId) {
			case 1:
				tv_time.setText(environment.getTime());
				tv_lng.setText(environment.getLng());
				tv_lat.setText(environment.getLat());
				tv_pm25.setText(environment.getPm25());
				tv_pm10.setText(environment.getPm10());
				tv_so.setText(environment.getSo());
				tv_hum.setText(environment.getHum());
				tv_tem.setText(environment.getTem());
				tv_date.setText(environment.getDate());
				updatechart();
				updatePM10chart();
				updateSOchart();
				updateHumchart();
				updateTemchart();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// cancel timer
		timer.cancel();
	}

	private XYMultipleSeriesRenderer getdemorenderer(String state) {
		// TODO Auto-generated method stub
		// render.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		render = new XYMultipleSeriesRenderer();
		render.setChartTitle("PM2.5实时曲线");
		render.setAxisTitleTextSize(16);// 设置轴标题文字的大小
		render.setAxesColor(Color.BLACK);
		render.setXTitle("时间");
		render.setYTitle(state);
		render.setLabelsTextSize(16);// 设置轴刻度文字的大小
		render.setLabelsColor(Color.BLUE);
		render.setXLabelsColor(Color.BLACK);
		render.setYLabelsColor(0, Color.BLACK);
		render.setLegendTextSize(15);// 设置图例文字大小
		
		// render.setShowLegend(false);//显示不显示在这里设置，非常完美
		XYSeriesRenderer r = new XYSeriesRenderer();// 设置颜色和点类型
		r.setColor(Color.RED);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		// r.setDisplayChartValues(true);
		r.setChartValuesSpacing(3);
		r.setChartValuesTextSize(30);
		render.addSeriesRenderer(r);
		render.setYLabelsAlign(Align.RIGHT);// 刻度值相对于刻度的位置
		render.setShowGrid(true);// 显示网格
		render.setYAxisMax(300);// 设置y轴的范围
		render.setYAxisMin(0);
		render.setYLabels(30);// 分七等份
		render.setXLabels(0);// 设置只显示如1月，2月等替换后的东西，不显示1,2,3等
		render.setInScroll(true);
		render.setLabelsTextSize(14);
		render.getSeriesRendererAt(0).setDisplayChartValues(true);
		// //显示折线上点的数值
		render.setPanEnabled(false, false);// 禁止报表的拖动
		render.setPointSize(5f);// 设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
		render.setMargins(new int[] { 25, 25, 20, 24 }); // 设置图形四周的留白
		render.setMarginsColor(Color.WHITE);
		return render;
	}

	private XYMultipleSeriesRenderer getPM10demorenderer(String state) {
		// TODO Auto-generated method stub
		// render.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		render2 = new XYMultipleSeriesRenderer();
		render2.setChartTitle("PM10实时曲线");
		render2.setAxisTitleTextSize(16);// 设置轴标题文字的大小
		render2.setAxesColor(Color.BLACK);
		render2.setXTitle("时间");
		render2.setYTitle(state);
		render2.setLabelsTextSize(16);// 设置轴刻度文字的大小
		render2.setLabelsColor(Color.BLUE);
		render2.setXLabelsColor(Color.BLACK);
		render2.setYLabelsColor(0, Color.BLACK);
		render2.setLegendTextSize(15);// 设置图例文字大小
		// render.setShowLegend(false);//显示不显示在这里设置，非常完美

		XYSeriesRenderer r = new XYSeriesRenderer();// 设置颜色和点类型
		r.setColor(Color.RED);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		// r.setDisplayChartValues(true);
		r.setChartValuesSpacing(3);
		r.setChartValuesTextSize(30);

		render2.addSeriesRenderer(r);
		render2.setYLabelsAlign(Align.RIGHT);// 刻度值相对于刻度的位置
		render2.setShowGrid(true);// 显示网格
		render2.setYAxisMax(200);// 设置y轴的范围
		render2.setYAxisMin(0);
		render2.setYLabels(20);// 分七等份
		render2.setXLabels(0);// 设置只显示如1月，2月等替换后的东西，不显示1,2,3等
		render2.setInScroll(true);
		render2.setLabelsTextSize(14);
		 render2.getSeriesRendererAt(0).setDisplayChartValues(true);
		// //显示折线上点的数值
		render2.setPanEnabled(false, false);// 禁止报表的拖动
		render2.setPointSize(5f);// 设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
		render2.setMargins(new int[] { 25, 25, 20, 24 }); // 设置图形四周的留白
		render2.setMarginsColor(Color.WHITE);
		return render2;
	}

	private XYMultipleSeriesRenderer getSOdemorenderer(String state) {
		// TODO Auto-generated method stub
		// render.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		render3 = new XYMultipleSeriesRenderer();
		render3.setChartTitle("二氧化硫（SO2）实时曲线");
		render3.setAxisTitleTextSize(16);// 设置轴标题文字的大小
		render3.setAxesColor(Color.BLACK);
		render3.setXTitle("时间");
		render3.setYTitle(state);
		render3.setLabelsTextSize(16);// 设置轴刻度文字的大小
		render3.setLabelsColor(Color.BLUE);
		render3.setXLabelsColor(Color.BLACK);
		render3.setYLabelsColor(0, Color.BLACK);
		render3.setLegendTextSize(15);// 设置图例文字大小
		// render.setShowLegend(false);//显示不显示在这里设置，非常完美

		XYSeriesRenderer r = new XYSeriesRenderer();// 设置颜色和点类型
		r.setColor(Color.RED);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		// r.setDisplayChartValues(true);
		r.setChartValuesSpacing(3);
		r.setChartValuesTextSize(30);

		render3.addSeriesRenderer(r);
		render3.setYLabelsAlign(Align.RIGHT);// 刻度值相对于刻度的位置
		render3.setShowGrid(true);// 显示网格
		render3.setYAxisMax(100);// 设置y轴的范围
		render3.setYAxisMin(10);
		render3.setYLabels(5);// 分七等份
		render3.setXLabels(0);// 设置只显示如1月，2月等替换后的东西，不显示1,2,3等
		render3.setInScroll(true);
		render3.setLabelsTextSize(14);
		 render3.getSeriesRendererAt(0).setDisplayChartValues(true);
		// //显示折线上点的数值
		render3.setPanEnabled(false, false);// 禁止报表的拖动
		render3.setPointSize(5f);// 设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
		render3.setMargins(new int[] { 25, 25, 20, 24 }); // 设置图形四周的留白
		render3.setMarginsColor(Color.WHITE);
		return render3;
	}
	
	
	private XYMultipleSeriesRenderer getHumdemorenderer(String state) {
		// TODO Auto-generated method stub
		// render.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		render4 = new XYMultipleSeriesRenderer();
		render4.setChartTitle("湿度实时曲线");
		render4.setAxisTitleTextSize(16);// 设置轴标题文字的大小
		render4.setAxesColor(Color.BLACK);
		render4.setXTitle("时间");
		render4.setYTitle(state);
		render4.setLabelsTextSize(16);// 设置轴刻度文字的大小
		render4.setLabelsColor(Color.BLUE);
		render4.setXLabelsColor(Color.BLACK);
		render4.setYLabelsColor(0, Color.BLACK);
		render4.setLegendTextSize(15);// 设置图例文字大小
		// render.setShowLegend(false);//显示不显示在这里设置，非常完美

		XYSeriesRenderer r = new XYSeriesRenderer();// 设置颜色和点类型
		r.setColor(Color.RED);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		// r.setDisplayChartValues(true);
		r.setChartValuesSpacing(3);
		r.setChartValuesTextSize(30);

		render4.addSeriesRenderer(r);
		render4.setYLabelsAlign(Align.RIGHT);// 刻度值相对于刻度的位置
		render4.setShowGrid(true);// 显示网格
		render4.setYAxisMax(100);// 设置y轴的范围
		render4.setYAxisMin(10);
		render4.setYLabels(5);// 分七等份
		render4.setXLabels(0);// 设置只显示如1月，2月等替换后的东西，不显示1,2,3等
		render4.setInScroll(true);
		render4.setLabelsTextSize(14);
		 render4.getSeriesRendererAt(0).setDisplayChartValues(true);
		// //显示折线上点的数值
		render4.setPanEnabled(false, false);// 禁止报表的拖动
		render4.setPointSize(5f);// 设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
		render4.setMargins(new int[] { 25, 25, 20, 24 }); // 设置图形四周的留白
		render4.setMarginsColor(Color.WHITE);
		return render4;
	}
	
	
	private XYMultipleSeriesRenderer getTemdemorenderer(String state) {
		// TODO Auto-generated method stub
		// render.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		render5 = new XYMultipleSeriesRenderer();
		render5.setChartTitle("温度实时曲线");
		render5.setAxisTitleTextSize(16);// 设置轴标题文字的大小
		render5.setAxesColor(Color.BLACK);
		render5.setXTitle("时间");
		render5.setYTitle(state);
		render5.setLabelsTextSize(16);// 设置轴刻度文字的大小
		render5.setLabelsColor(Color.BLUE);
		render5.setXLabelsColor(Color.BLACK);
		render5.setYLabelsColor(0, Color.BLACK);
		render5.setLegendTextSize(15);// 设置图例文字大小
		// render.setShowLegend(false);//显示不显示在这里设置，非常完美

		XYSeriesRenderer r = new XYSeriesRenderer();// 设置颜色和点类型
		r.setColor(Color.RED);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		// r.setDisplayChartValues(true);
		r.setChartValuesSpacing(3);
		r.setChartValuesTextSize(30);

		render5.addSeriesRenderer(r);
		render5.setYLabelsAlign(Align.RIGHT);// 刻度值相对于刻度的位置
		render5.setShowGrid(true);// 显示网格
		render5.setYAxisMax(50);// 设置y轴的范围
		render5.setYAxisMin(-20);
		render5.setYLabels(7);// 分七等份
		render5.setXLabels(0);// 设置只显示如1月，2月等替换后的东西，不显示1,2,3等
		render5.setInScroll(true);
		render5.setLabelsTextSize(14);
		render5.getSeriesRendererAt(0).setDisplayChartValues(true);
		// //显示折线上点的数值
		render5.setPanEnabled(false, false);// 禁止报表的拖动
		render5.setPointSize(5f);// 设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
		render5.setMargins(new int[] { 25, 25, 20, 24 }); // 设置图形四周的留白
		render5.setMarginsColor(Color.WHITE);
		return render5;
	}

	private XYMultipleSeriesDataset getdemodataset(String state) {

		// TODO Auto-generated method stub
		dataset1 = new XYMultipleSeriesDataset();// xy轴数据源
		final int nr = 4;// 显示数据个数
		series = new TimeSeries(state);// 这个事是显示多条用的，显不显示在上面render设置
		for (int i = 0; i < nr; i++) {
			series.add(i + 1, 0);// 横坐标date数据类型，纵坐标随即数等待更新
		}
		dataset1.addSeries(series);
		return dataset1;
	}

	private XYMultipleSeriesDataset getPM10demodataset(String state) {

		// TODO Auto-generated method stub
		dataset2 = new XYMultipleSeriesDataset();// xy轴数据源
		final int nr = 4;// 显示数据个数
		series2 = new TimeSeries(state);// 这个事是显示多条用的，显不显示在上面render设置
		for (int i = 0; i < nr; i++) {
			series2.add(i + 1, 0);// 横坐标date数据类型，纵坐标随即数等待更新
		}
		dataset2.addSeries(series2);
		return dataset2;
	}

	private XYMultipleSeriesDataset getSOdemodataset(String state) {

		// TODO Auto-generated method stub
		dataset3 = new XYMultipleSeriesDataset();// xy轴数据源
		final int nr = 4;// 显示数据个数
		series3 = new TimeSeries(state);// 这个事是显示多条用的，显不显示在上面render设置
		for (int i = 0; i < nr; i++) {
			series3.add(i + 1, 0);// 横坐标date数据类型，纵坐标随即数等待更新
		}
		dataset3.addSeries(series3);
		return dataset3;
	}

	private XYMultipleSeriesDataset getHumdemodataset(String state) {

		// TODO Auto-generated method stub
		dataset4 = new XYMultipleSeriesDataset();// xy轴数据源
		final int nr = 4;// 显示数据个数
		series4 = new TimeSeries(state);// 这个事是显示多条用的，显不显示在上面render设置
		for (int i = 0; i < nr; i++) {
			series4.add(i + 1, 0);// 横坐标date数据类型，纵坐标随即数等待更新
		}
		dataset4.addSeries(series4);
		return dataset4;
	}
	
	
	private XYMultipleSeriesDataset getTemdemodataset(String state) {

		// TODO Auto-generated method stub
		dataset5 = new XYMultipleSeriesDataset();// xy轴数据源
		final int nr = 4;// 显示数据个数
		series5 = new TimeSeries(state);// 这个事是显示多条用的，显不显示在上面render设置
		for (int i = 0; i < nr; i++) {
			series5.add(i + 1, 0);// 横坐标date数据类型，纵坐标随即数等待更新
		}
		dataset5.addSeries(series5);
		return dataset5;
	}

	
	// 更新PM2.5折线图
	private void updatechart() {
		x[1] = x[2];
		x[2] = x[3];
		x[3] = x[0];
		// 判断当前点集中到底有多少点，因为屏幕总共只能容纳5个，所以当点数超过5时，长度永远是5
		String date = environment.getTime();
		x[0] = date;
		int length = series.getItemCount();
		if (length > 4) {
			length = 4;
		}
		// 设置好下一个需要增加的节点
		/*
		 * try { if(guangzhi2.getText().toString()!=null){ addY =
		 * Float.valueOf(guangzhi2.getText().toString());//要不要判断再说 } } catch
		 * (NumberFormatException e) { e.printStackTrace(); }
		 */
		// addY = (float) (Math.random() * 10);
		addY = Float.parseFloat(environment.getPm25());
		// 移除数据集中旧的点集
		dataset1.removeSeries(series);
		// 将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果
		for (int i = 1; i < length; i++) {
			ycache[i - 1] = (float) series.getY(i);
		}
		// 点集先清空，为了做成新的点集而准备
		series.clear();
		// 将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
		for (int k = 0; k < length - 1; k++) {
			series.add(k + 1, ycache[k]);
			render.addXTextLabel(k + 1, x[k + 1]);
		}
		series.add(4, addY);
		render.addXTextLabel(4, date);// x轴加时间的这里
		// 在数据集中添加新的点集
		dataset1.addSeries(series);
		// 视图更新，没有这一步，曲线不会呈现动态
		chart.invalidate();
		chart.repaint();
	}

	// 更新PM10折线图
	private void updatePM10chart() {
		x2[1] = x2[2];
		x2[2] = x2[3];
		x2[3] = x2[0];
		// 判断当前点集中到底有多少点，因为屏幕总共只能容纳5个，所以当点数超过5时，长度永远是5
		String date = environment.getTime();
		x2[0] = date;
		int length = series2.getItemCount();
		if (length > 4) {
			length = 4;
		}
		// 设置好下一个需要增加的节点
		/*
		 * try { if(guangzhi2.getText().toString()!=null){ addY =
		 * Float.valueOf(guangzhi2.getText().toString());//要不要判断再说 } } catch
		 * (NumberFormatException e) { e.printStackTrace(); }
		 */
		// addY = (float) (Math.random() * 10);
		addY2 = Float.parseFloat(environment.getPm10());
		// 移除数据集中旧的点集
		dataset2.removeSeries(series2);
		// 将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果
		for (int i = 1; i < length; i++) {
			ycache2[i - 1] = (float) series2.getY(i);
		}
		// 点集先清空，为了做成新的点集而准备
		series2.clear();
		// 将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
		for (int k = 0; k < length - 1; k++) {
			series2.add(k + 1, ycache2[k]);
			render2.addXTextLabel(k + 1, x2[k + 1]);
		}
		series2.add(4, addY2);
		render2.addXTextLabel(4, date);// x轴加时间的这里
		// 在数据集中添加新的点集
		dataset2.addSeries(series2);
		// 视图更新，没有这一步，曲线不会呈现动态
		chart2.invalidate();
		chart2.repaint();
	}
	
	
	// 更新SO折线图
		private void updateSOchart() {
			x3[1] = x3[2];
			x3[2] = x3[3];
			x3[3] = x3[0];
			// 判断当前点集中到底有多少点，因为屏幕总共只能容纳5个，所以当点数超过5时，长度永远是5
			String date = environment.getTime();
			x3[0] = date;
			int length = series3.getItemCount();
			if (length > 4) {
				length = 4;
			}
			// 设置好下一个需要增加的节点
			/*
			 * try { if(guangzhi2.getText().toString()!=null){ addY =
			 * Float.valueOf(guangzhi2.getText().toString());//要不要判断再说 } } catch
			 * (NumberFormatException e) { e.printStackTrace(); }
			 */
			// addY = (float) (Math.random() * 10);
			addY3 = Float.parseFloat(environment.getSo());
			// 移除数据集中旧的点集
			dataset3.removeSeries(series3);
			// 将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果
			for (int i = 1; i < length; i++) {
				ycache3[i - 1] = (float) series3.getY(i);
			}
			// 点集先清空，为了做成新的点集而准备
			series3.clear();
			// 将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
			for (int k = 0; k < length - 1; k++) {
				series3.add(k + 1, ycache3[k]);
				render3.addXTextLabel(k + 1, x3[k + 1]);
			}
			series3.add(4, addY3);
			render3.addXTextLabel(4, date);// x轴加时间的这里
			// 在数据集中添加新的点集
			dataset3.addSeries(series3);
			// 视图更新，没有这一步，曲线不会呈现动态
			chart3.invalidate();
			chart3.repaint();
		}

		
		// 更新湿度折线图
		private void updateHumchart() {
			x4[1] = x4[2];
			x4[2] = x4[3];
			x4[3] = x4[0];
			// 判断当前点集中到底有多少点，因为屏幕总共只能容纳5个，所以当点数超过5时，长度永远是5
			String date = environment.getTime();
			x4[0] = date;
			int length = series4.getItemCount();
			if (length > 4) {
				length = 4;
			}
			// 设置好下一个需要增加的节点
			/*
			 * try { if(guangzhi2.getText().toString()!=null){ addY =
			 * Float.valueOf(guangzhi2.getText().toString());//要不要判断再说 } } catch
			 * (NumberFormatException e) { e.printStackTrace(); }
			 */
			// addY = (float) (Math.random() * 10);
			addY4 = Float.parseFloat(environment.getHum());
			// 移除数据集中旧的点集
			dataset4.removeSeries(series4);
			// 将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果
			for (int i = 1; i < length; i++) {
				ycache4[i - 1] = (float) series4.getY(i);
			}
			// 点集先清空，为了做成新的点集而准备
			series4.clear();
			// 将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
			for (int k = 0; k < length - 1; k++) {
				series4.add(k + 1, ycache4[k]);
				render4.addXTextLabel(k + 1, x4[k + 1]);
			}
			series4.add(4, addY4);
			render4.addXTextLabel(4, date);// x轴加时间的这里
			// 在数据集中添加新的点集
			dataset4.addSeries(series4);
			// 视图更新，没有这一步，曲线不会呈现动态
			chart4.invalidate();
			chart4.repaint();
		}

		
		// 更新温度折线图
		private void updateTemchart() {
			x5[1] = x5[2];
			x5[2] = x5[3];
			x5[3] = x5[0];
			// 判断当前点集中到底有多少点，因为屏幕总共只能容纳5个，所以当点数超过5时，长度永远是5
			String date = environment.getTime();
			x5[0] = date;
			int length = series5.getItemCount();
			if (length > 4) {
				length = 4;
			}
			// 设置好下一个需要增加的节点
			/*
			 * try { if(guangzhi2.getText().toString()!=null){ addY =
			 * Float.valueOf(guangzhi2.getText().toString());//要不要判断再说 } } catch
			 * (NumberFormatException e) { e.printStackTrace(); }
			 */
			// addY = (float) (Math.random() * 10);
			addY5 = Float.parseFloat(environment.getTem());
			// 移除数据集中旧的点集
			dataset5.removeSeries(series5);
			// 将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果
			for (int i = 1; i < length; i++) {
				ycache5[i - 1] = (float) series5.getY(i);
			}
			// 点集先清空，为了做成新的点集而准备
			series5.clear();
			// 将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
			for (int k = 0; k < length - 1; k++) {
				series5.add(k + 1, ycache5[k]);
				render5.addXTextLabel(k + 1, x5[k + 1]);
			}
			series5.add(4, addY5);
			render5.addXTextLabel(4, date);// x轴加时间的这里
			// 在数据集中添加新的点集
			dataset5.addSeries(series5);
			// 视图更新，没有这一步，曲线不会呈现动态
			chart5.invalidate();
			chart5.repaint();
		}

}
