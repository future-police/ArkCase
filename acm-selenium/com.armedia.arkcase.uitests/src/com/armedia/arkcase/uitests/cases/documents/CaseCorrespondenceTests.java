package com.armedia.arkcase.uitests.cases.documents;

import java.awt.AWTException;
import java.io.IOException;

import org.junit.Test;
import org.openqa.selenium.support.PageFactory;

import com.armedia.arkcase.uitests.base.ArkCaseAuthentication;
import com.armedia.arkcase.uitests.base.ArkCaseTestBase;
import com.armedia.arkcase.uitests.base.ArkCaseTestUtils;
import com.armedia.arkcase.uitests.base.ArkCaseUtils;
import com.armedia.arkcase.uitests.base.TestsPoperties;
import com.armedia.arkcase.uitests.cases.CasePage;
import com.armedia.arkcase.uitests.cases.CasesPage;

public class CaseCorrespondenceTests extends ArkCaseTestBase {

	CasesPage cases = PageFactory.initElements(driver, CasesPage.class);
	CasePage casePom = PageFactory.initElements(driver, CasePage.class);
	CaseDocumentsPage documentsPage = PageFactory.initElements(driver, CaseDocumentsPage.class);
	ArkCaseUtils check = new ArkCaseUtils();

	@Test
	public void createNewCaseAddCorrespondenceWitnesInterviewrequest() throws InterruptedException, IOException {

		ArkCaseAuthentication.logIn(TestsPoperties.getSupervisorUserUsername(),
				TestsPoperties.getSupervisorUserPassword(), driver, TestsPoperties.getBaseURL());
		casePom.newCase();
		Thread.sleep(20000);
		driver.switchTo().frame(cases.frameOne);
		driver.switchTo().frame(cases.frameTwo);
		casePom.vrifyGeneralInformationTabName();
		casePom.caseTitleInput("Add Witness Interview");
		casePom.verifyCaseTypeTitle();
		casePom.caseTypeInputClick();
		Thread.sleep(2000);
		casePom.caseTypeBackgroundInvestigation();
		Thread.sleep(2000);
		casePom.nextButton.click();
		Thread.sleep(3000);
		casePom.verifyInitiatorTab();
		casePom.initiatorTitle.click();
		Thread.sleep(2000);
		casePom.clickInitiatorMr();
		Thread.sleep(2000);
		casePom.initiatorFirstName("Milan");
		Thread.sleep(2000);
		casePom.initiatorLastName("Jovanovski");
		Thread.sleep(4000);
		casePom.participantnsTab.click();
		Thread.sleep(2000);
		casePom.selectParticipantTypeClick();
		Thread.sleep(2000);
		casePom.selectparticipantOwner();
		Thread.sleep(2000);
		casePom.selectParticipantClick();
		Thread.sleep(3000);
		casePom.searchForUsers();
		Thread.sleep(3000);
		casePom.searchedName();
		casePom.addSearchedNameClick();
		Thread.sleep(2000);
		casePom.submit.click();
		Thread.sleep(10000);
		driver.switchTo().defaultContent();
		Thread.sleep(5000);
		cases.caseDocuments.click();
		Thread.sleep(3000);
		documentsPage.performRightClickOnRoot();
		Thread.sleep(4000);
		documentsPage.checkIfRightClickOnRootIsWorking();
		Thread.sleep(3000);
		documentsPage.verifyNewCorrespondeneName();
		Thread.sleep(2000);
		documentsPage.NewCorrespondence.click();
		Thread.sleep(3000);
		documentsPage.verifyCorrespondenceMenu();
		Thread.sleep(2000);
		documentsPage.verifyWitnessInterviewRequest();
		documentsPage.corresponceWitnessInterview.click();
		Thread.sleep(4000);
		documentsPage.verifyIfSecondRowDocumentIsPresent();
		documentsPage.verifySecondDocument("Witness Interview Request.docx", "Witness Interview Request", "1.0",
				"ACTIVE");
		Thread.sleep(3000);
		documentsPage.chnageCaseStatusButton.click();
		Thread.sleep(10000);
		driver.switchTo().frame(cases.chnageCaseStausFrameOne);
		Thread.sleep(2000);
		driver.switchTo().frame(cases.chnageCaseStatusFrameTwo);
		Thread.sleep(2000);
		cases.deleteCase();
		driver.switchTo().defaultContent();
		cases.changeCaseStatusAproved();
		Thread.sleep(3000);
		ArkCaseAuthentication.logOut(driver);

	}

