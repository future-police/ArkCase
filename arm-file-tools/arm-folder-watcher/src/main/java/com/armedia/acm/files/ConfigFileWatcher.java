package com.armedia.acm.files;

import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.FileTypeSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Watch for file events in the ACM configuration folder.  Raise application events for each such event.
 */
public class ConfigFileWatcher implements FileListener, ApplicationEventPublisherAware, ApplicationContextAware
{
    private Logger log = LoggerFactory.getLogger(getClass());
    private FileObject baseFolder;
    private String baseFolderPath;
    private ApplicationEventPublisher applicationEventPublisher;
    private List<String> ignoreFolders;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        if ( log.isDebugEnabled() )
        {
            log.debug("The application context has been set!");
            log.debug("Looking for files in: " + getBaseFolder().getName());
        }


        // this event just tells us when the whole application context is ready.  we don't actually need the context.
        try
        {
            FileObject[] existingFiles = getBaseFolder().findFiles(new FileTypeSelector(FileType.FILE));
            for ( FileObject current : existingFiles )
            {
                if ( log.isDebugEnabled() )
                {
                    log.debug("Raising event for file " + current.getName());
                }
                fileCreated(new FileChangeEvent(current));
            }
        } catch (Exception e)
        {
            throw new BeanCreationException("Could not find existing files: " + e.getMessage(), e);
        }
    }

    @Override
    public void fileCreated(FileChangeEvent fileChangeEvent) throws Exception
    {
        if ( ignoreThisFile(fileChangeEvent.getFile().getURL() ))
        {
            return;
        }

        if ( log.isDebugEnabled() )
        {
            log.debug("file added: " + fileChangeEvent.getFile().getName());
        }

        File eventFile = getEventFile(fileChangeEvent);
        String baseFilePath = getEventFileBasePath(eventFile);

        ConfigurationFileAddedEvent event = new ConfigurationFileAddedEvent(fileChangeEvent);
        event.setBaseFileName(baseFilePath);
        event.setConfigFile(eventFile);

        getApplicationEventPublisher().publishEvent(event);
    }

    private String getEventFileBasePath(File eventFile) throws IOException
    {
        String filePath = eventFile.getCanonicalPath();
        return filePath.replace(getBaseFolderPath(), "");
    }

    private File getEventFile(FileChangeEvent fileChangeEvent) throws FileSystemException, URISyntaxException
    {

        URL fileUrl = fileChangeEvent.getFile().getURL();
        return new File(fileUrl.toURI());
    }

    @Override
    public void fileDeleted(FileChangeEvent fileChangeEvent) throws Exception
    {
        if ( ignoreThisFile(fileChangeEvent.getFile().getURL() ))
        {
            return;
        }

        if ( log.isDebugEnabled() )
        {
            log.debug("file deleted: " + fileChangeEvent.getFile().getName());
        }

        File eventFile = getEventFile(fileChangeEvent);
        String baseFilePath = getEventFileBasePath(eventFile);

        ConfigurationFileDeletedEvent event = new ConfigurationFileDeletedEvent(fileChangeEvent);
        event.setBaseFileName(baseFilePath);
        event.setConfigFile(eventFile);

        getApplicationEventPublisher().publishEvent(event);

    }

    @Override
    public void fileChanged(FileChangeEvent fileChangeEvent) throws Exception
    {
        if ( ignoreThisFile(fileChangeEvent.getFile().getURL() ))
        {
            return;
        }

        if ( log.isDebugEnabled() )
        {
            log.debug("file changed: " + fileChangeEvent.getFile().getName());
        }

        File eventFile = getEventFile(fileChangeEvent);
        String baseFilePath = getEventFileBasePath(eventFile);

        ConfigurationFileChangedEvent event = new ConfigurationFileChangedEvent(fileChangeEvent);
        event.setBaseFileName(baseFilePath);
        event.setConfigFile(eventFile);

        getApplicationEventPublisher().publishEvent(event);
    }

    public boolean ignoreThisFile(URL fileUrl)
    {
        boolean retval = false;
        for ( String ignoreFolder : getIgnoreFolders() )
        {
            String ignoreFolderPath = getBaseFolderPath() + ignoreFolder;

            if ( log.isTraceEnabled() )
            {
                log.trace("checking for " + ignoreFolderPath);
            }

            if ( fileUrl.toString().contains( ignoreFolderPath ))
            {
                if ( log.isTraceEnabled() )
                {
                    log.debug("this file will be ignored");
                }
                retval = true;
            }
        }

        return retval;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher)
    {
        log.debug("The application event publisher has been set!");
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public ApplicationEventPublisher getApplicationEventPublisher()
    {
        return applicationEventPublisher;
    }

    public FileObject getBaseFolder()
    {
        return baseFolder;
    }

    public void setBaseFolder(FileObject baseFolder)
    {
        this.baseFolder = baseFolder;

        if ( baseFolder != null )
        {
            try
            {
                URL baseUrl = baseFolder.getURL();
                File baseFile = new File(baseUrl.toURI());
                setBaseFolderPath(baseFile.getCanonicalPath());

            } catch (URISyntaxException | IOException e)
            {
                log.error("Something is wrong with the base folder url: " + e.getMessage(), e);
            }
        }
    }

    public String getBaseFolderPath()
    {
        return baseFolderPath;
    }

    public void setBaseFolderPath(String baseFolderPath)
    {
        this.baseFolderPath = baseFolderPath;
    }

    public void setIgnoreFolders(List<String> ignoreFolders)
    {
        this.ignoreFolders = ignoreFolders;
    }

    public List<String> getIgnoreFolders()
    {
        return ignoreFolders;
    }
}
