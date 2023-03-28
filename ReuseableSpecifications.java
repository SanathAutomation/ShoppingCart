package com.test.utils;

import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.RedirectConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.Filter;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.config.EncoderConfig.encoderConfig;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matchers;
import org.openqa.selenium.Cookie;

import cucumber.api.DataTable;

public class ReuseableSpecifications {
  public static RequestSpecBuilder rspec;
  
  public static RequestSpecification requestSpecification;
  
  public static ResponseSpecBuilder respec;
  
  public static ResponseSpecification responseSpecification;
  
  public static RequestSpecification getGenericRequestSpec() {
    rspec = new RequestSpecBuilder();
    rspec.setContentType(ContentType.JSON);
    rspec.setAccept("*/*");
    rspec.addFilter((Filter)ThreadSpecificUtils.cookieFilter.get());
    rspec.setUrlEncodingEnabled(false);
    requestSpecification = rspec.build();
    return requestSpecification;
  }
  
  public static RequestSpecification getGenericRequestJenkins() {	
		rspec = new RequestSpecBuilder();
	    PreemptiveBasicAuthScheme auth = new PreemptiveBasicAuthScheme();
	    auth.setUserName("imran");
	    auth.setPassword("imran@123");
	    rspec.setAuth((AuthenticationScheme)auth);
	    rspec.setBaseUri("http://jenkins.dainternal.com:8080");
		rspec.setContentType("application/json");
	    requestSpecification = rspec.build();
	    return requestSpecification;
	  }
  public static RequestSpecification GenericRequestSpec() {
	    rspec = new RequestSpecBuilder();
	    rspec.setContentType(ContentType.JSON);
	    rspec.setAccept("*/*");
	    rspec.setUrlEncodingEnabled(false);
	    requestSpecification = rspec.build();
	    return requestSpecification;
	  }
	  
	  

  public static RequestSpecification getGenericRequestSpecSessionLimit() {	
		rspec = new RequestSpecBuilder();
	    rspec.setContentType(ContentType.JSON);
	    rspec.setBaseUri("https://api-staging-int.docasap.com");
	    rspec.addCookie("laravel_session","I9FDSh0QKz3rvomkJ7nzxrL40vzp2lefNOmzg82R");
	    rspec.setRelaxedHTTPSValidation();
	    rspec.setUrlEncodingEnabled(false);
	    rspec.setAccept("*/*");
	    requestSpecification = rspec.build();
	    return requestSpecification;
	  }
  
  public static RequestSpecification getGenericRequestSpecESKibana() {	
		rspec = new RequestSpecBuilder();
		rspec.setContentType("application/json");
		rspec.addHeader("kbn-xsrf", "true");
	    rspec.setBaseUri(CommonUtils.read("ES_Execution_Kibana_url"));
	    rspec.setUrlEncodingEnabled(false);
	    requestSpecification = rspec.build();
	    return requestSpecification;
	  }
  
  public static String kibanaPath(String org_id) {
	    if(!CommonUtils.read("API_Execution_Env").contains("staging"))
		return "api/console/proxy?path=/provider_search_index_" + org_id + "/_search&method=GET";
	    else
		return "/provider_search_index_" + org_id + "/_search";
	}
  
  public static String kibanaPath_mapping(String org_id) {
	    if(!CommonUtils.read("API_Execution_Env").contains("staging"))
		return "api/console/proxy?path=/provider_search_index_" + org_id + "/_mapping&method=GET";
	    else
		return "/provider_search_index_" + org_id + "/_mapping";
	}
  
  public static String kibanaPathDelete(String org_id) {
	    if(!CommonUtils.read("API_Execution_Env").contains("staging"))
		return "api/console/proxy?path=/provider_search_index_" + org_id + "/_search&method=GET";
	    else
		return "/provider_search_index_" + org_id;
	}
	
  public static String kibanaPathReason(String org_id) {
	    if(CommonUtils.read("API_Execution_Env").contains("staging"))
		return "api/console/proxy?path=/org_reasons_index_"+org_id+"/_search&method=GET";
	    else
		return "/org_reasons_index_"+org_id+"/_search";
	}
  
  public static String kibanaPathGeneric(String org_id) {
	    if(!CommonUtils.read("API_Execution_Env").contains("staging"))
		return "api/console/proxy?path=provider_search_index_"+org_id+"/_search&method=GET";
	    else
		return "api/console/proxy?path=provider_search_index_"+org_id+"/_search&method=GET";
	}
  
  public static String kibanaPathGeneric() {
	    if(!CommonUtils.read("API_Execution_Env").contains("staging"))
		return "api/console/proxy?path=provider_search_index_*/_search&method=GET";
	    else
		return "/org_reasons_index_*/_search";
	}
  