	@Test
	public void createNewCaseAddCorrespondenceNoticeOfInvestigation() throws InterruptedException, IOException {

		ArkCaseAuthentication.logIn(TestsPoperties.getSupervisorUserUsername(),
				TestsPoperties.getSupervisorUserPassword(), driver, TestsPoperties.getBaseURL());
		casePom.newCase();
		Thread.sleep(20000);
		driver.switchTo().frame(cases.frameOne);
		driver.switchTo().frame(cases.frameTwo);
		casePom.vrifyGeneralInformationTabName();
		casePom.caseTitleInput("Add Notice of Investigation");
		casePom.verifyCaseTypeTitle();
		casePom.caseTypeInputClick();
		Thread.sleep(2000);
		casePom.caseTypeBackgroundInvestigation();
		Thread.sleep(2000);
		casePom.nextButton.click();
		Thread.sleep(3000);
		casePom.verifyInitiatorTab();
		casePom.initiatorTitle.click();
		Thread.sleep(2000);
		casePom.clickInitiatorMr();
		Thread.sleep(2000);
		casePom.initiatorFirstName("Milan");
		Thread.sleep(2000);
		casePom.initiatorLastName("Jovanovski");
		Thread.sleep(4000);
		casePom.participantnsTab.click();
		Thread.sleep(2000);
		casePom.selectParticipantTypeClick();
		Thread.sleep(2000);
		casePom.selectparticipantOwner();
		Thread.sleep(2000);
		casePom.selectParticipantClick();
		Thread.sleep(3000);
		casePom.searchForUsers();
		Thread.sleep(3000);
		casePom.searchedName();
		casePom.addSearchedNameClick();
		Thread.sleep(2000);
		casePom.submit.click();
		Thread.sleep(10000);
		driver.switchTo().defaultContent();
		Thread.sleep(5000);
		cases.caseDocuments.click();
		Thread.sleep(3000);
		cases.refreshPage.click();
		Thread.sleep(4000);
		documentsPage.performRightClickOnRoot();
		Thread.sleep(4000);
		documentsPage.checkIfRightClickOnRootIsWorking();
		Thread.sleep(3000);
		documentsPage.verifyNewCorrespondeneName();
		Thread.sleep(2000);
		documentsPage.NewCorrespondence.click();
		Thread.sleep(3000);
		documentsPage.verifyCorrespondenceMenu();
		Thread.sleep(2000);
		documentsPage.verifyNoticeofInvestigation();
		documentsPage.correspondenceNoticeOfInvestigation.click();
		Thread.sleep(4000);
		documentsPage.verifyIfSecondRowDocumentIsPresent();
		documentsPage.verifySecondDocument("Notice of Investigation.docx", "Notice Of Investigation", "1.0", "ACTIVE");
		Thread.sleep(3000);
		documentsPage.chnageCaseStatusButton.click();
		Thread.sleep(10000);
		driver.switchTo().frame(cases.chnageCaseStausFrameOne);
		Thread.sleep(2000);
		driver.switchTo().frame(cases.chnageCaseStatusFrameTwo);
		Thread.sleep(2000);
		cases.deleteCase();
		driver.switchTo().defaultContent();
		cases.changeCaseStatusAproved();
		Thread.sleep(3000);
		ArkCaseAuthentication.logOut(driver);

	}

	@Test
	public void createNewCaseAddCorrespondenceClearanceDenied() throws InterruptedException, IOException {

		ArkCaseAuthentication.logIn(TestsPoperties.getSupervisorUserUsername(),
				TestsPoperties.getSupervisorUserPassword(), driver, TestsPoperties.getBaseURL());
		casePom.newCase();
		Thread.sleep(20000);
		driver.switchTo().frame(cases.frameOne);
		driver.switchTo().frame(cases.frameTwo);
		casePom.vrifyGeneralInformationTabName();
		casePom.caseTitleInput("Add Clearance Denied");
		casePom.verifyCaseTypeTitle();
		casePom.caseTypeInputClick();
		Thread.sleep(2000);
		casePom.caseTypeBackgroundInvestigation();
		Thread.sleep(2000);
		casePom.nextButton.click();
		Thread.sleep(3000);
		casePom.verifyInitiatorTab();
		casePom.initiatorTitle.click();
		Thread.sleep(2000);
		casePom.clickInitiatorMr();
		Thread.sleep(2000);
		casePom.initiatorFirstName("Milan");
		Thread.sleep(2000);
		casePom.initiatorLastName("Jovanovski");
		Thread.sleep(4000);
		casePom.participantnsTab.click();
		Thread.sleep(2000);
		casePom.selectParticipantTypeClick();
		Thread.sleep(2000);
		casePom.selectparticipantOwner();
		Thread.sleep(2000);
		casePom.selectParticipantClick();
		Thread.sleep(3000);
		casePom.searchForUsers();
		Thread.sleep(3000);
		casePom.searchedName();
		casePom.addSearchedNameClick();
		Thread.sleep(2000);
		casePom.submit.click();
		Thread.sleep(10000);
		driver.switchTo().defaultContent();
		Thread.sleep(5000);
		cases.caseDocuments.click();
		Thread.sleep(3000);
		cases.refreshPage.click();
		Thread.sleep(4000);
		documentsPage.performRightClickOnRoot();
		Thread.sleep(4000);
		documentsPage.checkIfRightClickOnRootIsWorking();
		Thread.sleep(3000);
		documentsPage.verifyNewCorrespondeneName();
		Thread.sleep(2000);
		documentsPage.NewCorrespondence.click();
		Thread.sleep(3000);
		documentsPage.verifyCorrespondenceMenu();
		Thread.sleep(2000);
		documentsPage.verifyClearanceDenied();
		documentsPage.correspondenceClearanceDenide.click();
		Thread.sleep(4000);
		documentsPage.verifyIfSecondRowDocumentIsPresent();
		documentsPage.verifySecondDocument("Clearance Denied.docx", "Clearance Denied", "1.0", "ACTIVE");
		Thread.sleep(3000);
		documentsPage.chnageCaseStatusButton.click();
		Thread.sleep(10000);
		driver.switchTo().frame(cases.chnageCaseStausFrameOne);
		Thread.sleep(2000);
		driver.switchTo().frame(cases.chnageCaseStatusFrameTwo);
		Thread.sleep(2000);
		cases.deleteCase();
		driver.switchTo().defaultContent();
		cases.changeCaseStatusAproved();
		Thread.sleep(3000);
		ArkCaseAuthentication.logOut(driver);

	}

