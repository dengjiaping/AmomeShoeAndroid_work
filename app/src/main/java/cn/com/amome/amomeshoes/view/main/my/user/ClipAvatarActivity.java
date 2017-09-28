package cn.com.amome.amomeshoes.view.main.my.user;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.com.amome.amomeshoes.R;
import cn.com.amome.amomeshoes.util.Environments;
import cn.com.amome.amomeshoes.util.ImageAbsolutePathTransfrom;
import cn.com.amome.amomeshoes.util.ImageUtil;
import cn.com.amome.amomeshoes.widget.ClipImageLayout;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ClipAvatarActivity extends Activity implements OnClickListener {
	private TextView tv_cancle, tv_ok;
	private ClipImageLayout mClipImageLayout = null;
	private final int REQ_SELECT_IMAGE = 0x11;
	private final int REQ_TAKE_PHOTO = 0x12;
	private String avatarStr;
	private String iconurl = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_avatar);
		avatarStr = getIntent().getStringExtra("avatar");
		if (avatarStr.equals("takephoto")) {
			Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent2, REQ_TAKE_PHOTO);
		} else if (avatarStr.equals("album")) {
			Intent i = new Intent();
			i.setType("image/*");
			i.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(i, REQ_SELECT_IMAGE);
		}
		initView();
	}

	private void initView() {
		tv_cancle = (TextView) findViewById(R.id.tv_cancle);
		tv_ok = (TextView) findViewById(R.id.tv_ok);
		tv_cancle.setOnClickListener(this);
		tv_ok.setOnClickListener(this);
		mClipImageLayout = (ClipImageLayout) findViewById(R.id.clipImageLayout);
	}

	/**
	 * 创建图片
	 * 
	 * @param path
	 * @return
	 */
	private Bitmap createBitmap(String path) {
		if (path == null) {
			return null;
		}

		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 1;
		opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		opts.inDither = false;
		opts.inPurgeable = true;
		FileInputStream is = null;
		Bitmap bitmap = null;
		try {
			is = new FileInputStream(path);
			bitmap = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return bitmap;
	}

	/** 旋转图片 */
	private Bitmap rotateBitmap(int angle, Bitmap bitmap) {
		// 旋转图片动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, false);
		return resizedBitmap;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQ_SELECT_IMAGE && data != null) {
			Uri imageUri = data.getData();
			iconurl = ImageAbsolutePathTransfrom.getImageAbsolutePath(this,
					imageUri);
			// 有的系统返回的图片是旋转了，有的没有旋转，所以处理
			int degreee = ImageUtil.readBitmapDegree(iconurl);
			Bitmap bitmap = createBitmap(iconurl);
			if (bitmap != null) {
				if (degreee == 0) {
					mClipImageLayout.setImageBitmap(bitmap);
				} else {
					mClipImageLayout.setImageBitmap(rotateBitmap(degreee,
							bitmap));
				}
			} else {
				finish();
			}
			return;
		} else if (requestCode == REQ_TAKE_PHOTO && data != null
				&& data.getExtras() != null) {
			Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
			if (cameraBitmap != null) {
				mClipImageLayout.setImageBitmap(cameraBitmap);
			}
			return;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancle:
			finish();
			break;
		case R.id.tv_ok:
			Bitmap clipBitmap = mClipImageLayout.clip();
			compressImage(clipBitmap);
			Intent i = new Intent();
			i.putExtra("url", iconurl);
			setResult(RESULT_OK, i);
			finish();
			break;
		default:
			break;
		}
	}

	private void compressImage(Bitmap clipBitmap) {
		String iconPath = Environments.getImagePath() + "/momi_avatar.jpg";
		if (clipBitmap != null) {
			try {
				FileOutputStream fos = new FileOutputStream(iconPath);
				clipBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.close();
			} catch (Exception e) {
			}
		}
		ImageUtil.scal(iconPath, 64);
		iconurl = iconPath;
	}

}
