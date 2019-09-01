package com.ewc.activity;

import com.ewc.db.PreferenceMap;
import com.example.environmentwithcar.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;


public class SplashActivity extends BaseActivity{
	private static final int sleepTime = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lay_start_act);

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				long costTime = System.currentTimeMillis();
				try {
					if (sleepTime - costTime > 0) {
						Thread.sleep(sleepTime - costTime);
					} else {
						Thread.sleep(sleepTime);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(new PreferenceMap(ctx).isLogin()){
					startActivity(new Intent(SplashActivity.this,
							MainActivity.class));
				}else{
					startActivity(new Intent(SplashActivity.this,
							LoginActivity.class));
				}
				finish();
			}
		}).start();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}
