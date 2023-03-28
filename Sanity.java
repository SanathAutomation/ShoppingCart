package com.test.cucumber.sanity.steps;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.Color;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import com.test.utils.BrowserEventManager;
import com.test.utils.ReuseableSpecifications;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.rest.SerenityRest;

public class Sanity {
	public static String loginIcone = "//a[contains(@id,'login2')]";
	public static String loginInputText = "//input[contains(@id,'loginusername')]";
	public static String loginPassword = "//input[contains(@id,'loginpassword')]";
	public static String loginButton = "//button[contains(.,'Log in')]";
	public static String selectPhone = "//a[contains(.,'Samsung galaxy s6')]";
	public static String addToCart = "//a[contains(.,'Add to cart')]";
	public static String navigateToCart = "//a[contains(.,'Cart')]";
	public static String placeOrderButton = "//button[contains(.,'Place Order')]";
	public static String nameInput = "(//input[contains(@id,'name')])[3]";
	public static String countryInput = "//input[contains(@id,'country')]";
	public static String cityInput = "//input[contains(@id,'city')]";
	public static String cardInput = "//input[contains(@id,'card')]";
	public static String monthInput = "//input[contains(@id,'month')]";
	public static String yearInput = "//input[contains(@id,'year')]";
	public static String purchaseButton = "//button[contains(.,'Purchase')]";
	
	

			//h2//following-sibling::p
			//button OK
	ChromeDriver driver;
	Response response;
	/*
	 * @Before("Setup") public void SetupBrowser(){
	 * 
	 * 
	 * }
	 */
	
	@When("^User hits end point$")
	public void user_hits_end_point() throws Throwable {
	   RestAssured.baseURI="http://universities.hipolabs.com";
	   response=SerenityRest.rest().spec(ReuseableSpecifications.GenericRequestSpec()).given().get("/search?country=South+Africa");   
	   response.then().assertThat().statusCode(200).log();
	}

	@Then("^User should be able to see the province list$")
	public void user_should_be_able_to_see_the_province_list() throws Throwable {
	 // System.out.print(response.asString());
	  List <String> JsonString=JsonPath.parse(response.asString()).read("$..name");
	  for(int i=0;i<JsonString.size();i++) {
		  
		  if(JsonString.get(i).trim().contains("University of Witwatersrand")) {
			  System.out.println(JsonString.get(i));
		  }else {
			//  System.out.println(JsonString.get(i)); 
		  }
	  }
	   
	}
	
	
	@When("^I logged in to website with username \"([^\"]*)\" and password \"([^\"]*)\"$")
	public void i_logged_in_to_website_with_username_and_password(String arg1, String arg2) throws Throwable {
		ChromeOptions options = new ChromeOptions();
	    System.setProperty("webdriver.chrome.driver", "C:\\Users\\SChakrabor\\Softwares\\chromedriver_win32\\chromedriver.exe");
	     driver =new ChromeDriver(options);
	     driver.manage().window().maximize();
	     driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
	     driver.get("https://www.demoblaze.com/index.html");
	   driver.findElement(By.xpath(loginIcone)).click();
	   driver.findElement(By.xpath(loginInputText)).sendKeys("admin");
	   driver.findElement(By.xpath(loginPassword)).sendKeys("admin");
	   driver.findElement(By.xpath(loginButton)).click();
	   
	}

	@When("^I added item to the shopping cart$")
	public void i_added_item_to_the_shopping_cart() throws Throwable {	
		   Thread.sleep(10000);
		   driver.findElement(By.xpath(selectPhone)).click();
		   driver.findElement(By.xpath(addToCart)).click();
		   Thread.sleep(5000);
		   driver.switchTo().alert().accept();
		   
		   driver.findElement(By.xpath(navigateToCart)).click();
		   driver.findElement(By.xpath(placeOrderButton)).click();
		   Thread.sleep(5000);
		   driver.findElement(By.xpath(nameInput)).sendKeys("admin");
		   driver.findElement(By.xpath(countryInput)).sendKeys("admin");
		   driver.findElement(By.xpath(cityInput)).sendKeys("admin");
		   driver.findElement(By.xpath(cardInput)).sendKeys("admin");
		   driver.findElement(By.xpath(monthInput)).sendKeys("admin");
		   driver.findElement(By.xpath(yearInput)).sendKeys("admin");
		   driver.findElement(By.xpath(purchaseButton)).click();

	}

	@Then("^I validate that able to place an order$")
	public void i_validate_that_able_to_place_an_order() throws Throwable {
		String orderDetails=driver.findElements(By.xpath("//h2//following-sibling::p")).get(0).getText();
		System.out.println(orderDetails);
	}
	
	@After("@CloseBrowser")
    public void closeBrowser(){
		driver.close();
		/*
		 * try { if (browser != null) { browser.closeBrowser(); } } catch (Exception e)
		 * { e.printStackTrace(); }
		 */
	}
}
