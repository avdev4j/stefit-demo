package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

import com.mycompany.myapp.domain.enumeration.StatutLivraison;

/**
 * A Livraison.
 */
@Entity
@Table(name = "livraison")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Livraison implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "poids")
    private Float poids;

    @Column(name = "datelivraison")
    private LocalDate datelivraison;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatutLivraison status;

    @ManyToOne
    @JsonIgnoreProperties("livraisons")
    private Convoi convoi;

    @ManyToOne
    @JsonIgnoreProperties("livraisons")
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public Livraison code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Float getPoids() {
        return poids;
    }

    public Livraison poids(Float poids) {
        this.poids = poids;
        return this;
    }

    public void setPoids(Float poids) {
        this.poids = poids;
    }

    public LocalDate getDatelivraison() {
        return datelivraison;
    }

    public Livraison datelivraison(LocalDate datelivraison) {
        this.datelivraison = datelivraison;
        return this;
    }

    public void setDatelivraison(LocalDate datelivraison) {
        this.datelivraison = datelivraison;
    }

    public StatutLivraison getStatus() {
        return status;
    }

    public Livraison status(StatutLivraison status) {
        this.status = status;
        return this;
    }

    public void setStatus(StatutLivraison status) {
        this.status = status;
    }

    public Convoi getConvoi() {
        return convoi;
    }

    public Livraison convoi(Convoi convoi) {
        this.convoi = convoi;
        return this;
    }

    public void setConvoi(Convoi convoi) {
        this.convoi = convoi;
    }

    public Client getClient() {
        return client;
    }

    public Livraison client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Livraison)) {
            return false;
        }
        return id != null && id.equals(((Livraison) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Livraison{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", poids=" + getPoids() +
            ", datelivraison='" + getDatelivraison() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
