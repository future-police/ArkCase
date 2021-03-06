package com.armedia.acm.services.email.handler;

/*-
 * #%L
 * ACM Service: Email
 * %%
 * Copyright (C) 2014 - 2018 ArkCase LLC
 * %%
 * This file is part of the ArkCase software. 
 * 
 * If the software was purchased under a paid ArkCase license, the terms of 
 * the paid license agreement will prevail.  Otherwise, the software is 
 * provided under the following open source license terms:
 * 
 * ArkCase is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * ArkCase is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ArkCase. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.armedia.acm.auth.AuthenticationUtils;
import com.armedia.acm.core.AcmObject;
import com.armedia.acm.data.AcmNameDao;
import com.armedia.acm.data.AuditPropertyEntityAdapter;
import com.armedia.acm.plugins.ecm.model.AcmContainerEntity;
import com.armedia.acm.plugins.ecm.model.AcmFolder;
import com.armedia.acm.plugins.ecm.service.AcmFolderService;
import com.armedia.acm.plugins.ecm.service.EcmFileService;
import com.armedia.acm.services.email.event.SmtpEmailReceivedEvent;
import com.armedia.acm.web.api.MDCConstants;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.persistence.EntityNotFoundException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Acm Object mail handler, handles incoming entity related emails and imports the email files in entity contained email
 * folder
 *
 * @author dame.gjorgjievski
 */
public class AcmObjectMailHandler implements ApplicationEventPublisherAware
{
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AcmNameDao entityDao;

    private EcmFileService ecmFileService;
    private AcmFolderService acmFolderService;
    private AuditPropertyEntityAdapter auditPropertyEntityAdapter;
    private ApplicationEventPublisher eventPublisher;

    private String objectIdRegexPattern;
    private String mailDirectory;
    private boolean enabled;

    public AcmObjectMailHandler(AcmNameDao dao)
    {
        this.entityDao = dao;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
    {
        eventPublisher = applicationEventPublisher;
    }

    @Transactional
    public void handle(Message message) throws MessagingException, IllegalArgumentException, SecurityException, IOException
    {
        if (!enabled)
        {
            return;
        }

        String entityId = extractIdFromSubject(message);
        if (entityId == null)
        {
            throw new EntityNotFoundException("Subject in the mail didn't match correct entity number. subject: " + message.getSubject());
        }

        AcmObject entity = entityDao.findByName(entityId);
        if (entity == null)
        {
            throw new EntityNotFoundException("No entity was found with given number: " + entityId);
        }

        String userId = "mail-service";
        auditPropertyEntityAdapter.setUserId(userId);

        // set the Alfresco user id, so we can attach the incoming message to the parent object.
        MDC.put(MDCConstants.EVENT_MDC_REQUEST_ALFRESCO_USER_ID_KEY, "admin");
        MDC.put(MDCConstants.EVENT_MDC_REQUEST_ID_KEY, UUID.randomUUID().toString());

        String tempDir = System.getProperty("java.io.tmpdir");
        String messageFileName = System.currentTimeMillis() + "_" + entityId + ".eml";
        File messageFile = new File(tempDir + File.separator + messageFileName);
        Exception exception = null;

        try
        {
            try (OutputStream os = new FileOutputStream(messageFile))
            {
                message.writeTo(os);
            }

            AcmFolder folder = acmFolderService.addNewFolderByPath(entity.getObjectType(), entity.getId(), mailDirectory);
            try (InputStream is = new FileInputStream(messageFile))
            {
                Authentication auth = new UsernamePasswordAuthenticationToken(userId, "");
                ecmFileService.upload(messageFileName, "mail", "Document", is, "message/rfc822", messageFileName, auth,
                        folder.getCmisFolderId(), entity.getObjectType(), entity.getId());

            }

        }
        catch (Exception e)
        {
            log.error("Error processing complaint with number '{}'. Exception msg: '{}' ", entityId, e.getMessage());
            exception = e;
        }

        if (message.getContent() instanceof Multipart)
        {
            uploadAttachments(message, entity, userId);
        }

        SmtpEmailReceivedEvent event = new SmtpEmailReceivedEvent(message, userId, entity.getId(), entity.getObjectType(),
                AuthenticationUtils.getUserIpAddress());
        boolean success = (exception == null);
        event.setSucceeded(success);
        eventPublisher.publishEvent(event);

    }

    /**
     * Extract id from message subject
     *
     * @param message
     * @return
     * @throws MessagingException
     */
    public String extractIdFromSubject(Message message) throws MessagingException
    {
        String result = null;
        String subject = message.getSubject();
        if (!StringUtils.isEmpty(subject))
        {

            Pattern pattern = Pattern.compile(objectIdRegexPattern);
            Matcher matcher = pattern.matcher(subject);
            if (matcher.find())
            {
                result = subject.substring(matcher.start(), matcher.end());
            }
        }
        return result;
    }

    public void uploadAttachments(Message message, AcmObject entity, String userId)
    {
        if (message != null && entity != null)
        {
            try
            {

                Multipart multipart = (Multipart) message.getContent();

                for (int i = 0; i < multipart.getCount(); i++)
                {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) && StringUtils.isBlank(bodyPart.getFileName()))
                    {
                        continue;
                    }

                    try
                    {
                        AcmContainerEntity containerEntity = (AcmContainerEntity) entity;
                        AcmFolder folder = containerEntity.getContainer().getAttachmentFolder();
                        try (InputStream is = bodyPart.getInputStream())
                        {
                            Authentication auth = new UsernamePasswordAuthenticationToken(userId, "");
                            getEcmFileService().upload(bodyPart.getFileName(), "attachment", "Document", is, bodyPart.getContentType(),
                                    bodyPart.getFileName(), auth,
                                    folder.getCmisFolderId(), entity.getObjectType(), entity.getId());

                        }

                    }
                    catch (Exception e)
                    {
                        log.error("Error processing attachment with name '{}'. Exception msg: '{}' ", bodyPart.getFileName(),
                                e.getMessage());
                    }
                }
            }
            catch (IOException | MessagingException e)
            {
                log.error("Error processing Multipart message. Exception msg: '{}' ", e.getMessage());
            }
        }
    }

    public void setObjectIdRegexPattern(String objectIdRegexPattern)
    {
        this.objectIdRegexPattern = objectIdRegexPattern;
    }

    public AcmNameDao getEntityDao()
    {
        return entityDao;
    }

    public EcmFileService getEcmFileService()
    {
        return ecmFileService;
    }

    public void setEcmFileService(EcmFileService ecmFileService)
    {
        this.ecmFileService = ecmFileService;
    }

    public AcmFolderService getAcmFolderService()
    {
        return acmFolderService;
    }

    public void setAcmFolderService(AcmFolderService acmFolderService)
    {
        this.acmFolderService = acmFolderService;
    }

    public AuditPropertyEntityAdapter getAuditPropertyEntityAdapter()
    {
        return auditPropertyEntityAdapter;
    }

    public void setAuditPropertyEntityAdapter(AuditPropertyEntityAdapter auditPropertyEntityAdapter)
    {
        this.auditPropertyEntityAdapter = auditPropertyEntityAdapter;
    }

    public ApplicationEventPublisher getEventPublisher()
    {
        return eventPublisher;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public String getMailDirectory()
    {
        return mailDirectory;
    }

    public void setMailDirectory(String mailDirectory)
    {
        this.mailDirectory = mailDirectory;
    }
}
