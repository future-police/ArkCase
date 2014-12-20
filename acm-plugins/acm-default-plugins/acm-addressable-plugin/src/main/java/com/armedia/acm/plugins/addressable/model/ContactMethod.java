package com.armedia.acm.plugins.addressable.model;

import com.armedia.acm.data.AcmEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "acm_contact_method")
public class ContactMethod implements Serializable, AcmEntity
{
    private static final long serialVersionUID = 1827685289454605556L;
    private transient final Logger log = LoggerFactory.getLogger(getClass());

    @Id
    @Column(name = "cm_contact_method_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElements({
		@XmlElement(name="created"),
		@XmlElement(name="initiatorDeviceDate"),
		@XmlElement(name="peopleDeviceDate")
		
	})
    @Column(name = "cm_contact_method_created", nullable = false, insertable = true, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @XmlElements({
		@XmlElement(name="creator"),
		@XmlElement(name="initiatorDeviceAddedBy"),
		@XmlElement(name="peopleDeviceAddedBy")
		
	})
    @Column(name = "cm_contact_method_creator", insertable = true, updatable = false)
    private String creator;

    @Column(name = "cm_contact_method_modified", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    @Column(name = "cm_contact_method_modifier")
    private String modifier;

    @Column(name = "cm_contact_method_status")
    private String status;

    @XmlElements({
		@XmlElement(name="type"),
		@XmlElement(name="contactType"),
		@XmlElement(name="initiatorDeviceType"),
		@XmlElement(name="peopleDeviceType")
		
	})
    @Column(name = "cm_contact_type")
    private String type;
    
    @Transient
    private List<String> types;

    @XmlElements({
		@XmlElement(name="value"),
		@XmlElement(name="contactValue"),
		@XmlElement(name="initiatorDeviceValue"),
		@XmlElement(name="peopleDeviceValue")
		
	})
    @Column(name = "cm_contact_value")
    private String value;

    @PrePersist
    protected void beforeInsert()
    {
        if ( getStatus() == null || getStatus().trim().isEmpty() )
        {
            setStatus("ACTIVE");
        }
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
    
	public List<String> getTypes() {
		return types;
	}
	
	public void setTypes(List<String> types) {
		this.types = types;
	}

	public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}