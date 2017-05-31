package com.armedia.acm.services.users.service.group;


import com.armedia.acm.core.exceptions.AcmUserActionFailedException;
import com.armedia.acm.services.users.dao.group.AcmGroupDao;
import com.armedia.acm.services.users.dao.ldap.SpringLdapDao;
import com.armedia.acm.services.users.dao.ldap.UserDao;
import com.armedia.acm.services.users.model.group.AcmGroup;
import com.armedia.acm.services.users.model.ldap.AcmLdapActionFailedException;
import com.armedia.acm.services.users.model.ldap.AcmLdapConstants;
import com.armedia.acm.services.users.model.ldap.AcmLdapSyncConfig;
import com.armedia.acm.services.users.model.ldap.MapperUtils;
import com.armedia.acm.services.users.service.RetryExecutor;
import com.armedia.acm.services.users.service.ldap.LdapEntryTransformer;
import com.armedia.acm.spring.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.NameAlreadyBoundException;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.transaction.annotation.Transactional;


public class LdapGroupService
{
    private AcmGroupDao groupDao;

    private SpringLdapDao ldapDao;

    private UserDao userDao;

    private LdapEntryTransformer ldapEntryTransformer;

    private SpringContextHolder acmContextHolder;

    private Logger log = LoggerFactory.getLogger(getClass());

    @Transactional(rollbackFor = Exception.class)
    public AcmGroup createLdapGroup(AcmGroup group, String directoryName) throws AcmLdapActionFailedException
    {
        AcmLdapSyncConfig ldapSyncConfig = acmContextHolder.getAllBeansOfType(AcmLdapSyncConfig.class).
                get(String.format("%s_sync", directoryName));
        String groupDN = buildDnForGroup(group.getName(), ldapSyncConfig);

        group.setName(group.getName().toUpperCase());
        group.setType(AcmLdapConstants.GROUP_OBJECT_TYPE);
        group.setDescription(group.getDescription());
        group.setDistinguishedName(groupDN);
        group.setDirectoryName(directoryName);
        group.setStatus("ACTIVE");
        log.debug("Saving Group:{} with DN:{} in database", group.getName(), group.getDistinguishedName());
        AcmGroup acmGroup = getGroupDao().save(group);
        getGroupDao().getEm().flush();

        log.debug("Saving Group:{} with DN:{} in LDAP server", group.getName(), group.getDistinguishedName());
        LdapTemplate ldapTemplate = getLdapDao().buildLdapTemplate(ldapSyncConfig);
        DirContextAdapter context = ldapEntryTransformer.createContextForNewGroupEntry(directoryName, acmGroup,
                null, ldapSyncConfig.getBaseDC());
        log.debug("Ldap Group Context: {}", context.getAttributes());
        try
        {
            new RetryExecutor().retry(() -> ldapTemplate.bind(context));
        } catch (Exception e)
        {
            throw new AcmLdapActionFailedException("LDAP Action Failed Exception", e);
        }

        log.debug("Group:{} with DN:{} saved in DB and LDAP", acmGroup.getName(), acmGroup.getDistinguishedName());
        return acmGroup;
    }

