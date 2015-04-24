/** 
 * description��
 * @author wheat
 * date: 2015-3-23  
 * time: ����12:22:02
 */ 
package org.wheat.leaflets.basic;

import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * description:
 * @author wheat
 * date: 2015-3-23  
 * time: ����12:22:02
 */
public class DateTools 
{
	public static String getDifferenceFromDate(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date now=new Date();
		long between=(now.getTime()-date.getTime())/1000;//��ʱ��תΪ��
		
		long day=between/(24*3600);
		long hour=between%(24*3600)/3600;
		long minute=between%3600/60;
		long second=between%60;
		
		if(day>0)
		{
			return format.format(date);
		}
		else if(hour>0)
		{
			return hour+new String("Сʱǰ");
		}
		else if(minute>0)
		{
			return minute+new String("����ǰ");
		}
		else
		{
			return second+new String("��ǰ");
		}
	}
	
	public static String getStringFromDate(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}
}
