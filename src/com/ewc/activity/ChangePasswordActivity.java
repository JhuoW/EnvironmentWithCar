package com.ewc.activity;

import java.util.HashMap;
import java.util.Map;

import com.ewc.base.C;
import com.ewc.bean.GetObjectFromService;
import com.ewc.bean.User;
import com.ewc.db.PreferenceMap;
import com.ewc.https.WebService;
import com.ewc.tools.SimpleNetTask;
import com.ewc.utils.Utils;
import com.ewc.view.Code;
import com.ewc.view.HeaderLayout;
import com.example.environmentwithcar.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class ChangePasswordActivity extends BaseActivity implements OnClickListener{

	private EditText password, password2, code;
	private ImageView codeimage;
	private Button btn_change;

	private String realCode;
	private String oldpassword;
	private String newpassword;
	
	User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changepassword);
		user = new PreferenceMap(ctx).getUser();
		initView();
		initAction();
		initData();
	}

	private void initView() {
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		password = (EditText) findViewById(R.id.et_password);
		password2 = (EditText) findViewById(R.id.et_password2);
		code = (EditText) findViewById(R.id.et_code);
		codeimage = (ImageView) findViewById(R.id.iv_showCode);
		btn_change = (Button) findViewById(R.id.btn_change);
	}
	
	private void initAction() {
		headerLayout.showLeftBackButton(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ChangePasswordActivity.this.finish();
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_showCode:
			codeimage.setImageBitmap(Code.getInstance().createBitmap());
			realCode = Code.getInstance().getCode();
			break;
		case R.id.btn_change:
			oldpassword = password.getEditableText().toString();
			newpassword = password2.getEditableText().toString();
			if (!code.getEditableText().toString().equals(realCode)) {
				Utils.toast("��֤�벻��ȷ");
				return;
			} else if (oldpassword.length() == 0) {
				Utils.toast("��ʼ���벻��Ϊ��");
				return;
			} else if (newpassword.length() == 0) {
				Utils.toast("�����벻��Ϊ��");
				return;
			} else if (newpassword.length() < 5) {
				Utils.toast("��������Ϊ��λ��");
				return;
			} else {
				new SimpleNetTask(ctx, true) {
					boolean flag;
					@Override
					protected void onSucceed() {
						if (flag) {
//							try {
//								AVUser.getCurrentUser().updatePassword(oldpassword, newpassword);
//							} catch (AVException e) {
//								e.printStackTrace();
//							}
							Utils.toast("�޸ĳɹ�");
							ChangePasswordActivity.this.finish();
						} else {
							Utils.toast("�޸�ʧ��");
						}
					}
					@Override
					protected void doInBack() throws Exception {
						Map<String, String> param = new HashMap<String, String>();
						param.put("phone",user.getPhone()
								);
						param.put("oldPassword",
								oldpassword);
						param.put("newPassword",
								newpassword);
						String jsonstr = new WebService(C.MODIFYUSERPASSWORD,
								param).getReturnInfo();
						flag = GetObjectFromService.getSimplyResult(jsonstr);
					}
				}.execute();
			}
			break;
		}
	}
	
	private void initData() {
		codeimage.setImageBitmap(Code.getInstance().createBitmap());
		realCode = Code.getInstance().getCode();
		codeimage.setOnClickListener(this);
		btn_change.setOnClickListener(this);
		headerLayout.showTitle("�޸�����");
	}
}