	@Test
	public void createNewCaseAddCorrespondenceClearanceGranted() throws InterruptedException, IOException {

		ArkCaseAuthentication.logIn(TestsPoperties.getSupervisorUserUsername(),
				TestsPoperties.getSupervisorUserPassword(), driver, TestsPoperties.getBaseURL());
		casePom.newCase();
		Thread.sleep(20000);
		driver.switchTo().frame(cases.frameOne);
		driver.switchTo().frame(cases.frameTwo);
		casePom.vrifyGeneralInformationTabName();
		casePom.caseTitleInput("Add Clearance Granted");
		casePom.verifyCaseTypeTitle();
		casePom.caseTypeInputClick();
		Thread.sleep(2000);
		casePom.caseTypeBackgroundInvestigation();
		Thread.sleep(2000);
		casePom.nextButton.click();
		Thread.sleep(3000);
		casePom.verifyInitiatorTab();
		casePom.initiatorTitle.click();
		Thread.sleep(2000);
		casePom.clickInitiatorMr();
		Thread.sleep(2000);
		casePom.initiatorFirstName("Milan");
		Thread.sleep(2000);
		casePom.initiatorLastName("Jovanovski");
		Thread.sleep(4000);
		casePom.participantnsTab.click();
		Thread.sleep(2000);
		casePom.selectParticipantTypeClick();
		Thread.sleep(2000);
		casePom.selectparticipantOwner();
		Thread.sleep(2000);
		casePom.selectParticipantClick();
		Thread.sleep(3000);
		casePom.searchForUsers();
		Thread.sleep(3000);
		casePom.searchedName();
		casePom.addSearchedNameClick();
		Thread.sleep(2000);
		casePom.submit.click();
		Thread.sleep(10000);
		driver.switchTo().defaultContent();
		Thread.sleep(5000);
		cases.caseDocuments.click();
		Thread.sleep(3000);
		cases.refreshPage.click();
		Thread.sleep(4000);
		documentsPage.performRightClickOnRoot();
		Thread.sleep(4000);
		documentsPage.checkIfRightClickOnRootIsWorking();
		Thread.sleep(3000);
		documentsPage.verifyNewCorrespondeneName();
		Thread.sleep(2000);
		documentsPage.NewCorrespondence.click();
		Thread.sleep(3000);
		documentsPage.verifyCorrespondenceMenu();
		Thread.sleep(2000);
		documentsPage.verifyClearanceGranted();
		documentsPage.correspondenceClearanceGranted.click();
		Thread.sleep(4000);
		documentsPage.verifyIfSecondRowDocumentIsPresent();
		documentsPage.verifySecondDocument("Clearance Granted.docx", "Clearance Granted", "1.0", "ACTIVE");
		Thread.sleep(3000);
		documentsPage.chnageCaseStatusButton.click();
		Thread.sleep(10000);
		driver.switchTo().frame(cases.chnageCaseStausFrameOne);
		Thread.sleep(2000);
		driver.switchTo().frame(cases.chnageCaseStatusFrameTwo);
		Thread.sleep(2000);
		cases.deleteCase();
		driver.switchTo().defaultContent();
		cases.changeCaseStatusAproved();
		Thread.sleep(3000);
		ArkCaseAuthentication.logOut(driver);

	}

	@Test
	public void createNewCaseAddCorrespondenceMedicalRelease() throws InterruptedException, IOException {

		ArkCaseAuthentication.logIn(TestsPoperties.getSupervisorUserUsername(),
				TestsPoperties.getSupervisorUserPassword(), driver, TestsPoperties.getBaseURL());
		casePom.newCase();
		Thread.sleep(20000);
		driver.switchTo().frame(cases.frameOne);
		driver.switchTo().frame(cases.frameTwo);
		casePom.vrifyGeneralInformationTabName();
		casePom.caseTitleInput("Add Medical Release");
		casePom.verifyCaseTypeTitle();
		casePom.caseTypeInputClick();
		Thread.sleep(2000);
		casePom.caseTypeBackgroundInvestigation();
		Thread.sleep(2000);
		casePom.nextButton.click();
		Thread.sleep(3000);
		casePom.verifyInitiatorTab();
		casePom.initiatorTitle.click();
		Thread.sleep(2000);
		casePom.clickInitiatorMr();
		Thread.sleep(2000);
		casePom.initiatorFirstName("Milan");
		Thread.sleep(2000);
		casePom.initiatorLastName("Jovanovski");
		Thread.sleep(4000);
		casePom.participantnsTab.click();
		Thread.sleep(2000);
		casePom.selectParticipantTypeClick();
		Thread.sleep(2000);
		casePom.selectparticipantOwner();
		Thread.sleep(2000);
		casePom.selectParticipantClick();
		Thread.sleep(3000);
		casePom.searchForUsers();
		Thread.sleep(3000);
		casePom.searchedName();
		casePom.addSearchedNameClick();
		Thread.sleep(2000);
		casePom.submit.click();
		Thread.sleep(10000);
		driver.switchTo().defaultContent();
		Thread.sleep(5000);
		cases.caseDocuments.click();
		Thread.sleep(3000);
		cases.refreshPage.click();
		Thread.sleep(4000);
		documentsPage.performRightClickOnRoot();
		Thread.sleep(4000);
		documentsPage.checkIfRightClickOnRootIsWorking();
		Thread.sleep(3000);
		documentsPage.verifyNewCorrespondeneName();
		Thread.sleep(2000);
		documentsPage.NewCorrespondence.click();
		Thread.sleep(3000);
		documentsPage.verifyCorrespondenceMenu();
		Thread.sleep(2000);
		documentsPage.verifyMedicalRelease();
		documentsPage.correspondenceMedicalRelease.click();
		Thread.sleep(4000);
		documentsPage.verifyIfSecondRowDocumentIsPresent();
		documentsPage.verifySecondDocument("Medical Release.docx", "Medical Release", "1.0", "ACTIVE");
		Thread.sleep(3000);
		documentsPage.chnageCaseStatusButton.click();
		Thread.sleep(10000);
		driver.switchTo().frame(cases.chnageCaseStausFrameOne);
		Thread.sleep(2000);
		driver.switchTo().frame(cases.chnageCaseStatusFrameTwo);
		Thread.sleep(2000);
		cases.deleteCase();
		driver.switchTo().defaultContent();
		cases.changeCaseStatusAproved();
		Thread.sleep(3000);
		ArkCaseAuthentication.logOut(driver);

	}

