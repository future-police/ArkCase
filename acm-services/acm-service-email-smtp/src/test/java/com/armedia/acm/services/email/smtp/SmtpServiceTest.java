package com.armedia.acm.services.email.smtp;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.armedia.acm.data.AuditPropertyEntityAdapter;
import com.armedia.acm.files.propertymanager.PropertyFileManager;
import com.armedia.acm.muletools.mulecontextmanager.MuleContextManager;
import com.armedia.acm.plugins.ecm.model.EcmFile;
import com.armedia.acm.plugins.ecm.model.EcmFileConstants;
import com.armedia.acm.plugins.ecm.service.EcmFileService;
import com.armedia.acm.services.authenticationtoken.dao.AuthenticationTokenDao;
import com.armedia.acm.services.authenticationtoken.model.AuthenticationToken;
import com.armedia.acm.services.authenticationtoken.model.AuthenticationTokenConstants;
import com.armedia.acm.services.authenticationtoken.service.AuthenticationTokenService;
import com.armedia.acm.services.email.model.EmailWithAttachmentsAndLinksDTO;
import com.armedia.acm.services.email.model.EmailWithAttachmentsDTO;
import com.armedia.acm.services.email.model.EmailWithEmbeddedLinksDTO;
import com.armedia.acm.services.email.model.EmailWithEmbeddedLinksResultDTO;
import com.armedia.acm.services.email.sender.model.EmailSenderConfigurationConstants;
import com.armedia.acm.services.users.model.AcmUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Lazo Lazarev a.k.a. Lazarius Borg @ zerogravity Jul 5, 2017
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SmtpServiceTest
{

    @InjectMocks
    private SmtpService service;

    @Mock
    private MuleContextManager mockMuleContextManager;

    @Mock
    private AuditPropertyEntityAdapter mockAuditPropertyEntityAdapter;

    @Mock
    private PropertyFileManager mockPropertyFileManager;

    @Mock
    private MuleException mockMuleException;

    @Mock
    private MuleMessage mockMuleMessage;

    @Mock
    private Authentication mockAuthentication;

    @Mock
    private AuthenticationTokenService mockAuthenticationTokenService;

    @Mock
    private AuthenticationTokenDao mockAuthenticationTokenDao;

    @Mock
    private AcmUser mockAcmUser;

    @Mock
    private EcmFileService mockEcmFileService;

    @Mock
    private InputStream mockInputStream;

    @Mock
    private EcmFile mockEcmFile;

    @Mock
    private File mockFile;

    @Mock
    private FileInputStream mockFileInputStream;

    @Mock
    private ApplicationEventPublisher mockApplicationEventPublisher;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        service.setFlow("vm://sendEmailViaSmtp.in");
        service.setEmailSenderPropertyFileLocation("dummy_location");
    }

    @Test
    public void testSendEmailWithEmbeddedLinks() throws Exception
    {
        // given
        final String email = "user_email";
        final String header = "header";
        final String baseUrl = "base_url";
        final String title = "title";
        final String footer = "footer";
        final long fileId = 1234;
        final String token = "token";
        final String note = header + "\\s*" + "<br/>" + baseUrl + fileId + "&acm_email_ticket=" + token + "<br/>" + "\\s*" + footer;

        List<String> addresses = new ArrayList<>();
        addresses.add(email);
        List<Long> fileIds = new ArrayList<>();
        fileIds.add(fileId);
        EmailWithEmbeddedLinksDTO inputDTO = new EmailWithEmbeddedLinksDTO();
        inputDTO.setTitle(title);
        inputDTO.setHeader(header);
        inputDTO.setEmailAddresses(addresses);
        inputDTO.setBaseUrl(baseUrl);
        inputDTO.setFileIds(fileIds);
        inputDTO.setFooter(footer);

        when(mockMuleContextManager.send(eq("vm://sendEmailViaSmtp.in"), matches(note), any(Map.class))).thenReturn(mockMuleMessage);

        setSendExpectations();
        when(mockPropertyFileManager.load("", EmailSenderConfigurationConstants.ENCRYPTION, null)).thenReturn("off");
        when(mockMuleMessage.getInboundProperty("sendEmailException")).thenReturn(null);

        when(mockAuthenticationTokenService.getUncachedTokenForAuthentication(mockAuthentication)).thenReturn(token);
        AuthenticationToken authenticationToken = new AuthenticationToken();
        authenticationToken.setKey(token);
        authenticationToken.setStatus(AuthenticationTokenConstants.ACTIVE);
        authenticationToken.setEmail(email);
        authenticationToken.setFileId(fileId);

        when(mockAuthenticationTokenDao.save(any(AuthenticationToken.class))).thenReturn(authenticationToken);

        when(mockAcmUser.getUserId()).thenReturn("ann-acm");

        // when
        List<EmailWithEmbeddedLinksResultDTO> results = service.sendEmailWithEmbeddedLinks(inputDTO, mockAuthentication, mockAcmUser);

        // then
        assertThat(results.size(), is(1));

        EmailWithEmbeddedLinksResultDTO resultDTO = results.get(0);
        assertThat(resultDTO.isState(), is(true));
        assertThat(resultDTO.getEmailAddress(), is(email));
        verify(mockApplicationEventPublisher).publishEvent(any(SmtpEventSentEvent.class));

    }

    @Test
    public void testSendEmailWithEmbeddedLinksSTARTTLS() throws Exception
    {
        // given
        final String email = "user_email";
        final String header = "header";
        final String baseUrl = "base_url";
        final String title = "title";
        final String footer = "footer";
        final long fileId = 1234;
        final String token = "token";
        final String note = header + "\\s*" + "<br/>" + baseUrl + fileId + "&acm_email_ticket=" + token + "<br/>" + "\\s*" + footer;

        List<String> addresses = new ArrayList<>();
        addresses.add(email);
        List<Long> fileIds = new ArrayList<>();
        fileIds.add(fileId);
        EmailWithEmbeddedLinksDTO inputDTO = new EmailWithEmbeddedLinksDTO();
        inputDTO.setTitle(title);
        inputDTO.setHeader(header);
        inputDTO.setEmailAddresses(addresses);
        inputDTO.setBaseUrl(baseUrl);
        inputDTO.setFileIds(fileIds);
        inputDTO.setFooter(footer);

        ArgumentCaptor<Map> messagePropsCaptor = ArgumentCaptor.forClass(Map.class);
        when(mockMuleContextManager.send(eq("vm://sendEmailViaSmtp.in"), matches(note), messagePropsCaptor.capture()))
                .thenReturn(mockMuleMessage);

        setSendExpectations();
        when(mockPropertyFileManager.load(anyString(), eq(EmailSenderConfigurationConstants.ENCRYPTION), isNull(String.class)))
                .thenReturn("starttls");
        when(mockMuleMessage.getInboundProperty("sendEmailException")).thenReturn(null);

        when(mockAuthenticationTokenService.getUncachedTokenForAuthentication(mockAuthentication)).thenReturn(token);
        AuthenticationToken authenticationToken = new AuthenticationToken();
        authenticationToken.setKey(token);
        authenticationToken.setStatus(AuthenticationTokenConstants.ACTIVE);
        authenticationToken.setEmail(email);
        authenticationToken.setFileId(fileId);

        when(mockAuthenticationTokenDao.save(any(AuthenticationToken.class))).thenReturn(authenticationToken);

        when(mockAcmUser.getUserId()).thenReturn("ann-acm");

        // when
        List<EmailWithEmbeddedLinksResultDTO> results = service.sendEmailWithEmbeddedLinks(inputDTO, mockAuthentication, mockAcmUser);

        // then
        assertThat(results.size(), is(1));

        EmailWithEmbeddedLinksResultDTO resultDTO = results.get(0);
        assertThat(resultDTO.isState(), is(true));
        assertThat(resultDTO.getEmailAddress(), is(email));
        verify(mockApplicationEventPublisher).publishEvent(any(SmtpEventSentEvent.class));
        assertThat(messagePropsCaptor.getValue().get("encryption"), is("starttls"));

    }

    @Test
    public void testSendEmailWithAttachments() throws Exception
    {
        // given
        final String email = "user_email";
        final String header = "header";
        final String body = "body";
        final String footer = "footer";
        final String note = header + "\\s*" + body + "\\s*" + footer;

        List<String> addresses = new ArrayList<>();
        addresses.add(email);
        EmailWithAttachmentsDTO inputDTO = new EmailWithAttachmentsDTO();
        inputDTO.setEmailAddresses(addresses);
        inputDTO.setHeader(header);
        inputDTO.setBody(body);
        inputDTO.setFooter(footer);

        List<Long> attachmentIds = new ArrayList<>();
        attachmentIds.add(new Long(999));
        inputDTO.setAttachmentIds(attachmentIds);

        List<String> filePaths = new ArrayList<>();
        Resource resource = new ClassPathResource("temp.zip");
        filePaths.add(resource.getFile().getAbsolutePath());
        inputDTO.setFilePaths(filePaths);

        ArgumentCaptor<Map> messagePropsCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> capturedNote = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Map> capturedAttachments = ArgumentCaptor.forClass(Map.class);
        when(mockMuleContextManager.send(eq("vm://sendEmailViaSmtp.in"), capturedNote.capture(), capturedAttachments.capture(),
                messagePropsCaptor.capture())).thenReturn(mockMuleMessage);

        setSendExpectations();
        when(mockPropertyFileManager.load("", EmailSenderConfigurationConstants.ENCRYPTION, null)).thenReturn("off");

        when(mockMuleMessage.getInboundProperty("sendEmailException")).thenReturn(null);

        ArgumentCaptor<byte[]> read = ArgumentCaptor.forClass(byte[].class);
        when(mockEcmFileService.downloadAsInputStream(attachmentIds.get(0))).thenReturn(mockInputStream);
        when(mockEcmFileService.findById(attachmentIds.get(0))).thenReturn(mockEcmFile);
        when(mockInputStream.read(read.capture(), eq(0), eq(16384))).thenReturn(-1);

        when(mockEcmFile.getFileName()).thenReturn("fileName");
        when(mockEcmFile.getFileActiveVersionNameExtension()).thenReturn(".extension");

        mockInputStream.close();

        when(mockAcmUser.getUserId()).thenReturn("ann-acm");
        when(mockEcmFile.getParentObjectId()).thenReturn(103L);
        when(mockEcmFile.getParentObjectType()).thenReturn("COMPLAINT");

        // expected calls to raise the file emailed event on the file itself - AFDP-3029
        when(mockEcmFile.getId()).thenReturn(attachmentIds.get(0));
        when(mockEcmFile.getObjectType()).thenReturn(EcmFileConstants.OBJECT_FILE_TYPE);

        whenNew(File.class).withArguments(filePaths.get(0)).thenReturn(mockFile);
        whenNew(FileInputStream.class).withArguments(mockFile).thenReturn(mockFileInputStream);
        when(mockFile.getName()).thenReturn("temp.zip");

        // when
        service.sendEmailWithAttachments(inputDTO, mockAuthentication, mockAcmUser);

        // then
        assertThat(Pattern.compile(note).matcher(capturedNote.getValue()).matches(), is(true));

        assertThat(capturedAttachments.getValue(), notNullValue());
        assertThat(capturedAttachments.getValue().size(), is(2));
        assertThat(capturedAttachments.getValue().get("fileName.extension"), notNullValue());
        assertThat(capturedAttachments.getValue().get("temp.zip"), notNullValue());
        mockApplicationEventPublisher.publishEvent(any(SmtpEventSentEvent.class));
    }

    @Test
    public void testSendEmailWithAttachmentsSTARTTLS() throws Exception
    {
        // given
        final String email = "user_email";
        final String header = "header";
        final String body = "body";
        final String footer = "footer";
        final String note = header + "\\s*" + body + "\\s*" + footer;

        List<String> addresses = new ArrayList<>();
        addresses.add(email);
        EmailWithAttachmentsDTO inputDTO = new EmailWithAttachmentsDTO();
        inputDTO.setEmailAddresses(addresses);
        inputDTO.setHeader(header);
        inputDTO.setBody(body);
        inputDTO.setFooter(footer);

        List<Long> attachmentIds = new ArrayList<>();
        attachmentIds.add(new Long(999));
        inputDTO.setAttachmentIds(attachmentIds);

        List<String> filePaths = new ArrayList<>();
        Resource resource = new ClassPathResource("temp.zip");
        filePaths.add(resource.getFile().getAbsolutePath());
        inputDTO.setFilePaths(filePaths);

        ArgumentCaptor<Map> messagePropsCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> capturedNote = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Map> capturedAttachments = ArgumentCaptor.forClass(Map.class);
        when(mockMuleContextManager.send(eq("vm://sendEmailViaSmtp.in"), capturedNote.capture(), capturedAttachments.capture(),
                messagePropsCaptor.capture())).thenReturn(mockMuleMessage);

        setSendExpectations();
        when(mockPropertyFileManager.load(anyString(), eq(EmailSenderConfigurationConstants.ENCRYPTION), isNull(String.class)))
                .thenReturn("starttls");

        when(mockMuleMessage.getInboundProperty("sendEmailException")).thenReturn(null);

        ArgumentCaptor<byte[]> read = ArgumentCaptor.forClass(byte[].class);
        when(mockEcmFileService.downloadAsInputStream(attachmentIds.get(0))).thenReturn(mockInputStream);
        when(mockEcmFileService.findById(attachmentIds.get(0))).thenReturn(mockEcmFile);
        when(mockInputStream.read(read.capture(), eq(0), eq(16384))).thenReturn(-1);

        when(mockEcmFile.getFileName()).thenReturn("fileName");
        when(mockEcmFile.getFileActiveVersionNameExtension()).thenReturn(".extension");

        mockInputStream.close();

        when(mockAcmUser.getUserId()).thenReturn("ann-acm");
        when(mockEcmFile.getParentObjectId()).thenReturn(103L);
        when(mockEcmFile.getParentObjectType()).thenReturn("COMPLAINT");

        // expected calls to raise the file emailed event on the file itself - AFDP-3029
        when(mockEcmFile.getId()).thenReturn(attachmentIds.get(0));
        when(mockEcmFile.getObjectType()).thenReturn(EcmFileConstants.OBJECT_FILE_TYPE);

        whenNew(File.class).withArguments(filePaths.get(0)).thenReturn(mockFile);
        whenNew(FileInputStream.class).withArguments(mockFile).thenReturn(mockFileInputStream);
        when(mockFile.getName()).thenReturn("temp.zip");

        // when
        service.sendEmailWithAttachments(inputDTO, mockAuthentication, mockAcmUser);

        // then
        assertThat(Pattern.compile(note).matcher(capturedNote.getValue()).matches(), is(true));

        assertThat(capturedAttachments.getValue(), notNullValue());
        assertThat(capturedAttachments.getValue().size(), is(2));
        assertThat(capturedAttachments.getValue().get("fileName.extension"), notNullValue());
        assertThat(capturedAttachments.getValue().get("temp.zip"), notNullValue());
        mockApplicationEventPublisher.publishEvent(any(SmtpEventSentEvent.class));
        assertThat(messagePropsCaptor.getValue().get("encryption"), is("starttls"));
    }

    @Test
    public void testSendEmailWithEmbeddedLinksAndAttachments() throws Exception
    {
        // given
        final String email = "user_email";
        final String header = "header";
        final String baseUrl = "base_url";
        final String title = "title";
        final String footer = "footer";
        final long fileId = 1234;
        final String token = "token";
        final String note = header + "\\s*" + "<br/>" + baseUrl + fileId + "&acm_email_ticket=" + token + "<br/>" + "\\s*" + footer;

        List<String> addresses = new ArrayList<>();
        addresses.add(email);
        List<Long> fileIds = new ArrayList<>();
        fileIds.add(fileId);
        EmailWithAttachmentsAndLinksDTO inputDTO = new EmailWithAttachmentsAndLinksDTO();
        inputDTO.setTitle(title);
        inputDTO.setHeader(header);
        inputDTO.setEmailAddresses(addresses);
        inputDTO.setBaseUrl(baseUrl);
        inputDTO.setFileIds(fileIds);
        inputDTO.setFooter(footer);
        List<Long> attachmentIds = new ArrayList<>();
        attachmentIds.add(new Long(999));
        inputDTO.setAttachmentIds(attachmentIds);

        List<String> filePaths = new ArrayList<>();
        Resource resource = new ClassPathResource("temp.zip");
        filePaths.add(resource.getFile().getAbsolutePath());
        inputDTO.setFilePaths(filePaths);

        ArgumentCaptor<Map> messagePropsCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> capturedNote = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Map> capturedAttachments = ArgumentCaptor.forClass(Map.class);
        when(mockMuleContextManager.send(eq("vm://sendEmailViaSmtp.in"), capturedNote.capture(), capturedAttachments.capture(),
                messagePropsCaptor.capture())).thenReturn(mockMuleMessage);

        setSendExpectations();
        when(mockPropertyFileManager.load("", EmailSenderConfigurationConstants.ENCRYPTION, null)).thenReturn("off");

        when(mockMuleMessage.getInboundProperty("sendEmailException")).thenReturn(null);

        when(mockAuthenticationTokenService.getUncachedTokenForAuthentication(mockAuthentication)).thenReturn(token);
        AuthenticationToken authenticationToken = new AuthenticationToken();
        authenticationToken.setKey(token);
        authenticationToken.setStatus(AuthenticationTokenConstants.ACTIVE);
        authenticationToken.setEmail(email);
        authenticationToken.setFileId(fileId);

        when(mockAuthenticationTokenDao.save(any(AuthenticationToken.class))).thenReturn(authenticationToken);

        ArgumentCaptor<byte[]> read = ArgumentCaptor.forClass(byte[].class);
        when(mockEcmFileService.downloadAsInputStream(attachmentIds.get(0))).thenReturn(mockInputStream);
        when(mockEcmFileService.findById(attachmentIds.get(0))).thenReturn(mockEcmFile);
        when(mockInputStream.read(read.capture(), eq(0), eq(16384))).thenReturn(-1);

        when(mockEcmFile.getFileName()).thenReturn("fileName");
        when(mockEcmFile.getFileActiveVersionNameExtension()).thenReturn(".extension");

        mockInputStream.close();

        when(mockAcmUser.getUserId()).thenReturn("ann-acm");
        when(mockEcmFile.getParentObjectId()).thenReturn(103L);
        when(mockEcmFile.getParentObjectType()).thenReturn("COMPLAINT");

        // expected calls to raise the file emailed event on the file itself - AFDP-3029
        when(mockEcmFile.getId()).thenReturn(attachmentIds.get(0));
        when(mockEcmFile.getObjectType()).thenReturn(EcmFileConstants.OBJECT_FILE_TYPE);

        whenNew(File.class).withArguments(filePaths.get(0)).thenReturn(mockFile);
        whenNew(FileInputStream.class).withArguments(mockFile).thenReturn(mockFileInputStream);
        when(mockFile.getName()).thenReturn("temp.zip");

        // when
        service.sendEmailWithAttachmentsAndLinks(inputDTO, mockAuthentication, mockAcmUser);

        // then
        assertThat(Pattern.compile(note).matcher(capturedNote.getValue()).matches(), is(true));

        assertThat(capturedAttachments.getValue(), notNullValue());
        assertThat(capturedAttachments.getValue().size(), is(2));
        assertThat(capturedAttachments.getValue().get("fileName.extension"), notNullValue());
        assertThat(capturedAttachments.getValue().get("temp.zip"), notNullValue());
        mockApplicationEventPublisher.publishEvent(any(SmtpEventSentEvent.class));
    }

    @Test
    public void testSendEmailWithEmbeddedLinksAndAttachmentsSTARTTLS() throws Exception
    {
        // given
        final String email = "user_email";
        final String header = "header";
        final String baseUrl = "base_url";
        final String title = "title";
        final String footer = "footer";
        final long fileId = 1234;
        final String token = "token";
        final String note = header + "\\s*" + "<br/>" + baseUrl + fileId + "&acm_email_ticket=" + token + "<br/>" + "\\s*" + footer;

        List<String> addresses = new ArrayList<>();
        addresses.add(email);
        List<Long> fileIds = new ArrayList<>();
        fileIds.add(fileId);
        EmailWithAttachmentsAndLinksDTO inputDTO = new EmailWithAttachmentsAndLinksDTO();
        inputDTO.setTitle(title);
        inputDTO.setHeader(header);
        inputDTO.setEmailAddresses(addresses);
        inputDTO.setBaseUrl(baseUrl);
        inputDTO.setFileIds(fileIds);
        inputDTO.setFooter(footer);
        List<Long> attachmentIds = new ArrayList<>();
        attachmentIds.add(new Long(999));
        inputDTO.setAttachmentIds(attachmentIds);

        List<String> filePaths = new ArrayList<>();
        Resource resource = new ClassPathResource("temp.zip");
        filePaths.add(resource.getFile().getAbsolutePath());
        inputDTO.setFilePaths(filePaths);

        ArgumentCaptor<Map> messagePropsCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> capturedNote = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Map> capturedAttachments = ArgumentCaptor.forClass(Map.class);
        when(mockMuleContextManager.send(eq("vm://sendEmailViaSmtp.in"), capturedNote.capture(), capturedAttachments.capture(),
                messagePropsCaptor.capture())).thenReturn(mockMuleMessage);

        setSendExpectations();
        when(mockPropertyFileManager.load(anyString(), eq(EmailSenderConfigurationConstants.ENCRYPTION), isNull(String.class)))
                .thenReturn("starttls");

        when(mockMuleMessage.getInboundProperty("sendEmailException")).thenReturn(null);

        when(mockAuthenticationTokenService.getUncachedTokenForAuthentication(mockAuthentication)).thenReturn(token);
        AuthenticationToken authenticationToken = new AuthenticationToken();
        authenticationToken.setKey(token);
        authenticationToken.setStatus(AuthenticationTokenConstants.ACTIVE);
        authenticationToken.setEmail(email);
        authenticationToken.setFileId(fileId);

        when(mockAuthenticationTokenDao.save(any(AuthenticationToken.class))).thenReturn(authenticationToken);

        ArgumentCaptor<byte[]> read = ArgumentCaptor.forClass(byte[].class);
        when(mockEcmFileService.downloadAsInputStream(attachmentIds.get(0))).thenReturn(mockInputStream);
        when(mockEcmFileService.findById(attachmentIds.get(0))).thenReturn(mockEcmFile);
        when(mockInputStream.read(read.capture(), eq(0), eq(16384))).thenReturn(-1);

        when(mockEcmFile.getFileName()).thenReturn("fileName");
        when(mockEcmFile.getFileActiveVersionNameExtension()).thenReturn(".extension");

        mockInputStream.close();

        when(mockAcmUser.getUserId()).thenReturn("ann-acm");
        when(mockEcmFile.getParentObjectId()).thenReturn(103L);
        when(mockEcmFile.getParentObjectType()).thenReturn("COMPLAINT");

        // expected calls to raise the file emailed event on the file itself - AFDP-3029
        when(mockEcmFile.getId()).thenReturn(attachmentIds.get(0));
        when(mockEcmFile.getObjectType()).thenReturn(EcmFileConstants.OBJECT_FILE_TYPE);

        whenNew(File.class).withArguments(filePaths.get(0)).thenReturn(mockFile);
        whenNew(FileInputStream.class).withArguments(mockFile).thenReturn(mockFileInputStream);
        when(mockFile.getName()).thenReturn("temp.zip");

        // when
        service.sendEmailWithAttachmentsAndLinks(inputDTO, mockAuthentication, mockAcmUser);

        // then
        assertThat(Pattern.compile(note).matcher(capturedNote.getValue()).matches(), is(true));

        assertThat(capturedAttachments.getValue(), notNullValue());
        assertThat(capturedAttachments.getValue().size(), is(2));
        assertThat(capturedAttachments.getValue().get("fileName.extension"), notNullValue());
        assertThat(capturedAttachments.getValue().get("temp.zip"), notNullValue());
        mockApplicationEventPublisher.publishEvent(any(SmtpEventSentEvent.class));
        assertThat(messagePropsCaptor.getValue().get("encryption"), is("starttls"));
    }

    /**
     * @throws Exception
     *
     */
    private void setSendExpectations() throws Exception
    {
        when(mockPropertyFileManager.load("", EmailSenderConfigurationConstants.HOST, null)).thenReturn("host_value");
        when(mockPropertyFileManager.load("", EmailSenderConfigurationConstants.PORT, null)).thenReturn("port_value");
        when(mockPropertyFileManager.load("", EmailSenderConfigurationConstants.USERNAME, null)).thenReturn("email_user_value");
        when(mockPropertyFileManager.load("", EmailSenderConfigurationConstants.PASSWORD, null)).thenReturn("email_password_value");
        when(mockPropertyFileManager.load("", EmailSenderConfigurationConstants.USER_FROM, null)).thenReturn("email_from_value");
    }

}
