package com.ewc.activity;

import com.ewc.base.C;
import com.ewc.bean.GetObjectFromService;
import com.ewc.db.PreferenceMap;
import com.ewc.https.WebService;
import com.ewc.tools.CommonUtils;
import com.ewc.tools.SimpleNetTask;
import com.ewc.utils.Utils;
import com.example.environmentwithcar.R;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends BaseEntryActivity implements OnClickListener {

	private EditText usernameEdit, passwordEdit;
	private Button loginBtn;
	private TextView login_forgetpassword,registerBtn;
	private ImageView image_password, login_remenber_password_image;
	private View login_remenber_password;
	private PreferenceMap accountPre;
	private String account, password;

	ProgressDialog dialog;

	private boolean canseePassword = false;
	private boolean remenberAccount = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		accountPre = new PreferenceMap(ctx);
		findView();
		initData();
		initAction();
	}

	public void findView() {
		usernameEdit = (EditText) findViewById(R.id.et_username);
		passwordEdit = (EditText) findViewById(R.id.et_password);
		loginBtn = (Button) findViewById(R.id.btn_login);
		login_forgetpassword = (TextView) findViewById(R.id.login_forgetpassword);
		image_password = (ImageView) findViewById(R.id.image_password);
		login_remenber_password_image = (ImageView) findViewById(R.id.login_remenber_password_image);
		login_remenber_password = findViewById(R.id.login_remenber_password);
		registerBtn = (TextView) findViewById(R.id.btn_register);
		dialog = new ProgressDialog(LoginActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage("登录中......");
		image_password.setOnClickListener(this);
		login_remenber_password.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		login_forgetpassword.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
	}

	private void initData() {
		if (accountPre.isRemenberAccount()) {
			usernameEdit.setText(accountPre.getAccount());
			passwordEdit.setText(accountPre.getPassword());
			login_remenber_password_image
					.setImageResource(R.drawable.ic_register_agree_true);
			remenberAccount = true;
			Utils.setEditTextLastPosition(usernameEdit);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == registerBtn) {
			Utils.goActivity(ctx, RegisterActivity.class);
		}else if (v == image_password) {
			if (canseePassword) {
				image_password
						.setImageResource(R.drawable.ic_login_input_password_false);
				passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				canseePassword = false;
			} else {
				image_password
						.setImageResource(R.drawable.ic_login_input_password_true);
				passwordEdit
						.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				canseePassword = true;
			}
			Utils.setEditTextLastPosition(passwordEdit);
		} else if (v == login_remenber_password) {
			if (remenberAccount) {
				login_remenber_password_image
						.setImageResource(R.drawable.ic_register_agree_false);
				remenberAccount = false;
			} else {
				login_remenber_password_image
						.setImageResource(R.drawable.ic_register_agree_true);
				remenberAccount = true;
			}
		} else {

			login();
		}
	}

	private void initAction() {
		// 如果用户名改变，清空密码
		usernameEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				passwordEdit.setText(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private void login() {
		if (!CommonUtils.isNetWorkConnected(this)) {
			Utils.toast("net wrong");
			return;
		}

		account = usernameEdit.getText().toString().trim();
		password = passwordEdit.getText().toString().trim();

		if (TextUtils.isEmpty(account)) {
			Utils.toast("用户名不能为空");
			return;
		}
		if (TextUtils.isEmpty(password)) {
			Utils.toast("密码不能为空");
			return;
		}
		accountPre.setIsRemenberAccount(remenberAccount);
		if (remenberAccount) {
			accountPre.setAccount(account);
			accountPre.setPassword(password);
		} else {
			accountPre.setAccount("");
			accountPre.setPassword("");
		}
		dialog.show();

		param.clear();
		param.put("phone", account);
		param.put("password", password);
		new SimpleNetTask(ctx, true) {
			boolean flag;

			@Override
			protected void onSucceed() {
				if (!flag) {
					Utils.toast("登录失败");
					dialog.dismiss();
				} else {
					dialog.dismiss();
					MainActivity.goMainActivity(LoginActivity.this);
					new PreferenceMap(ctx).setIsLogin(true);
					finish();
				}
			}

			@Override
			protected void doInBack() throws Exception {
				String jsonstr = new WebService(C.LOGIN, param).getReturnInfo();
				flag = GetObjectFromService.getLogin2Result(jsonstr);
			}
		}.execute();
	}
}
