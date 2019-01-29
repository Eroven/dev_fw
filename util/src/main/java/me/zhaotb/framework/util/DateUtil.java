package me.zhaotb.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
	
	public static final String dateTimeSdf = "yyyyMMddHHmmss";
	
	public static final String dateTimeSdfWithSp = "yyyy-MM-dd HH:mm:ss";
	
	public static final String time = "HH:mm:ss";
	
	public static final String dateSdf = "yyyyMMdd";
	
	public static String nowDateTime() {
		return new SimpleDateFormat(dateTimeSdf).format(new Date());
	}
	
	public static String nowDate() {
		return new SimpleDateFormat(dateSdf).format(new Date());
	}

	public static String nowTime(){
	    return new SimpleDateFormat(time).format(new Date());
    }

	public static String formate(String pattern, Date date){
		return new SimpleDateFormat(pattern).format(date);
	}

	public static Date parse(String pattern, String date) throws ParseException {
		return new SimpleDateFormat(pattern).parse(date);
	}

}
