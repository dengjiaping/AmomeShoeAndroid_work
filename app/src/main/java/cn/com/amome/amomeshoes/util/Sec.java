package cn.com.amome.amomeshoes.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class Sec {

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4',
		'5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * md5取文件摘要
	 * @param file
	 * @return
	 */
	public static String md5sum(File file) {
		if (!file.isFile()) {
			return null;
		}
		InputStream fis;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		MessageDigest md5;
		try {
			fis = new FileInputStream(file);
			md5 = MessageDigest.getInstance("MD5");
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			return toHexString(md5.digest());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 32位
	 * @param message
	 * @return
	 */
	public static String MD5Encrypt(String message) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(message.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuilder buf = new StringBuilder("");
			for (byte aB : b) {
				i = aB;
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			String str32 = buf.toString().toUpperCase();
			return str32;
		} catch (Exception e) {
			return e.toString();
		}
	}

	/**
	 * 使用MD5算法加密字符串.
	 * @param message
	 *            要加密的字符串.
	 * @param length
	 *            指定返回加密后字符串长度，其值必须是16或者32.
	 * @return 加密之后的字符串
	 */
	public static String MD5Encrypt(String message, int length) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(message.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuilder buf = new StringBuilder("");
			for (byte aB : b) {
				i = aB;
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			String str32 = buf.toString().toUpperCase();
			if (length == 32)
				return str32;
			else if (length == 16)
				return str32.substring(8, 24);
			else
				return "error";
		} catch (Exception e) {
			return e.toString();
		}
	}
	/** 16进制字符串转换ASCII码 */
	public static String toStringHex1(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "ASCII");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}
	/**
	 * Turns array of bytes into string
	 * @param buf
	 *            Array of bytes to convert to hex string
	 * @return Generated hex string
	 */
	@SuppressWarnings("unused")
	private static String byteArr2HexStr(byte[] buf) {
		StringBuilder sb = new StringBuilder(buf.length * 2);
		int i;
		for (i = 0; i < buf.length; i++) {
			if (((int) buf[i] & 0xff) < 0x10)
				sb.append("0");

			sb.append(Long.toString((int) buf[i] & 0xff, 16));
		}
		return sb.toString();
	}

	/**
	 * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[]
	 * buf)互为可逆的转换过程
	 * @param src
	 *            需要转换的字符串
	 * @return 转换后的byte数组
	 */
	@SuppressWarnings("unused")
	private static byte[] hexStr2ByteArr(String src) {
		if (src.length() < 1) {
			return null;
		}
		byte[] encrypted = new byte[src.length() / 2];
		for (int i = 0; i < src.length() / 2; i++) {
			int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);
			encrypted[i] = (byte) (high * 16 + low);
		}
		return encrypted;
	}
}
