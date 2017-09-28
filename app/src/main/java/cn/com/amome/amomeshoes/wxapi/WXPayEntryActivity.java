package cn.com.amome.amomeshoes.wxapi;

import cn.com.amome.amomeshoes.util.T;
import cn.com.amome.amomeshoes.view.account.PayResultActivity;
import cn.com.amome.amomeshoes.weixin.ThirdConstants;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	private static final String TAG = "WXPayEntryActivity";
    private IWXAPI api;
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, ThirdConstants.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode == 0){//支付成功
				T.showToast(this, "支付成功", 0);
				Intent intent = new Intent(WXPayEntryActivity.this, PayResultActivity.class);             
				intent.putExtra("payresult", "0");
				startActivity(intent);
			}else if(resp.errCode == -2){//支付取消
			}else{//支付失败
				T.showToast(this, "支付失败", 0);
				Intent intent = new Intent(WXPayEntryActivity.this, PayResultActivity.class);				
				intent.putExtra("payresult", "1");
				startActivity(intent);
			}
			finish();
		}
	}
}