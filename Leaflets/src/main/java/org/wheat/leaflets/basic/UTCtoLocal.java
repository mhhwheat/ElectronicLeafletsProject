
package org.wheat.leaflets.basic;




import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/** 
 * description:��utcʱ���תΪ����ʱ���
 * @author wheat
 * date: 2015-3-4  
 * time: ����5:07:16
 */
public class UTCtoLocal 
{
	/**
	 * 
	 * @param utcTime			Դʱ��
	 * @param utcTimePatten		Դʱ��ĸ�ʽ
	 * @param localTimePatten 	���صĵ���ʱ��ĸ�ʽ
	 * @return
	 */
	public static String utc2Local(String utcTime,String utcTimePatten,String localTimePatten)
	{
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date gpsUTCDate = null;
		try {
			gpsUTCDate = utcFormater.parse(utcTime);
		} catch (ParseException e) {
		e.printStackTrace();
		}
		SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
		localFormater.setTimeZone(TimeZone.getDefault());
		String localTime = localFormater.format(gpsUTCDate.getTime());
		return localTime;
	}
	
	public static String utc2Local(String utcTime,String localTimePatten)
	{
		String utcTimePatten="yyyy-MM-dd'T'HH:mm:ss'Z'";
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date gpsUTCDate = null;
		try {
			gpsUTCDate = utcFormater.parse(utcTime);
		} catch (ParseException e) {
		e.printStackTrace();
		}
		SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
		localFormater.setTimeZone(TimeZone.getDefault());
		String localTime = localFormater.format(gpsUTCDate.getTime());
		return localTime;
	}
	
	/**
	 * Ĭ�ϵ�utcʱ����iso 8601��ʽ��Ĭ��ҪתΪ����ʱ��ĸ�ʽ��yyyy-MM-dd HH:mm:ss
	 * @param utcTime
	 * @return
	 */
	public static String utc2Local(String utcTime)
	{
		String utcTimePatten="yyyy-MM-dd'T'HH:mm:ss'Z'";
		String localTimePatten="yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date gpsUTCDate = null;
		try {
			gpsUTCDate = utcFormater.parse(utcTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
		localFormater.setTimeZone(TimeZone.getDefault());
		String localTime = localFormater.format(gpsUTCDate.getTime());
		return localTime;
		
	}
	
	
	
	public static Date utc2LocalDate(String utcTime)
	{
		String utcTimePatten="yyyy-MM-dd'T'HH:mm:ss'Z'";
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date gpsUTCDate = null;
		try {
			gpsUTCDate = utcFormater.parse(utcTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return gpsUTCDate;
	}
	
	public static String localDate2UTC(Date date)
	{
		String utcTimePatten="yyyy-MM-dd'T'HH:mm:ss'Z'";
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		return utcFormater.format(date);
	}
	
	public static String localDate2UTC()
	{
		String utcTimePatten="yyyy-MM-dd'T'HH:mm:ss'Z'";
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		return utcFormater.format(new Date());
	}

	public static String local2UTC(String local,SimpleDateFormat format)
	{
		Date date=null;

		try {
			date = format.parse(local);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if(date==null)
			return "";

		return localDate2UTC(date);
	}
	
}
