package com.ewc.activity;

import com.ewc.base.C;
import com.ewc.bean.GetObjectFromService;
import com.ewc.bean.PostModel;
import com.ewc.bean.User;
import com.ewc.db.PreferenceMap;
import com.ewc.https.WebService;
import com.ewc.tools.SimpleNetTask;
import com.ewc.utils.Utils;
import com.ewc.view.HeaderLayout;
import com.example.environmentwithcar.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PostDetailActivity extends BaseActivity {

	public static String URL = "http://103.229.127.7:8080/web-dynamic-visit-clean.aspx?nid=";
	private WebView webView;
	private ImageView blog_iscollection;
	private TextView tvDetailAuthor;
	private ProgressBar load_pro;
	private TextView tv_iscollection;
	boolean iscollection = false;

	PostModel curPost;

	User user ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postdetail);
		user = new PreferenceMap(ctx).getUser();
		Intent intent = getIntent();
		findView();
		setData(intent);
		initAction();
	}

	
	private void findView(){
		headerLayout = (HeaderLayout) findViewById(R.id.headerLayout);
		headerLayout.showLeftBackButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PostDetailActivity.this.finish();
			}
		});
		webView = (WebView) findViewById(R.id.webView);
		setWebSeeting(webView);
		tvDetailAuthor = (TextView) findViewById(R.id.tvDetailAuthor);
		load_pro = (ProgressBar) findViewById(R.id.load_pro);
		blog_iscollection = (ImageView) findViewById(R.id.img_collection);
		tv_iscollection = (TextView) findViewById(R.id.tv_collection);
		WebSettings settings = webView.getSettings();
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		settings.setLoadWithOverviewMode(true);
		
	}
	
	
	
	
	private void initAction(){
		blog_iscollection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				param.clear();
				String id = Utils.getID();
				param.put("phone", user.getPhone());
				param.put("healthNewsID", curPost.getId());
				new SimpleNetTask(ctx, true) {
					boolean flag;
					String method = "";

					@Override
					protected void onSucceed() {
						if (flag) {
							if (iscollection) {
								blog_iscollection
										.setImageResource(R.drawable.ic_collection_normal);
								tv_iscollection.setText("收藏");
								iscollection = false;
							} else {
								final Dialog dialog = new Dialog(ctx,
										R.style.like_toast_dialog_style);
								View view = LayoutInflater.from(ctx).inflate(
										R.layout.record_layout, null);
								dialog.setContentView(view, new LayoutParams(
										ViewGroup.LayoutParams.WRAP_CONTENT,
										ViewGroup.LayoutParams.WRAP_CONTENT));
								WindowManager.LayoutParams lp = dialog
										.getWindow().getAttributes();
								lp.alpha = 0.9f;
								lp.gravity = Gravity.CENTER;
								dialog.show();

								new Thread(new Runnable() {
									@Override
									public void run() {
										try {
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										Message msg = mHandler.obtainMessage();
										msg.obj = dialog;
										mHandler.sendMessage(msg);
									}
								}).start();

								blog_iscollection
										.setImageResource(R.drawable.ic_collection_pressed);
								tv_iscollection.setText("已收藏");
								iscollection = true;
							}

						}
					}

					@Override
					protected void doInBack() throws Exception {
						if (iscollection) {   //已收藏则isCollection是false
							method = C.DELETEHEALTHNEWSCOLLECTION;
						} else {
							method = C.ADDHEALTHNEWSCOLLECTION;
						}
						String jsonstr = new WebService(method, param)
								.getReturnInfo();
						flag = GetObjectFromService.getSimplyResult(jsonstr);
					}
				}.execute();
			}
		});
	}
	
	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			((Dialog) msg.obj).cancel();
			;
		}
	};
	
	
	
	@SuppressLint("SetJavaScriptEnabled") 
	public void setWebSeeting(final WebView webView) {
		webView.getSettings().setDefaultTextEncodingName("utf-8");
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					// 加载完毕，关闭进度条
					load_pro.setVisibility(View.GONE);
				} else {
					load_pro.setVisibility(View.VISIBLE);
					// 网页正在加载，显示进度框
					load_pro.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}
		});
	}
	
	public void clearWebView(WebView webView) {
		if (webView != null) {
			webView.stopLoading();
			webView.removeAllViews();
			webView.loadUrl("");
			webView.destroy();
		}
	}

	
	
	public boolean setData(Intent intent) {

		curPost = (PostModel) intent.getSerializableExtra("post");
		String id = curPost.getId();
		String title = curPost.getTitle();
		String url = URL + id;
		webView.loadUrl(url);
		headerLayout.showTitle(title);
		tvDetailAuthor.setText("本文由 " + curPost.getSource() + " 发布");
		if (curPost.getCollection().equals("已收藏")) {
			blog_iscollection
					.setImageResource(R.drawable.ic_collection_pressed);
			tv_iscollection.setText("已收藏");
			iscollection = true;
		} else {
			blog_iscollection.setImageResource(R.drawable.ic_collection_normal);
			tv_iscollection.setText("收藏");
			iscollection = false;
		}
		return true;
	}
}
