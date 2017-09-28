package cn.com.amome.amomeshoes.http;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import cn.com.amome.amomeshoes.util.Environments;
import cn.com.amome.amomeshoes.util.L;
import cn.com.amome.amomeshoes.util.T;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;

public class AsynHttpDowanloadFile {
	private static String TAG = "AsynHttpDowanloadFile";
	private static ProgressDialog mDownloadProgress;

	/**
	 * @param url
	 *            要下载的图片文件URL
	 * @throws Exception
	 */
	public static void downloadImageFile(final String url, final String path)
			throws Exception {
		// 获取二进制数据如图片和其他文件
		ShoesHttpClient.client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				L.d(TAG, "downloadImageFile=" + url + "-------" + path);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] binaryData) {
				String tempPath = path;
				L.e(TAG, "共下载了：" + binaryData.length);
				Bitmap bmp = byteToBitmap(binaryData);
				// Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0,
				// binaryData.length);
				File file = new File(tempPath);
				// 压缩格式
				CompressFormat format = Bitmap.CompressFormat.JPEG;
				// 压缩比例
				int quality = 100;
				try {
					// 若存在则删除
					if (file.exists())
						file.delete();
					// 创建文件
					file.createNewFile();
					OutputStream stream = new FileOutputStream(file);
					// 压缩输出
					bmp.compress(format, quality, stream);
					// 关闭
					stream.close();
					L.d(TAG, "下载成功" + tempPath);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] binaryData, Throwable error) {
				L.e(TAG, "下载失败");
			}

		});
	}

	/**
	 * @param url
	 *            要下载的文件URL
	 * @throws Exception
	 */
	public static void downloadApkFile(final String url, final String path,
			final Context mContext) throws Exception {
		// 获取二进制数据如图片和其他文件
		ShoesHttpClient.client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				L.d(TAG, "downloadFile=" + url + "\n" + path);
				L.i("url", url);
				L.i("path", path);
				initFwDownloadProgress(mContext);
				showDownloadProgress();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] binaryData) {
				String tempPath = path;
				L.e(TAG, "共下载了：" + binaryData.length);
				File file = new File(tempPath);
				final File to = new File(file.getAbsolutePath()
						+ System.currentTimeMillis());
				file.renameTo(to);
				to.delete();
				try {
					// 若存在则删除
					if (file.exists()) {
						file.delete();
					}
					// 创建文件
					file.createNewFile();
					InputStream inputstream = new ByteArrayInputStream(
							binaryData);
					if (inputstream != null) {
						write2SDFromInput(path, inputstream);
						inputstream.close();
					}
					L.d(TAG, "下载成功" + tempPath);
					dismissDownloadProgress();
					// 安装文件
					Environments.installApk(mContext, file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] binaryData, Throwable error) {
				L.e(TAG, "下载失败");
				dismissDownloadProgress();
				T.showToast(mContext, "下载失败", 1);
			}

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				// TODO Auto-generated method stub
				super.onProgress(bytesWritten, totalSize);
				int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
				// 下载进度显示
				mDownloadProgress.setProgress(count);
				// L.e("下载 Progress>>>>>", bytesWritten + " / " + totalSize);
			}
		});
	}

	/**
	 * @param url
	 *            要下载的文件URL
	 * @throws Exception
	 */
	public static void downloadOtherFile(final String url, final String path,
			final Context mContext) throws Exception {
		// 获取二进制数据如图片和其他文件
		ShoesHttpClient.client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				L.d(TAG, "downloadFile=" + url + "\n" + path);
				L.i("url", url);
				L.i("path", path);
				initFwDownloadProgress(mContext);
				showDownloadProgress();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] binaryData) {
				String tempPath = path;
				L.e(TAG, "共下载了：" + binaryData.length);
				File file = new File(tempPath);
				final File to = new File(file.getAbsolutePath()
						+ System.currentTimeMillis());
				file.renameTo(to);
				to.delete();
				try {
					// 若存在则删除
					if (file.exists()) {
						file.delete();
					}
					// 创建文件
					file.createNewFile();
					InputStream inputstream = new ByteArrayInputStream(
							binaryData);
					if (inputstream != null) {
						write2SDFromInput(path, inputstream);
						inputstream.close();
					}
					L.d(TAG, "下载成功" + tempPath);
					dismissDownloadProgress();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] binaryData, Throwable error) {
				L.e(TAG, "下载失败");
				dismissDownloadProgress();
				T.showToast(mContext, "下载失败", 1);
			}

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				// TODO Auto-generated method stub
				super.onProgress(bytesWritten, totalSize);
				int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
				// 下载进度显示
				mDownloadProgress.setProgress(count);
				// L.e("下载 Progress>>>>>", bytesWritten + " / " + totalSize);
			}
		});
	}

	/**
	 * 将一个inputstream里面的数据写入SD卡中 第一个参数为目录名 第二个参数为文件名
	 */
	public static File write2SDFromInput(String path, InputStream inputstream) {
		File file = null;
		OutputStream output = null;
		try {
			file = new File(path);
			output = new FileOutputStream(file);
			// 4k为单位，每4K写一次
			byte buffer[] = new byte[4 * 1024];
			int temp = 0;
			while ((temp = inputstream.read(buffer)) != -1) {
				// 获取指定信,防止写入没用的信息
				output.write(buffer, 0, temp);
			}
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return file;
	}

	public static void initFwDownloadProgress(Context context) {
		if (mDownloadProgress == null) {
			mDownloadProgress = new ProgressDialog(context);
			mDownloadProgress.setMax(100);
			mDownloadProgress.setProgress(0);
			mDownloadProgress.setTitle("魔秘下载");
			mDownloadProgress.setMessage("下载进度");
			mDownloadProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDownloadProgress.setIndeterminate(false);
			mDownloadProgress.setCancelable(false);
		}
	}

	public static void showDownloadProgress() {
		if (mDownloadProgress != null) {
			try {
				mDownloadProgress.show();
			} catch (Exception e) {
				// TODO: handle exception
				Log.i(TAG, "crash");
			}

		}
	}

	public static void dismissDownloadProgress() {
		if (mDownloadProgress != null) {
			mDownloadProgress.dismiss();
		}
	}

	public static Bitmap byteToBitmap(byte[] imgByte) {
		InputStream input = null;
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 8;
		input = new ByteArrayInputStream(imgByte);
		SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
				input, null, options));
		bitmap = (Bitmap) softRef.get();
		if (imgByte != null) {
			imgByte = null;
		}

		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}
}
