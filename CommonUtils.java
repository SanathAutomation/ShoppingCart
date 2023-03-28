package com.test.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import cucumber.api.DataTable;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

public class CommonUtils {

	private static Properties props = new Properties();
	public static final String PROPERTIES_FILE = "Config.properties";
	private static Properties property = new Properties();
	static StringBuilder sb = new StringBuilder();
	public static String NUMERICS = "12345678987654321";
	private static final String ALPHA_NUMERIC_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	static {
		try {

			props.load(CommonUtils.class.getClassLoader().getResourceAsStream("Config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String get(String name) {
		return property.getProperty(name);
	}
	
	public static String returnDate(int a) {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		c.add(Calendar.DAY_OF_MONTH, -a);
		now = c.getTime();
		String pattern = "dd-MM-yy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(now);
		return date;
	}
	public static String getDriver() throws URISyntaxException {

		URI uri = CommonUtils.class.getClassLoader().getResource("chromedriver_win32\\chromedriver.exe").toURI();
		// System.out.println(uri);
		String path = Paths.get(uri).toString();
		return path;

	}

	public static String read(String name) {
		return props.getProperty(name);
	}

	public static void main(String args[]) throws ParseException {

		try {
			PhoneNumberGenerator();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void ConsoleLog(Object... arg) {
		String nameofCurrMethod = (new Exception()).getStackTrace()[1].getMethodName();
		ObjectMapper Obj = new ObjectMapper();
		String jsonStr = "";
		int i = 0;
		for (Object a : arg) {
			if (i == 0) {
				try {
					jsonStr = Obj.writeValueAsString(a);
					System.out.println(jsonStr);
					Obj.readTree(a.toString());
				} catch (Exception e) {
					db_logs(a.toString());
					i++;
				}
			} else {
				System.out.println("***********" + a + "***********");
			}
			i++;
		}
		System.out.println("***********" + nameofCurrMethod + "***********");
	}

	public static String GetFile() {
		File directory = new File(read("XLS_DATA"));
		if (directory.exists())
			return read("XLS_DATA");
		else
			return CommonUtils.class.getClassLoader().getResource(read("XLS_DATA")).getFile();

	}

	public static String getTestDataFileForES() {
		return CommonUtils.class.getClassLoader().getResource("ES_Data/ESData.xlsx").getFile();

	}

	public static void db_logs(String log) {
		if ((log.toString().toUpperCase().contains("Verify".toUpperCase()))
				|| (log.toString().toUpperCase().contains("Select".toUpperCase()))
				|| (log.toString().toUpperCase().contains("Query".toUpperCase()))
				|| (log.toString().toUpperCase().contains("Values".toUpperCase()))
				|| (log.toString().toUpperCase().contains("Validate".toUpperCase()))
				|| (log.toString().toUpperCase().contains("Get".toUpperCase()))
				|| (log.toString().toUpperCase().contains("Intergration Message".toUpperCase()))
				|| (log.toString().toUpperCase().contains("Intergration".toUpperCase()))
				|| (log.toString().toUpperCase().contains("Config Used".toUpperCase()))
				|| (log.toString().toUpperCase().contains("UPDATE".toUpperCase()))
				|| (log.toString().toUpperCase().contains("TRUNCATE".toUpperCase()))
				|| (log.toString().toUpperCase().contains("ResultSet".toUpperCase()))
				|| (log.toString().toUpperCase().contains("Notification".toUpperCase()))
				|| (log.toString().toUpperCase().contains("Status".toUpperCase()))
				|| (log.toString().toUpperCase().contains("Info".toUpperCase()))
				|| (log.toString().toUpperCase().contains("UNIX command".toUpperCase()))) {
			sb.append(log);
			sb.append("\n");
			try {
				logger(log);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ((log.toString().toUpperCase().contains("END OF Method".toUpperCase())) && sb.length() != 0) {
			Serenity.recordReportData().withTitle("Verification data").andContents(sb.toString());
			sb.setLength(0);
		}
	}

	private static void logger(String info) throws IOException {
		File file = new File("logs/log.txt");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		PrintWriter output;
		output = new PrintWriter(new FileWriter(
				file,
				true)); 
		String message = dtf.format(now)+" INFO  : "+info;
		output.printf("%s\r\n", message);
		output.close();
	}
	
	public static long getTimeDifference(List<String> appt_time) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		Date date1 = format.parse(appt_time.get(0));
		Date date2 = format.parse(appt_time.get(1));
		long difference = date2.getTime() - date1.getTime();
		difference = difference / 1000;
		difference = difference / 60;
		return difference;
	}



	public static String getApptTime(int day) {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		c.add(Calendar.MINUTE, 10);
		c.add(Calendar.DATE, day);
		now = c.getTime();
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(now);
		return date;
	}

	public static String currentDate(int a) {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		c.add(5, a);
		now = c.getTime();
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(now);
		return date;
	}

	public static String currentDate() {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		now = c.getTime();
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(now);
		return date;
	}

	public static String yyMMddDate() {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		c.add(5, 3);
		now = c.getTime();
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHH");
		String d = f.format(now);
		return d;
	}

	public static String yyMMddDateSpecificTime(int timetoadd) {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		c.add(Calendar.DAY_OF_MONTH, timetoadd);
		now = c.getTime();
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHH");
		String d = f.format(now);
		return d;
	}

	public static String yyMMddDateSpecificDate(int timetoadd) {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		c.add(Calendar.DAY_OF_MONTH, timetoadd);
		now = c.getTime();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String d = f.format(now);
		return d;
	}
	
	public static String calendarSpecificFormat(int timetoadd) {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		c.add(Calendar.DAY_OF_MONTH, timetoadd);
		now = c.getTime();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String d = f.format(now);
		LocalDate currentDate = LocalDate.parse(d);
		int day = currentDate.getDayOfMonth();
		DayOfWeek week = currentDate.getDayOfWeek();
		Month month = currentDate.getMonth();
		String d1=CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, week.toString()+", ");
		String d2=CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, month.toString()+", "+day+", ");
		return d1+d2;
	}


	public static String yyMMddDateSpecificTimeDiffFomat(int timetoadd) {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		c.add(Calendar.DAY_OF_MONTH, timetoadd);
		now = c.getTime();
		SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy");
		String d = f.format(now);
		return d;
	}

	public static int yyMMddDateSpecificTimeDiffFomatDay(int timetoadd) {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		c.add(Calendar.DAY_OF_MONTH, timetoadd);
		now = c.getTime();
		return c.get(Calendar.DAY_OF_MONTH);

	}

	public static int WeekNoOfYear(String input) throws ParseException {
		String format = "MM/dd/yyyy";
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = df.parse(input);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	public static int MonthNoOfYear(String input) throws ParseException {
		String format = "MM/dd/yyyy";
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = df.parse(input);

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.MONTH);
	}

	public static String getDay(int timetoadd) {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		c.add(Calendar.DAY_OF_MONTH, 7+timetoadd);
		now = c.getTime();
		int value = c.get(Calendar.DAY_OF_WEEK);
		switch (value) {
		case 1:
			return "sun";
		case 2:
			return "mon";
		case 3:
			return "tue";
		case 4:
			return "wed";
		case 5:
			return "thu";
		case 6:
			return "fri";
		case 7:
			return "sat";
		}
		return "null";

	}

	public static int yyMMddDateSpecificTimeDiffFomatDayofWeek(int timetoadd) {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		c.add(Calendar.DAY_OF_MONTH, timetoadd);
		now = c.getTime();
		return c.get(Calendar.DAY_OF_WEEK);

	}

	public static int yyMMddDateSpecificTimeDiffFomatWeekNo(int timetoadd) {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		c.add(Calendar.DAY_OF_MONTH, timetoadd);
		now = c.getTime();
		return c.get(Calendar.WEEK_OF_MONTH);

	}

	public static int yyMMddDateSpecificTimeDiffFomatMonth(int timetoadd) {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		c.add(Calendar.DAY_OF_MONTH, timetoadd);
		now = c.getTime();
		return c.get(Calendar.MONTH);
	}

	public static String yyMMddDateReschedule() {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		c.add(5, 2);
		now = c.getTime();
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHH");
		String d = f.format(now);
		return d;
	}

	public static String convert1(String dateString) throws ParseException {
		DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Date date = sdf.parse(dateString);
		String Date1 = (new SimpleDateFormat("yyyy-MM-dd HH:mm aa")).format(date);
		return Date1;
	}

	public static String convert1(String dateString, String time) throws ParseException {
		time = time.replaceAll("\"", "");
		int L = time.length();
		if (L == 6) {
			time = time.substring(5, 6).toUpperCase();
		} else if (L == 7) {
			time = time.substring(6, 7).toUpperCase();
		}
		DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		Date date = sdf.parse(dateString);
		String Date1 = (new SimpleDateFormat("yyyy-MM-dd HH:mm aa")).format(date);
		String Date2 = Date1.substring(17, 18).toUpperCase();
		if (time.equalsIgnoreCase(Date2)) {
			System.out.println(Date1);
		} else {
			String Date3 = Date1.substring(0, 16);
			Date1 = Date3 + " " + time + "M";
			System.out.println("-------------------" + Date1);
		}
		return Date1;
	}

	public static List<String> convert(String dateString) throws ParseException {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(dateString);
		String Date1 = (new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm")).format(date);
		String[] Date3 = StringUtils.split(Date1, ",");
		List<String> DateTime = new ArrayList<>();
		DateTime.add(Date3[0]);
		DateTime.add(Date3[1].trim().substring(0, 3));
		DateTime.add(Date3[1].trim().substring(4, 6));
		DateTime.add(Date3[2].trim().substring(0, 4));
		DateTime.add(Date3[2].trim().substring(5, 10));
		return DateTime;
	}

	public static String getCurrentYearMonthConcatenated() {
		String year = Integer.toString(getCurrentYear());
		String month = getCurrentMonth();
		return year + month;
	}

	public static int getCurrentYear() {
		int year = Calendar.getInstance().get(1);
		return year;
	}

	public static String getCurrentMonth() {
		String month = Integer.toString(Calendar.getInstance().get(2) + 1);
		if (Integer.parseInt(month) <= 9)
			month = "0" + month;
		return month;
	}

	public static String Time12To24Format(String input) throws ParseException {
		DateFormat df = new SimpleDateFormat("hh:mm aa");
		DateFormat outputformat = new SimpleDateFormat("HH:mm aa");
		Date date = null;
		String output = null;
		date = df.parse(input);
		output = outputformat.format(date);
		return output;
	}

	public static String getRandomNumeric(int len) {
		StringBuffer sb = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
			int ndx = (int) (Math.random() * NUMERICS.length());
			sb.append(NUMERICS.charAt(ndx));
		}
		return sb.toString();
	}


	public static String getMonthName(String monthName) {
		monthName = monthName.toUpperCase();
		switch (monthName) {
		case "JAN":
			monthName = "January";
			return monthName;
		case "FEB":
			monthName = "February";
			return monthName;
		case "MAR":
			monthName = "March";
			return monthName;
		case "APR":
			monthName = "April";
			return monthName;
		case "MAY":
			monthName = "May";
			return monthName;
		case "JUN":
			monthName = "June";
			return monthName;
		case "JUL":
			monthName = "July";
			return monthName;
		case "AUG":
			monthName = "August";
			return monthName;
		case "SEP":
			monthName = "September";
			return monthName;
		case "OCT":
			monthName = "October";
			return monthName;
		case "NOV":
			monthName = "November";
			return monthName;
		case "DEC":
			monthName = "December";
			return monthName;
		}
		monthName = "Invalid month";
		return monthName;
	}



	public static long epochTime(String strDate) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm zzz");
		Date date = df.parse(strDate);
		long epoch = date.getTime();
		// convert(strDate);
		return epoch;
	}

	public static long FutureDateEpoch(int a) throws ParseException {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		c.add(5, a);
		now = c.getTime();
		String pattern = "MMM dd yyyy HH:mm zzz";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(now);
		long epoch = epochTime(date);
		System.out.println(epoch);
		return epoch;
	}

	public static String dateconverter(String dateString) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
//		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = dateFormat.parse(dateString);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		String dateStr = formatter.format(date);
		System.out.println(dateStr);
		return dateStr;
	}

	public static void ResponseVerification_logs(String log) {
		if ((log.toString().toUpperCase().contains("message".toUpperCase()))
				|| (log.toString().toUpperCase().contains("details".toUpperCase()))
				|| (log.toString().toUpperCase().contains("status".toUpperCase()))) {
			sb.append(log);
			sb.append("\n");
		} else if ((log.toString().toUpperCase().contains("END OF Method".toUpperCase())) && sb.length() != 0) {
			Serenity.recordReportData().withTitle("API Response Verification").andContents(sb.toString());
			sb.setLength(0);
		}
	}

	public static void ConsoleLog1(Object... arg) {
//		String nameofCurrMethod = (new Exception()).getStackTrace()[1].getMethodName();
//		ObjectMapper Obj = new ObjectMapper();
//		String jsonStr = "";
//		int i = 0;
//		for (Object a : arg) {
//			if (i == 0) {
//				try {
//					jsonStr = Obj.writeValueAsString(a);
//					System.out.println(jsonStr);
//					Obj.readTree(a.toString());
//				} catch (Exception e) {
//					ResponseVerification_logs(a.toString());
//					i++;
//				} 
//			} else {
//				System.out.println("***********" + a + "***********");
//			} 
//			i++;
//		} 
		// System.out.println("***********" + nameofCurrMethod + "***********");
	}

	public static String GetTeleHealthFile() {
//		File directory = new File(read("teleHealth_Data"));
//		if (directory.exists())
//			return read("teleHealth_Data");
//		else
		return CommonUtils.class.getClassLoader().getResource("TeleHealthData/teleHealthData.xlsx").getFile();

	}

	public static String GetSessionLimitFile() {

		return CommonUtils.class.getClassLoader().getResource("SessionLimitData/SessionLimitData.xlsx").getFile();

	}

	public static String getRandomAlphaNumeric(int len) {
		StringBuffer sb = new StringBuffer(len);
		for (int j = 0; j < len; j++) {
			for (int i = 0; i < len; i++) {
				int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
				sb.append(ALPHA_NUMERIC_STRING.charAt(character));
			}
		}
		return sb.toString();
	}

	public static String PhoneNumberGenerator() {
		int num1, num2, num3; // 3 numbers in area code
		int set2, set3; // sequence 2 and 3 of the phone number
		String number;

		Random generator = new Random();

		// Area code number; Will not print 8 or 9
		num1 = generator.nextInt(7) + 1; // add 1 so there is no 0 to begin
		num2 = generator.nextInt(8); // randomize to 8 becuase 0 counts as a number in the generator
		num3 = generator.nextInt(8);

		// Sequence two of phone number
		// the plus 100 is so there will always be a 3 digit number
		// randomize to 643 because 0 starts the first placement so if i randomized up
		// to 642 it would only go up yo 641 plus 100
		// and i used 643 so when it adds 100 it will not succeed 742
		set2 = generator.nextInt(643) + 100;

		// Sequence 3 of numebr
		// add 1000 so there will always be 4 numbers
		// 8999 so it wont succed 9999 when the 1000 is added
		set3 = generator.nextInt(8999) + 1000;

		number = ("" + num1 + "" + num2 + "" + num3 + "" + "" + set2 + "" + set3);
		return number;

	}

	public static String readJson(String jsonName) {
		String jsonString = null;
		try {
			InputStream file = new FileInputStream(
					"src/test/resources/configFiles/jsonFiles/RequestJson/" + jsonName + ".json");
			jsonString = IOUtils.toString(file, "UTF-8");
		} catch (Exception e) {
			assertValues("Error while reading json file: " + jsonName + " " + e.getMessage(), true);
		}
		return jsonString;
	}

	public static String updateRequestBody(DataTable table, String jsonBody) {
		for (int i = 0; i < table.raw().size(); i++) {
			switch (table.raw().get(i).get(0).toLowerCase()) {
			case "key to be updated": {
				jsonBody = keyToBeUpdatedInJson(jsonBody, table.raw().get(i).get(1), table.raw().get(i).get(2));
				break;
			}
			case "key to be added": {
				jsonBody = keyToBeAddedInJson(jsonBody, table.raw().get(i).get(1), table.raw().get(i).get(2));
				break;
			}
			case "key to be removed": {
				jsonBody = keyToBeRemovedInJson(jsonBody, table.raw().get(i).get(1));
				break;
			}
			}
		}
		return jsonBody;
	}

	public static String keyToBeRemovedInJson(String json, String jsonPath) {
		DocumentContext updateJson = null;
		try {
			Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
			updateJson = JsonPath.parse(document)
					.delete(PropertyHolder.booleanProperty(jsonPath) ? PropertyHolder.getProperty(jsonPath) : jsonPath);
		} catch (Exception e) {
			assertValues("Error while removing key: " + jsonPath + ", " + e.getMessage(), false);
		}
		return updateJson.jsonString();
	}

	public static String keyToBeAddedInJson(String json, String jsonPath, String keyValue) {
		DocumentContext updateJson = null;
		try {

			Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
			Object addObject = Configuration.defaultConfiguration().jsonProvider()
					.parse(checkIfGivenStringIsSomeKeyword(keyValue.split("-")[1]));
			updateJson = JsonPath.parse(document).put(jsonPath, keyValue.split("-")[0], addObject);
		} catch (Exception e) {
			assertValues("Error while adding keyValue: " + keyValue + ", " + e.getMessage(), false);
		}
		return updateJson.jsonString();
	}

	public static String keyToBeUpdatedInJson(String json, String jsonPath, String valueToBeUpdated) {
		DocumentContext updateJson = null;
		boolean bool = false;
		try {
			Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
			bool = valueToBeUpdated.contains("-") && !valueToBeUpdated.contains("Date");
			valueToBeUpdated = checkIfGivenStringIsSomeKeyword(
					bool ? valueToBeUpdated.split("-")[1] : valueToBeUpdated);
			jsonPath = PropertyHolder.booleanProperty(jsonPath) ? PropertyHolder.getProperty(jsonPath) : jsonPath;
			Object addObject = Configuration.defaultConfiguration().jsonProvider().parse(valueToBeUpdated);
			updateJson = JsonPath.parse(document).set(jsonPath, bool ? addObject : String.valueOf(addObject));
		} catch (Exception e) {
			assertValues("Error while updating key: " + jsonPath + ", " + e.getMessage(), false);
		}
		return updateJson.jsonString();
	}

	public static String getKeyValueFromJsonUsingJsonPath(String json, String jsonPath) {
		String updateJson = null;
		try {
			Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
			updateJson = JsonPath.parse(document).read(
					PropertyHolder.getProperty(jsonPath) == null ? jsonPath : PropertyHolder.getProperty(jsonPath))
					.toString();
		} catch (Exception e) {
			assertValues("Error while fetching key: " + jsonPath + ", " + e.getMessage(), false);
		}
		return updateJson;
	}

	public static String checkIfGivenStringIsSomeKeyword(String valueToBeUpdated) {
		String updatedValue = "";
		if (valueToBeUpdated.equalsIgnoreCase("RandomName")) {
			updatedValue = getRandomString(10);
		} else if (valueToBeUpdated.equalsIgnoreCase("RandomPatientId")) {
			updatedValue = getRandomNumeric(7);
		} else if (valueToBeUpdated.equalsIgnoreCase("RandomMobNum")) {
			updatedValue = randomUsPhoneNumberGenerator();
			PropertyHolder.setProperty("PREV_NUM", updatedValue);
		} else if (valueToBeUpdated.equalsIgnoreCase("RandomDob")) {
			updatedValue = getRandomNumeric(10);
		} else if (valueToBeUpdated.equalsIgnoreCase("RandomEmail")) {
			updatedValue = getRandomString(10).concat(".auto@docasap.com");
		} else if (valueToBeUpdated.equalsIgnoreCase("RandomGender")) {
			updatedValue = getRandomGender();
		} else if (valueToBeUpdated.equalsIgnoreCase("RandomString")) {
			updatedValue = getRandomString(8);
		} else if (valueToBeUpdated.equalsIgnoreCase("RandomNumber")) {
			updatedValue = getRandomNumeric(10);
		} else if (valueToBeUpdated.equalsIgnoreCase("NextHourTimeInMilliSecond")) {
			updatedValue = String.valueOf(System.currentTimeMillis() + 3600000);
		} else if (valueToBeUpdated.equalsIgnoreCase("CampaignName")) {
			updatedValue = "Automation Campaign - " + PropertyHolder.getProperty("FILE_NAME");
		} else if (valueToBeUpdated.equalsIgnoreCase("CampaignCode")) {
			updatedValue = "AutomationCampaign_" + PropertyHolder.getProperty("FILE_NAME");
		} else if (valueToBeUpdated.contains("Date")) {
			updatedValue = getDateInYYYYMMDD(valueToBeUpdated);
		} else {
			updatedValue = PropertyHolder.booleanProperty(valueToBeUpdated)
					? PropertyHolder.getProperty(valueToBeUpdated)
					: valueToBeUpdated;
		}
		return updatedValue;
	}

	public static String getDateInYYYYMMDD(String valueToBeUpdated) {
		int noOfDays = 0;
		if (valueToBeUpdated.contains("-")) {
			return getDateTime(valueToBeUpdated.substring(valueToBeUpdated.indexOf("-") + 1),
					valueToBeUpdated.split("-")[0]);
		} else {
			if (!valueToBeUpdated.equals("CurrentDate")) {
				valueToBeUpdated = valueToBeUpdated.replaceAll("[^\\d]", "").trim();
				noOfDays = Integer.parseInt(valueToBeUpdated);
			}
			Date now = new Date();
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
			Calendar c = Calendar.getInstance();
			c.setTime(now);
			c.add(Calendar.DATE, noOfDays);
			// to check if desired date is on Sat/Sunday then move it to monday
			c.add(Calendar.DATE, c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ? 2
					: c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ? 1 : 0);

			return f.format(c.getTime());
		}
	}

	public static String randomUsPhoneNumberGenerator() {
		StringBuffer sb = new StringBuffer(10);
		String code = "301";
		sb.append(code);
		for (int i = 0; i < 3; i++) {
			sb.append(String.valueOf(new Random().nextInt(7) + 2));
		}
		for (int i = 0; i < 4; i++) {
			int ndx = (int) (Math.random() * NUMERICS.length());
			sb.append(NUMERICS.charAt(ndx));
		}
		return sb.toString();
	}

	public static String getRandomGender() {
		String[] array = { "M", "F", "U" };
		int rnd = new Random().nextInt(array.length);
		return array[rnd];
	}

	public static void putVariablesInMap(Class clz) throws Exception {
		Field[] fields = clz.getFields();
		for (Field field : fields) {
			PropertyHolder.setProperty(field.getName(), (String) field.get(field));
		}
	}

	public static Map<String, String> setValuesInMap(Map<String, String> map) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String updatedValue = checkIfGivenStringIsSomeKeyword(entry.getValue());
			map.put(entry.getKey(), updatedValue);
		}
		return map;
	}

