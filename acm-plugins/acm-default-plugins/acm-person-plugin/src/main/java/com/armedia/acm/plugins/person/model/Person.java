package com.armedia.acm.plugins.person.model;

import com.armedia.acm.data.AcmEntity;
import com.armedia.acm.plugins.addressable.model.ContactMethod;
import com.armedia.acm.plugins.addressable.model.PostalAddress;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by armdev on 4/7/14.
 */
@XmlRootElement
@Entity
@Table(name = "acm_person")
public class Person implements Serializable, AcmEntity
{
    private static final long serialVersionUID = 7413755227864370548L;
    private transient final Logger log = LoggerFactory.getLogger(getClass());

    @Id
    @TableGenerator(name = "acm_person_gen",
            table = "acm_person_id",
            pkColumnName = "cm_seq_name",
            valueColumnName = "cm_seq_num",
            pkColumnValue = "acm_person",
            initialValue = 100,
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "acm_person_gen")
    @Column(name = "cm_person_id")
    private Long id;

    @Column(name = "cm_person_title")
    private String title;

    @Column(name = "cm_person_company_name")
    private String company;

    @Column(name = "cm_person_status", insertable = true, updatable = false)
    private String status;

    @Column(name = "cm_given_name")
    private String givenName;

    @Column(name = "cm_family_name")
    private String familyName;

    @Column(name = "cm_person_hair_color")
    private String hairColor;

    @Column(name = "cm_person_eye_color")
    private String eyeColor;

    @Column(name = "cm_person_height_inches")
    private Long heightInInches;

    @Column(name = "cm_person_weight_pounds")
    private Long weightInPounds;

    @Column(name = "cm_person_date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "cm_person_date_married")
    @Temporal(TemporalType.DATE)
    private Date dateMarried;

    @Column(name = "cm_person_created", nullable = false, insertable = true, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(name = "cm_person_creator", insertable = true, updatable = false)
    private String creator;

    @Column(name = "cm_person_modified", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    @Column(name = "cm_person_modifier")
    private String modifier;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "acm_person_postal_address",
            joinColumns = { @JoinColumn(name="cm_person_id", referencedColumnName = "cm_person_id") },
            inverseJoinColumns = { @JoinColumn(name = "cm_address_id", referencedColumnName = "cm_address_id") }
    )
    private List<PostalAddress> addresses = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "acm_person_contact_method",
            joinColumns = { @JoinColumn(name="cm_person_id", referencedColumnName = "cm_person_id") },
            inverseJoinColumns = { @JoinColumn(name = "cm_contact_method_id", referencedColumnName = "cm_contact_method_id") }
    )
    private List<ContactMethod> contactMethods = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "acm_person_security_tag",
            joinColumns = @JoinColumn(name = "cm_person_id", referencedColumnName = "cm_person_id")
    )
    @Column(name = "cm_security_tag")
    private List<String> securityTags = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy="person")
    private List<PersonAlias> personAliases = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy ="person")
    private List<PersonAssociation> personAssociations = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "acm_person_identification",
            joinColumns = { @JoinColumn(name="cm_person_id", referencedColumnName = "cm_person_id") },
            inverseJoinColumns = { @JoinColumn(name = "cm_identification_id", referencedColumnName = "cm_identification_id", unique = true)
            }
    )
    private List<Identification> identifications = new ArrayList<>();
    
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "acm_person_organization",
            joinColumns = { @JoinColumn(name="cm_person_id", referencedColumnName = "cm_person_id") },
            inverseJoinColumns = { @JoinColumn(name = "cm_organization_id", referencedColumnName = "cm_organization_id") }
    )
    private List<Organization> organizations = new ArrayList<>();
    
    @PrePersist
    protected void beforeInsert()
    {
        if ( getStatus() == null || getStatus().trim().isEmpty() )
        {
            setStatus("ACTIVE");
        }     
       
        for ( PersonAlias pa : getPersonAliases() )
        {
            pa.setPerson(this);
        }
    }

    @PreUpdate
    protected void beforeUpdate()
    {
        for ( PersonAlias pa : getPersonAliases() )
        {
            pa.setPerson(this);
        }
    }

    @XmlTransient
    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @XmlTransient
    public String getGivenName()
    {
        return givenName;
    }

    public void setGivenName(String givenName)
    {
        this.givenName = givenName;
    }

    @XmlTransient
    public String getFamilyName()
    {
        return familyName;
    }

    public void setFamilyName(String familyName)
    {
        this.familyName = familyName;
    }

    @XmlTransient
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

    @XmlTransient
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

    @XmlTransient
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

    @XmlTransient
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

    @XmlTransient
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @XmlTransient
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @XmlTransient
    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    @XmlTransient
    public List<PostalAddress> getAddresses()
    {
        return addresses;
    }

    public void setAddresses(List<PostalAddress> addresses)
    {
        this.addresses = addresses;
    }

    @XmlTransient
    public List<ContactMethod> getContactMethods()
    {
        return contactMethods;
    }

    public void setContactMethods(List<ContactMethod> contactMethods)
    {
        this.contactMethods = contactMethods;
    }

    @XmlTransient
    public List<String> getSecurityTags()
    {
        return securityTags;
    }
    
    public void setSecurityTags(List<String> securityTags)
    {
        this.securityTags = securityTags;
    }

    @XmlTransient
    public List<PersonAlias> getPersonAliases() 
    {
        return personAliases;
    }

    public void setPersonAliases(List<PersonAlias> personAliases) 
    {
        this.personAliases = personAliases;
        for ( PersonAlias pa : personAliases )
        {
            pa.setPerson(this);
        }
    }

    // use @XmlTransient to prevent recursive XML when serializing containers that refer to this person
    @XmlTransient
    public List<PersonAssociation> getPersonAssociations()
    {
        return personAssociations;
    }

    public void setPersonAssociations(List<PersonAssociation> personAssociations)
    {
        this.personAssociations = personAssociations;
        
        for(PersonAssociation personAssoc : personAssociations)
        {
            personAssoc.setPerson(this);
        }
    }

    @XmlTransient
    public String getHairColor()
    {
        return hairColor;
    }

    public void setHairColor(String hairColor)
    {
        this.hairColor = hairColor;
    }

    @XmlTransient
    public String getEyeColor()
    {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor)
    {
        this.eyeColor = eyeColor;
    }

    @XmlTransient
    public Long getHeightInInches()
    {
        return heightInInches;
    }

    public void setHeightInInches(Long heightInInches)
    {
        this.heightInInches = heightInInches;
    }

    @XmlTransient
    public Long getWeightInPounds()
    {
        return weightInPounds;
    }

    public void setWeightInPounds(Long weightInPounds)
    {
        this.weightInPounds = weightInPounds;
    }

    @XmlTransient
    public Date getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    @XmlTransient
    public Date getDateMarried()
    {
        return dateMarried;
    }

    public void setDateMarried(Date dateMarried)
    {
        this.dateMarried = dateMarried;
    }

    @XmlTransient
    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    @XmlTransient
    public List<Identification> getIdentifications() {
        return identifications;
    }

    public void setIdentifications(List<Identification> identifications) {
        this.identifications = identifications;
    }
    
    public Person returnBase()
    {
    	return this;
    }
}
