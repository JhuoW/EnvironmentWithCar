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
 * ���ݸ�ʽ��{"ret":"success", "Lng":"0","Lat":"0", "Tim":"2015-9-5 11:56:24",
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
			"10" };// x�Ỻ��
	Float[] ycache = new Float[10];
	private XYSeries series;// ������յ�һ���ټ���һ��
	private XYMultipleSeriesDataset dataset1;// xy������Դ
	LinearLayout linearLayout;

	LinearLayout linearlayout2;
	private GraphicalView chart2;
	private XYMultipleSeriesDataset dataset2;// xy������Դ
	private XYSeries series2;// ������յ�һ���ټ���һ��
	private Float addY2;
	Float[] ycache2 = new Float[10];
	String[] x2 = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10" };// x�Ỻ��
	private XYMultipleSeriesRenderer render2;

	LinearLayout linearlayout3;
	private GraphicalView chart3;
	private XYMultipleSeriesDataset dataset3;// xy������Դ
	private XYSeries series3;// ������յ�һ���ټ���һ��
	private Float addY3;
	Float[] ycache3 = new Float[10];
	String[] x3 = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10" };// x�Ỻ��
	private XYMultipleSeriesRenderer render3;
	
	
	LinearLayout linearlayout4;
	private GraphicalView chart4;
	private XYMultipleSeriesDataset dataset4;// xy������Դ
	private XYSeries series4;// ������յ�һ���ټ���һ��
	private Float addY4;
	Float[] ycache4 = new Float[10];
	String[] x4 = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10" };// x�Ỻ��
	private XYMultipleSeriesRenderer render4;
	
	
	LinearLayout linearlayout5;
	private GraphicalView chart5;
	private XYMultipleSeriesDataset dataset5;// xy������Դ
	private XYSeries series5;// ������յ�һ���ټ���һ��
	private Float addY5;
	Float[] ycache5 = new Float[10];
	String[] x5 = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10" };// x�Ỻ��
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
		// ����Adapter���Ұ�����Դ  
		ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
		mSpinner.setAdapter(_Adapter);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("�ڵ�һ��������");
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
		// ����PM2.5����ͼ
		chart = ChartFactory.getLineChartView(this, getdemodataset("PM2.5"),
				getdemorenderer("PM2.5"));
		linearLayout.removeAllViews();// ��remove��add����ʵ��ͳ��ͼ����
		linearLayout.addView(chart, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		// ����PM10����ͼ
		chart2 = ChartFactory.getLineChartView(this,
				getPM10demodataset("PM10"), getPM10demorenderer("PM10"));
		linearlayout2.removeAllViews();// ��remove��add����ʵ��ͳ��ͼ����
		linearlayout2.addView(chart2, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		// ����SO����ͼ
		chart3 = ChartFactory.getLineChartView(this, getSOdemodataset("SO2"),
				getSOdemorenderer("SO2"));
		linearlayout3.removeAllViews();// ��remove��add����ʵ��ͳ��ͼ����
		linearlayout3.addView(chart3, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		//����ʪ������ͼ
		chart4 = ChartFactory.getLineChartView(this, getHumdemodataset("ʪ��"),
				getHumdemorenderer("ʪ��"));
		linearlayout4.removeAllViews();// ��remove��add����ʵ��ͳ��ͼ����
		linearlayout4.addView(chart4, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		//�����¶�����ͼ
		chart5 = ChartFactory.getLineChartView(this, getTemdemodataset("�¶�"),
				getTemdemorenderer("�¶�"));
		linearlayout5.removeAllViews();// ��remove��add����ʵ��ͳ��ͼ����
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
		}, 2000, 2000); /* ��ʾ1000����֮��ÿ��1000����ִ��һ�� */
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
		render.setChartTitle("PM2.5ʵʱ����");
		render.setAxisTitleTextSize(16);// ������������ֵĴ�С
		render.setAxesColor(Color.BLACK);
		render.setXTitle("ʱ��");
		render.setYTitle(state);
		render.setLabelsTextSize(16);// ������̶����ֵĴ�С
		render.setLabelsColor(Color.BLUE);
		render.setXLabelsColor(Color.BLACK);
		render.setYLabelsColor(0, Color.BLACK);
		render.setLegendTextSize(15);// ����ͼ�����ִ�С
		
		// render.setShowLegend(false);//��ʾ����ʾ���������ã��ǳ�����
		XYSeriesRenderer r = new XYSeriesRenderer();// ������ɫ�͵�����
		r.setColor(Color.RED);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		// r.setDisplayChartValues(true);
		r.setChartValuesSpacing(3);
		r.setChartValuesTextSize(30);
		render.addSeriesRenderer(r);
		render.setYLabelsAlign(Align.RIGHT);// �̶�ֵ����ڿ̶ȵ�λ��
		render.setShowGrid(true);// ��ʾ����
		render.setYAxisMax(300);// ����y��ķ�Χ
		render.setYAxisMin(0);
		render.setYLabels(30);// ���ߵȷ�
		render.setXLabels(0);// ����ֻ��ʾ��1�£�2�µ��滻��Ķ���������ʾ1,2,3��
		render.setInScroll(true);
		render.setLabelsTextSize(14);
		render.getSeriesRendererAt(0).setDisplayChartValues(true);
		// //��ʾ�����ϵ����ֵ
		render.setPanEnabled(false, false);// ��ֹ������϶�
		render.setPointSize(5f);// ���õ�Ĵ�С(ͼ����ʾ�ĵ�Ĵ�С��ͼ���е�Ĵ�С���ᱻ����)
		render.setMargins(new int[] { 25, 25, 20, 24 }); // ����ͼ�����ܵ�����
		render.setMarginsColor(Color.WHITE);
		return render;
	}

	private XYMultipleSeriesRenderer getPM10demorenderer(String state) {
		// TODO Auto-generated method stub
		// render.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		render2 = new XYMultipleSeriesRenderer();
		render2.setChartTitle("PM10ʵʱ����");
		render2.setAxisTitleTextSize(16);// ������������ֵĴ�С
		render2.setAxesColor(Color.BLACK);
		render2.setXTitle("ʱ��");
		render2.setYTitle(state);
		render2.setLabelsTextSize(16);// ������̶����ֵĴ�С
		render2.setLabelsColor(Color.BLUE);
		render2.setXLabelsColor(Color.BLACK);
		render2.setYLabelsColor(0, Color.BLACK);
		render2.setLegendTextSize(15);// ����ͼ�����ִ�С
		// render.setShowLegend(false);//��ʾ����ʾ���������ã��ǳ�����

		XYSeriesRenderer r = new XYSeriesRenderer();// ������ɫ�͵�����
		r.setColor(Color.RED);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		// r.setDisplayChartValues(true);
		r.setChartValuesSpacing(3);
		r.setChartValuesTextSize(30);

		render2.addSeriesRenderer(r);
		render2.setYLabelsAlign(Align.RIGHT);// �̶�ֵ����ڿ̶ȵ�λ��
		render2.setShowGrid(true);// ��ʾ����
		render2.setYAxisMax(200);// ����y��ķ�Χ
		render2.setYAxisMin(0);
		render2.setYLabels(20);// ���ߵȷ�
		render2.setXLabels(0);// ����ֻ��ʾ��1�£�2�µ��滻��Ķ���������ʾ1,2,3��
		render2.setInScroll(true);
		render2.setLabelsTextSize(14);
		 render2.getSeriesRendererAt(0).setDisplayChartValues(true);
		// //��ʾ�����ϵ����ֵ
		render2.setPanEnabled(false, false);// ��ֹ������϶�
		render2.setPointSize(5f);// ���õ�Ĵ�С(ͼ����ʾ�ĵ�Ĵ�С��ͼ���е�Ĵ�С���ᱻ����)
		render2.setMargins(new int[] { 25, 25, 20, 24 }); // ����ͼ�����ܵ�����
		render2.setMarginsColor(Color.WHITE);
		return render2;
	}

	private XYMultipleSeriesRenderer getSOdemorenderer(String state) {
		// TODO Auto-generated method stub
		// render.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		render3 = new XYMultipleSeriesRenderer();
		render3.setChartTitle("��������SO2��ʵʱ����");
		render3.setAxisTitleTextSize(16);// ������������ֵĴ�С
		render3.setAxesColor(Color.BLACK);
		render3.setXTitle("ʱ��");
		render3.setYTitle(state);
		render3.setLabelsTextSize(16);// ������̶����ֵĴ�С
		render3.setLabelsColor(Color.BLUE);
		render3.setXLabelsColor(Color.BLACK);
		render3.setYLabelsColor(0, Color.BLACK);
		render3.setLegendTextSize(15);// ����ͼ�����ִ�С
		// render.setShowLegend(false);//��ʾ����ʾ���������ã��ǳ�����

		XYSeriesRenderer r = new XYSeriesRenderer();// ������ɫ�͵�����
		r.setColor(Color.RED);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		// r.setDisplayChartValues(true);
		r.setChartValuesSpacing(3);
		r.setChartValuesTextSize(30);

		render3.addSeriesRenderer(r);
		render3.setYLabelsAlign(Align.RIGHT);// �̶�ֵ����ڿ̶ȵ�λ��
		render3.setShowGrid(true);// ��ʾ����
		render3.setYAxisMax(100);// ����y��ķ�Χ
		render3.setYAxisMin(10);
		render3.setYLabels(5);// ���ߵȷ�
		render3.setXLabels(0);// ����ֻ��ʾ��1�£�2�µ��滻��Ķ���������ʾ1,2,3��
		render3.setInScroll(true);
		render3.setLabelsTextSize(14);
		 render3.getSeriesRendererAt(0).setDisplayChartValues(true);
		// //��ʾ�����ϵ����ֵ
		render3.setPanEnabled(false, false);// ��ֹ������϶�
		render3.setPointSize(5f);// ���õ�Ĵ�С(ͼ����ʾ�ĵ�Ĵ�С��ͼ���е�Ĵ�С���ᱻ����)
		render3.setMargins(new int[] { 25, 25, 20, 24 }); // ����ͼ�����ܵ�����
		render3.setMarginsColor(Color.WHITE);
		return render3;
	}
	
	
	private XYMultipleSeriesRenderer getHumdemorenderer(String state) {
		// TODO Auto-generated method stub
		// render.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		render4 = new XYMultipleSeriesRenderer();
		render4.setChartTitle("ʪ��ʵʱ����");
		render4.setAxisTitleTextSize(16);// ������������ֵĴ�С
		render4.setAxesColor(Color.BLACK);
		render4.setXTitle("ʱ��");
		render4.setYTitle(state);
		render4.setLabelsTextSize(16);// ������̶����ֵĴ�С
		render4.setLabelsColor(Color.BLUE);
		render4.setXLabelsColor(Color.BLACK);
		render4.setYLabelsColor(0, Color.BLACK);
		render4.setLegendTextSize(15);// ����ͼ�����ִ�С
		// render.setShowLegend(false);//��ʾ����ʾ���������ã��ǳ�����

		XYSeriesRenderer r = new XYSeriesRenderer();// ������ɫ�͵�����
		r.setColor(Color.RED);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		// r.setDisplayChartValues(true);
		r.setChartValuesSpacing(3);
		r.setChartValuesTextSize(30);

		render4.addSeriesRenderer(r);
		render4.setYLabelsAlign(Align.RIGHT);// �̶�ֵ����ڿ̶ȵ�λ��
		render4.setShowGrid(true);// ��ʾ����
		render4.setYAxisMax(100);// ����y��ķ�Χ
		render4.setYAxisMin(10);
		render4.setYLabels(5);// ���ߵȷ�
		render4.setXLabels(0);// ����ֻ��ʾ��1�£�2�µ��滻��Ķ���������ʾ1,2,3��
		render4.setInScroll(true);
		render4.setLabelsTextSize(14);
		 render4.getSeriesRendererAt(0).setDisplayChartValues(true);
		// //��ʾ�����ϵ����ֵ
		render4.setPanEnabled(false, false);// ��ֹ������϶�
		render4.setPointSize(5f);// ���õ�Ĵ�С(ͼ����ʾ�ĵ�Ĵ�С��ͼ���е�Ĵ�С���ᱻ����)
		render4.setMargins(new int[] { 25, 25, 20, 24 }); // ����ͼ�����ܵ�����
		render4.setMarginsColor(Color.WHITE);
		return render4;
	}
	
	
	private XYMultipleSeriesRenderer getTemdemorenderer(String state) {
		// TODO Auto-generated method stub
		// render.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
		render5 = new XYMultipleSeriesRenderer();
		render5.setChartTitle("�¶�ʵʱ����");
		render5.setAxisTitleTextSize(16);// ������������ֵĴ�С
		render5.setAxesColor(Color.BLACK);
		render5.setXTitle("ʱ��");
		render5.setYTitle(state);
		render5.setLabelsTextSize(16);// ������̶����ֵĴ�С
		render5.setLabelsColor(Color.BLUE);
		render5.setXLabelsColor(Color.BLACK);
		render5.setYLabelsColor(0, Color.BLACK);
		render5.setLegendTextSize(15);// ����ͼ�����ִ�С
		// render.setShowLegend(false);//��ʾ����ʾ���������ã��ǳ�����

		XYSeriesRenderer r = new XYSeriesRenderer();// ������ɫ�͵�����
		r.setColor(Color.RED);
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillPoints(true);
		// r.setDisplayChartValues(true);
		r.setChartValuesSpacing(3);
		r.setChartValuesTextSize(30);

		render5.addSeriesRenderer(r);
		render5.setYLabelsAlign(Align.RIGHT);// �̶�ֵ����ڿ̶ȵ�λ��
		render5.setShowGrid(true);// ��ʾ����
		render5.setYAxisMax(50);// ����y��ķ�Χ
		render5.setYAxisMin(-20);
		render5.setYLabels(7);// ���ߵȷ�
		render5.setXLabels(0);// ����ֻ��ʾ��1�£�2�µ��滻��Ķ���������ʾ1,2,3��
		render5.setInScroll(true);
		render5.setLabelsTextSize(14);
		render5.getSeriesRendererAt(0).setDisplayChartValues(true);
		// //��ʾ�����ϵ����ֵ
		render5.setPanEnabled(false, false);// ��ֹ������϶�
		render5.setPointSize(5f);// ���õ�Ĵ�С(ͼ����ʾ�ĵ�Ĵ�С��ͼ���е�Ĵ�С���ᱻ����)
		render5.setMargins(new int[] { 25, 25, 20, 24 }); // ����ͼ�����ܵ�����
		render5.setMarginsColor(Color.WHITE);
		return render5;
	}

	private XYMultipleSeriesDataset getdemodataset(String state) {

		// TODO Auto-generated method stub
		dataset1 = new XYMultipleSeriesDataset();// xy������Դ
		final int nr = 4;// ��ʾ���ݸ���
		series = new TimeSeries(state);// ���������ʾ�����õģ��Բ���ʾ������render����
		for (int i = 0; i < nr; i++) {
			series.add(i + 1, 0);// ������date�������ͣ��������漴���ȴ�����
		}
		dataset1.addSeries(series);
		return dataset1;
	}

	private XYMultipleSeriesDataset getPM10demodataset(String state) {

		// TODO Auto-generated method stub
		dataset2 = new XYMultipleSeriesDataset();// xy������Դ
		final int nr = 4;// ��ʾ���ݸ���
		series2 = new TimeSeries(state);// ���������ʾ�����õģ��Բ���ʾ������render����
		for (int i = 0; i < nr; i++) {
			series2.add(i + 1, 0);// ������date�������ͣ��������漴���ȴ�����
		}
		dataset2.addSeries(series2);
		return dataset2;
	}

	private XYMultipleSeriesDataset getSOdemodataset(String state) {

		// TODO Auto-generated method stub
		dataset3 = new XYMultipleSeriesDataset();// xy������Դ
		final int nr = 4;// ��ʾ���ݸ���
		series3 = new TimeSeries(state);// ���������ʾ�����õģ��Բ���ʾ������render����
		for (int i = 0; i < nr; i++) {
			series3.add(i + 1, 0);// ������date�������ͣ��������漴���ȴ�����
		}
		dataset3.addSeries(series3);
		return dataset3;
	}

	private XYMultipleSeriesDataset getHumdemodataset(String state) {

		// TODO Auto-generated method stub
		dataset4 = new XYMultipleSeriesDataset();// xy������Դ
		final int nr = 4;// ��ʾ���ݸ���
		series4 = new TimeSeries(state);// ���������ʾ�����õģ��Բ���ʾ������render����
		for (int i = 0; i < nr; i++) {
			series4.add(i + 1, 0);// ������date�������ͣ��������漴���ȴ�����
		}
		dataset4.addSeries(series4);
		return dataset4;
	}
	
	
	private XYMultipleSeriesDataset getTemdemodataset(String state) {

		// TODO Auto-generated method stub
		dataset5 = new XYMultipleSeriesDataset();// xy������Դ
		final int nr = 4;// ��ʾ���ݸ���
		series5 = new TimeSeries(state);// ���������ʾ�����õģ��Բ���ʾ������render����
		for (int i = 0; i < nr; i++) {
			series5.add(i + 1, 0);// ������date�������ͣ��������漴���ȴ�����
		}
		dataset5.addSeries(series5);
		return dataset5;
	}

	
	// ����PM2.5����ͼ
	private void updatechart() {
		x[1] = x[2];
		x[2] = x[3];
		x[3] = x[0];
		// �жϵ�ǰ�㼯�е����ж��ٵ㣬��Ϊ��Ļ�ܹ�ֻ������5�������Ե���������5ʱ��������Զ��5
		String date = environment.getTime();
		x[0] = date;
		int length = series.getItemCount();
		if (length > 4) {
			length = 4;
		}
		// ���ú���һ����Ҫ���ӵĽڵ�
		/*
		 * try { if(guangzhi2.getText().toString()!=null){ addY =
		 * Float.valueOf(guangzhi2.getText().toString());//Ҫ��Ҫ�ж���˵ } } catch
		 * (NumberFormatException e) { e.printStackTrace(); }
		 */
		// addY = (float) (Math.random() * 10);
		addY = Float.parseFloat(environment.getPm25());
		// �Ƴ����ݼ��оɵĵ㼯
		dataset1.removeSeries(series);
		// ���ɵĵ㼯��x��y����ֵȡ��������backup�У����ҽ�x��ֵ��1�������������ƽ�Ƶ�Ч��
		for (int i = 1; i < length; i++) {
			ycache[i - 1] = (float) series.getY(i);
		}
		// �㼯����գ�Ϊ�������µĵ㼯��׼��
		series.clear();
		// ���²����ĵ����ȼ��뵽�㼯�У�Ȼ����ѭ�����н�����任���һϵ�е㶼���¼��뵽�㼯��
		for (int k = 0; k < length - 1; k++) {
			series.add(k + 1, ycache[k]);
			render.addXTextLabel(k + 1, x[k + 1]);
		}
		series.add(4, addY);
		render.addXTextLabel(4, date);// x���ʱ�������
		// �����ݼ�������µĵ㼯
		dataset1.addSeries(series);
		// ��ͼ���£�û����һ�������߲�����ֶ�̬
		chart.invalidate();
		chart.repaint();
	}

	// ����PM10����ͼ
	private void updatePM10chart() {
		x2[1] = x2[2];
		x2[2] = x2[3];
		x2[3] = x2[0];
		// �жϵ�ǰ�㼯�е����ж��ٵ㣬��Ϊ��Ļ�ܹ�ֻ������5�������Ե���������5ʱ��������Զ��5
		String date = environment.getTime();
		x2[0] = date;
		int length = series2.getItemCount();
		if (length > 4) {
			length = 4;
		}
		// ���ú���һ����Ҫ���ӵĽڵ�
		/*
		 * try { if(guangzhi2.getText().toString()!=null){ addY =
		 * Float.valueOf(guangzhi2.getText().toString());//Ҫ��Ҫ�ж���˵ } } catch
		 * (NumberFormatException e) { e.printStackTrace(); }
		 */
		// addY = (float) (Math.random() * 10);
		addY2 = Float.parseFloat(environment.getPm10());
		// �Ƴ����ݼ��оɵĵ㼯
		dataset2.removeSeries(series2);
		// ���ɵĵ㼯��x��y����ֵȡ��������backup�У����ҽ�x��ֵ��1�������������ƽ�Ƶ�Ч��
		for (int i = 1; i < length; i++) {
			ycache2[i - 1] = (float) series2.getY(i);
		}
		// �㼯����գ�Ϊ�������µĵ㼯��׼��
		series2.clear();
		// ���²����ĵ����ȼ��뵽�㼯�У�Ȼ����ѭ�����н�����任���һϵ�е㶼���¼��뵽�㼯��
		for (int k = 0; k < length - 1; k++) {
			series2.add(k + 1, ycache2[k]);
			render2.addXTextLabel(k + 1, x2[k + 1]);
		}
		series2.add(4, addY2);
		render2.addXTextLabel(4, date);// x���ʱ�������
		// �����ݼ�������µĵ㼯
		dataset2.addSeries(series2);
		// ��ͼ���£�û����һ�������߲�����ֶ�̬
		chart2.invalidate();
		chart2.repaint();
	}
	
	
	// ����SO����ͼ
		private void updateSOchart() {
			x3[1] = x3[2];
			x3[2] = x3[3];
			x3[3] = x3[0];
			// �жϵ�ǰ�㼯�е����ж��ٵ㣬��Ϊ��Ļ�ܹ�ֻ������5�������Ե���������5ʱ��������Զ��5
			String date = environment.getTime();
			x3[0] = date;
			int length = series3.getItemCount();
			if (length > 4) {
				length = 4;
			}
			// ���ú���һ����Ҫ���ӵĽڵ�
			/*
			 * try { if(guangzhi2.getText().toString()!=null){ addY =
			 * Float.valueOf(guangzhi2.getText().toString());//Ҫ��Ҫ�ж���˵ } } catch
			 * (NumberFormatException e) { e.printStackTrace(); }
			 */
			// addY = (float) (Math.random() * 10);
			addY3 = Float.parseFloat(environment.getSo());
			// �Ƴ����ݼ��оɵĵ㼯
			dataset3.removeSeries(series3);
			// ���ɵĵ㼯��x��y����ֵȡ��������backup�У����ҽ�x��ֵ��1�������������ƽ�Ƶ�Ч��
			for (int i = 1; i < length; i++) {
				ycache3[i - 1] = (float) series3.getY(i);
			}
			// �㼯����գ�Ϊ�������µĵ㼯��׼��
			series3.clear();
			// ���²����ĵ����ȼ��뵽�㼯�У�Ȼ����ѭ�����н�����任���һϵ�е㶼���¼��뵽�㼯��
			for (int k = 0; k < length - 1; k++) {
				series3.add(k + 1, ycache3[k]);
				render3.addXTextLabel(k + 1, x3[k + 1]);
			}
			series3.add(4, addY3);
			render3.addXTextLabel(4, date);// x���ʱ�������
			// �����ݼ�������µĵ㼯
			dataset3.addSeries(series3);
			// ��ͼ���£�û����һ�������߲�����ֶ�̬
			chart3.invalidate();
			chart3.repaint();
		}

		
		// ����ʪ������ͼ
		private void updateHumchart() {
			x4[1] = x4[2];
			x4[2] = x4[3];
			x4[3] = x4[0];
			// �жϵ�ǰ�㼯�е����ж��ٵ㣬��Ϊ��Ļ�ܹ�ֻ������5�������Ե���������5ʱ��������Զ��5
			String date = environment.getTime();
			x4[0] = date;
			int length = series4.getItemCount();
			if (length > 4) {
				length = 4;
			}
			// ���ú���һ����Ҫ���ӵĽڵ�
			/*
			 * try { if(guangzhi2.getText().toString()!=null){ addY =
			 * Float.valueOf(guangzhi2.getText().toString());//Ҫ��Ҫ�ж���˵ } } catch
			 * (NumberFormatException e) { e.printStackTrace(); }
			 */
			// addY = (float) (Math.random() * 10);
			addY4 = Float.parseFloat(environment.getHum());
			// �Ƴ����ݼ��оɵĵ㼯
			dataset4.removeSeries(series4);
			// ���ɵĵ㼯��x��y����ֵȡ��������backup�У����ҽ�x��ֵ��1�������������ƽ�Ƶ�Ч��
			for (int i = 1; i < length; i++) {
				ycache4[i - 1] = (float) series4.getY(i);
			}
			// �㼯����գ�Ϊ�������µĵ㼯��׼��
			series4.clear();
			// ���²����ĵ����ȼ��뵽�㼯�У�Ȼ����ѭ�����н�����任���һϵ�е㶼���¼��뵽�㼯��
			for (int k = 0; k < length - 1; k++) {
				series4.add(k + 1, ycache4[k]);
				render4.addXTextLabel(k + 1, x4[k + 1]);
			}
			series4.add(4, addY4);
			render4.addXTextLabel(4, date);// x���ʱ�������
			// �����ݼ�������µĵ㼯
			dataset4.addSeries(series4);
			// ��ͼ���£�û����һ�������߲�����ֶ�̬
			chart4.invalidate();
			chart4.repaint();
		}

		
		// �����¶�����ͼ
		private void updateTemchart() {
			x5[1] = x5[2];
			x5[2] = x5[3];
			x5[3] = x5[0];
			// �жϵ�ǰ�㼯�е����ж��ٵ㣬��Ϊ��Ļ�ܹ�ֻ������5�������Ե���������5ʱ��������Զ��5
			String date = environment.getTime();
			x5[0] = date;
			int length = series5.getItemCount();
			if (length > 4) {
				length = 4;
			}
			// ���ú���һ����Ҫ���ӵĽڵ�
			/*
			 * try { if(guangzhi2.getText().toString()!=null){ addY =
			 * Float.valueOf(guangzhi2.getText().toString());//Ҫ��Ҫ�ж���˵ } } catch
			 * (NumberFormatException e) { e.printStackTrace(); }
			 */
			// addY = (float) (Math.random() * 10);
			addY5 = Float.parseFloat(environment.getTem());
			// �Ƴ����ݼ��оɵĵ㼯
			dataset5.removeSeries(series5);
			// ���ɵĵ㼯��x��y����ֵȡ��������backup�У����ҽ�x��ֵ��1�������������ƽ�Ƶ�Ч��
			for (int i = 1; i < length; i++) {
				ycache5[i - 1] = (float) series5.getY(i);
			}
			// �㼯����գ�Ϊ�������µĵ㼯��׼��
			series5.clear();
			// ���²����ĵ����ȼ��뵽�㼯�У�Ȼ����ѭ�����н�����任���һϵ�е㶼���¼��뵽�㼯��
			for (int k = 0; k < length - 1; k++) {
				series5.add(k + 1, ycache5[k]);
				render5.addXTextLabel(k + 1, x5[k + 1]);
			}
			series5.add(4, addY5);
			render5.addXTextLabel(4, date);// x���ʱ�������
			// �����ݼ�������µĵ㼯
			dataset5.addSeries(series5);
			// ��ͼ���£�û����һ�������߲�����ֶ�̬
			chart5.invalidate();
			chart5.repaint();
		}

}
