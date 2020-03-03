package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Convoi.
 */
@Entity
@Table(name = "convoi")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Convoi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label")
    private String label;

    @OneToMany(mappedBy = "convoi")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Livraison> livraisons = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public Convoi label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<Livraison> getLivraisons() {
        return livraisons;
    }

    public Convoi livraisons(Set<Livraison> livraisons) {
        this.livraisons = livraisons;
        return this;
    }

    public Convoi addLivraison(Livraison livraison) {
        this.livraisons.add(livraison);
        livraison.setConvoi(this);
        return this;
    }

    public Convoi removeLivraison(Livraison livraison) {
        this.livraisons.remove(livraison);
        livraison.setConvoi(null);
        return this;
    }

    public void setLivraisons(Set<Livraison> livraisons) {
        this.livraisons = livraisons;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Convoi)) {
            return false;
        }
        return id != null && id.equals(((Convoi) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Convoi{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            "}";
    }
}
