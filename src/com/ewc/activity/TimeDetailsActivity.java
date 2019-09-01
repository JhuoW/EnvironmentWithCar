package com.ewc.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.simonvt.numberpicker.NumberPicker;
import net.simonvt.numberpicker.NumberPicker.OnValueChangeListener;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ewc.base.C;
import com.ewc.bean.Environment;
import com.ewc.bean.GetObjectFromService;
import com.ewc.https.WebService;
import com.ewc.tools.SimpleNetTask;
import com.ewc.utils.Utils;
import com.ewc.view.HeaderLayout;
import com.example.environmentwithcar.R;

public class TimeDetailsActivity extends BaseActivity {

	private TextView tv_YearAndMouth;
	private TextView tv_FromTime;
	private TextView tv_ToTime;
	private LinearLayout ll_YearAndMouth;
	private LinearLayout ll_FromTime;
	private LinearLayout ll_ToTime;
	private Button btn_query;
	String Lng;
	String Lat;
	int num;
	ProgressDialog dialog;
	Environment environment;
	List<Environment> datas = new ArrayList<Environment>();

	LinearLayout linearLayout;
	LinearLayout linearLayout2;
	LinearLayout linearLayout3;
	LinearLayout linearLayout4;
	LinearLayout linearLayout5;
	// // 数据
	// private List<Double[]> values;
	// // 每个数据点的颜色
	// private List<int[]> colors;
	// // 折线的线条颜色
	// private int[] mSeriescolors;
	// // 渲染器
	// 用于存放每条折线的点数据
	private XYSeries line1,line2,line3,line4,line5;
	// 用于存放所有需要绘制的XYSeries
	private XYMultipleSeriesDataset mDataset,mDataset2,mDataset3,mDataset4,mDataset5;
	// 用于存放每条折线的风格
	private XYSeriesRenderer renderer1, renderer2,renderer3,renderer4,renderer5;
	// 用于存放所有需要绘制的折线的风格
	private XYMultipleSeriesRenderer mXYMultipleSeriesRenderer ,mXYMultipleSeriesRenderer2,mXYMultipleSeriesRenderer3,mXYMultipleSeriesRenderer4,mXYMultipleSeriesRenderer5;
	private GraphicalView chart,chart2,chart3,chart4,chart5;
	
	Spinner mSpinner;

	LinearLayout[] mll ; 
	
