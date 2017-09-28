package cn.com.amome.amomeshoes.view.main.my.setting;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.ClassType;

import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserHelpActivity extends Activity implements OnClickListener {
	private TextView tv_title, tv_left;
	private RelativeLayout rl_left;
	private ImageView iv_left;
	private WebView webView;
	private String leftValue, titleValue;
	private String indexUrl = "file:///android_asset/amome.ser Assistance.html";
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		mContext = this;
//		leftValue = getIntent().getStringExtra("left");
		titleValue = getIntent().getStringExtra("title");
		initView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		rl_left = (RelativeLayout) findViewById(R.id.rl_left);
		rl_left.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.title_tv);
		tv_left = (TextView) findViewById(R.id.left_tv);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_title.setText(titleValue);
		tv_title.setTextColor(mContext.getResources().getColor(R.color.turkeygreen));
	//	tv_left.setText(leftValue);


		webView = (WebView) findViewById(R.id.webView);
		webView.loadUrl(indexUrl);
		WebSettings wSet = webView.getSettings();
		wSet.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
		wSet.setLoadWithOverviewMode(true);
		wSet.setJavaScriptEnabled(true); // 设置WebView属性，能够执行Javascript脚本
		wSet.setJavaScriptCanOpenWindowsAutomatically(true);
		wSet.setSupportZoom(true);// 设置自动缩放
		wSet.setUseWideViewPort(true);// 将图片调整到适合webview的大小
		webView.getSettings().setDefaultTextEncodingName("UTF-8");
		// wSet.setBuiltInZoomControls(true); //设置支持缩放
		// webView.setHorizontalScrollBarEnabled(false);// 设置水平滚动条
		// webView.setVerticalScrollBarEnabled(false);// 设置竖直滚动条
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				webView.getSettings().setBlockNetworkImage(false);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				webView.getSettings().setBlockNetworkImage(true);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_left:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == android.view.KeyEvent.KEYCODE_BACK
				&& webView.canGoBack()) {

			String currentUrl = webView.getUrl();
			if (currentUrl.indexOf(indexUrl) != -1) {
				webView.clearHistory();
				webView.loadUrl(indexUrl);
				return true;
			} else {
				webView.goBack();// 返回前一个页面
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.UserHelpActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.UserHelpActivity);
		MobclickAgent.onPause(mContext);
	}
}