	@Test
	public void createNewCaseAddCorrespondenceGeneralRelease() throws InterruptedException, IOException {

		ArkCaseAuthentication.logIn(TestsPoperties.getSupervisorUserUsername(),
				TestsPoperties.getSupervisorUserPassword(), driver, TestsPoperties.getBaseURL());
		casePom.newCase();
		Thread.sleep(20000);
		driver.switchTo().frame(cases.frameOne);
		driver.switchTo().frame(cases.frameTwo);
		casePom.vrifyGeneralInformationTabName();
		casePom.caseTitleInput("Add General Release");
		casePom.verifyCaseTypeTitle();
		casePom.caseTypeInputClick();
		Thread.sleep(2000);
		casePom.caseTypeBackgroundInvestigation();
		Thread.sleep(2000);
		casePom.nextButton.click();
		Thread.sleep(3000);
		casePom.verifyInitiatorTab();
		casePom.initiatorTitle.click();
		Thread.sleep(2000);
		casePom.clickInitiatorMr();
		Thread.sleep(2000);
		casePom.initiatorFirstName("Milan");
		Thread.sleep(2000);
		casePom.initiatorLastName("Jovanovski");
		Thread.sleep(4000);
		casePom.participantnsTab.click();
		Thread.sleep(2000);
		casePom.selectParticipantTypeClick();
		Thread.sleep(2000);
		casePom.selectparticipantOwner();
		Thread.sleep(2000);
		casePom.selectParticipantClick();
		Thread.sleep(3000);
		casePom.searchForUsers();
		Thread.sleep(3000);
		casePom.searchedName();
		casePom.addSearchedNameClick();
		Thread.sleep(2000);
		casePom.submit.click();
		Thread.sleep(10000);
		driver.switchTo().defaultContent();
		Thread.sleep(5000);
		cases.caseDocuments.click();
		Thread.sleep(3000);
		cases.refreshPage.click();
		Thread.sleep(4000);
		documentsPage.performRightClickOnRoot();
		Thread.sleep(4000);
		documentsPage.checkIfRightClickOnRootIsWorking();
		Thread.sleep(3000);
		documentsPage.verifyNewCorrespondeneName();
		Thread.sleep(2000);
		documentsPage.NewCorrespondence.click();
		Thread.sleep(3000);
		documentsPage.verifyCorrespondenceMenu();
		Thread.sleep(2000);
		documentsPage.verifyGeneralRelease();
		documentsPage.correspondenceGeneralRelease.click();
		Thread.sleep(4000);
		documentsPage.verifyIfSecondRowDocumentIsPresent();
		documentsPage.verifySecondDocument("General Release.docx", "General Release", "1.0", "ACTIVE");
		Thread.sleep(3000);
		documentsPage.chnageCaseStatusButton.click();
		Thread.sleep(10000);
		driver.switchTo().frame(cases.chnageCaseStausFrameOne);
		Thread.sleep(2000);
		driver.switchTo().frame(cases.chnageCaseStatusFrameTwo);
		Thread.sleep(2000);
		cases.deleteCase();
		driver.switchTo().defaultContent();
		cases.changeCaseStatusAproved();
		Thread.sleep(3000);
		ArkCaseAuthentication.logOut(driver);

	}

	@Test
	public void createNewCaseAddCorrespondenceDeleteTheCorrespondence() throws InterruptedException, IOException {

		ArkCaseAuthentication.logIn(TestsPoperties.getSupervisorUserUsername(),
				TestsPoperties.getSupervisorUserPassword(), driver, TestsPoperties.getBaseURL());
		casePom.newCase();
		Thread.sleep(20000);
		driver.switchTo().frame(cases.frameOne);
		driver.switchTo().frame(cases.frameTwo);
		casePom.vrifyGeneralInformationTabName();
		casePom.caseTitleInput("Add Delete General Release");
		casePom.verifyCaseTypeTitle();
		casePom.caseTypeInputClick();
		Thread.sleep(2000);
		casePom.caseTypeBackgroundInvestigation();
		Thread.sleep(2000);
		casePom.nextButton.click();
		Thread.sleep(3000);
		casePom.verifyInitiatorTab();
		casePom.initiatorTitle.click();
		Thread.sleep(2000);
		casePom.clickInitiatorMr();
		Thread.sleep(2000);
		casePom.initiatorFirstName("Milan");
		Thread.sleep(2000);
		casePom.initiatorLastName("Jovanovski");
		Thread.sleep(4000);
		casePom.participantnsTab.click();
		Thread.sleep(2000);
		casePom.selectParticipantTypeClick();
		Thread.sleep(2000);
		casePom.selectparticipantOwner();
		Thread.sleep(2000);
		casePom.selectParticipantClick();
		Thread.sleep(3000);
		casePom.searchForUsers();
		Thread.sleep(3000);
		casePom.searchedName();
		casePom.addSearchedNameClick();
		Thread.sleep(2000);
		casePom.submit.click();
		Thread.sleep(10000);
		driver.switchTo().defaultContent();
		Thread.sleep(5000);
		cases.caseDocuments.click();
		Thread.sleep(3000);
		cases.refreshPage.click();
		Thread.sleep(4000);
		documentsPage.performRightClickOnRoot();
		Thread.sleep(4000);
		documentsPage.checkIfRightClickOnRootIsWorking();
		Thread.sleep(3000);
		documentsPage.verifyNewCorrespondeneName();
		Thread.sleep(2000);
		documentsPage.NewCorrespondence.click();
		Thread.sleep(3000);
		documentsPage.verifyCorrespondenceMenu();
		Thread.sleep(2000);
		documentsPage.verifyGeneralRelease();
		documentsPage.correspondenceGeneralRelease.click();
		Thread.sleep(4000);
		documentsPage.verifyIfSecondRowDocumentIsPresent();
		documentsPage.verifySecondDocument("General Release.docx", "General Release", "1.0", "ACTIVE");
		Thread.sleep(3000);
		documentsPage.performRighClickOnSecondDocument();
		Thread.sleep(3000);
		documentsPage.checkIfRightClickOnSecondDocumentIsWorking();
		Thread.sleep(2000);
		documentsPage.deleteDocument();
		Thread.sleep(3000);
		documentsPage.verifySecondDocumentIsDeleted();
		Thread.sleep(3000);
		documentsPage.chnageCaseStatusButton.click();
		Thread.sleep(10000);
		driver.switchTo().frame(cases.chnageCaseStausFrameOne);
		Thread.sleep(2000);
		driver.switchTo().frame(cases.chnageCaseStatusFrameTwo);
		Thread.sleep(2000);
		cases.deleteCase();
		driver.switchTo().defaultContent();
		cases.changeCaseStatusAproved();
		Thread.sleep(3000);
		ArkCaseAuthentication.logOut(driver);

	}

