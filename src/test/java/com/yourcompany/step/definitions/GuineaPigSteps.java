package com.yourcompany.step.definitions;

import com.yourcompany.utils.SauceUtils;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import java.util.UUID;
import static org.junit.Assert.*;


import com.yourcompany.Pages.*;

import static org.hamcrest.CoreMatchers.containsString;

public class GuineaPigSteps {
	public static final String URL = "https://ondemand.saucelabs.com:443/wd/hub";
	public static WebDriver driver;
	public static SandboxPage page;
	public String commentInputText;
	public String sessionId;
	public String jobName;

	@Before
	public void setUp(Scenario scenario) {
        DesiredCapabilities caps = new DesiredCapabilities();

        caps.setCapability("platform", "Windows 10");
        caps.setCapability("browserName", "chrome");
        caps.setCapability("version", "72.0");
		caps.setCapability("username", System.getenv("SAUCE_USERNAME"));
		caps.setCapability("accessKey", System.getenv("SAUCE_ACCESS_KEY"));

        jobName = scenario.getName();
        caps.setCapability("name", jobName);

		try {
			driver = new RemoteWebDriver(new URL(URL), caps);
		} catch (MalformedURLException e) {
			throw new RuntimeException("URL was mal-formed: " + URL);
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
	}

	@Given("^I am on the Guinea Pig homepage$")
	public void user_is_on_guinea_pig_page() {
		page = SandboxPage.visitPage(driver);
	}

	@When("^I click on the link$")
	public void user_click_on_the_link() {
		page.followLink();
	}

	@When("^I submit a comment$")
	public void user_submit_comment() {
		commentInputText = UUID.randomUUID().toString();
		page.submitComment(commentInputText);
	}

	@Then("^I should be on another page$")
	public void new_page_displayed() {
		assertFalse(page.isOnPage());
	}

	@Then("^I should see that comment displayed$")
	public void comment_displayed() {
		assertThat(page.getSubmittedCommentText(), containsString(commentInputText));
	}

	@After
	public void tearDown(Scenario scenario) {
		driver.quit();
		SauceUtils.UpdateResults(System.getenv("SAUCE_USERNAME"), System.getenv("SAUCE_ACCESS_KEY"), !scenario.isFailed(), sessionId);
		System.out.println("SauceOnDemandSessionID="+ sessionId + "job-name="+ jobName);
	}
}