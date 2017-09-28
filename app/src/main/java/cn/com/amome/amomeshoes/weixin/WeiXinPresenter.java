package cn.com.amome.amomeshoes.weixin;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.http.ClientConstant;
import cn.com.amome.amomeshoes.http.ShoesHttpClient;
import cn.com.amome.amomeshoes.util.T;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.utils.L;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信分享,登陆,支付
 * @create time 2015-08-29
 */
public class WeiXinPresenter{
	public static final int IMAGE_SIZE = 32768;// 微信分享图片大小限制
	public static IWXAPI wxAPI = null;
	private static String weixinCode = "";
	private static String ACCESS_TOKEN = "";
	private static String OPENID = "";
	private static String REFRESH_TOKEN = "";
	private static Context mContext;
	private static Handler mHandler;

	public WeiXinPresenter(Context context) {
		mContext = context;
		if (wxAPI == null) {
			wxAPI = WXAPIFactory.createWXAPI(context, ThirdConstants.APP_ID,
					true);
			wxAPI.registerApp(ThirdConstants.APP_ID);
		}
	}

	public static void initWX() {
		if (wxAPI == null) {
			wxAPI = WXAPIFactory.createWXAPI(mContext, ThirdConstants.APP_ID,
					true);
			wxAPI.registerApp(ThirdConstants.APP_ID);
		}
	}

	/**
	 * 微信登陆(三个步骤) 1.微信授权登陆 2.根据授权登陆code 获取该用户token 3.根据token获取用户资料
	 * @param activity
	 */
	public static void loginWeixin(Handler handler) {
		mHandler = handler;
		initWX();
		if (!wxAPI.isWXAppInstalled()) {
			T.showToast(mContext, "未安装微信!", 1);
			return;
		}
		String time = String.valueOf(System.currentTimeMillis());
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = time;
		wxAPI.sendReq(req);
	}

