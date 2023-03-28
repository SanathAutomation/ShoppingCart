package com.test.utils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ReportUtils extends CommonUtils{

	public static void updateReportPortalAttr() {
		try (InputStream input = new FileInputStream("src/main/resources/reportportal.properties")) {
			Properties prop = new Properties();
			prop.load(input);
			String arr[]=prop.getProperty("rp.attributes").split(";");
			String description = prop.getProperty("rp.description");
			String other = prop.getProperty("rp.other");
			String descriptionfinal = description.replace(" ", "%20");
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			Request request = new Request.Builder()
					.url(prop.getProperty("rp.endpoint")
							+ "/api/v1/backend_automation/launch/latest?filter.eq.description=" + descriptionfinal)
					.method("GET", null).addHeader("Authorization", "Bearer " + prop.getProperty("rp.uuid")).build();
			okhttp3.Response response = client.newCall(request).execute();
			String resStr = response.body().string();
			JSONObject json = new JSONObject(resStr);
			System.out.println(json);
			JSONArray Jarray = json.getJSONArray("content");
			int value = 0;
			for (int i = 0; i < Jarray.length(); i++) {
				JSONObject object = Jarray.getJSONObject(i);
				value = object.getInt("id");
			}
			JSONObject attributes1 = new JSONObject();
			attributes1.put("key", arr[0].split(":")[0]);
			attributes1.put("value", arr[0].split(":")[1]);
			JSONObject attributes2 = new JSONObject();
			attributes2.put("key", arr[1].split(":")[0]);
			attributes2.put("value", arr[1].split(":")[1]);
			JSONArray Addr = new JSONArray();
			Addr.put(0, attributes1);
			Addr.put(1, attributes2);

			JSONObject payload = new JSONObject();
			payload.put("description", "["+description+"]"
					+ "(http://jenkins.dainternal.com/job/QA-Integration-Automation-Backend/ws/reports/serenity_"+other+"index.html)");
			payload.put("attributes", Addr);
			System.out.println(payload);
			System.out.println(descriptionfinal);
			System.out.println(value+" : "+prop.getProperty("rp.endpoint") + "/api/v1/backend_automation/launch/" + value + "/update");
			OkHttpClient client1 = new OkHttpClient().newBuilder().followRedirects(false).build();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, payload.toString());
			Request request1 = new Request.Builder()
					.url(prop.getProperty("rp.endpoint") + "/api/v1/backend_automation/launch/" + value + "/update")
					.method("PUT", body).addHeader("accept", "*/*").addHeader("Content-Type", "application/json")
					.addHeader("Authorization", "Bearer " + prop.getProperty("rp.uuid")).build();
			client1.newCall(request1).execute();

		} catch (IOException ex) {
			//ex.printStackTrace();
		}
	}
}
