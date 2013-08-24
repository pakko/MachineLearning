package com.ml.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
/**
 * 分析时间戳
 * 
 */
public class TimeExtractUtil {
 
	private static final String TIME_REGEX = "((:|>|\\s)?20[0-9]{2}(-|/|\\.|\\u5e74)\\d{1,2}(-|/|\\.|\\u6708)\\d{1,2}(\\u65e5)?\\s?\\d{1,2}(:|\\u65f6)\\d{2}((:|\\u5206)\\d{2})?|(:|>|\\s)?[0-9]{2}\\u5e74\\d{1,2}\\u6708\\d{1,2}(\\u65e5)?\\s?\\d{1,2}(:|\\u65f6)\\d{2}((:|\\u5206)\\d{2})?)";
	private static final String TIME_REGEX_1 = "((:|>|\\s)?20[0-9]{2}(-|/|\\.|\\u5e74)\\d{1,2}(-|/|\\.|\\u6708)\\d{1,2}(\\u65e5)?(\\s|&nbsp|<)+|(:|>|\\s)?[0-9]{2}\\u5e74\\d{1,2}\\u6708\\d{1,2}(\\u65e5)?(\\s|&nbsp|<)+)";
	private static Pattern pattern = Pattern.compile(TIME_REGEX);
	private static Pattern pattern1 = Pattern.compile(TIME_REGEX_1);
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final String BBS_URL = "(http://bbs/\\..*|http://www\\.tianya\\.cn/[a-zA-Z]*forum/content/.*)";
 
	/**
	 * @param content
	 * @param url
	 * @return
	 */
	public static Date extractDate(String content, String url) {
 
		String con = content
				.replaceAll("<!--.*?-->", "");
 
		Matcher m = pattern.matcher(con);
		Date now = new Date();
 
		//
		if (url.matches(BBS_URL)) {
 
			String dateStr = null;
 
			Date date = null;
 
			while (m.find()) {
 
				dateStr = m.group();
 
				if (dateStr == null)
					continue;
 
				dateStr = dateStr.trim().replaceAll(">", "");
 
				if (dateStr.startsWith(":")) {
					dateStr = dateStr.replaceFirst(":", "");
				}
 
				dateStr = dateStr.replaceAll("\\.|/|\\u5e74|\\u6708", "-")
						.replaceAll("\\u65e5", " ");
 
				dateStr = dateStr.replaceAll("\\u65f6|\\u5206", ":");
 
				Date tempDate;
 
				try {
					tempDate = sdf.parse(changeString2Date(dateStr));
 
					if (tempDate.after(now)) {
						continue;
					}
 
				} catch (ParseException e) {
					continue;
				}
 
				if (date == null) {
					date = tempDate;
				} else if (tempDate.after(date)) {
					date = tempDate;
				}
			}
 
			if (date != null) {
 
				return date;
			}
 
		} else {
 
			String dateStr = null;
 
			if (m.find()) {
				dateStr = m.group();
			}
 
			if (dateStr != null) {
 
				dateStr = dateStr.trim().replaceAll(">", "");
 
				if (dateStr.startsWith(":")) {
					dateStr = dateStr.replaceFirst(":", "");
				}
 
				dateStr = dateStr.replaceAll("\\.|/|\\u5e74|\\u6708", "-")
						.replaceAll("\\u65e5", " ");
				;
				dateStr = dateStr.replaceAll("\\u65f6|\\u5206", ":");
 
				try {
 
					return (sdf.parse(changeString2Date(dateStr)));
 
				} catch (ParseException e) {
					System.out.println("parsedate error: " + e.getMessage());
				}
			}
		}
 
		m = pattern1.matcher(con);
 
		if (url.matches(BBS_URL)) {
 
			String dateStr = null;
 
			Date date = null;
 
			while (m.find()) {
 
				dateStr = m.group();
 
				if (dateStr == null)
					continue;
 
				dateStr = dateStr.trim().replaceAll(">", "");
 
				if (dateStr.startsWith(":")) {
					dateStr = dateStr.replaceFirst(":", "");
				}
 
				dateStr = dateStr.replaceAll("\\.|/|\\u5e74|\\u6708", "-")
						.replaceAll("\\u65e5", " ");
 
				dateStr = dateStr.replaceAll("(&nbsp|<)", "");
 
				Date tempDate;
 
				try {
					tempDate = sdf.parse(changeString2Date(dateStr));
 
					if (tempDate.after(now)) {
						continue;
					}
 
				} catch (ParseException e) {
					continue;
				}
 
				if (date == null) {
					date = tempDate;
				} else if (tempDate.after(date)) {
					date = tempDate;
				}
			}
 
			if (date != null) {
 
				return date;
			}
 
		} else {
 
			String dateStr = null;
 
			if (m.find()) {
				dateStr = m.group();
			}
 
			if (dateStr != null) {
 
				dateStr = dateStr.trim().replaceAll(">", "");
 
				if (dateStr.startsWith(":")) {
					dateStr = dateStr.replaceFirst(":", "");
				}
 
				dateStr = dateStr.replaceAll("\\.|/|\\u5e74|\\u6708", "-")
						.replaceAll("\\u65e5", " ");
				;
				dateStr = dateStr.replaceAll("(&nbsp|<)", "");
 
				try {
 
					return (sdf.parse(changeString2Date(dateStr)));
 
				} catch (ParseException e) {
					System.out.println("parsedate error: " + e.getMessage());
				}
			}
		}
 
		return null;
	}
 
	private static String changeString2Date(String dateStr) {
 
		//System.out.println("Pre Date: " + dateStr);
 
		String date = "";
 
		String[] tempDateStr = dateStr.trim().replaceAll("\\s+", "@")
				.replaceAll("(-|:)", "@").split("@");
 
		if (tempDateStr.length > 0) {
 
			String year = tempDateStr[0];
 
			if (year.length() == 2) {
				year = "20" + year;
			}
 
			date = date + year + "-";
		}
 
		if (tempDateStr.length > 1) {
			date = date
					+ (tempDateStr[1].length() > 1 ? tempDateStr[1] : "0"
							+ tempDateStr[1]) + "-";
		}
 
		if (tempDateStr.length > 2) {
			date = date
					+ (tempDateStr[2].length() > 1 ? tempDateStr[2] : "0"
							+ tempDateStr[2]) + " ";
		}
 
		if (tempDateStr.length > 3) {
			date = date
					+ (tempDateStr[3].length() > 1 ? tempDateStr[3] : "0"
							+ tempDateStr[3]) + ":";
		} else {
			date = date + "00:";
		}
 
		if (tempDateStr.length > 4) {
			date = date
					+ (tempDateStr[4].length() > 1 ? tempDateStr[4] : "0"
							+ tempDateStr[4]) + ":";
		} else {
			date = date + "00:";
		}
 
		if (tempDateStr.length > 5) {
			date = date
					+ (tempDateStr[5].length() > 1 ? tempDateStr[5] : "0"
							+ tempDateStr[5]);
		} else {
			date = date + "00";
		}
 
		//System.out.println("Date: " + date);
 
		return date;
	}
 
	public static void main(String[] args) {
 
		extractDate(
				" <td align=\"center\">http://www.rednet.cn &nbsp;<!--时间开始-->12/3/2 9:14:36<!--时间结束-->&nbsp;&nbsp;",
				"");
 
	}
}