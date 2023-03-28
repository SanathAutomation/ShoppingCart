package com.test.utils;

import static com.test.utils.CommonUtils.getDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Cookie;	

import com.google.common.base.Function;
import com.ibm.icu.impl.Assert;
import com.paulhammant.ngwebdriver.NgWebDriver;
import com.test.utils.*;

/*http://172.19.42.49:1111/grid/sessions?action=doCleanupActiveSessions
*/
public class BrowserEventManager {

	private static final String WD_HUB = "";
	public WebDriver driverObject = null;
	private int explicitWait = 5;
	private WebDriverWait explicitWaitObject;
	private Actions actions = null;
	private JavascriptExecutor scriptExecuterObject = null;
	private int implicitWait = 60;
	private int fluentWait = 60;
	public static final String JS_SCRIPT_FOR_SCROLL = "arguments[0].scrollIntoView(false);";
	public static Cookie ck;

	public BrowserEventManager() throws IOException, URISyntaxException {
		try {
			this.driverObject = initializeAndReturnDriver();
			getscriptExecuterObject();
			getActionsInstance(this.driverObject);
			getExplicitWaitInstance();
		} catch (Exception e) {
			e.printStackTrace();
			this.driverObject = initializeAndReturnDriver();
			getscriptExecuterObject();
			getActionsInstance(this.driverObject);
			getExplicitWaitInstance();
			this.driverObject.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
		}
	}

	public void setImplicitWait(int implicitWaitByUser) {
		this.driverObject.manage().timeouts().implicitlyWait(implicitWaitByUser, TimeUnit.SECONDS);
	}

	public void setExplicitWait(int i) {

		this.explicitWait = i;
	}

	public void setFluentWait(int i) {

		this.fluentWait = i;
	}

	private WebDriver initializeAndReturnDriver() throws URISyntaxException, MalformedURLException {
		ChromeOptions options = new ChromeOptions();
		//options.addArguments("auto-open-devtools-for-tabs", "true");
				
				System.setProperty("webdriver.chrome.driver", getDriver());
				this.driverObject = new ChromeDriver(options);
		
		return this.driverObject;
	}
	
