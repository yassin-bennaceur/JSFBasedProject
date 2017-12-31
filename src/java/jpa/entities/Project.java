/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author yassinbennaceur
 */
@Entity
@Table(name = "project")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p")
    , @NamedQuery(name = "Project.findByClientName", query = "SELECT p FROM Project p WHERE p.projectPK.clientName = :clientName")
    , @NamedQuery(name = "Project.findByClientDepartmentNumber", query = "SELECT p FROM Project p WHERE p.projectPK.clientDepartmentNumber = :clientDepartmentNumber")
    , @NamedQuery(name = "Project.findByProjectName", query = "SELECT p FROM Project p WHERE p.projectPK.projectName = :projectName")
    , @NamedQuery(name = "Project.findByContactEmail", query = "SELECT p FROM Project p WHERE p.contactEmail = :contactEmail")
    , @NamedQuery(name = "Project.findByContactPassword", query = "SELECT p FROM Project p WHERE p.contactPassword = :contactPassword")})
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProjectPK projectPK;
    @Size(max = 50)
    @Column(name = "contact_email")
    private String contactEmail;
    @Size(max = 50)
    @Column(name = "contact_password")
    private String contactPassword;
    @ManyToMany(mappedBy = "projectCollection")
    private Collection<Consultant> consultantCollection;
    @JoinColumns({
        @JoinColumn(name = "client_name", referencedColumnName = "client_name", insertable = false, updatable = false)
        , @JoinColumn(name = "client_department_number", referencedColumnName = "client_department_number", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Client client;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Collection<Billable> billableCollection;

    public Project() {
    }

    public Project(ProjectPK projectPK) {
        this.projectPK = projectPK;
    }

    public Project(String clientName, short clientDepartmentNumber, String projectName) {
        this.projectPK = new ProjectPK(clientName, clientDepartmentNumber, projectName);
    }

    public ProjectPK getProjectPK() {
        return projectPK;
    }

    public void setProjectPK(ProjectPK projectPK) {
        this.projectPK = projectPK;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPassword() {
        return contactPassword;
    }

    public void setContactPassword(String contactPassword) {
        this.contactPassword = contactPassword;
    }

    @XmlTransient
    public Collection<Consultant> getConsultantCollection() {
        return consultantCollection;
    }

    public void setConsultantCollection(Collection<Consultant> consultantCollection) {
        this.consultantCollection = consultantCollection;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @XmlTransient
    public Collection<Billable> getBillableCollection() {
        return billableCollection;
    }

    public void setBillableCollection(Collection<Billable> billableCollection) {
        this.billableCollection = billableCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectPK != null ? projectPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.projectPK == null && other.projectPK != null) || (this.projectPK != null && !this.projectPK.equals(other.projectPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Project[ projectPK=" + projectPK + " ]";
    }
    
}
