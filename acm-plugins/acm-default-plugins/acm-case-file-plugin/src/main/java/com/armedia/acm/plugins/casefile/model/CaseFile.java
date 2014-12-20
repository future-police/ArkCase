package com.armedia.acm.plugins.casefile.model;

import com.armedia.acm.core.AcmObject;
import com.armedia.acm.data.AcmEntity;
import com.armedia.acm.plugins.objectassociation.model.ObjectAssociation;
import com.armedia.acm.plugins.person.model.PersonAssociation;
import com.armedia.acm.service.milestone.model.AcmMilestone;
import com.armedia.acm.services.users.model.AcmParticipant;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name="acm_case_file")
@XmlRootElement(name = "caseFile")
public class CaseFile implements Serializable, AcmObject, AcmEntity
{
    private static final long serialVersionUID = -6035628455385955008L;

    @Id
    @Column(name = "cm_case_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cm_case_number", insertable = true, updatable = false)
    private String caseNumber;

    @Column(name = "cm_case_type")
    private String caseType;

    @Column(name = "cm_case_title")
    private String title;

    @Column(name = "cm_case_status")
    private String status;

    @Lob
    @Column(name = "cm_case_details")
    private String details;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "cm_case_incident_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date incidentDate;

    @Column(name = "cm_case_created", nullable = false, insertable = true, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "cm_case_creator", insertable = true, updatable = false)
    private String creator;