	List<Double> pm25list = new ArrayList<Double>();
	Double[] dd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_detail);
		// dd = new double[10];
		Intent intent = getIntent();
		Lng = intent.getStringExtra("Lng");
		Lat = intent.getStringExtra("Lat");
		initView();
		initAction();
	}

	private void initView() {
		dialog = new ProgressDialog(ctx);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage("查询中......");
		mSpinner = (Spinner) findViewById(R.id.spinner1);  
		String[] mItems = getResources().getStringArray(R.array.spinnername);  
		// 建立Adapter并且绑定数据源  
		ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
		mSpinner.setAdapter(_Adapter);

		linearLayout = (LinearLayout) findViewById(R.id.guangzhexian);
		linearLayout2 = (LinearLayout) findViewById(R.id.guangzhexian2);
		linearLayout3 = (LinearLayout) findViewById(R.id.guangzhexian3);
		linearLayout4 = (LinearLayout) findViewById(R.id.guangzhexian4);
		linearLayout5 = (LinearLayout) findViewById(R.id.guangzhexian5);
		mll =new LinearLayout[]{linearLayout,linearLayout2,linearLayout3,linearLayout4,linearLayout5};

		
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

		tv_YearAndMouth = (TextView) findViewById(R.id.tv_YearAndMouth);
		tv_FromTime = (TextView) findViewById(R.id.tv_FromTime);
		tv_ToTime = (TextView) findViewById(R.id.tv_ToTime);
		ll_YearAndMouth = (LinearLayout) findViewById(R.id.YearAndMouth);
		ll_FromTime = (LinearLayout) findViewById(R.id.FromTime);
		ll_ToTime = (LinearLayout) findViewById(R.id.ToTime);
		btn_query = (Button) findViewById(R.id.btn_query);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("查询");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimeDetailsActivity.this.finish();
			}
		});
	}

	private void initAction() {
		ll_YearAndMouth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showTimeDialog();
			}
		});

		ll_FromTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showFromTimeDialog();
			}
		});

		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
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
		
		
		
		btn_query.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.show();
				String yearandmouth = tv_YearAndMouth.getText().toString();
				String fromTime = tv_FromTime.getText().toString();
				String toTime = tv_ToTime.getText().toString();
				final String time01 = yearandmouth + " " + fromTime;
				final String time02 = yearandmouth + " " + toTime;
				Log.i("TimeDetailsActivity", "time01:" + time01);
				Log.i("TimeDetailsActivity", "time02:" + time02);
				Log.i("TimeDetailsActivity", "Lng:" + Lng);
				Log.i("TimeDetailsActivity", "Lat:" + Lat);

				new SimpleNetTask(ctx) {
					List<Environment> temp = new ArrayList<Environment>();

					@Override
					protected void onSucceed() {
						dialog.dismiss();
						if (temp == null) {
							Utils.toast("获取数据失败");
						} else {
							datas.clear();
							datas.addAll(temp);
							// for (int i = 0; i < datas.size(); i++) {
							// Double d = Double.parseDouble(datas.get(i)
							// .getPm25());
							// pm25list.add(d);
							// }
							// // 将list转化为double数组
							// Double[] pm25d = new Double[10];
							// dd = pm25list.toArray(pm25d);
							// values = new ArrayList<Double[]>();
							// colors = new ArrayList<int[]>();
							// values.add(dd);
							// colors.add(new int[] { Color.RED, Color.RED,
							// Color.RED, Color.RED, Color.RED, Color.RED,
							// Color.RED, Color.RED, Color.RED, Color.RED});
							// mSeriescolors = new int[] {Color.rgb(153, 204,
							// 0)};

							for (int i = 0; i < 10; i++) {
								line1.add(i, Double.parseDouble(datas.get(i)
										.getPm25()));
								line2.add(i, Double.parseDouble(datas.get(i).getPm10()));
								line3.add(i, Double.parseDouble(datas.get(i).getSo()));
								line4.add(i, Double.parseDouble(datas.get(i).getHum()));
								line5.add(i, Double.parseDouble(datas.get(i).getTem()));

							}
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
									"PM2.5",-1, 10, 0, 200, Color.BLACK, Color.BLUE);
							setChartSettings("PM10",mXYMultipleSeriesRenderer2, "X",
									"PM10",-1, 10, 0, 200, Color.BLACK, Color.BLUE);
							setChartSettings("SO2",mXYMultipleSeriesRenderer3, "X",
									"SO2",-1, 10, 0, 150, Color.BLACK, Color.BLUE);
							setChartSettings("湿度",mXYMultipleSeriesRenderer4, "X",
									"湿度",-1, 10, 0, 100, Color.BLACK, Color.BLUE);
							setChartSettings("温度",mXYMultipleSeriesRenderer5, "X",
									"温度",-1, 10, -20, 50, Color.BLACK, Color.BLUE);
							// 通过该函数获取到一个View 对象
							chart = ChartFactory.getLineChartView(TimeDetailsActivity.this,  //不能用this
									mDataset, mXYMultipleSeriesRenderer);
							chart2 = ChartFactory.getLineChartView(TimeDetailsActivity.this,  //不能用this
									mDataset2, mXYMultipleSeriesRenderer2);
							chart3 = ChartFactory.getLineChartView(TimeDetailsActivity.this,  //不能用this
									mDataset3, mXYMultipleSeriesRenderer3);
							chart4 = ChartFactory.getLineChartView(TimeDetailsActivity.this,  //不能用this
									mDataset4, mXYMultipleSeriesRenderer4);
							chart5 = ChartFactory.getLineChartView(TimeDetailsActivity.this,  //不能用this
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
						param.put("Lng", Lng);
						param.put("Lat", Lat);
						param.put("FromTime", time01);
						param.put("ToTime", time02);
						param.put("NeedCount", 10 + "");
						String jsonStr = new WebService(C.QUERYHISTORY, param)
								.getReturnInfo();
						temp = GetObjectFromService.getHistroyList(jsonStr);
					}
				}.execute();

			}
		});
	}

	// 时间
	private void showTimeDialog() {
		DatePickerFragment datePicker = new DatePickerFragment();
		datePicker.show(getFragmentManager(), "datePicker");
	}

	public class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			Log.d("OnDateSet", "select year:" + year + ";month:" + month
					+ ";day:" + day);
			int month2 = month + 1;
			tv_YearAndMouth.setText(year + "/" + month2 + "/" + day);
			tv_YearAndMouth.setKeyListener(null);
		}
	}

	private void showFromTimeDialog() {
		final AlertDialog dlg = new AlertDialog.Builder(
				TimeDetailsActivity.this).create();
		dlg.show();
		Window window = dlg.getWindow();
		window.setContentView(R.layout.alertdialog_time);
		TextView tv_title = (TextView) window.findViewById(R.id.title);
		Button btn_finish = (Button) window.findViewById(R.id.finish);
		tv_title.setText("选择起始时刻");
		final NumberPicker np = (NumberPicker) window
				.findViewById(R.id.numberPicker);
		np.setMaxValue(23);
		np.setMinValue(0);
		np.setFocusable(true);
		np.setFocusableInTouchMode(true);
		num = 1;
		np.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				// TODO Auto-generated method stub
				num = np.getValue();
			}
		});
		btn_finish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_FromTime.setText(num + ":00");
				tv_ToTime.setText(num + 1 + ":00");
				dlg.cancel();
			}
		});
	}

	private XYSeriesRenderer initRenderer(XYSeriesRenderer renderer, int color,
			PointStyle style, boolean fill) {
		// 设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
		renderer.setColor(color);
		renderer.setPointStyle(style);
		renderer.setFillPoints(fill);
		renderer.setLineWidth(1);
		renderer.setDisplayChartValues(true);
		renderer.setDisplayChartValuesDistance(30);
		renderer.setChartValuesTextSize(25);
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
		mXYMultipleSeriesRenderer.setLabelsTextSize(16);
		mXYMultipleSeriesRenderer.setXAxisMax(xMax);
		mXYMultipleSeriesRenderer.setYAxisMin(yMin);
		mXYMultipleSeriesRenderer.setYAxisMax(yMax);
		mXYMultipleSeriesRenderer.setAxesColor(axesColor);
		mXYMultipleSeriesRenderer.setLabelsColor(labelsColor);
		mXYMultipleSeriesRenderer.setShowGrid(false);
		mXYMultipleSeriesRenderer.setGridColor(Color.GRAY);
		mXYMultipleSeriesRenderer.setXLabels(10);
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
        mXYMultipleSeriesRenderer.setMargins(new int[] { 25, 25, 20, 24 }); // 设置图形四周的留白
        mXYMultipleSeriesRenderer.setMarginsColor(Color.WHITE);
	}

}