	public static Map<String, String> getMapFromDataTableUsingKey(DataTable table, String key) {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < table.raw().size(); i++) {
			if (table.raw().get(i).get(0).equalsIgnoreCase(key)) {
				map.put(table.raw().get(i).get(1), checkIfGivenStringIsSomeKeyword(table.raw().get(i).get(2)));
			}
		}
		return map;
	}

	public static String getEncodingStatus(DataTable table) {
		for (int i = 0; i < table.raw().size(); i++) {
			if (table.raw().get(i).get(0).equalsIgnoreCase("urlEncoding")) {
				return table.raw().get(i).get(1);
			}
		}
		return null;
	}



	public static String getRandomString(int length) {
		String randomString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder randString = new StringBuilder();
		Random rnd = new Random();
		while (randString.length() < length) { // length of the random string.
			int index = (int) (rnd.nextFloat() * randomString.length());
			randString.append(randomString.charAt(index));
		}

		return randString.toString();
	}

	public static void assertValues(String message, boolean bool) {
		Assert.assertTrue(message, bool);
	}

	public static String getBackDayName(String dayName) {
//		dayName = dayName.toUpperCase();
		switch (dayName) {
		case "Monday":
			dayName = "Tuesday";
			return dayName;
		case "Tuesday":
			dayName = "Wednessday";
			return dayName;
		case "Wednessday":
			dayName = "Thursday";
			return dayName;
		case "Thursday":
			dayName = "Friday";
			return dayName;
		case "Friday":
			dayName = "Saturday";
			return dayName;
		case "Saturday":
			dayName = "Sunday";
			return dayName;
		case "Sunday":
			dayName = "Monday";
			return dayName;
		}
		dayName = "Invalid day";
		return dayName;
	}

	public static String getBackDate(String date) {
		int date1 = Integer.parseInt(date);
		date1 = (date1 + 1);
		date = Integer.toString(date1);
		return date;
	}

	public static List<String[]> readCsvFile(String csvFileName, char delimiter) {
		CSVReader reader;
		List<String[]> item = new ArrayList<String[]>();
		try {
			reader = new CSVReader(new FileReader("src/test/resources/configFiles/csvFiles/" + csvFileName + ".csv"),
					delimiter, CSVWriter.NO_QUOTE_CHARACTER);
			item = reader.readAll();
		} catch (Throwable e) {
			assertValues("Error while reading csv file: " + csvFileName + ", " + e.getMessage(), false);
		}
		return item;

	}

	public static int getIndexOfOutreachCsvHeader(String key) {
		ArrayList<String> headers = new ArrayList<>(Arrays.asList("firstName", "lastName", "uniqueIdentifier",
				"street1", "street2", "city", "state", "zip", "emailId", "phoneNumber", "otherPhoneNumber", "pcpNPI",
				"pcpFirstName", "pcpLastName", "specialistNPI", "specialistFirstName", "specialistLastName",
				"preferredLang", "isEmailNotificationOn", "isTextNotificationOn", "isVoiceNotificationOn"));
		return headers.indexOf(key);
	}

	public static void createCsvFile(List<String[]> csvData, String csvFileName, char delimiter) {
		try {
			CSVWriter writer = new CSVWriter(new FileWriter("target/" + csvFileName + ".csv"), delimiter,
					CSVWriter.NO_QUOTE_CHARACTER);
			writer.writeAll(csvData);
			writer.close();
		} catch (IOException e) {
			assertValues("Error while creating csv file: " + csvFileName + ", " + e.getMessage(), false);
		}

	}

	public static String getCurrentDateTime(String format) {
		Date now = new Date();
		SimpleDateFormat f = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		return f.format(c.getTime());
	}

	public static String getDateTime(String format, String updateTime) {
		Date now = new Date();
		SimpleDateFormat f = new SimpleDateFormat(format);
		f.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		if (!updateTime.equalsIgnoreCase("currentDate")) {
			int number = Integer.parseInt(updateTime.replaceAll("[^\\d]", "").trim());
			if (updateTime.startsWith("Prev")) {
				number = -number;
			}
			String keyToBeUpdate = updateTime.split("\\d{1,2}")[1];
			keyToBeUpdate = keyToBeUpdate.replace("Date", "");
			switch (keyToBeUpdate.toLowerCase()) {
			case "second":
				c.add(Calendar.SECOND, number);
				break;
			case "minute":
				c.add(Calendar.MINUTE, number);
				break;
			case "hour":
				c.add(Calendar.HOUR, number);
				break;
			case "day":
				c.add(Calendar.DATE, number);
				break;

			default:
				assertValues("Invalid keyword: " + keyToBeUpdate + ", should be like second/minute/hour/day", false);
			}
		}
		return f.format(c.getTime());
	}

	public static void addDataInSerenityReportFromFile(String title, String targetPath) {
		try {
			Serenity.recordReportData().withTitle(title).fromFile(Paths.get("target/" + targetPath));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void addDataInSerenityReportFromString(String title, String data) {
		Serenity.recordReportData().withTitle(title).andContents(data);

	}

	public static String getDateInTimezone(String dateFormat, String timezone) {
		Date today = new Date();
		DateFormat df = new SimpleDateFormat(dateFormat);
		df.setTimeZone(TimeZone.getTimeZone(timezone));
		String date = df.format(today);
		return date;
	}

	public static String currentDateMMDDYYYY() {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		now = c.getTime();
		String pattern = "MMddyyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(now);
		System.out.println(date);
		return date;
	}

	public static String currentDateMMDDYY() {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		now = c.getTime();
		String pattern = "MM/dd/yy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(now);
		return date;
	}
	
	public static String currentDateMMDDYYHHmmss() {
		Date now = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(now);
		now = c.getTime();
		String pattern = "YYYY-MM-dd 00:00:00";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(now);
		System.out.println(date);
		return date;
	}
}