    @Column(name = "cm_case_modified", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    @Column(name = "cm_case_modifier")
    private String modifier;

    @Column(name = "cm_case_closed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date closed;

    @Column(name = "cm_case_disposition")
    private String disposition;

    @Column(name = "cm_case_priority")
    private String priority;

    @OneToMany (cascade = {CascadeType.ALL})
    @JoinColumn(name = "cm_object_id")
    private List<AcmParticipant> participants = new ArrayList<>();

    @Column(name = "cm_due_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    
    @Transient
    private ChangeCaseStatus changeCaseStatus;

    /**
     * These approvers are added by the web application and they become the assignees of the Activiti business process.
     * They are not persisted to the database.
     */
    @Transient
    private List<String> approvers;

    /**
     * This field is only used when the complaint is created. Usually it will be null.  Use the ecmFolderId
     * to get the CMIS object ID of the complaint folder.
     */
    @Transient
    private String ecmFolderPath;

    @OneToMany (cascade = CascadeType.ALL)
    @JoinColumn(name = "cm_person_assoc_parent_id")
    private List<PersonAssociation> personAssociations = new ArrayList<>();

    /**
     * Milestones are read-only in the parent object; use the milestone service to add them.
     */
    @OneToMany
    @JoinColumn(name = "cm_milestone_object_id", updatable = false, insertable = false)
    private List<AcmMilestone> milestones = new ArrayList<>();

    // the same person could originate many complaints, but each complaint is originated by
    // only one person, so a ManyToOne mapping makes sense here.
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cm_originator_id")
    private PersonAssociation originator;

    @PrePersist
    protected void beforeInsert()
    {
        if ( getStatus() == null || getStatus().trim().isEmpty() )
        {
            setStatus("DRAFT");
        }

        if ( getOriginator() != null )
        {
            personAssociationResolver(getOriginator());
        }

        setupChildPointers();
    }

    private void setupChildPointers()
    {
        for ( ObjectAssociation childObject : childObjects )
        {
            childObject.setParentId(getId());
            childObject.setParentName(getCaseNumber());
            childObject.setParentType("CASE_FILE");
        }
        for ( PersonAssociation persAssoc : personAssociations)
        {
            personAssociationResolver(persAssoc);
        }
        for ( AcmParticipant ap : getParticipants() )
        {
            ap.setObjectId(getId());
            ap.setObjectType("CASE_FILE");
        }
    }

    @PreUpdate
    protected void beforeUpdate()
    {
        setupChildPointers();
    }
    private void personAssociationResolver (PersonAssociation personAssoc)
    {
        personAssoc.setParentId(getId());
        personAssoc.setParentType("CASE");

        personAssoc.getPerson().setPersonAssociations(Arrays.asList(personAssoc));
    }
    /**
     * CMIS object ID of the folder where the complaint's attachments/content files are stored.
     */
    @Column(name = "cm_case_ecm_folder_id")
    private String ecmFolderId;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "cm_parent_id")
    private Collection<ObjectAssociation> childObjects = new ArrayList<>();

    public Collection<ObjectAssociation> getChildObjects()
    {
        return Collections.unmodifiableCollection(childObjects);
    }

    public void addChildObject(ObjectAssociation childObject)
    {
        childObjects.add(childObject);
        childObject.setParentName(getCaseNumber());
        childObject.setParentType("CASE");
        childObject.setParentId(getId());
    }

    public PersonAssociation getOriginator() {
        return originator;
    }

    public void setOriginator(PersonAssociation originator) {
        this.originator = originator;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getCaseNumber()
    {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber)
    {
        this.caseNumber = caseNumber;
        setupChildPointers();
    }

    public String getCaseType()
    {
        return caseType;
    }

    public void setCaseType(String caseType)
    {
        this.caseType = caseType;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public Date getCreated()
    {
        return created;
    }

    @Override
    public void setCreated(Date created)
    {
        this.created = created;
    }

    @Override
    public String getCreator()
    {
        return creator;
    }

    @Override
    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    @Override
    public Date getModified()
    {
        return modified;
    }

    @Override
    public void setModified(Date modified)
    {
        this.modified = modified;
    }

    @Override
    public String getModifier()
    {
        return modifier;
    }

    @Override
    public void setModifier(String modifier)
    {
        this.modifier = modifier;
    }

    public String getEcmFolderPath()
    {
        return ecmFolderPath;
    }

    public void setEcmFolderPath(String ecmFolderPath)
    {
        this.ecmFolderPath = ecmFolderPath;
    }

    public String getEcmFolderId()
    {
        return ecmFolderId;
    }

    public void setEcmFolderId(String ecmFolderId)
    {
        this.ecmFolderId = ecmFolderId;
    }


    public Date getClosed()
    {
        return closed;
    }

    public void setClosed(Date closed)
    {
        this.closed = closed;
    }

    public String getDisposition()
    {
        return disposition;
    }

    public void setDisposition(String disposition)
    {
        this.disposition = disposition;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(Date incidentDate) {
        this.incidentDate = incidentDate;
    }

    public List<AcmParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<AcmParticipant> participants) {
        this.participants = participants;
    }

    public void setAssignee(String assigneeUserId)
    {
        boolean found = false;
        if ( participants != null )
        {
            for ( AcmParticipant p : participants )
            {
                if ( "assignee".equals(p.getParticipantType() ) )
                {
                    p.setParticipantLdapId(assigneeUserId);
                    found = true;
                    break;
                }
            }
        }

        if ( ! found )
        {
            AcmParticipant p = new AcmParticipant();
            p.setParticipantLdapId(assigneeUserId);
            p.setParticipantType("assignee");
            p.setObjectType("CASE_FILE");
            p.setObjectId(getId());
            participants.add(p);
        }
    }

    public String getAssignee()
    {
        if ( participants != null )
        {
            for ( AcmParticipant p : participants )
            {
                if ( "assignee".equals(p.getParticipantType() ) )
                {
                    return p.getParticipantLdapId();
                }
            }
        }

        return null;
    }

    @JsonGetter
    public List<ObjectAssociation> getReferences()
    {
        List<ObjectAssociation> retval = new ArrayList<>();

        if ( getChildObjects() != null )
        {
            for ( ObjectAssociation child : childObjects )
            {
                if ( "REFERENCE".equals(child.getAssociationType()) )
                {
                    retval.add(child);
                }
            }
        }

        return retval;
    }


    public ChangeCaseStatus getChangeCaseStatus() {
		return changeCaseStatus;
	}

	public void setChangeCaseStatus(ChangeCaseStatus changeCaseStatus) {
		this.changeCaseStatus = changeCaseStatus;
	}

	public List<String> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<String> approvers) {
        this.approvers = approvers;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public List<PersonAssociation> getPersonAssociations()
    {
        return personAssociations;
    }

    public void setPersonAssociations(List<PersonAssociation> personAssociations)
    {
        this.personAssociations = personAssociations;
    }

    public List<AcmMilestone> getMilestones()
    {
        return milestones;
    }

    public void setMilestones(List<AcmMilestone> milestones)
    {
        this.milestones = milestones;
    }

    @Override
    @JsonIgnore
    public String getObjectType()
    {
        return "caseFile";
    }

    @Override
    public String toString()
    {
        return "CaseFile{" +
                "id=" + id +
                ", caseNumber='" + caseNumber + '\'' +
                ", caseType='" + caseType + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", created=" + created +
                ", creator='" + creator + '\'' +
                ", modified=" + modified +
                ", modifier='" + modifier + '\'' +
                ", closed=" + closed +
                ", disposition='" + disposition + '\'' +
                ", ecmFolderPath='" + ecmFolderPath + '\'' +
                ", ecmFolderId='" + ecmFolderId + '\'' +
                '}';
    }
}