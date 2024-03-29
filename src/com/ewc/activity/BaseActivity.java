package com.ewc.activity;

import java.util.HashMap;
import java.util.Map;

import com.ewc.base.App;
import com.ewc.utils.Utils;
import com.ewc.view.HeaderLayout;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;


public abstract class BaseActivity extends FragmentActivity {
	protected Context ctx;
	protected HeaderLayout headerLayout;
	protected Map<String, String> param = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.getInstance().addActivity(this);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ctx = this;
	}

	protected void hideSoftInputView() {
		Utils.hideSoftInputView(this);
	}

	protected void setSoftInputMode() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

}
