package com.zhsj.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil
{
	/**
	 * 格式化字符串日期<br>
	 * 字符串格式为 yyyyMMddHHmmss
	 * 
	 * @param stringTime
	 *            字符串日期
	 * @return Date 实例 <br>
	 *         null 失败
	 */
	public static Date formatStringTimeNotBlank(String stringTime)
	{
		try
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = new Date();
			date = format.parse(stringTime);
			return date;
		} catch (ParseException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当前时间的yyyy-MM-dd HH:mm:ss字符串格式
	 * 
	 * @return 成功： yyyy-MM-dd HH:mm:ss 字符串 <br>
	 *         失败： null
	 */
	public static String getCurrentTimeHaveHR()
	{
		try
		{
			String stringTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			return stringTime;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取当前时间的 yyyyMMddHHmmss 字符串格式
	 * 
	 * @return 成功： yyyyMMddHHmmss 字符串 <br>
	 *         失败： null
	 */
	public static String getCurrentTime()
	{
		try
		{
			String stringTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			return stringTime;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static long unixTime() {
		return (long)(System.currentTimeMillis() / 1000L);
	}

	public static int getTodayStartTime() {
		Calendar time = Calendar.getInstance();
		time.set(Calendar.HOUR_OF_DAY, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		return (int)(time.getTimeInMillis()/1000L);
	}

	public static void main(String[] args)
	{
		String time = DateUtil.getCurrentTimeHaveHR();
		System.out.println(time);
	}
}
