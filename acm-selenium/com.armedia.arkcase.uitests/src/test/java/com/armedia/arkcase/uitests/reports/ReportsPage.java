package com.armedia.arkcase.uitests.reports;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.armedia.arkcase.uitests.base.ArkCaseTestBase;
import com.thoughtworks.selenium.webdriven.commands.WaitForCondition;

public class ReportsPage extends ArkCaseTestBase {

	@FindBy(how = How.XPATH, using = ".//*[@title='Reports']")
	WebElement ReportsLink;
	@FindBy(how = How.ID, using = "selectionReports")
	WebElement selectReport;
	@FindBy(how = How.ID, using = "selectStateReports")
	WebElement selectState;
	@FindBy(how = How.ID, using = "dateFromReports")
	WebElement reportDateFrom;
	// cases by status
	@FindBy(how = How.ID, using = "dateToReports")
	WebElement reportDateTo;
	@FindBy(how = How.XPATH, using = "//*[contains(text(),'Generate report')]")
	WebElement generateReportButton;
	@FindBy(how = How.ID, using = "dijit_form_NumberTextBox_0")
	WebElement pageNumber;
	@FindBy(how = How.XPATH, using = ".//*[contains(@id, 'panel-')]/div[2]/select")
	WebElement outputType;
	@FindBy(how = How.ID, using = "reportContent")
	WebElement reportContent;
	@FindBy(how = How.XPATH, using = "//table")
	WebElement reportTable;

	public ReportsPage ReportsMenuClick() {
       WebDriverWait wait = new WebDriverWait(driver, 10); 
       WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@title='Reports']")));
	   ReportsLink.click();
	return this;
	}

	public ReportsPage selectReport(String report) {
		selectReport.click();
		new Select(driver.findElement(By.id("selectionReports"))).selectByVisibleText(report);
		return this;
	}

	public ReportsPage selectState(String state) {
		new Select(driver.findElement(By.id("selectStateReports"))).selectByVisibleText(state);
		return this;
	}

	public ReportsPage insertDateFrom(String date) {

		reportDateFrom.click();
		reportDateFrom.clear();
		reportDateFrom.sendKeys(date);
		return this;

	}

	public ReportsPage insertDateTo(String date) {

		reportDateTo.click();
		reportDateTo.clear();
		reportDateTo.sendKeys(date);
		return this;

	}

	public ReportsPage generateReportButtonClick() {

		generateReportButton.click();
		return this;
	}

	public ReportsPage generateReport(String report, String state, String datefrom, String dateto) {
		WebDriverWait wait = new WebDriverWait(driver, 10); 
	    WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id("selectionReports")));	    
		selectReport(report);
		selectState(state);
		insertDateFrom(datefrom);
		insertDateTo(dateto);
		generateReportButtonClick();
		return this;
	}

}
