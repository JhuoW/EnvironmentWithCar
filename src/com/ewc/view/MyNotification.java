package com.ewc.view;

import com.ewc.activity.PostDetailActivity;
import com.ewc.bean.Notify;
import com.ewc.bean.PostModel;
import com.example.environmentwithcar.R;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Notification.Builder;
import android.content.Context;
import android.content.Intent;


public class MyNotification {

	Context context;
	String jsonstr;//
	Notify notify;//
	String title;// ¢ò
	String image;//
	int type;//

	NotificationManager manager;
	int notificationID;
	public MyNotification(Context context, Notify notify) {
		super();
		this.context = context;
		this.notify = notify;
		manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	@SuppressLint("NewApi")
	public void sendNotification() {
		Intent intent = new Intent();
		if(notify.getType().equals("PushNews")){
			PostModel post=new PostModel();
			post.setId(notify.getContent());
			post.setTitle(notify.getTitle());
			post.setSource("≥µ‘ÿª∑æ≥º‡≤‚");
			post.setCollection(notify.getIsCollection());
			intent.putExtra("post", post);
			intent.setClass(context, PostDetailActivity.class);
		}
		// else if (notify.getType() == 1) {
		// intent.setClass(context, NewsDetailActivity.class);
		// } else if (notify.getType() == 2) {
		// intent.setClass(context, NewsDetailActivity.class);
		// }
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);

		Builder builder = new Notification.Builder(context);

		builder.setSmallIcon(R.drawable.logo);
		builder.setTicker(notify.getTitle());
		builder.setWhen(System.currentTimeMillis());
		builder.setContentTitle(notify.getTitle());
		builder.setContentText(notify.getTitle());
		builder.setContentIntent(pendingIntent);

		builder.setDefaults(Notification.DEFAULT_SOUND);
		Notification notification = builder.build();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		manager.notify(notificationID, notification);
	}
}