	/**
	 * 获取微信访问token
	 */
	public static void getAccessToken(String code) {
		weixinCode = code;
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
				+ "appid=" + ThirdConstants.APP_ID + "&secret="
				+ ThirdConstants.App_Secret + "&code=" + weixinCode
				+ "&grant_type=authorization_code";
		ShoesHttpClient.client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] arg1,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, arg1, responseBody);
				if (statusCode == 200) {
					String result = new String(responseBody);
					L.i("111",
							"=================getAccessToken==================="
									+ result);
					Gson gson = new Gson();
					Presenter per = gson.fromJson(result, Presenter.class);
					L.i("111", "================access_token="
							+ per.access_token);
					L.i("111", "================expires_in=" + per.expires_in);
					L.i("111", "================openid=" + per.openid);
					L.i("111", "================refresh_token="
							+ per.refresh_token);
					L.i("111", "================scope=" + per.scope);
					ACCESS_TOKEN = per.access_token;
					OPENID = per.openid;
					REFRESH_TOKEN = per.refresh_token;
					if (!TextUtils.isEmpty(per.access_token)) {
						verifyToken();
					} else {
						Toast.makeText(mContext, R.string.auth_error,
								Toast.LENGTH_LONG).show();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1, arg2, arg3);
			}
		});
	}

	/**
	 * 校验access_token是否过期
	 */
	protected static void verifyToken() {
		String url = "https://api.weixin.qq.com/sns/auth?" + "access_token="
				+ ACCESS_TOKEN + "&openid=" + OPENID;
		ShoesHttpClient.client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] arg1,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, arg1, responseBody);
				if (statusCode == 200) {
					String result = new String(responseBody);
					L.i("111",
							"=================verifyToken==================="
									+ result);
					Gson gson = new Gson();
					Presenter per = gson.fromJson(result, Presenter.class);
					if (per.errcode == 0) {
						getWeiXinUserInfo();
					} else {// 过期
						refreshToken();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1, arg2, arg3);
			}
		});
	}

	/**
	 * 刷新token
	 */
	protected static void refreshToken() {
		String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?"
				+ "appid=" + ThirdConstants.APP_ID
				+ "&grant_type=refresh_token&refresh_token=" + REFRESH_TOKEN;
		ShoesHttpClient.client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] arg1,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, arg1, responseBody);
				if (statusCode == 200) {
					String result = new String(responseBody);
					L.i("111",
							"=================refreshToken==================="
									+ result);
					Gson gson = new Gson();
					Presenter per = gson.fromJson(result, Presenter.class);
					if (!TextUtils.isEmpty(per.access_token)) {
						getWeiXinUserInfo();
					} else {// 获取失败
						Toast.makeText(mContext, R.string.auth_error,
								Toast.LENGTH_LONG).show();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1, arg2, arg3);
			}
		});
	}

	/**
	 * 获取微信用户信息
	 */
	public static void getWeiXinUserInfo() {
		ThirdConstants.saveStr(mContext, ThirdConstants.W_ACCESS_TOKEN,
				ACCESS_TOKEN);
		String url = "https://api.weixin.qq.com/sns/userinfo?"
				+ "access_token=" + ACCESS_TOKEN + "&openid=" + OPENID;
		ShoesHttpClient.client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] arg1,
					byte[] responseBody) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, arg1, responseBody);
				if (statusCode == 200) {
					String result = new String(responseBody);
					if (!TextUtils.isEmpty(result)) {
						Message msg = new Message();
						msg.arg1 = 1;
						msg.obj = result;
						msg.what = ClientConstant.HANDLER_SUCCESS;
						mHandler.sendMessage(msg);
					} else {
						Toast.makeText(mContext, R.string.auth_error,
								Toast.LENGTH_LONG).show();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1, arg2, arg3);
			}
		});
	}

  /**
   * 微信分享
   * @param friendsCircle 是否分享到朋友圈
   */
  /*public void share(final boolean friendsCircle,final VideoB videoB){
    new LoadPicThread(videoB.getCover_url(),new Handler(){
      @Override
      public void handleMessage(Message msg) {
        byte[] bytes=(byte[]) msg.obj;
        if(bytes.length>IMAGE_SIZE){
          iView.showToast(R.string.image_no_big);
          return;
        }
        System.out.println("图片长度："+bytes.length);
        WXVideoObject videoObject = new WXVideoObject();//视频类型
        videoObject.videoUrl = videoB.getShare_url() + Constants.WEI_XIN + "&share_from="+com.kaka.utils.Constants.ANDROID;// 视频播放url
        WXMediaMessage wxMessage = new WXMediaMessage(videoObject);
        wxMessage.title = videoB.getContent();
        wxMessage.thumbData = bytes;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //transaction字段用于唯一标识一个请求
        req.transaction = String.valueOf(videoB.getId() + System.currentTimeMillis());
        req.message = wxMessage;
        req.scene = friendsCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        wxAPI.sendReq(req);
      }
    }).start();
  }*/

 /* private class LoadPicThread extends Thread{
    private String url;
    private Handler handler;
    public LoadPicThread(String url,Handler handler){
      this.url=url;
      this.handler=handler;
    }
    @Override
    public void run(){
      try {
        URL picurl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection)picurl.openConnection(); // 获得连接
        conn.setConnectTimeout(6000);//设置超时
        conn.setDoInput(true);
        conn.setUseCaches(false);//不缓存
        conn.connect();
        Bitmap bmp=BitmapFactory.decodeStream(conn.getInputStream());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
        int options = 100;
        while (output.toByteArray().length > IMAGE_SIZE && options != 10) {
          output.reset(); // 清空baos
          bmp.compress(Bitmap.CompressFormat.JPEG, options, output);// 这里压缩options%，把压缩后的数据存放到baos中
          options -= 10;
        }
        bmp.recycle();
        byte[] result = output.toByteArray();
        output.close();

        Message message=handler.obtainMessage();
        message.obj=result;
        message.sendToTarget();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }*/

  /**
   * 检查微信是否安装
   * @return true,支持；false,不支持
   */
	public boolean isWXAppInstalled() {
		return wxAPI.isWXAppInstalled();
	}

  /**
	 * 是否支持支付
	 * @return true,支持；false,不支持
	 */
	public boolean isPaySupported() {
		boolean isPaySupported = wxAPI.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		return isPaySupported;
	}
}