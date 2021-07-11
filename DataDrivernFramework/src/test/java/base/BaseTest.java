package base;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import Utilities.DbManager;
import Utilities.ExcelReader;
import Utilities.MonitoringMail;
import io.github.bonigarcia.wdm.WebDriverManager;





public class BaseTest {

	
	
	private WebDriver driver;
	private MonitoringMail mail = new MonitoringMail();
	
	public ExcelReader excel = new ExcelReader("src\\test\\resources\\excel\\testdata.xlsx");
	public WebDriverWait wait;
	private Logger log = Logger.getLogger(this.getClass());
	private Properties OR = new Properties();
	private Properties config = new Properties();
	private FileInputStream fis;
	
	
	
	
	

public void click(String locator) {
	try {

	if (locator.endsWith("_XPATH")) {
		driver.findElement(By.xpath(OR.getProperty(locator))).click();
	} else if (locator.endsWith("_CSS")) {
		driver.findElement(By.cssSelector(OR.getProperty(locator))).click();
	} else if (locator.endsWith("_ID")) {
		driver.findElement(By.id(OR.getProperty(locator))).click();
	}

	log.info("Clicking on an Element: " + locator);
	
	}catch(Throwable t) {
		
		log.info("Error while clicking on an Element:  "+locator);
		
		Assert.fail(t.getMessage());
		
		
	}
}

public boolean isElementPresent(String locator) {
	try {

	if (locator.endsWith("_XPATH")) {
		driver.findElement(By.xpath(OR.getProperty(locator)));
	} else if (locator.endsWith("_CSS")) {
		driver.findElement(By.cssSelector(OR.getProperty(locator)));
	} else if (locator.endsWith("_ID")) {
		driver.findElement(By.id(OR.getProperty(locator)));
	}

	log.info("verifying Element presence for: " + locator);
	return true ;
	}catch(Throwable t) {
		
		log.info("Error while finding an Element :  "+locator);
		return false ;
		
		
	}

}
public void type(String locator, String value) {
	
	try {

	if (locator.endsWith("_XPATH")) {
		driver.findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);
	} else if (locator.endsWith("_CSS")) {
		driver.findElement(By.cssSelector(OR.getProperty(locator))).sendKeys(value);
	} else if (locator.endsWith("_ID")) {
		driver.findElement(By.id(OR.getProperty(locator))).sendKeys(value);
	}

	log.info("Typing in an Element: " + locator + "  entered the value as : " + value);
	
}catch(Throwable t) {
		
		log.info("Error while typing in an Element:  " + locator);
		
		Assert.fail(t.getMessage());
		
		
		
}
}
	
	
	@BeforeSuite
	public void setup() {
		
		
		if(driver==null) {
			
			
			PropertyConfigurator.configure("src\\test\\resources\\properties\\log4j.properties");
			
			try {
				fis = new FileInputStream("src\\test\\resources\\properties\\OR.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				OR.load(fis);
				log.info("OR Properties file loaded");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			try {
				fis = new FileInputStream("src\\test\\resources\\properties\\Config.properties");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				config.load(fis);
				log.info("Config Properties file loaded");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			if(config.getProperty("browser").equals("chrome")) {
				
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver();
				
				String url = config.getProperty("testsiteurl");
				driver.get(url);

				 
				wait = new WebDriverWait(driver,Integer.parseInt(config.getProperty("explicit.wait")));
		
				log.info("Chrome browser launched");
				
				
			}else if(config.getProperty("browser").equals("firefox")) {
				
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
				log.info("Chrome browser launched");
				
				
				
				log.info("Navigated to : "+config.getProperty("testsiteurl"));
				driver.manage().window().maximize();
				driver.manage().timeouts().implicitlyWait(Integer.parseInt(config.getProperty("implicit.wait")) , TimeUnit.SECONDS);
				
				wait = new WebDriverWait(driver,Integer.parseInt(config.getProperty("explicit.wait")));
		
			
				
				
				
				
				try {
					DbManager.setMysqlDbConnection();
					log.info("Database connection configured");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
				
				
				
			}
			
			
			
			
		}
		
		
		
		
	}
	@AfterSuite
	public void tearDown() {
		
		driver.quit();
		
		
	}

	
	
}