	@Test
	public void createNewCaseAddCorrespondenceDownloadTheCorrespondence() throws InterruptedException, IOException {

		ArkCaseAuthentication.logIn(TestsPoperties.getSupervisorUserUsername(),
				TestsPoperties.getSupervisorUserPassword(), driver, TestsPoperties.getBaseURL());
		casePom.newCase();
		Thread.sleep(20000);
		driver.switchTo().frame(cases.frameOne);
		driver.switchTo().frame(cases.frameTwo);
		casePom.vrifyGeneralInformationTabName();
		casePom.caseTitleInput("Add Delete General Release");
		casePom.verifyCaseTypeTitle();
		casePom.caseTypeInputClick();
		Thread.sleep(2000);
		casePom.caseTypeBackgroundInvestigation();
		Thread.sleep(2000);
		casePom.nextButton.click();
		Thread.sleep(3000);
		casePom.verifyInitiatorTab();
		casePom.initiatorTitle.click();
		Thread.sleep(2000);
		casePom.clickInitiatorMr();
		Thread.sleep(2000);
		casePom.initiatorFirstName("Milan");
		Thread.sleep(2000);
		casePom.initiatorLastName("Jovanovski");
		Thread.sleep(4000);
		casePom.participantnsTab.click();
		Thread.sleep(2000);
		casePom.selectParticipantTypeClick();
		Thread.sleep(2000);
		casePom.selectparticipantOwner();
		Thread.sleep(2000);
		casePom.selectParticipantClick();
		Thread.sleep(3000);
		casePom.searchForUsers();
		Thread.sleep(3000);
		casePom.searchedName();
		casePom.addSearchedNameClick();
		Thread.sleep(2000);
		casePom.submit.click();
		Thread.sleep(10000);
		driver.switchTo().defaultContent();
		Thread.sleep(5000);
		cases.caseDocuments.click();
		Thread.sleep(3000);
		cases.refreshPage.click();
		Thread.sleep(4000);
		documentsPage.performRightClickOnRoot();
		Thread.sleep(4000);
		documentsPage.checkIfRightClickOnRootIsWorking();
		Thread.sleep(3000);
		documentsPage.verifyNewCorrespondeneName();
		Thread.sleep(2000);
		documentsPage.NewCorrespondence.click();
		Thread.sleep(3000);
		documentsPage.verifyCorrespondenceMenu();
		Thread.sleep(2000);
		documentsPage.verifyGeneralRelease();
		documentsPage.correspondenceGeneralRelease.click();
		Thread.sleep(4000);
		documentsPage.verifyIfSecondRowDocumentIsPresent();
		documentsPage.verifySecondDocument("General Release.docx", "General Release", "1.0", "ACTIVE");
		Thread.sleep(3000);
		documentsPage.performRighClickOnSecondDocument();
		Thread.sleep(3000);
		documentsPage.checkIfRightClickOnSecondDocumentIsWorking();
		Thread.sleep(2000);
		documentsPage.downloadDocument();
		Thread.sleep(6000);
		check.checkIfFileIsDownloaded("General Release");
		Thread.sleep(3000);
		documentsPage.chnageCaseStatusButton.click();
		Thread.sleep(10000);
		driver.switchTo().frame(cases.chnageCaseStausFrameOne);
		Thread.sleep(2000);
		driver.switchTo().frame(cases.chnageCaseStatusFrameTwo);
		Thread.sleep(2000);
		cases.deleteCase();
		driver.switchTo().defaultContent();
		cases.changeCaseStatusAproved();
		Thread.sleep(3000);
		ArkCaseAuthentication.logOut(driver);

	}
	
	
	
