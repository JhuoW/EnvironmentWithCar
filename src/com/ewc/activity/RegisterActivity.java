package com.ewc.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ewc.base.C;
import com.ewc.bean.GetObjectFromService;
import com.ewc.https.WebService;
import com.ewc.utils.Utils;
import com.ewc.view.HeaderLayout;
import com.example.environmentwithcar.R;

public class RegisterActivity extends BaseActivity {
	private EditText et_phonenumber, et_inputusername, et_inputpassword;
	private Button btn_register;
	private String phonenumber, username, password;

	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initView();
		initAction();
	}

	private void initView() {
		dialog = new ProgressDialog(ctx);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage("×¢²áÖÐ......");
		et_phonenumber = (EditText) findViewById(R.id.et_inputphone);
		et_inputusername = (EditText) findViewById(R.id.et_inputusername);
		et_inputpassword = (EditText) findViewById(R.id.et_inputpassword);
		btn_register = (Button) findViewById(R.id.btn_register);
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showTitle("ÕÊºÅ×¢²á");
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RegisterActivity.this.finish();
			}
		});
	}

	private void initAction() {
		btn_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				username = et_inputusername.getEditableText().toString();
				password = et_inputpassword.getEditableText().toString();
				phonenumber = et_phonenumber.getEditableText().toString();
				if (TextUtils.isEmpty(username)) {
					Utils.toast(R.string.User_name_cannot_be_empty);
					return;
				}
				if (TextUtils.isEmpty(password)) {
					Utils.toast(R.string.Password_cannot_be_empty);
					return;
				}
				if (TextUtils.isEmpty(phonenumber)) {
					Utils.toast(R.string.Phone_number_cannot_be_empty);
					return;
				}
				// if (!Utils.isMobileNum(phonenumber)) {
				// Utils.toast(R.string.Phone_number_is_wrong);
				// return;
				// }
				if (!Utils.isMobileNO(phonenumber)) {
					Utils.toast(R.string.Phone_number_is_wrong);
					return;
				}
				param.clear();
				param.put("phonenumber", phonenumber);
				param.put("username", username);
				param.put("password", password);
				new RegistAsyncTask().execute();
			}
		});
	}

	class RegistAsyncTask extends AsyncTask<String, Void, Object> {
		Boolean flag = true;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}

		@Override
		protected Object doInBackground(String... params) {
			String jsonstr = new WebService(C.REGIST, param).getReturnInfo();
			Boolean result = GetObjectFromService.getSimplyResult(jsonstr);
			return result;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);

			Boolean _result = (Boolean) result;
			if (_result) {
				Utils.toast("×¢²á³É¹¦");
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				RegisterActivity.this.finish();
			} else {
				Utils.toast("×¢²áÊ§°Ü");
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		}

	}

}
