package com.test.utils;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import io.restassured.filter.cookie.CookieFilter;

public class ThreadSpecificUtils {
  public static ThreadLocal<String> email_id = new ThreadLocal<String>();
  
  public static ThreadLocal<CookieFilter> cookieFilter = new ThreadLocal<CookieFilter>();
  public static ThreadLocal<String> RuleName = new ThreadLocal<String>();
  public static ThreadLocal<String> SubRuleName = new ThreadLocal<String>();
  public static ThreadLocal<String> ruleGid = new ThreadLocal<String>();
  public static ThreadLocal<String> ruleGid2 = new ThreadLocal<String>();
  public static ThreadLocal<Integer> ruleId = new ThreadLocal<Integer>();
  public static ThreadLocal<Integer> subRuleId = new ThreadLocal<Integer>();
  public static ThreadLocal<String> subRuleGid = new ThreadLocal<String>();
  public static ThreadLocal<String> subRuleGid2 = new ThreadLocal<String>();
  public static ThreadLocal<Integer> Counter = new ThreadLocal<Integer>();
  public static ThreadLocal<String>userId = new ThreadLocal<String>();
  public static ThreadLocal<JsonNode>s1 = new ThreadLocal<JsonNode>();
  public static ThreadLocal<JsonNode>s2 = new ThreadLocal<JsonNode>();
}