	@Test
	public void createNewCaseAddCorrespondenceDeclareASRecord() throws InterruptedException, IOException {

		ArkCaseAuthentication.logIn(TestsPoperties.getSupervisorUserUsername(),
				TestsPoperties.getSupervisorUserPassword(), driver, TestsPoperties.getBaseURL());
		casePom.newCase();
		Thread.sleep(20000);
		driver.switchTo().frame(cases.frameOne);
		driver.switchTo().frame(cases.frameTwo);
		casePom.vrifyGeneralInformationTabName();
		casePom.caseTitleInput("General Release record");
		casePom.verifyCaseTypeTitle();
		casePom.caseTypeInputClick();
		Thread.sleep(2000);
		casePom.caseTypeBackgroundInvestigation();
		Thread.sleep(2000);
		casePom.nextButton.click();
		Thread.sleep(3000);
		casePom.verifyInitiatorTab();
		casePom.initiatorTitle.click();
		Thread.sleep(2000);
		casePom.clickInitiatorMr();
		Thread.sleep(2000);
		casePom.initiatorFirstName("Milan");
		Thread.sleep(2000);
		casePom.initiatorLastName("Jovanovski");
		Thread.sleep(4000);
		casePom.participantnsTab.click();
		Thread.sleep(2000);
		casePom.selectParticipantTypeClick();
		Thread.sleep(2000);
		casePom.selectparticipantOwner();
		Thread.sleep(2000);
		casePom.selectParticipantClick();
		Thread.sleep(3000);
		casePom.searchForUsers();
		Thread.sleep(3000);
		casePom.searchedName();
		casePom.addSearchedNameClick();
		Thread.sleep(2000);
		casePom.submit.click();
		Thread.sleep(10000);
		driver.switchTo().defaultContent();
		Thread.sleep(5000);
		cases.caseDocuments.click();
		Thread.sleep(3000);
		cases.refreshPage.click();
		Thread.sleep(4000);
		documentsPage.performRightClickOnRoot();
		Thread.sleep(4000);
		documentsPage.checkIfRightClickOnRootIsWorking();
		Thread.sleep(3000);
		documentsPage.verifyNewCorrespondeneName();
		Thread.sleep(2000);
		documentsPage.NewCorrespondence.click();
		Thread.sleep(3000);
		documentsPage.verifyCorrespondenceMenu();
		Thread.sleep(2000);
		documentsPage.verifyGeneralRelease();
		documentsPage.correspondenceGeneralRelease.click();
		Thread.sleep(4000);
		documentsPage.verifyIfSecondRowDocumentIsPresent();
		documentsPage.verifySecondDocument("General Release.docx", "General Release", "1.0", "ACTIVE");
		Thread.sleep(3000);
		documentsPage.performRighClickOnSecondDocument();
		Thread.sleep(3000);
		documentsPage.checkIfRightClickOnSecondDocumentIsWorking();
		Thread.sleep(2000);
		documentsPage.clickDeclareAsRecordDocument();
		Thread.sleep(6000);
	    documentsPage.verifyFirstDocument("General Release.docx", "General Release","Samuel Supervisor", "1.0", "RECORD");
		Thread.sleep(3000);
		documentsPage.chnageCaseStatusButton.click();
		Thread.sleep(10000);
		driver.switchTo().frame(cases.chnageCaseStausFrameOne);
		Thread.sleep(2000);
		driver.switchTo().frame(cases.chnageCaseStatusFrameTwo);
		Thread.sleep(2000);
		cases.deleteCase();
		driver.switchTo().defaultContent();
		cases.changeCaseStatusAproved();
		Thread.sleep(3000);
		ArkCaseAuthentication.logOut(driver);

	}
	
	
	@Test
	public void createNewCaseAddCorrespondenceEditWithWord() throws InterruptedException, IOException, AWTException {

		ArkCaseAuthentication.logIn(TestsPoperties.getSupervisorUserUsername(),
				TestsPoperties.getSupervisorUserPassword(), driver, TestsPoperties.getBaseURL());
		casePom.newCase();
		Thread.sleep(20000);
		driver.switchTo().frame(cases.frameOne);
		driver.switchTo().frame(cases.frameTwo);
		casePom.vrifyGeneralInformationTabName();
		casePom.caseTitleInput("Correspondence edit with word");
		casePom.verifyCaseTypeTitle();
		casePom.caseTypeInputClick();
		Thread.sleep(2000);
		casePom.caseTypeBackgroundInvestigation();
		Thread.sleep(2000);
		casePom.nextButton.click();
		Thread.sleep(3000);
		casePom.verifyInitiatorTab();
		casePom.initiatorTitle.click();
		Thread.sleep(2000);
		casePom.clickInitiatorMr();
		Thread.sleep(2000);
		casePom.initiatorFirstName("Milan");
		Thread.sleep(2000);
		casePom.initiatorLastName("Jovanovski");
		Thread.sleep(4000);
		casePom.participantnsTab.click();
		Thread.sleep(2000);
		casePom.selectParticipantTypeClick();
		Thread.sleep(2000);
		casePom.selectparticipantOwner();
		Thread.sleep(2000);
		casePom.selectParticipantClick();
		Thread.sleep(3000);
		casePom.searchForUsers();
		Thread.sleep(3000);
		casePom.searchedName();
		casePom.addSearchedNameClick();
		Thread.sleep(2000);
		casePom.submit.click();
		Thread.sleep(10000);
		driver.switchTo().defaultContent();
		Thread.sleep(5000);
		cases.caseDocuments.click();
		Thread.sleep(3000);
		cases.refreshPage.click();
		Thread.sleep(4000);
		documentsPage.performRightClickOnRoot();
		Thread.sleep(4000);
		documentsPage.checkIfRightClickOnRootIsWorking();
		Thread.sleep(3000);
		documentsPage.verifyNewCorrespondeneName();
		Thread.sleep(2000);
		documentsPage.NewCorrespondence.click();
		Thread.sleep(3000);
		documentsPage.verifyCorrespondenceMenu();
		Thread.sleep(2000);
		documentsPage.verifyGeneralRelease();
		documentsPage.correspondenceGeneralRelease.click();
		Thread.sleep(4000);
		documentsPage.verifyIfSecondRowDocumentIsPresent();
		documentsPage.verifySecondDocument("General Release.docx", "General Release", "1.0", "ACTIVE");
		Thread.sleep(3000);
		documentsPage.performRighClickOnSecondDocument();
		Thread.sleep(3000);
		documentsPage.checkIfRightClickOnSecondDocumentIsWorking();
		Thread.sleep(3000);
		documentsPage.editWithWordClick();
		Thread.sleep(5000);
		ArkCaseTestUtils.presEnter();
		Thread.sleep(3000);
		ArkCaseTestUtils.shiftLeftAndPressEnter();
		Thread.sleep(4000);
		documentsPage.refreshTableButton.click();
		Thread.sleep(4000);
		documentsPage.verifyLockedDocumentAfterEditWithWordClick();
		Thread.sleep(8000);
		ArkCaseTestUtils.uploadDocx();
		Thread.sleep(4000);
		ArkCaseTestUtils.saveWordDocument();
		Thread.sleep(3000);
		ArkCaseTestUtils.closeWordDocument();
		Thread.sleep(6000);
		documentsPage.refreshTableButton.click();
		Thread.sleep(6000);
		documentsPage.verifyModifiedDocument("General Release.docx", "General Release", "Samuel Supervisor", "2.0", "ACTIVE");
		documentsPage.verifyUnlockedDocumentAfterUplaodNewVersion();
		documentsPage.chnageCaseStatusButton.click();
		Thread.sleep(10000);
		driver.switchTo().frame(cases.chnageCaseStausFrameOne);
		Thread.sleep(2000);
		driver.switchTo().frame(cases.chnageCaseStatusFrameTwo);
		Thread.sleep(2000);
		cases.deleteCase();
		driver.switchTo().defaultContent();
		cases.changeCaseStatusAproved();
		Thread.sleep(3000);
		ArkCaseAuthentication.logOut(driver);

	}
	
