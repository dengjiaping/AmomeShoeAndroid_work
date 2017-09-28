package cn.com.amome.amomeshoes.view.account;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.model.ClassType;

import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RegProtoActivity extends Activity implements OnClickListener {
	private TextView tv_title;
	private RelativeLayout rl_left;
	private ImageView iv_left;
	private Context mContext;
	private String titleValue;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		mContext = this;
		titleValue = getIntent().getStringExtra("title");
		initView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		tv_title = (TextView) findViewById(R.id.title_tv);
		iv_left = (ImageView) findViewById(R.id.iv_left);
		tv_title.setText(titleValue);
		tv_title.setTextColor(mContext.getResources().getColor(R.color.blue));
		iv_left.setImageResource(R.drawable.ic_back_blue);
		rl_left = (RelativeLayout) findViewById(R.id.rl_left);
		rl_left.setOnClickListener(this);

		webView = (WebView) findViewById(R.id.webView);
		WebSettings wSet = webView.getSettings();
		wSet.setJavaScriptEnabled(true);
		wSet.setJavaScriptCanOpenWindowsAutomatically(true);
		wSet.setSupportZoom(true);// 设置自动缩放
		wSet.setBuiltInZoomControls(true);
		wSet.setUseWideViewPort(true);
		webView.setHorizontalScrollBarEnabled(false);// 设置水平滚动条
		webView.setVerticalScrollBarEnabled(false);// 设置竖直滚动条
		webView.getSettings().setDefaultTextEncodingName("UTF-8");
		wSet.setLoadWithOverviewMode(true);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		webView.loadUrl("file:///android_asset/useragreement.html");
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

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(ClassType.WebViewActivity);
		MobclickAgent.onResume(mContext);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(ClassType.WebViewActivity);
		MobclickAgent.onPause(mContext);
	}
}
