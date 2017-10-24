package cn.com.amome.amomeshoes.util;

import java.text.SimpleDateFormat;
import java.util.Date;

//暂时屏蔽
//import android.annotation.SuppressLint;
//
//import java.text.DecimalFormat;
//import java.text.ParseException;
//import java.text.ParsePosition;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//@SuppressLint("SimpleDateFormat")
public class DateUtils {
//	/**
//	   * 获取现在时间
//	   * @return 返回短时间字符串格式yyyy-MM-dd
//	   */
//	public static String getStringDateShort() {
//		   Date currentTime = new Date();
//		   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		   String dateString = formatter.format(currentTime);
//		   return dateString;
//		}
//
//	/**
//	   * 获取时间 小时:分;秒 HH:mm:ss
//	   * @return
//	   */
//	public static String getTimeShort() {
//	   SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//	   Date currentTime = new Date();
//	   String dateString = formatter.format(currentTime);
//	   return dateString;
//	}
//
//	/**
//	   * 获取指定日期的时间 小时:分 HH:mm
//	   * @return
//	   */
//	public static String getDateOfTime(Date date) {
//	   SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
//	   String dateString = formatter.format(date);
//	   return dateString;
//	}
//
	/**
	   * 获取现在时间
	   * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	   */
	public static String getStringDate() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}


//把long的时间转换成秒  不带单位
	public static String changeToS(long time){
		Date date=new Date(time);
		return new SimpleDateFormat("ss").format(date);
	}

//	/**
//	   * 将短时间格式字符串转换为时间 yyyy-MM-dd
//	   * @param strDate
//	   * @return
//	   */
//	public static Date strToDate(String strDate) {
//	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//	   ParsePosition pos = new ParsePosition(0);
//	   Date strtodate = formatter.parse(strDate, pos);
//	   return strtodate;
//	}
//
//	/**
//	   * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
//	   * @param dateDate
//	   * @return
//	   */
//	public static String dateToStrLong(java.util.Date dateDate) {
//	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	   String dateString = formatter.format(dateDate);
//	   return dateString;
//	}
//
//	/**
//	   * 将短时间格式时间转换为字符串 yyyy-MM-dd
//	   * @param dateDate
//	   * @param k
//	   * @return
//	   */
//	public static String dateToStr(java.util.Date dateDate) {
//	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//	   String dateString = formatter.format(dateDate);
//	   return dateString;
//	}
//
//	/**
//	 * 转换为字符串 yyyy-MM-dd
//	 * @param year 年
//	 * @param month 月
//	 * @param day 天
//	 * @return
//	 */
//	public static String intFormatToDateStr(int year ,int month,int day){
//		String parten = "00";
//		DecimalFormat decimal = new DecimalFormat(parten);
//		String dateStr = year + "-"+
//		decimal.format(month)+"-"+decimal.format(day);
//		return dateStr;
//	}
//
//	/**
//	 * 将字符串日期转换为long类型
//	 * @param date
//	 * @return
//	 * @throws ParseException
//	 * @throws Exception
//	 */
//	public static Long strDateToLong(String date) {
//		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		try {
//			Date dt = sdf.parse(date);
//			return dt.getTime();
//		} catch (ParseException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	/**
//	 * 将10位数毫秒转换为详细时间
//	 */
//	public static String getDateTime(long time){
//		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(time*1000);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		return sdf.format(c.getTime());
//	}
//
//	/**
//	 * 将10位数毫秒转换为日期
//	 */
//	public static String getDate(String time){
//		if(time.length()<10){
//			return "0";
//		}
//		long timel = Long.parseLong(time);
//		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(timel *1000);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		return sdf.format(c.getTime());
//	}
//
//	/**
//	 * 将13位毫秒转换为日期
//	 */
//	public static String getLongDate(long time){
//		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(time);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		return sdf.format(c.getTime());
//	}
//
//	/**
//	 * 得到下个月具体时间还款日期的时间戳
//	 */
//	public static long getNextMonth(){
//		Calendar c = Calendar.getInstance();
//		int month = c.get(Calendar.MONTH)+2;
//		int year = c.get(Calendar.YEAR);
//		String date = year+"-"+month+"-16 00:00:00";
//		long time = strDateToLong(date);
//		return time/1000;
//	}
//
//	/**
//	 * 得到添加月份的时间
//	 * @param month
//	 * @return
//	 */
//	public static String getAddMonth(int month){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.MONTH, month);
//		c.set(Calendar.DAY_OF_MONTH, 1);
//		c.set(Calendar.HOUR_OF_DAY, 0);
//		c.set(Calendar.MINUTE, 0);
//		c.set(Calendar.SECOND, 0);
//		return sdf.format(c.getTime());
//	}
//
//	/**
//	 *得到添加月份的时间戳
//	 * @param month
//	 * @return
//	 */
//	public static long getMonthTime(int month){
//		long time = strDateToLong(getAddMonth(month));
//		return time/1000;
//	}
//
//	/**
//	 * 返回13位月份时间戳
//	 * @param month
//	 * @return
//	 */
//	public static long getMonthLongTime(int month){
//		return strDateToLong(getAddMonth(month));
//	}
//
//	/**
//	 * 根据时间戳得到月份和年
//	 * @param time
//	 * @return
//	 */
//	public static String getCurrYearMonth(long time){
//		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(time*1000);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
//		return sdf.format(c.getTime());
//	}
//
//	/**
//	 * 根据时间戳得到年月日
//	 * @param time
//	 * @return
//	 */
//	public static String getCurrYearMonthDay(long time){
//		Calendar c = Calendar.getInstance();
//		c.setTimeInMillis(time*1000);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
//		return sdf.format(c.getTime());
//	}
//
//	/**
//	 * 得到当前时间的月份和年
//	 */
//	public static int getThisYearMonth(){
//		Calendar c = Calendar.getInstance();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
//		c.get(Calendar.YEAR);
//		c.get(Calendar.MONTH+1);
//		return Integer.parseInt(sdf.format(c.getTime()));
//	}
//
//	/**
//	 * 得到添加月份的月份
//	 * @param month
//	 * @return
//	 */
//	public static String getMonth(int month){
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.MONTH, month);
//		return c.get(Calendar.MONTH)+1+"";
//	}
//
//	public static String getYearMonth(int month){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.MONTH, month);
//		return sdf.format(c.getTime());
//	}
//
//	public static void main(String[] args) {
//		System.out.println(getAddMonth(-1));
//	}
//
//	/**
//	 * 获取当前时间戳
//	 */
//	public static long getLongTime(){
//		return System.currentTimeMillis();
//	}
}