  public static String kibanaPathUpdate(String org_id,String id) {
	    if(CommonUtils.read("API_Execution_Env").contains("staging"))
		return "api/console/proxy?path=/org_reasons_index_"+org_id+"/_update/"+id+"/&method=POST";
	    else
		return "/org_reasons_index_"+org_id+"/_update"+id;
		//https://esmonitor-qa.docasap.com/api/console/proxy?path=provider_search_index_74091%2F_doc%2F425284-74091%2F_update&method=POST

	}
  
  public static String kibanaUpdate(String org_id,String id) {
	    if(CommonUtils.read("API_Execution_Env").contains("staging"))
		return "api/console/proxy?path=provider_search_index_"+org_id+"/_doc/"+id+"-"+org_id+"/_update/&method=POST";
	    else
		return "api/console/proxy?path=provider_search_index_"+org_id+"/_doc/"+id+"-"+org_id+"/_update/&method=POST";

	}
  
  public static String kibanaPathIns() {
	    if(!CommonUtils.read("API_Execution_Env").contains("staging"))
		return "api/console/proxy?path=/insurance_index/_search&method=GET";
	    else
		return "/insurance_index/_search";
	}
  
  public static RequestSpecification getGenericRequestSpecESKibanaFeatureBranch() {	
		rspec = new RequestSpecBuilder();
		rspec.addHeader("kbn-xsrf", "true");
		rspec.setContentType("application/json");
	    rspec.setUrlEncodingEnabled(false);
	    rspec.setBaseUri(CommonUtils.read("ES_Execution_Kibana_url"));
	    requestSpecification = rspec.build();
	    return requestSpecification;
	  }
  
  public static RequestSpecification getGenericRequestSpecQA() {	
		rspec = new RequestSpecBuilder();
		rspec.addHeader("kbn-xsrf", "true");
		rspec.setContentType("application/json");
	    rspec.setUrlEncodingEnabled(false);
	    rspec.setBaseUri("https://esmonitor-staging.docasap.com");
	    requestSpecification = rspec.build();
	    return requestSpecification;
	  }
  
  public static RequestSpecification getGenericRequestSpecEnhancementQA() {	
		rspec = new RequestSpecBuilder();
	//	rspec.addHeader("kbn-xsrf", "true");
		rspec.setContentType("application/json");
	    rspec.setUrlEncodingEnabled(false);
	    rspec.setBaseUri("https://api-staging.docasap.com");
	    requestSpecification = rspec.build();
	    return requestSpecification;
	  }
  
  public static RequestSpecification getGenericRequestBooking() {	
		rspec = new RequestSpecBuilder();
	    PreemptiveBasicAuthScheme auth = new PreemptiveBasicAuthScheme();
	    auth.setUserName("daprod_apiuser");
	    auth.setPassword("9JvwHqfXqlmlNA");
	    rspec.setAuth((AuthenticationScheme)auth);
		rspec.setContentType("application/json");
	    requestSpecification = rspec.build();
	    return requestSpecification;
	  }
  
  public static RequestSpecification getGenericiHub() {	
		rspec = new RequestSpecBuilder();
	    PreemptiveBasicAuthScheme auth = new PreemptiveBasicAuthScheme();
	    auth.setUserName("engineadmin");
	    auth.setPassword("engineadmin");
	    rspec.setAuth((AuthenticationScheme)auth);
		rspec.setContentType("application/json");
	    requestSpecification = rspec.build();
	    return requestSpecification;
	  }

  
  public static RequestSpecification getGenericRequestSpecKibanaEnhancementQA() {	
		rspec = new RequestSpecBuilder();
		rspec.addHeader("kbn-xsrf", "true");
		rspec.setContentType("application/json");
	    rspec.setUrlEncodingEnabled(false);
	    rspec.setBaseUri("https://esmonitor-staging.docasap.com");
	    requestSpecification = rspec.build();
	    return requestSpecification;
	  }
  public static RequestSpecification getGenericRequestSpecESLaravel(){
	  rspec = new RequestSpecBuilder();
		rspec.setContentType("application/json");
	    rspec.setUrlEncodingEnabled(false);
		rspec.addHeader("kbn-xsrf", "true");
	    rspec.setBaseUri(CommonUtils.read("ES_Execution_Laravel_url"));
	    requestSpecification = rspec.build();
	    return requestSpecification;
  }
  public static RequestSpecification getGenericRequestSpecESLookup(){
	  rspec = new RequestSpecBuilder();
		rspec.setContentType("application/json");
		rspec.addHeader("kbn-xsrf", "true");
	    rspec.setBaseUri(CommonUtils.read("API_Execution_Env"));
	    requestSpecification = rspec.build();
	    return requestSpecification;
  }
  public static RequestSpecification getGenericRequestSpecESEnv(){
	  rspec = new RequestSpecBuilder();
		rspec.setContentType("application/json");
	    rspec.setBaseUri(CommonUtils.read("API_Execution_Env"));
	    requestSpecification = rspec.build();
	    return requestSpecification;
  }
  
