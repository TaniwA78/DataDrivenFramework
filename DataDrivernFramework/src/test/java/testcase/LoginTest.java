package testcase;


import org.openqa.selenium.By;
import org.testng.annotations.Test;

import base.BaseTest;

public class LoginTest extends BaseTest {

		
		
		@Test
		public void dologin() {
			
			
			
			type("username_ID" , "wau2@automation.com");
			type("password_ID" , "teset");
			click("id");

		}
			
}
