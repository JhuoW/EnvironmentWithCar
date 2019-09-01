package com.ewc.db;


import com.ewc.bean.User;
import com.ewc.utils.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class PreferenceMap {
	public static final String ISREMENBER = "isremenber";
	public static final String ISPUSH = "ispush";
	public static final String ACCOUNT = "account";
	public static final String PASSWORD = "password";

	public static final String NAME = "curusername";
	public static final String SEX = "curusersex";
	public static final String ID = "curuserid";
	public static final String IMAGE = "curuserimage";
	public static final String USERACCOUNT = "curuseraccount";
	public static final String DEPARTMENT = "curuserdepartment";
	public static final String ISLOGIN = "islogin";
	public static final String AUTHORITY = "authority";
	
	public static final String USERNAME = "username";
	public static final String PHONE = "phone";

	Context cxt;
	SharedPreferences pref;
	SharedPreferences.Editor editor;
	public static PreferenceMap currentUserPreferenceMap;

	public PreferenceMap(Context cxt) {
		this.cxt = cxt;
		pref = PreferenceManager.getDefaultSharedPreferences(cxt);
		editor = pref.edit();
	}

	public PreferenceMap(Context cxt, String prefName) {
		this.cxt = cxt;
		pref = cxt.getSharedPreferences(prefName, Context.MODE_PRIVATE);
		editor = pref.edit();
	}

	public static PreferenceMap getCurUserPrefDao(Context ctx) {
		if (currentUserPreferenceMap == null) {
			currentUserPreferenceMap = new PreferenceMap(ctx, Utils.getID());
		}
		return currentUserPreferenceMap;
	}

	public boolean isRemenberAccount() {
		return pref.getBoolean(ISREMENBER, false);
	}

	public void setIsRemenberAccount(Boolean flag) {
		editor.putBoolean(ISREMENBER, flag).commit();
	}

	public boolean isPush() {
		return pref.getBoolean(ISPUSH, true);
	}

	public void setIsPush(Boolean flag) {
		editor.putBoolean(ISPUSH, flag).commit();
	}

	public String getAccount() {
		return pref.getString(ACCOUNT, "");
	}

	public void setAccount(String account) {
		editor.putString(ACCOUNT, account).commit();
	}

	public String getPassword() {
		return pref.getString(PASSWORD, "");
	}

	public void setPassword(String password) {
		editor.putString(PASSWORD, password).commit();
	}
	public void setUser(User user) {
		editor.putString(ID, user.getUserId()).commit();
		editor.putString(USERNAME, user.getUserName()).commit();
		editor.putString(PHONE, user.getPhone()).commit();
	}

	public User getUser() {
		User user = new User();
		user.setUserId(pref.getString(ID, ""));
		user.setUserName(pref.getString(USERNAME, ""));
		user.setPhone(pref.getString(PHONE, ""));
		return user;
	}
	
	public boolean isLogin() {
		return pref.getBoolean(ISLOGIN, false);
	}
	public void setIsLogin(Boolean flag) {
		editor.putBoolean(ISLOGIN, flag).commit();
	}
}