  public static RequestSpecification getGenericRequestSpecSessionLimitCheckRule() {	
		rspec = new RequestSpecBuilder();
	    rspec.setContentType(ContentType.JSON);
	    rspec.setBaseUri(CommonUtils.read("Execution_Env_url"));
	    rspec.addCookie("laravel_session","I9FDSh0QKz3rvomkJ7nzxrL40vzp2lefNOmzg82R");
	    rspec.setRelaxedHTTPSValidation();
	    rspec.setAccept("*/*");
	    rspec.setUrlEncodingEnabled(false);
	    requestSpecification = rspec.build();
	    return requestSpecification;  
	  }
  
  public static RequestSpecification getGenericRequestSpecGet() {
    rspec = new RequestSpecBuilder();
    requestSpecification = rspec.build();
    return requestSpecification;
  }
  
  public static RequestSpecification getGenericRequestSpecNotification(String eventType, int isOasAppt, long non_oas_appt_id) {
    rspec = new RequestSpecBuilder();
    rspec.setContentType(ContentType.JSON);
    rspec.setAccept("*/*");
    rspec.addFilter((Filter)ThreadSpecificUtils.cookieFilter.get());
    rspec.addQueryParameter("eventType", new Object[] { eventType });
    rspec.addQueryParameter("isOasAppt", new Object[] { Integer.valueOf(isOasAppt) });
    rspec.addQueryParameter("apptId", new Object[] { Long.valueOf(non_oas_appt_id) });
    requestSpecification = rspec.build();
    return requestSpecification;
  }
  
  public static RequestSpecification getGenericRequestSpecWithAuth(String email) {
    rspec = new RequestSpecBuilder();
    rspec.setContentType(ContentType.JSON);
    rspec.addFilter((Filter)ThreadSpecificUtils.cookieFilter.get());
    PreemptiveBasicAuthScheme auth = new PreemptiveBasicAuthScheme();
    auth.setUserName(email);
    auth.setPassword("Docasap12#");
    rspec.setAuth((AuthenticationScheme)auth);
    requestSpecification = rspec.build();
    return requestSpecification;
  }
  
  @SuppressWarnings("unchecked")
public static RequestSpecification getGenericRequestSpecWithAuth1(String email, Cookies cookie) {
    rspec = new RequestSpecBuilder();
    rspec.setContentType(ContentType.JSON);
    rspec.addFilter((Filter)ThreadSpecificUtils.cookieFilter.get());
    PreemptiveBasicAuthScheme auth = new PreemptiveBasicAuthScheme();
    auth.setUserName(email);
    auth.setPassword("Docasap12#");
    rspec.setAuth((AuthenticationScheme)auth);
    rspec.addCookies((Map<String, ?>) cookie);
    requestSpecification = rspec.build();
    return requestSpecification;
  }
  
  
  @SuppressWarnings("unchecked")
public static RequestSpecification getGenericRequestSpecWithAuth2(String email, Cookie allDetailedCookies) {
    rspec = new RequestSpecBuilder();
    rspec.setContentType(ContentType.JSON);
    rspec.addFilter((Filter)ThreadSpecificUtils.cookieFilter.get());
    PreemptiveBasicAuthScheme auth = new PreemptiveBasicAuthScheme();
    auth.setUserName(email);
    auth.setPassword("Docasap12#");
    rspec.setAuth((AuthenticationScheme)auth);
    rspec.addCookies((Map<String, ?>) allDetailedCookies);
    requestSpecification = rspec.build();
    return requestSpecification;
  }
  
  
  public static RequestSpecification getGenericRequestSpecWithAuth() {
    rspec = new RequestSpecBuilder();
    rspec.setContentType(ContentType.JSON);
    rspec.setAccept("*/*");
    rspec.addFilter((Filter)ThreadSpecificUtils.cookieFilter.get());
    PreemptiveBasicAuthScheme auth = new PreemptiveBasicAuthScheme();
    auth.setUserName("supersync");
    auth.setPassword("supersync");
    rspec.setAuth((AuthenticationScheme)auth);
    rspec.setBaseUri(CommonUtils.read("Integration_Env_url"));
    requestSpecification = rspec.build();
    return requestSpecification;
  }
  
  public static String gettoken() {
    RestAssured.baseURI = CommonUtils.read("Execution_Env_url");
    Response response = (Response)((RequestSpecification)RestAssured.given().log().all()).filter((Filter)ThreadSpecificUtils.cookieFilter.get()).get("/token", new Object[0]);
    return response.getBody().asString();
  }
  
