package com.armedia.acm.plugins.alfrescorma.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.armedia.acm.muletools.mulecontextmanager.MuleContextManager;
import com.armedia.acm.web.api.MDCConstants;

import org.apache.chemistry.opencmis.client.api.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "/spring/spring-alfresco-records-service-test.xml",
        "/spring/spring-library-alfresco-service.xml",
        "/spring/spring-library-acm-encryption.xml",
        "/spring/spring-library-property-file-manager.xml" })
public class DeclareRecordServiceIT
{
    private transient final Logger LOG = LoggerFactory.getLogger(getClass());
    @Autowired
    private MuleContextManager muleContextManager;
    @Autowired
    @Qualifier("declareRecordService")
    private AlfrescoService<String> service;
    private String ecmFileId;
    private CmisFileWriter cmisFileWriter = new CmisFileWriter();

    @Before
    public void setUp() throws Exception
    {
        MDC.put(MDCConstants.EVENT_MDC_REQUEST_ALFRESCO_USER_ID_KEY, "admin");
        MDC.put(MDCConstants.EVENT_MDC_REQUEST_ID_KEY, UUID.randomUUID().toString());

        Document testFile = cmisFileWriter.writeTestFile(muleContextManager);
        ecmFileId = testFile.getVersionSeriesId();
    }

    @Test
    public void declareRecord() throws Exception
    {
        assertNotNull(service);

        Map<String, Object> context = new HashMap<>();

        context.put("ecmFileId", ecmFileId);

        String retval = service.service(context);

        assertEquals(ecmFileId, retval);

    }
}
