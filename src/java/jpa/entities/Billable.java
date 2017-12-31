/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author yassinbennaceur
 */
@Entity
@Table(name = "billable")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Billable.findAll", query = "SELECT b FROM Billable b")
    , @NamedQuery(name = "Billable.findByBillableId", query = "SELECT b FROM Billable b WHERE b.billableId = :billableId")
    , @NamedQuery(name = "Billable.findByStartDate", query = "SELECT b FROM Billable b WHERE b.startDate = :startDate")
    , @NamedQuery(name = "Billable.findByEndDate", query = "SELECT b FROM Billable b WHERE b.endDate = :endDate")
    , @NamedQuery(name = "Billable.findByHours", query = "SELECT b FROM Billable b WHERE b.hours = :hours")
    , @NamedQuery(name = "Billable.findByHourlyRate", query = "SELECT b FROM Billable b WHERE b.hourlyRate = :hourlyRate")
    , @NamedQuery(name = "Billable.findByBillableHourlyRate", query = "SELECT b FROM Billable b WHERE b.billableHourlyRate = :billableHourlyRate")
    , @NamedQuery(name = "Billable.findByDescription", query = "SELECT b FROM Billable b WHERE b.description = :description")})
public class Billable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "billable_id")
    private Long billableId;
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hours")
    private short hours;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "hourly_rate")
    private BigDecimal hourlyRate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "billable_hourly_rate")
    private BigDecimal billableHourlyRate;
    @Size(max = 50)
    @Column(name = "description")
    private String description;
    @Lob
    @Size(max = 65535)
    @Column(name = "artifacts")
    private String artifacts;
    @JoinColumn(name = "consultant_id", referencedColumnName = "consultant_id")
    @ManyToOne(optional = false)
    private Consultant consultantId;
    @JoinColumns({
        @JoinColumn(name = "client_name", referencedColumnName = "client_name")
        , @JoinColumn(name = "client_department_number", referencedColumnName = "client_department_number")
        , @JoinColumn(name = "project_name", referencedColumnName = "project_name")})
    @ManyToOne(optional = false)
    private Project project;

    public Billable() {
    }

    public Billable(Long billableId) {
        this.billableId = billableId;
    }

    public Billable(Long billableId, short hours, BigDecimal hourlyRate, BigDecimal billableHourlyRate) {
        this.billableId = billableId;
        this.hours = hours;
        this.hourlyRate = hourlyRate;
        this.billableHourlyRate = billableHourlyRate;
    }

    public Long getBillableId() {
        return billableId;
    }

    public void setBillableId(Long billableId) {
        this.billableId = billableId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public short getHours() {
        return hours;
    }

    public void setHours(short hours) {
        this.hours = hours;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public BigDecimal getBillableHourlyRate() {
        return billableHourlyRate;
    }

    public void setBillableHourlyRate(BigDecimal billableHourlyRate) {
        this.billableHourlyRate = billableHourlyRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(String artifacts) {
        this.artifacts = artifacts;
    }

    public Consultant getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(Consultant consultantId) {
        this.consultantId = consultantId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (billableId != null ? billableId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Billable)) {
            return false;
        }
        Billable other = (Billable) object;
        if ((this.billableId == null && other.billableId != null) || (this.billableId != null && !this.billableId.equals(other.billableId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.Billable[ billableId=" + billableId + " ]";
    }
    
}
