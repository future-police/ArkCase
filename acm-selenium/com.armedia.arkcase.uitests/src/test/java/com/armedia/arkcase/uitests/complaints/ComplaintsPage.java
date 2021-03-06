package com.armedia.arkcase.uitests.complaints;

import com.armedia.arkcase.uitests.base.ArkCaseTestBase;
import com.armedia.arkcase.uitests.base.WaitHelper;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.testng.asserts.SoftAssert;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ComplaintsPage extends ArkCaseTestBase
{

    @FindBy(how = How.XPATH, using = "/html/body/div[2]/div/div[2]/section/div/div/section[1]/div[4]/ul/li[7]/a")
    public WebElement documentLink;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[2]/div/div/div[2]/div/button[3]")
    public WebElement refreshPageBtn;
    @FindBy(how = How.XPATH, using = "/html/body/div[2]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[1]/div/div/button")
    public WebElement detailsSaveBtn;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/div/div[1]/h3/span")
    WebElement complaintsPageTitle;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/div/div[1]/h3/span")
    WebElement complaintsTitle;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[1]/div/div/div/div[1]/div[1]/h4/a")
    WebElement complaintTitle;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[4]/ul/li[5]")
    WebElement documentsLink;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[2]/div/div/div[2]/div/a[1]")
    WebElement newComplaintBtn;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[2]/div/div/div[2]/div/a[2]")
    WebElement closeComplaintBtn;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[2]/div/div/div[2]/div/button[2]")
    WebElement subscribeBtn;
    // Notes
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[4]/ul/li[7]/a")
    WebElement notesLink;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/core-notes/div/div[1]/div/span")
    WebElement noteTableTitle;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/core-notes/div/div[2]/div/div[1]/div[1]/div[2]")
    WebElement emptyNotesTable;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/core-notes/div/div[1]/div/div/button")
    WebElement addNoteBtn;
    @FindBy(how = How.XPATH, using = "/html/body/div[5]/div/div")
    WebElement addNotePopUp;
    @FindBy(how = How.XPATH, using = "/html/body/div[5]/div/div/div[1]/h3[1]/span")
    WebElement addNoteTitle;
    @FindBy(how = How.XPATH, using = "/html/body/div[5]/div/div/div[2]/form/div/textarea")
    WebElement addNoteTextArea;
    @FindBy(how = How.XPATH, using = "/html/body/div[5]/div/div/div[3]/button[2]")
    WebElement addNoteSaveBtn;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/core-notes/div/div[2]/div/div[1]/div[1]/div[2]/div/div/div/div[1]/div")
    WebElement noteName;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/core-notes/div/div[2]/div/div[1]/div[1]/div[2]/div/div/div/div[2]/div")
    WebElement noteCreatedDate;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/core-notes/div/div[2]/div/div[1]/div[1]/div[2]/div/div/div/div[3]/div")
    WebElement noteAuthor;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/div/div[2]/div/object-tree/section/header/div[3]/button")
    WebElement sortBtn;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/div/div[2]/div/object-tree/section/header/div[3]/ul/li[4]")
    WebElement sortDateDesc;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/div/div[2]/div/object-tree/section/div/div/div/ul/li[1]/span/span[3]")
    WebElement firstComplaint;
    // documents section
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[1]/div/span")
    WebElement documentsTableTitle;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[2]/doc-tree/table/tbody/tr/td[3]/span/span[3]")
    WebElement root;
    @FindBy(how = How.XPATH, using = "/html/body/ul/li[2]")
    WebElement newDocument;
    @FindBy(how = How.XPATH, using = ".//li[@data-command='file/Other']")
    WebElement newDocumentOther;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[2]/doc-tree/table/tbody/tr[3]/td[3]/span")
    WebElement secondRowTitle;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[2]/doc-tree/table/tbody/tr[3]/td[4]")
    WebElement secondRowType;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[2]/doc-tree/table/tbody/tr[3]/td[5]")
    WebElement secondRowCreated;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[2]/doc-tree/table/tbody/tr[3]/td[6]]")
    WebElement secondRowModified;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[2]/doc-tree/table/tbody/tr[3]/td[7]")
    WebElement secondRowAuthor;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[2]/doc-tree/table/tbody/tr[3]/td[8]/span")
    WebElement secondRowVersion;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[2]/doc-tree/table/tbody/tr[3]/td[9]")
    WebElement secondRowStatus;
    // Details
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[4]/ul/li[2]")
    WebElement detailsLink;
    @FindBy(how = How.XPATH, using = "/html/body/div[2]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[2]/div[2]/div[2]/div[9]/button[2]")
    WebElement addPictureBtn;
    @FindBy(how = How.XPATH, using = "/html/body/div[2]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[1]/div/span")
    WebElement detailsTitle;
    @FindBy(how = How.XPATH, using = "/html/body/div[2]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[2]/div[2]/div[3]/div[4]")
    WebElement detailsTextArea;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div[1]/div/div/div[1]/h4")
    WebElement insertImagePopUpTitle;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div[1]/div/div/div[2]/div[1]/input")
    WebElement browseImageBtn;
    @FindBy(how = How.XPATH, using = "/html/body/div[2]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[2]/div[2]/div[3]/div[1]/div/div[1]")
    WebElement insertedImage;
    // Tasks
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[4]/ul/li[8]/a")
    WebElement tasksLink;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[1]/div/span")
    WebElement tasksTableTitle;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[1]/div/div/button")
    WebElement addTaskBtn;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[2]/div/div[1]/div[1]/div[2]")
    WebElement emptyTaskTable;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[1]/nav/ul/li[2]/a")
    WebElement moduleComplaints;
    @FindBy(how = How.XPATH, using = "/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[1]/div/div/div[2]/div[1]/div[1]/h4/a")
    WebElement complaintsTitleInTasks;

    public ComplaintsPage clickDetailsLink()
    {
        detailsLink.click();
        return this;
    }

    public ComplaintsPage verifyDetailsTitle()
    {
        Assert.assertEquals("Details header title is wrong", "Details", detailsTitle.getText());
        return this;
    }

    public ComplaintsPage clickInsertPictureBtn()
    {
        addPictureBtn.click();
        return this;
    }

    public ComplaintsPage verifyInsertImagePopUp()
    {
        int i = driver.findElements(By.xpath("/html/body/div[1]/div[1]/div/div")).size();
        Assert.assertTrue("Insert image popup is not displayed", i != 0);
        return this;
    }

    public ComplaintsPage clickBrowseButton()
    {
        WaitHelper.ClickElementAtPoint(browseImageBtn, driver);
        return this;
    }

    public ComplaintsPage verifyUploadedImage()
    {
        int i = driver
                .findElements(By
                        .xpath("/html/body/div[2]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[2]/div[2]/div[3]/div[1]/div/div[1]"))
                .size();
        Assert.assertTrue("Image is not uploaded", i != 0);
        return this;
    }

    public ComplaintsPage clickNoteLink()
    {
        notesLink.click();
        return this;
    }

    public ComplaintsPage verifyNotesTableTitle()
    {

        Assert.assertEquals("Notes table title is wrong", "Notes", noteTableTitle.getText());
        return this;

    }

    public ComplaintsPage clickAddNoteButton()
    {

        addNoteBtn.click();
        return this;
    }

    public ComplaintsPage verifyAddNotePopUp()
    {

        int i = driver.findElements(By.xpath("/html/body/div[5]/div/div")).size();
        Assert.assertTrue("After add note button is clicked, add note popup is not displayed", i != 0);
        return this;

    }

    public ComplaintsPage verifyAddNotePopUpTitle()
    {
        Assert.assertEquals("Add  note popup title is wrong", "Add note", addNoteTitle.getText());
        return this;
    }

    public ComplaintsPage setNoteTextArea(String note) throws InterruptedException
    {
        addNoteTextArea.click();
        Thread.sleep(2000);
        addNoteTextArea.sendKeys(note);
        return this;
    }

    public ComplaintsPage clickSaveButton()
    {
        addNoteSaveBtn.click();
        return this;
    }

    public ComplaintsPage verifyAddNotePopUpDisapierd()
    {
        int i = driver.findElements(By.xpath("/html/body/div[5]/div/div")).size();
        Assert.assertTrue("After add note save button is clicked, add note popup is still displayed", i == 0);
        return this;

    }

    public ComplaintsPage verifyIfNoteIsCreated()
    {

        int i = driver
                .findElements(By
                        .xpath("/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/core-notes/div/div[2]/div/div[1]/div[1]/div[2]/div/div/div/div[1]/div"))
                .size();
        Assert.assertTrue("Note is note created", i != 0);
        return this;

    }

    public ComplaintsPage verifyCreatedNote(String name, String author)
    {

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String createdDate = formatter.format(date);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(noteName.getText(), name, "Created note name is wrong");
        softAssert.assertEquals(noteCreatedDate.getText(), createdDate, "created note date is wrong");
        softAssert.assertEquals(noteAuthor.getText(), author, "Created note author is wrong");
        softAssert.assertAll();
        return this;

    }

    public ComplaintsPage clickSortBtn()
    {
        sortBtn.click();
        return this;
    }

    public ComplaintsPage sortDateDesc()
    {
        Assert.assertEquals("Sort by crated date desc name is wrong", "Sort Created Date Desc", sortDateDesc.getText());
        sortDateDesc.click();
        return this;
    }

    public ComplaintsPage clickFirstComplaint()
    {
        firstComplaint.click();
        return this;
    }

    public ComplaintsPage verifyComplaintTitle(String title)
    {
        Assert.assertEquals("Complaint title is wrong", title, complaintTitle.getText());
        return this;
    }

    public ComplaintsPage clickDocumentsLink()
    {
        documentsLink.click();
        return this;
    }

    public void verifyDocumenstTableTitle()
    {
        Assert.assertEquals("Documents table title is wrong", "Documents", documentsTableTitle.getText());
    }

    public ComplaintsPage performRightClickOnRoot()
    {

        Actions actions = new Actions(driver);
        Action action = actions.contextClick(root).build();
        action.perform();
        return this;
    }

    public void checkIfRightClickOnRootIsWorking()
    {

        int i = driver.findElements(By.xpath("/html/body/ul")).size();
        Assert.assertTrue("Right Click on root is not working", i > 0);

    }

    public ComplaintsPage clickNewDocument()
    {
        Assert.assertEquals("New document label name is wrong", "New Document", newDocument.getText());
        newDocument.click();
        return this;
    }

    public void verifyNewDocumentMenu()
    {
        int i = driver.findElements(By.xpath("/html/body/ul/li[2]/ul")).size();
        Assert.assertTrue("New Document menu is not displayed", i != 0);
    }

    public void clickNewDocumentOther()
    {
        Assert.assertEquals("New document other label name is wrong", "Other", newDocumentOther.getText());
        newDocumentOther.click();
    }

    public void verifyIfDocumentIsCreated()
    {
        int i = driver
                .findElements(By
                        .xpath("/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[2]/doc-tree/table/tbody/tr[3]"))
                .size();
        Assert.assertTrue("New document is not created", i != 0);
    }

    public void verifyCreatedDocument(String title, String type, String author, String version, String status)
    {

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String createdDate = formatter.format(date);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(secondRowTitle.getText(), title, "Created document title is wrong");
        softAssert.assertEquals(secondRowType.getText(), type, "Created document type is wrong");
        softAssert.assertEquals(secondRowCreated.getText(), createdDate, "Created document date is wrong");
        softAssert.assertEquals(secondRowAuthor.getText(), author, "Created document author is wrong");
        softAssert.assertEquals(secondRowVersion.getText(), version, "Created document version is wrong");
        softAssert.assertEquals(secondRowStatus.getText(), status, "Created document status is wrong");
        softAssert.assertAll();

    }

    public ComplaintsPage clickTaskLink()
    {
        tasksLink.click();
        return this;
    }

    public void verifyTaskTableTitle()
    {
        Assert.assertEquals("Task table title is wrong", "Tasks", tasksTableTitle.getText());
    }

    public ComplaintsPage clickAddTaskButton()
    {
        addTaskBtn.click();
        return this;
    }

    public ComplaintsPage clickModuleComplaints()
    {
        Assert.assertEquals("Complaints module lable name is wrong", "Complaints", moduleComplaints.getText());
        moduleComplaints.click();
        return this;
    }

    public void verifyIfTaskIsCreated()
    {
        int i = driver
                .findElements(By
                        .xpath("/html/body/div[1]/div/div[2]/section/div/div/section[1]/div[3]/div/div/div/div[2]/div/div[1]/div[1]/div[2]/div/div/div/div[1]"))
                .size();
        Assert.assertTrue("Created task is not shown in tasks table", i != 0);
    }

    public void verifyComplaintTitleInTasksPage(String title)
    {
        Assert.assertEquals("Complaint title in tasks page is wrong", title, complaintsTitleInTasks.getText());
    }

    public ComplaintsPage clickComplaintTitleInTasksPage()
    {
        complaintsTitleInTasks.click();
        return this;
    }

    public ComplaintsPage clickRefreshButton()
    {
        refreshPageBtn.click();
        return this;
    }

}