	public void launchURL(String launchURL) {
		try {
			getURL(launchURL);
			this.driverObject.manage().window().maximize();
			this.driverObject.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private WebDriverWait getExplicitWaitInstance() {
		if (explicitWaitObject == null) {
			explicitWaitObject = new WebDriverWait(this.driverObject, this.explicitWait);
		}
		return explicitWaitObject;
	}

	private JavascriptExecutor getscriptExecuterObject() {
		if (scriptExecuterObject == null) {
			scriptExecuterObject = (JavascriptExecutor) this.driverObject;
		}
		return scriptExecuterObject;
	}

	public JavascriptExecutor getJSExecuter() {
		return this.scriptExecuterObject;
	}

	public void waitForElementToBeClickable(String elementIdentifier) {
		WebDriverWait wait = new WebDriverWait(this.driverObject, 120);
		wait.until(ExpectedConditions.elementToBeClickable(driverObject.findElement(By.xpath(elementIdentifier))));

	}
	
	public void waitForElementToBeClickable(WebElement element) {
		WebDriverWait wait = new WebDriverWait(this.driverObject, 120);
		wait.until(ExpectedConditions.elementToBeClickable(element));

	}
	
	public void waitForElementToBeVisible(String elementIdentifier, int second) {
		WebDriverWait wait = new WebDriverWait(this.driverObject, second);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementIdentifier)));

	}

	public boolean waitForElementToBePresent(String elementIdentifier, int second) throws InterruptedException {
		int USER_SET_SECONDS = second;
		this.setExplicitWait(USER_SET_SECONDS);
		this.findElement(elementIdentifier);
		return true;

	}

	public void waitForElementToBeVisible(WebElement element, int second) throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(this.driverObject, second);
		wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	public int getExplicitWait() {
		return this.explicitWait;
	}

	public void navigateURL(String url) {
		driverObject.navigate().to(url);

	}

	public void getURL(String url) {
		driverObject.get(url);

	}

	public WebDriver getWebDriver() {

		return driverObject;
	}
	
	public void setWeDriver(WebDriver value) {
		this.driverObject=value;
	}

	public void maximizingWindow() {
		driverObject.manage().window().maximize();

	}

	public String getElementIdentifier(String logicalIdentifierOfElement) {
		if (logicalIdentifierOfElement.contains("__")) {
			String[] elementIdentifier = logicalIdentifierOfElement.split("__");
			return elementIdentifier[1];
		} else {
			return logicalIdentifierOfElement;
		}
	}

	public void closeBrowser() {
		if(driverObject!=null) {
		this.driverObject.quit();
		}
	}

	private Boolean isActivejs(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			return (Boolean) js.executeScript("return jQuery.active== 0");
		} catch (Exception e) {
			return true;
		}
	}

	private Boolean isCompletejs(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			return (Boolean) "complete".equals(js.executeScript("return document.readyState").toString());
		} catch (Exception e) {
			return true;
		}
	}

	private Boolean isPendingjs(WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			return (Boolean) js.executeScript(
					"return angular.element(document.body).injector().get(\'$http\').pendingRequests.length== 0");
		} catch (Exception e) {
			return true;
		}
	}

	public void ajaxHandler() throws InterruptedException {
		Thread.sleep(1000);
		while ((isActivejs(this.driverObject) == false) || (isCompletejs(this.driverObject) == false)
				|| (isPendingjs(this.driverObject) == false)) {
			Thread.sleep(1000);
			ajaxHandler();

		}
	}

	public WebElement findWebElementFluently(final By selector) {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(this.driverObject)
				.withTimeout(fluentWait, TimeUnit.SECONDS).pollingEvery(100, TimeUnit.MILLISECONDS)
				.ignoring(Exception.class);
		return wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(selector);
			}
		});
	}

	public WebElement findElement(String elementIdentifier) throws InterruptedException {
		WebElement elementToReturn = null;
		if (elementIdentifier.isEmpty()) {
			return elementToReturn;
		}

		else {
			elementIdentifier = elementIdentifier.trim();
			String identifier;
			String[] elementIdentiferAfterSplit = elementIdentifier.split("__");
			String findElementWith = elementIdentiferAfterSplit[0];
			int identifierSize = elementIdentiferAfterSplit.length;
			if (identifierSize == 2) {
				identifier = elementIdentiferAfterSplit[1];
			} else {
				String buffer = elementIdentiferAfterSplit[0];
				identifier = buffer;
				findElementWith = "xpath";
			}

			switch (findElementWith) {
			case "xpath":
				//ajaxHandler();
				this.findWebElementFluently(By.xpath(identifier));
				this.explicitWaitObject.until(ExpectedConditions.presenceOfElementLocated(By.xpath(identifier)));
				elementToReturn = this.driverObject.findElement(By.xpath(identifier));
				break;
			case "id":
			//	ajaxHandler();
				this.findWebElementFluently(By.id(identifier));
				this.explicitWaitObject.until(ExpectedConditions.presenceOfElementLocated(By.id(identifier)));
				elementToReturn = this.driverObject.findElement(By.id(identifier));
				break;
			case "name":
				//ajaxHandler();
				this.findWebElementFluently(By.name(identifier));
				this.explicitWaitObject.until(ExpectedConditions.presenceOfElementLocated(By.name(identifier)));
				elementToReturn = this.driverObject.findElement(By.name(identifier));
				break;
			case "css":
				//ajaxHandler();
				this.findWebElementFluently(By.cssSelector(identifier));
				this.explicitWaitObject.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(identifier)));
				elementToReturn = this.driverObject.findElement(By.cssSelector(identifier));
				break;
			case "classname":
				//ajaxHandler();
				this.findWebElementFluently(By.className(identifier));
				this.explicitWaitObject.until(ExpectedConditions.presenceOfElementLocated(By.className(identifier)));
				elementToReturn = this.driverObject.findElement(By.className(identifier));
				break;
			case "linktext":
			//	ajaxHandler();
				this.findWebElementFluently(By.linkText(identifier));
				this.explicitWaitObject.until(ExpectedConditions.presenceOfElementLocated(By.linkText(identifier)));
				elementToReturn = this.driverObject.findElement(By.linkText(identifier));
				break;
			case "partiallinktext":
				//ajaxHandler();
				this.findWebElementFluently(By.partialLinkText(identifier));
				this.explicitWaitObject
						.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText(identifier)));
				elementToReturn = this.driverObject.findElement(By.partialLinkText(identifier));
				break;
			default:
				break;
			}
			try {
				JavascriptExecutor jsExecutor = (JavascriptExecutor) this.driverObject;
				jsExecutor.executeScript("arguments[0].style.border='2px solid red'", elementToReturn);
			} catch (Exception e) {

			}
			return elementToReturn;
		}

	}

	public WebElement waitFor(WebDriver driver, final By by) {

		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(50, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS).ignoring(Exception.class);

		wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {

				return driver.findElement(by);
			}
		});

		return driver.findElement(by);
	}

	public void clickSimple(String elementIdentifier) throws InterruptedException {
		try {
			findElement(elementIdentifier).click();
	
		} catch (Exception e) {
			Thread.sleep(500);
			getJSExecuter().executeScript(JS_SCRIPT_FOR_SCROLL, findElement(elementIdentifier));
			Thread.sleep(500);
			findElement(elementIdentifier).click();
		}

	}

	public String getTextOfWebElement(String elementIdentifier) throws InterruptedException {
		WebElement elementToGetTextFrom = this.findElement(elementIdentifier);
		return elementToGetTextFrom.getText();
	}

	private Actions getActionsInstance(WebDriver driver) {
		if (actions == null) {
			actions = new Actions(this.driverObject);
		}
		return this.actions;
	}

	public Actions dragAnddropBy(WebElement source, WebElement destination) {
		return getActionsInstance(driverObject).dragAndDrop(source, destination);
	}

	public void clickDoubleClick(String elementIdentifier) throws InterruptedException {
		getActionsInstance(this.driverObject).doubleClick(findElement(elementIdentifier)).build().perform();
	}

	public void clickByJS(String elementIdentifier) throws InterruptedException {
		try {
		this.scriptExecuterObject.executeScript("arguments[0].style.border='2px solid red'", elementIdentifier);
		}catch(Exception e) {
			
		}
		this.scriptExecuterObject.executeScript("arguments[0].click();", findElement(elementIdentifier));

	}

	public void clickByActionClass(String elementIdentifier) throws InterruptedException {
		getActionsInstance(this.driverObject).moveToElement(findElement(elementIdentifier)).click().build().perform();
	}

	public boolean isElementPresent(String elementIdentifier) {
		String[] elementIdentiferAfterSplit = elementIdentifier.split("__");
		String findElementWith = elementIdentiferAfterSplit[1];
		if (this.driverObject.findElements(By.xpath(findElementWith)).size() != 0) {
			return true;
		}
		return false;
	}

	public void waitForPageLoaded() {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ("complete"
						.equals(((JavascriptExecutor) driver).executeScript("return document.readyState").toString()));
			}
		};
		try {
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driverObject, 30);
			wait.until(expectation);
		} catch (Exception e) {
			Assert.fail(e);
		}
	}

	public void inputText(String elementIdentifier, String inputText) throws InterruptedException {
		findElement(elementIdentifier).clear();
		findElement(elementIdentifier).sendKeys(inputText);
	}

	public void switchToFrameByNameOrId(String frameNameOrId) {
		driverObject.switchTo().frame(frameNameOrId);
	}

	public void switchToFrameByIndex(int frameIndex) {
		driverObject.switchTo().frame(frameIndex);
	}

	public void switchToFrameByWebElement(WebElement frameElement) {
		driverObject.switchTo().frame(frameElement);
	}

	public void switchTodefaultContent() {
		driverObject.switchTo().defaultContent();
	}

	public void scrollToElementAndClick(String elementIdentifier) throws InterruptedException {
		Thread.sleep(500);
		getJSExecuter().executeScript(JS_SCRIPT_FOR_SCROLL, findElement(elementIdentifier));
		Thread.sleep(500);
		findElement(elementIdentifier).click();
	}

	public void scrollToElement(String elementIdentifier) throws InterruptedException {
		Thread.sleep(500);
		getJSExecuter().executeScript(JS_SCRIPT_FOR_SCROLL, findElement(elementIdentifier));
		Thread.sleep(500);
	}

	public void refreshWebPage() throws InterruptedException {
		this.driverObject.navigate().refresh();
		ajaxHandler();
	}

	public List<WebElement> findElements(String elementIdentifier) {
		return this.driverObject.findElements(By.xpath(this.getElementIdentifier(elementIdentifier)));

	}

	public void findElementsAndClickOnNthElement(String elementIdentifier, int index) throws InterruptedException {
		List <WebElement> options=this.driverObject.findElements(By.xpath(this.getElementIdentifier(elementIdentifier)));
	//	getJSExecuter().executeScript("window.scrollBy(0,500)");
		Thread.sleep(5000);
		try {
		this.scriptExecuterObject.executeScript("arguments[0].style.border='2px solid red'", options.get(index));
		}catch(Exception e) {		
		}
		options.get(index).click();
		}
	
	public void findElementsAndClickOnLastElement(String elementIdentifier) throws InterruptedException {
		List <WebElement> options=this.driverObject.findElements(By.xpath(this.getElementIdentifier(elementIdentifier)));
	//	getJSExecuter().executeScript("window.scrollBy(0,500)");
		Thread.sleep(2000);
		try {
		this.scriptExecuterObject.executeScript("arguments[0].style.border='2px solid red'", options.get(options.size()-1));
		}catch(Exception e) {		
		}
		options.get(options.size()-1).click();
	}
	
	public void findElementsAndSendOnLastElement(String elementIdentifier,String inputText) throws InterruptedException {
		List <WebElement> options=this.driverObject.findElements(By.xpath(this.getElementIdentifier(elementIdentifier)));
	//	getJSExecuter().executeScript("window.scrollBy(0,500)");
		Thread.sleep(2000);
		try {
		this.scriptExecuterObject.executeScript("arguments[0].style.border='2px solid red'", options.get(options.size()-1));
		}catch(Exception e) {		
		}
		options.get(options.size()-1).sendKeys(inputText);
	}
	
	public Actions getActionsObject() {
		return this.actions;
	}

	public void mouseHover(String elementIdentifier) throws InterruptedException {
		this.getActionsObject().moveToElement(this.findElement(elementIdentifier)).perform();

	}
	
	public Cookie getBrowserCookies(String elementIdentifier,String elementIdentifier1,String elementIdentifier2,String value1,String value2) throws InterruptedException {
		try {
			findElement(elementIdentifier).sendKeys(value1);
			findElement(elementIdentifier1).sendKeys(value2);
			findElement(elementIdentifier2).click();
			
			BrowserEventManager browser = new BrowserEventManager();
			ck = (Cookie) browser.driverObject.manage().getCookies();
	
		} catch (Exception e) {
		
		}
		return ck;

	}
	
	public NgWebDriver getNGDriver() {
		JavascriptExecutor js = (JavascriptExecutor) driverObject;
		return (new NgWebDriver(js).withRootSelector("app-root"));
	}
	
	public void waitforangularRequestsToFinish() {
		try {
			getNGDriver().waitForAngularRequestsToFinish();
		} catch (Exception e) {
			Assert.fail("Error while waiting for Angular requests to finish: " + e.getMessage());
		}
	}
	
}
