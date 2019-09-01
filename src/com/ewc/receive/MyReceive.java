package com.ewc.receive;

import com.ewc.bean.GetObjectFromService;
import com.ewc.bean.Notify;
import com.ewc.db.PreferenceMap;
import com.ewc.view.MyNotification;

import cn.jpush.android.api.JPushInterface;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MyReceive extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle bundle = intent.getExtras();
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			String content = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			Notify notify = GetObjectFromService.getNotificationDetail(content);
			if (notify.getType().equals("PushNews")&&new PreferenceMap(context).isPush()) {
				MyNotification notification = new MyNotification(context,
						notify);
				notification.sendNotification();
			}

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
		} else {
		}
	}

}
