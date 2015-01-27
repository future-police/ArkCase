/**
 * 
 */
package com.armedia.acm.services.users.web.api.group;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.armedia.acm.core.exceptions.AcmUserActionFailedException;
import com.armedia.acm.services.users.dao.group.AcmGroupDao;
import com.armedia.acm.services.users.model.AcmUser;
import com.armedia.acm.services.users.model.group.AcmGroup;

/**
 * @author riste.tutureski
 *
 */
@Controller
@RequestMapping({ "/api/v1/users", "/api/latest/users" })
public class RemoveMembersFromGroupAPIController {

	private Logger LOG = LoggerFactory.getLogger(getClass());
	
	private AcmGroupDao groupDao;
	
	@RequestMapping(value="/group/{groupId}/members/remove", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AcmGroup removeSupervisorsFromGroup(@RequestBody Set<AcmUser> members,
								    		   @PathVariable("groupId") String groupId, 
											   Authentication auth) throws AcmUserActionFailedException
    {		
		if (LOG.isInfoEnabled()) 
		{
			LOG.info("Removing members from the group with ID = " + groupId);
		}
		
		try
		{
			AcmGroup group = getGroupDao().findByName(groupId);

			Set<AcmUser> cleanedMembers = cleanMembers(group.getMembers(), members);
			group.setMembers(cleanedMembers);
			
			// Remove members from all child groups
			List<AcmGroup> children = group.getChildGroups();
			// Recursion (I couldn't find batter solution for going deep through list of lists)
			removeMembersFromChilds(children, members);
			
			AcmGroup saved = getGroupDao().save(group);
			
			return saved;
		}
		catch(Exception e)
		{
			LOG.error("Failed to remove members to the group with ID = " + groupId, e);
			throw new AcmUserActionFailedException("Remove Members", "Group", -1L, e.getMessage(), e);
		}
    }
	
	private Set<AcmUser> cleanMembers(Set<AcmUser> members, Set<AcmUser> toRemoveArray)
	{
		Set<AcmUser> result = new HashSet<AcmUser>();
		
		if (toRemoveArray != null && members != null)
		{
			for (AcmUser member : members)
			{
				boolean found = false;
				
				for (AcmUser toRemove : toRemoveArray)
				{
					if (member.getUserId().equals(toRemove.getUserId()))
					{
						found = true;
					}
				}
				
				if (!found)
				{
					result.add(member);
				}
			}
		}
		
		return result;
	}
	
	private void removeMembersFromChilds(List<AcmGroup> childs, Set<AcmUser> membersToRemove)
	{
		if (childs != null)
		{
			for (AcmGroup child : childs)
			{
				Set<AcmUser> cleanedMembers = cleanMembers(child.getMembers(), membersToRemove);
				child.setMembers(cleanedMembers);
				
				getGroupDao().save(child);
				
				if (child.getChildGroups() != null)
				{
					removeMembersFromChilds(child.getChildGroups(), membersToRemove);
				}
			}
		}
	}
	

	public AcmGroupDao getGroupDao() {
		return groupDao;
	}

	public void setGroupDao(AcmGroupDao groupDao) {
		this.groupDao = groupDao;
	}
	
}