	@Test
	public void createNewCaseAddCorrespondenceCancelEditWithWord() throws InterruptedException, IOException, AWTException {

		ArkCaseAuthentication.logIn(TestsPoperties.getSupervisorUserUsername(),
				TestsPoperties.getSupervisorUserPassword(), driver, TestsPoperties.getBaseURL());
		casePom.newCase();
		Thread.sleep(20000);
		driver.switchTo().frame(cases.frameOne);
		driver.switchTo().frame(cases.frameTwo);
		casePom.vrifyGeneralInformationTabName();
		casePom.caseTitleInput("Correspondence cancel edit with word");
		casePom.verifyCaseTypeTitle();
		casePom.caseTypeInputClick();
		Thread.sleep(2000);
		casePom.caseTypeBackgroundInvestigation();
		Thread.sleep(2000);
		casePom.nextButton.click();
		Thread.sleep(3000);
		casePom.verifyInitiatorTab();
		casePom.initiatorTitle.click();
		Thread.sleep(2000);
		casePom.clickInitiatorMr();
		Thread.sleep(2000);
		casePom.initiatorFirstName("Milan");
		Thread.sleep(2000);
		casePom.initiatorLastName("Jovanovski");
		Thread.sleep(4000);
		casePom.participantnsTab.click();
		Thread.sleep(2000);
		casePom.selectParticipantTypeClick();
		Thread.sleep(2000);
		casePom.selectparticipantOwner();
		Thread.sleep(2000);
		casePom.selectParticipantClick();
		Thread.sleep(3000);
		casePom.searchForUsers();
		Thread.sleep(3000);
		casePom.searchedName();
		casePom.addSearchedNameClick();
		Thread.sleep(2000);
		casePom.submit.click();
		Thread.sleep(10000);
		driver.switchTo().defaultContent();
		Thread.sleep(5000);
		cases.caseDocuments.click();
		Thread.sleep(3000);
		cases.refreshPage.click();
		Thread.sleep(4000);
		documentsPage.performRightClickOnRoot();
		Thread.sleep(4000);
		documentsPage.checkIfRightClickOnRootIsWorking();
		Thread.sleep(3000);
		documentsPage.verifyNewCorrespondeneName();
		Thread.sleep(2000);
		documentsPage.NewCorrespondence.click();
		Thread.sleep(3000);
		documentsPage.verifyCorrespondenceMenu();
		Thread.sleep(2000);
		documentsPage.verifyGeneralRelease();
		documentsPage.correspondenceGeneralRelease.click();
		Thread.sleep(4000);
		documentsPage.verifyIfSecondRowDocumentIsPresent();
		documentsPage.verifySecondDocument("General Release.docx", "General Release", "1.0", "ACTIVE");
		Thread.sleep(3000);
		documentsPage.performRighClickOnSecondDocument();
		Thread.sleep(3000);
		documentsPage.checkIfRightClickOnSecondDocumentIsWorking();
		Thread.sleep(3000);
		documentsPage.editWithWordClick();
		Thread.sleep(5000);
		ArkCaseTestUtils.presEnter();
		Thread.sleep(3000);
		ArkCaseTestUtils.presEnter();
		Thread.sleep(4000);
		documentsPage.refreshTableButton.click();
		Thread.sleep(4000);
		documentsPage.verifyUnlockedDocumentAfterCancelEditWithWord();
		Thread.sleep(3000);
		documentsPage.chnageCaseStatusButton.click();
		Thread.sleep(10000);
		driver.switchTo().frame(cases.chnageCaseStausFrameOne);
		Thread.sleep(2000);
		driver.switchTo().frame(cases.chnageCaseStatusFrameTwo);
		Thread.sleep(2000);
		cases.deleteCase();
		driver.switchTo().defaultContent();
		cases.changeCaseStatusAproved();
		Thread.sleep(3000);
		ArkCaseAuthentication.logOut(driver);

	}
	@Test
	public void createNewCaseAddCorrespondenceRenameTheCorrespondence() throws InterruptedException, IOException, AWTException {

		ArkCaseAuthentication.logIn(TestsPoperties.getSupervisorUserUsername(),
				TestsPoperties.getSupervisorUserPassword(), driver, TestsPoperties.getBaseURL());
		casePom.newCase();
		Thread.sleep(20000);
		driver.switchTo().frame(cases.frameOne);
		driver.switchTo().frame(cases.frameTwo);
		casePom.vrifyGeneralInformationTabName();
		casePom.caseTitleInput("Correspondence cancel edit with word");
		casePom.verifyCaseTypeTitle();
		casePom.caseTypeInputClick();
		Thread.sleep(2000);
		casePom.caseTypeBackgroundInvestigation();
		Thread.sleep(2000);
		casePom.nextButton.click();
		Thread.sleep(3000);
		casePom.verifyInitiatorTab();
		casePom.initiatorTitle.click();
		Thread.sleep(2000);
		casePom.clickInitiatorMr();
		Thread.sleep(2000);
		casePom.initiatorFirstName("Milan");
		Thread.sleep(2000);
		casePom.initiatorLastName("Jovanovski");
		Thread.sleep(4000);
		casePom.participantnsTab.click();
		Thread.sleep(2000);
		casePom.selectParticipantTypeClick();
		Thread.sleep(2000);
		casePom.selectparticipantOwner();
		Thread.sleep(2000);
		casePom.selectParticipantClick();
		Thread.sleep(3000);
		casePom.searchForUsers();
		Thread.sleep(3000);
		casePom.searchedName();
		casePom.addSearchedNameClick();
		Thread.sleep(2000);
		casePom.submit.click();
		Thread.sleep(10000);
		driver.switchTo().defaultContent();
		Thread.sleep(5000);
		cases.caseDocuments.click();
		Thread.sleep(3000);
		cases.refreshPage.click();
		Thread.sleep(4000);
		documentsPage.performRightClickOnRoot();
		Thread.sleep(4000);
		documentsPage.checkIfRightClickOnRootIsWorking();
		Thread.sleep(3000);
		documentsPage.verifyNewCorrespondeneName();
		Thread.sleep(2000);
		documentsPage.NewCorrespondence.click();
		Thread.sleep(3000);
		documentsPage.verifyCorrespondenceMenu();
		Thread.sleep(2000);
		documentsPage.verifyGeneralRelease();
		documentsPage.correspondenceGeneralRelease.click();
		Thread.sleep(4000);
		documentsPage.verifyIfSecondRowDocumentIsPresent();
		documentsPage.verifySecondDocument("General Release.docx", "General Release", "1.0", "ACTIVE");
		Thread.sleep(3000);
		documentsPage.performRighClickOnSecondDocument();
		Thread.sleep(3000);
		documentsPage.checkIfRightClickOnSecondDocumentIsWorking();
		Thread.sleep(3000);
		documentsPage.clicRenameDocument();
		Thread.sleep(3000);
		documentsPage.renameSecondDocument("correspondence");
		Thread.sleep(2000);
		documentsPage.documentsTableTitle.click();
		Thread.sleep(3000);
		documentsPage.verifySecondDocument("correspondence", "General Release", "1.0", "ACTIVE");
		documentsPage.chnageCaseStatusButton.click();
		Thread.sleep(10000);
		driver.switchTo().frame(cases.chnageCaseStausFrameOne);
		Thread.sleep(2000);
		driver.switchTo().frame(cases.chnageCaseStatusFrameTwo);
		Thread.sleep(2000);
		cases.deleteCase();
		driver.switchTo().defaultContent();
		cases.changeCaseStatusAproved();
		Thread.sleep(3000);
		ArkCaseAuthentication.logOut(driver);
	
	
	}
	@Test
	public void createNewCaseAddCorrespondenceCheckoutCancelEditing() throws InterruptedException, IOException, AWTException {

		ArkCaseAuthentication.logIn(TestsPoperties.getSupervisorUserUsername(),
				TestsPoperties.getSupervisorUserPassword(), driver, TestsPoperties.getBaseURL());
		casePom.newCase();
		Thread.sleep(20000);
		driver.switchTo().frame(cases.frameOne);
		driver.switchTo().frame(cases.frameTwo);
		casePom.vrifyGeneralInformationTabName();
		casePom.caseTitleInput("Correspondence cancel edit with word");
		casePom.verifyCaseTypeTitle();
		casePom.caseTypeInputClick();
		Thread.sleep(2000);
		casePom.caseTypeBackgroundInvestigation();
		Thread.sleep(2000);
		casePom.nextButton.click();
		Thread.sleep(3000);
		casePom.verifyInitiatorTab();
		casePom.initiatorTitle.click();
		Thread.sleep(2000);
		casePom.clickInitiatorMr();
		Thread.sleep(2000);
		casePom.initiatorFirstName("Milan");
		Thread.sleep(2000);
		casePom.initiatorLastName("Jovanovski");
		Thread.sleep(4000);
		casePom.participantnsTab.click();
		Thread.sleep(2000);
		casePom.selectParticipantTypeClick();
		Thread.sleep(2000);
		casePom.selectparticipantOwner();
		Thread.sleep(2000);
		casePom.selectParticipantClick();
		Thread.sleep(3000);
		casePom.searchForUsers();
		Thread.sleep(3000);
		casePom.searchedName();
		casePom.addSearchedNameClick();
		Thread.sleep(2000);
		casePom.submit.click();
		Thread.sleep(10000);
		driver.switchTo().defaultContent();
		Thread.sleep(5000);
		cases.caseDocuments.click();
		Thread.sleep(3000);
		cases.refreshPage.click();
		Thread.sleep(4000);
		documentsPage.performRightClickOnRoot();
		Thread.sleep(4000);
		documentsPage.checkIfRightClickOnRootIsWorking();
		Thread.sleep(3000);
		documentsPage.verifyNewCorrespondeneName();
		Thread.sleep(2000);
		documentsPage.NewCorrespondence.click();
		Thread.sleep(3000);
		documentsPage.verifyCorrespondenceMenu();
		Thread.sleep(2000);
		documentsPage.verifyGeneralRelease();
		documentsPage.correspondenceGeneralRelease.click();
		Thread.sleep(4000);
		documentsPage.verifyIfSecondRowDocumentIsPresent();
		documentsPage.verifySecondDocument("General Release.docx", "General Release", "1.0", "ACTIVE");
		Thread.sleep(3000);
		documentsPage.performRighClickOnSecondDocument();
		Thread.sleep(3000);
		documentsPage.checkIfRightClickOnSecondDocumentIsWorking();
		Thread.sleep(3000);
		documentsPage.checkoutDocument();
		Thread.sleep(5000);
		check.checkIfFileIsDownloaded("General Release");
		documentsPage.verifyLockedDocumentAfterCheckout();
		Thread.sleep(3000);
		documentsPage.clickCancelEditing();
		Thread.sleep(5000);
		documentsPage.verifyUnlockedDocumentAfterCancelEditing();
		Thread.sleep(3000);
		documentsPage.chnageCaseStatusButton.click();
		Thread.sleep(10000);
		driver.switchTo().frame(cases.chnageCaseStausFrameOne);
		Thread.sleep(2000);
		driver.switchTo().frame(cases.chnageCaseStatusFrameTwo);
		Thread.sleep(2000);
		cases.deleteCase();
		driver.switchTo().defaultContent();
		cases.changeCaseStatusAproved();
		Thread.sleep(3000);
		ArkCaseAuthentication.logOut(driver);

	}


}
