package com.armedia.acm.service.objectlock.annotation;

import com.armedia.acm.core.AcmObject;
import com.armedia.acm.core.exceptions.AcmObjectLockException;
import com.armedia.acm.service.objectlock.service.AcmObjectLockingManager;
import com.armedia.acm.web.api.MDCConstants;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Around aspect targeting annotation: {@link AcmReleaseObjectLock}
 * 
 * Created by bojan.milenkoski on 03/05/2018.
 */
@Aspect
@Component
public class AcmReleaseObjectLockAspect
{
    private Logger log = LoggerFactory.getLogger(getClass());

    private AcmObjectLockingManager objectLockingManager;

    @Around(value = "@annotation(acmReleaseObjectLock)")
    public Object aroundReleaseObjectLock(ProceedingJoinPoint pjp, AcmReleaseObjectLock acmReleaseObjectLock)
            throws Throwable, AcmObjectLockException
    {
        Object[] args = pjp.getArgs();
        String objectType = acmReleaseObjectLock.objectType();
        Long objectId = getObjectId(pjp, acmReleaseObjectLock, args);
        String lockType = acmReleaseObjectLock.lockType();
        boolean unlockChildObjects = acmReleaseObjectLock.unlockChildObjects();
        Long lockId = acmReleaseObjectLock.lockIdArgIndex() != -1 ? (Long) args[acmReleaseObjectLock.lockIdArgIndex()]
                : null;
        String userId = MDC.get(MDCConstants.EVENT_MDC_REQUEST_USER_ID_KEY);
        if (userId == null)
        {
            log.warn("Releasing object lock without userId set in {}.{}", pjp.getSignature().getDeclaringTypeName(),
                    pjp.getSignature().getName());
            userId = "no-user-id";
        }

        try
        {
            Object ret = pjp.proceed();

            // if objectId is null, probably we are trying to release an object before it is persisted for the first
            // time, we cannot lock nor release a lock from such objects, but we don't raise an error
            if (objectId != null)
            {
                objectLockingManager.releaseObjectLock(objectId, objectType, lockType, unlockChildObjects, userId, lockId);
            }
            return ret;
        }
        catch (Exception e)
        {
            // log exception and re-throw
            log.error(e.getMessage());
            throw e;
        }
    }

    private Long getObjectId(ProceedingJoinPoint pjp, AcmReleaseObjectLock releaseObjectLock, Object[] args)
    {
        if (releaseObjectLock.objectIdArgIndex() != -1)
        {
            if (!(args[releaseObjectLock.objectIdArgIndex()] instanceof Long))
            {
                throw new RuntimeException(String.format("AcmReleaseObjectLock objectIdArgIndex does not resolve to Long argument in {}.{}",
                        pjp.getSignature().getDeclaringTypeName(),
                        pjp.getSignature().getName()));
            }
            return (Long) args[releaseObjectLock.objectIdArgIndex()];
        }
        else if (releaseObjectLock.acmObjectArgIndex() != -1)
        {
            if (!(args[releaseObjectLock.acmObjectArgIndex()] instanceof AcmObject))
            {
                throw new RuntimeException(
                        String.format("AcmReleaseObjectLock acmObjectArgIndex does not resolve to AcmObject argument in {}.{}",
                                pjp.getSignature().getDeclaringTypeName(),
                                pjp.getSignature().getName()));
            }
            return ((AcmObject) args[releaseObjectLock.acmObjectArgIndex()]).getId();
        }
        else
        {
            throw new RuntimeException("AcmReleaseObjectLock requires objectIdArgIndex or acmObjectArgIndex to be specified!");
        }
    }

    public AcmObjectLockingManager getObjectLockingManager()
    {
        return objectLockingManager;
    }

    public void setObjectLockingManager(AcmObjectLockingManager objectLockingManager)
    {
        this.objectLockingManager = objectLockingManager;
    }
}