    @Transactional(rollbackFor = Exception.class)
    public AcmGroup createLdapSubgroup(AcmGroup group, String parentGroupName, String directoryName)
            throws AcmUserActionFailedException, AcmLdapActionFailedException
    {
        AcmLdapSyncConfig ldapSyncConfig = acmContextHolder.getAllBeansOfType(AcmLdapSyncConfig.class).
                get(String.format("%s_sync", directoryName));

        String groupDN = buildDnForGroup(group.getName(), ldapSyncConfig);

        AcmGroup existingGroup = getGroupDao().findByName(group.getName().toUpperCase());
        if (existingGroup != null)
        {
            log.debug("Group with name:{} already exists!", group.getName());
            throw new NameAlreadyBoundException(null);
        }
        AcmGroup parentGroup = getGroupDao().findByName(parentGroupName);
        log.debug("Found parent-group:{} for new LDAP sub-group:{}", parentGroup.getName(), group.getName());

        group.setName(group.getName().toUpperCase());
        group.setType(AcmLdapConstants.GROUP_OBJECT_TYPE);
        group.setDescription(group.getDescription());
        group.setDistinguishedName(groupDN);
        group.setParentGroup(parentGroup);
        group.setDirectoryName(directoryName);

        log.debug("Saving sub-group:{} with parent-group:{} in database", group.getName(), parentGroup.getName());
        AcmGroup acmGroup = getGroupDao().save(group);
        getGroupDao().getEm().flush();

        log.debug("Saving sub-group:{} with parent-group:{} in LDAP server", acmGroup.getDistinguishedName(), parentGroup.getName());
        LdapTemplate ldapTemplate = getLdapDao().buildLdapTemplate(ldapSyncConfig);
        DirContextAdapter context = ldapEntryTransformer.createContextForNewGroupEntry(directoryName, acmGroup,
                parentGroupName, ldapSyncConfig.getBaseDC());
        log.debug("Ldap Sub-Group Context: {}", context.getAttributes());
        try
        {
            new RetryExecutor().retry(() -> ldapTemplate.bind(context));
        } catch (Exception e)
        {
            throw new AcmLdapActionFailedException("LDAP Action Failed Exception", e);
        }

        log.debug("Sub-group:{} with DN:{} saved in LDAP server", acmGroup.getName(), acmGroup.getDistinguishedName());

        String parentGroupDnStrippedBase = MapperUtils.stripBaseFromDn(parentGroup.getDistinguishedName(), ldapSyncConfig.getBaseDC());

        try
        {
            log.debug("Update parent-group:{} with DN:{} with the new member:{} in LDAP server", parentGroup.getName(),
                    parentGroup.getDistinguishedName(), acmGroup.getDistinguishedName());
            DirContextOperations parentGroupContext = new RetryExecutor<DirContextOperations>()
                    .retryResult(() -> ldapTemplate.lookupContext(parentGroupDnStrippedBase));
            parentGroupContext.addAttributeValue("member", acmGroup.getDistinguishedName());
            new RetryExecutor().retry(() -> ldapTemplate.modifyAttributes(parentGroupContext));
        } catch (Exception e)
        {
            log.error("Updating parent-group DN:{} failed! Rollback saved sub-group DN:{} ",
                    parentGroup.getDistinguishedName(), acmGroup.getDistinguishedName());
            try
            {
                new RetryExecutor().retry(() -> ldapTemplate.unbind(parentGroupDnStrippedBase));
            } catch (Exception ee)
            {
                log.warn("Rollback failed", ee);
            }

            log.debug("Sub-group entry DN:{} deleted", groupDN);
            throw new AcmUserActionFailedException("create new LDAP subgroup", null, null, "Adding new LDAP subgroup failed!", e);
        }
        return acmGroup;
    }

    @Transactional(rollbackFor = Exception.class)
    public AcmGroup removeLdapGroup(String group, String directoryName) throws AcmLdapActionFailedException
    {
        AcmLdapSyncConfig ldapSyncConfig = acmContextHolder.getAllBeansOfType(AcmLdapSyncConfig.class).
                get(String.format("%s_sync", directoryName));
        AcmGroup acmGroup = getGroupDao().markGroupDelete(group);
        log.debug("Group:{} with DN:{} was deleted in DB and LDAP", acmGroup.getName(), acmGroup.getDistinguishedName());
        return acmGroup;
    }

    private String buildDnForGroup(String cn, AcmLdapSyncConfig ldapSyncConfig)
    {
        return String.format("cn=%s,%s,%s", cn, ldapSyncConfig.getGroupSearchBase(), ldapSyncConfig.getBaseDC());
    }

    public AcmGroupDao getGroupDao()
    {
        return groupDao;
    }

    public void setGroupDao(AcmGroupDao groupDao)
    {
        this.groupDao = groupDao;
    }

    public SpringLdapDao getLdapDao()
    {
        return ldapDao;
    }

    public void setLdapDao(SpringLdapDao ldapDao)
    {
        this.ldapDao = ldapDao;
    }

    public SpringContextHolder getAcmContextHolder()
    {
        return acmContextHolder;
    }

    public void setAcmContextHolder(SpringContextHolder acmContextHolder)
    {
        this.acmContextHolder = acmContextHolder;
    }

    public UserDao getUserDao()
    {
        return userDao;
    }

    public void setUserDao(UserDao userDao)
    {
        this.userDao = userDao;
    }

    public LdapEntryTransformer getLdapEntryTransformer()
    {
        return ldapEntryTransformer;
    }

    public void setLdapEntryTransformer(LdapEntryTransformer ldapEntryTransformer)
    {
        this.ldapEntryTransformer = ldapEntryTransformer;
    }
}
