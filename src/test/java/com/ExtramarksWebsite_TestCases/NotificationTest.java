package com.ExtramarksWebsite_TestCases;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.ExtramarksWebsite_Pages.DashBoardPage;
import com.ExtramarksWebsite_Pages.LoginPage;
import com.ExtramarksWebsite_Pages.NotificationPage;
import com.ExtramarksWebsite_Pages.ProfilePage;
import com.ExtramarksWebsite_Utils.Constants;
import com.ExtramarksWebsite_Utils.DataUtil;
import com.ExtramarksWebsite_Utils.ExtentManager;
import com.ExtramarksWebsite_Utils.Xls_Reader;
import com.relevantcodes.extentreports.LogStatus;

public class NotificationTest extends BaseTest {

	@BeforeMethod(alwaysRun = true)
	public void init(Method method) {
		rep = ExtentManager.getInstance();
		String testMethodName = method.getName();
		test = rep.startTest(testMethodName);
	}

	@AfterMethod(alwaysRun = true)
	public void logOut(ITestResult itr) throws InterruptedException {

		try {
			logStatus(itr);
			DashBoardPage dp = new DashBoardPage(driver, test);
			Thread.sleep(1000);
			// JavascriptExecutor js = (JavascriptExecutor) driver;
			// js.executeScript("arguments[0].click();", dp.SettingsIcon.get(0));
			click(dp.SettingsIcon, 0, "Settingicon");
			click(dp.LogOut, 0, "Logout");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rep.endTest(test);
			rep.flush();
			if (driver != null) {
				driver.quit();
				driver = null;
			}
		}
	}

	@AfterTest(alwaysRun = true)
	public void quit() throws InterruptedException {
     System.out.println("Test Execution finished");
     test.log(LogStatus.INFO, "Test Execution finished");
     Reporter.log("Test Execution finished");
	}
	

	@DataProvider
	public Object[][] getData() {
		Xls_Reader xls = new Xls_Reader(Constants.XLS_FILE_PATH);
		Object[][] data = DataUtil.getData(xls, "NotificationTest");
		return data;
	}

	@Test(dataProvider = "getData", priority = 1)
	public void notificationAccept(Hashtable<String, String> data) throws Exception {
		defaultLogin(data.get("Browser"),data.get("Username"),data.get("Password"));
		SoftAssert sAssert=new SoftAssert();
		driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
		LoginPage lp = new LoginPage(driver, test);
		DashBoardPage dp = new DashBoardPage(driver, test);
		Object resultPage = dp.openNotification();
		if (resultPage instanceof NotificationPage) {
			test.log(LogStatus.INFO, "Opening Notification Page");
			System.out.println("Opening Notification Page");
			NotificationPage np = (NotificationPage) resultPage;
		    np.notificationAccept(data);
		}
	}
}