  public static ResponseSpecification getGenericResponseSpec() {
    respec = new ResponseSpecBuilder();
    rspec.setUrlEncodingEnabled(false);
    respec.expectHeader("Content-Type", "application/json;charset=UTF-8");
    respec.expectHeader("Transfer-Encoding", "chunked");
    respec.expectResponseTime(Matchers.lessThan(Long.valueOf(8L)), TimeUnit.SECONDS);
    responseSpecification = respec.build();
    return responseSpecification;
  }
  
  public static RequestSpecification getGenericRequestSpecWithAuthProv(String email, CookieFilter cookieFilter) {
	    rspec = new RequestSpecBuilder();
	    rspec.setContentType(ContentType.JSON);
	    rspec.addFilter((Filter)ThreadSpecificUtils.cookieFilter.get());
	    PreemptiveBasicAuthScheme auth = new PreemptiveBasicAuthScheme();
	    auth.setUserName(email);
	    auth.setPassword("Docasap12#");
	    rspec.setAuth((AuthenticationScheme)auth);
	    rspec.addFilter(cookieFilter);
	    requestSpecification = rspec.build();
	    return requestSpecification;
	  }


  public static RequestSpecification getGenericRequestSpecTeleHealth() {	
		rspec = new RequestSpecBuilder();
	    rspec.setContentType(ContentType.JSON);
	    rspec.setBaseUri(CommonUtils.read("API_Execution_Env")); 
	    rspec.setRelaxedHTTPSValidation();
	    rspec.setUrlEncodingEnabled(false);
	    rspec.setAccept("*/*");
	    requestSpecification = rspec.build();
	    return requestSpecification;
	  }
  
  public static RequestSpecification getGenericRequestSpecTeleHealth_Http() {	
		rspec = new RequestSpecBuilder();
	    rspec.setContentType(ContentType.JSON);
	    rspec.setUrlEncodingEnabled(false);
	    rspec.setBaseUri("http://"+CommonUtils.read("api_url")); 
	    rspec.setRelaxedHTTPSValidation();
	    rspec.setAccept("*/*");
	    requestSpecification = rspec.build();
	    return requestSpecification;
	  }

public static RequestSpecification getGenericRequestSpecTeleHealth(String api_url) {	
		rspec = new RequestSpecBuilder();
	    rspec.setContentType(ContentType.JSON);
	    rspec.setBaseUri(api_url); 
	    rspec.setRelaxedHTTPSValidation();
	    rspec.setAccept("*/*");
	    rspec.setConfig(RestAssuredConfig.newConfig().redirect(RedirectConfig.redirectConfig().followRedirects(false)));
	    requestSpecification = rspec.build();
	    return requestSpecification;
	  }

public static RequestSpecification getGenericRequestSpecWithAuthProvBookAppointmentApi() {
	  rspec = new RequestSpecBuilder();
	    rspec.setContentType(ContentType.JSON);
	    rspec.setAccept("*/*");
	    PreemptiveBasicAuthScheme auth = new PreemptiveBasicAuthScheme();
	    auth.setUserName("daprod_apiuser");
	    auth.setPassword("9JvwHqfXqlmlNA");
	    rspec.setAuth((AuthenticationScheme)auth);
	    rspec.setUrlEncodingEnabled(false);
	    rspec.setBaseUri(CommonUtils.read("Execution_Env_url"));
	    requestSpecification = rspec.build();
	    return requestSpecification;
	  }

public static RequestSpecification getGenericRequestSpecGet1() {
	    rspec = new RequestSpecBuilder();
	    rspec.setUrlEncodingEnabled(false);
	    rspec.setBaseUri(CommonUtils.read("Execution_Env_url"));
	    requestSpecification = rspec.build();
	    return requestSpecification;
	  }

public static RequestSpecification buildRequest(DataTable table) {
	rspec = new RequestSpecBuilder();
	rspec.addHeaders(CommonUtils.setValuesInMap(
            		CommonUtils.getMapFromDataTableUsingKey(table, "header")));
	rspec.addFormParams(CommonUtils.setValuesInMap(
            		CommonUtils.getMapFromDataTableUsingKey(table, "formParam")));
	rspec.addPathParams(CommonUtils.setValuesInMap(
            		CommonUtils.getMapFromDataTableUsingKey(table, "pathParam")));
	rspec.addQueryParams(CommonUtils.setValuesInMap(
            		CommonUtils.getMapFromDataTableUsingKey(table, "queryParam")));
	rspec.setUrlEncodingEnabled(CommonUtils.getEncodingStatus(table) == null ? false : true);
	rspec.setRelaxedHTTPSValidation();
	rspec.setConfig(RestAssured.config().encoderConfig(encoderConfig()
                    .appendDefaultContentCharsetToContentTypeIfUndefined(
                            false)));
	requestSpecification = rspec.build();
    return requestSpecification;
}
}

