package com.ewc.view;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class AverageTemperatureChart extends AbstractDemoChart {
	public String getName() {
		return "Average temperature";
	}

	public String getDesc() {
		return "The average temperature in 4 Greek islands (line chart)";
	}

	public Intent execute(Context context) {
		String[] titles = new String[] { "Crete", "Corfu", "Thassos",
				"Skiathos" };// ͼ��
		List<double[]> x = new ArrayList<double[]>();
		for (int i = 0; i < titles.length; i++) {
			x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });// ÿ�������е��X����
		}
		List<double[]> values = new ArrayList<double[]>();
		values.add(new double[] { 12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4,
				26.1, 23.6, 20.3, 17.2, 13.9 });// ����1�е��y����
		values.add(new double[] { 10, 10, 12, 15, 20, 24, 26, 26, 23, 18, 14,
				11 });// ����2�е��Y����
		values.add(new double[] { 5, 5.3, 8, 12, 17, 22, 24.2, 24, 19, 15, 9, 6 });// ����3�е��Y����
		values.add(new double[] { 9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10 });// ����4�е��Y����
		int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.CYAN,
				Color.YELLOW };// ÿ�����е���ɫ����
		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE,
				PointStyle.DIAMOND, PointStyle.TRIANGLE, PointStyle.SQUARE };// ÿ�������е����״����
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);// ����AbstractDemoChart�еķ�������renderer.
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setFillPoints(true);// ����ͼ�ϵĵ�Ϊʵ��
		}
		setChartSettings(renderer, "Average temperature", "Month",
				"Temperature", 0.5, 12.5, -10, 40, Color.LTGRAY, Color.LTGRAY);// ����AbstractDemoChart�еķ�������ͼ���renderer����.
		renderer.setXLabels(12);// ����x����ʾ12����,����setChartSettings�����ֵ����Сֵ�Զ������ļ��
		renderer.setYLabels(10);// ����y����ʾ10����,����setChartSettings�����ֵ����Сֵ�Զ������ļ��
		renderer.setShowGrid(true);// �Ƿ���ʾ����
		renderer.setXLabelsAlign(Align.RIGHT);// �̶�����̶ȱ�ע֮������λ�ù�ϵ
		renderer.setYLabelsAlign(Align.CENTER);// �̶�����̶ȱ�ע֮������λ�ù�ϵ
		renderer.setZoomButtonsVisible(true);// �Ƿ���ʾ�Ŵ���С��ť

		renderer.setPanLimits(new double[] { -10, 20, -10, 40 }); // �����϶�ʱX��Y����������ֵ��Сֵ.
		renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });// ���÷Ŵ���СʱX��Y������������Сֵ.
		Intent intent = ChartFactory.getLineChartIntent(context,
				buildDataset(titles, x, values), renderer,
				"Average temperature111");// ����Intent
		return intent;
	}

}