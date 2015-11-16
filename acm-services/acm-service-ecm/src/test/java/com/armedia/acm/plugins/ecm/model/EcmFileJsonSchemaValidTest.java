package com.armedia.acm.plugins.ecm.model;

import com.armedia.acm.objectonverter.json.validator.JsonSchemaValidator;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.junit.Assert.*;


public class EcmFileJsonSchemaValidTest
{

    private transient final Logger log = LoggerFactory.getLogger(getClass());

    private JsonSchemaValidator jsonSchemaValidator;

    @Before
    public void setUp() throws Exception
    {
        jsonSchemaValidator = new JsonSchemaValidator();
    }

    @Test
    public void validateEcmFileSchema() throws Exception
    {
        validate("/jsonSchemas/ecm-file-schema.json");
    }

    @Test
    public void validateFolderListSchema() throws Exception
    {
        validate("/jsonSchemas/folder-list-schema.json");
    }

    @Test
    public void validateCopyFileDtoSchema() throws Exception
    {
        validate("/jsonSchemas/copy-file-dto-schema.json");
    }

    @Test
    public void validateFolderSchema() throws Exception
    {
        validate("/jsonSchemas/folder-schema.json");
    }

    @Test
    public void validateMoveCopyFileRequestSchema() throws Exception
    {
        validate("/jsonSchemas/move-copy-file-schema.json");
    }

    @Test
    public void validateCopyFolderDtoSchema() throws Exception
    {
        validate("/jsonSchemas/copy-folder-dto-schema.json");
    }

    @Test
    public void deleteFileResultsSchema() throws Exception
    {
        validate("/jsonSchemas/delete-file-results-schema.json");
    }

    @Test
    public void deleteFolderResultsSchema() throws Exception
    {
        validate("/jsonSchemas/delete-folder-results-schema.json");
    }


    private void validate(String path) throws IOException, ProcessingException
    {
        Resource schemaResource = new ClassPathResource(path);
        ProcessingReport report = jsonSchemaValidator.validate(schemaResource.getFile());

        log.debug("Schema Report: " + report);

        // any warning messages will appear in the report.toString, but report.isSuccess() will still
        // be true; so to verify there are no warnings, we have to examine the report.toString itself
        assertTrue(report.toString().trim().endsWith("ListProcessingReport: success"));
    }


}