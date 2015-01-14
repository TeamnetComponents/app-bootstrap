package ro.teamnet.bootstrap.dictionary.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A DictionaryRelation.
 */
@Entity
@Table(name = "T_DICTIONARY_RELATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DictionaryRelation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "required")
    private Boolean required;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Dictionary sourceDictionary;

    @Column(name = "multiple_sources")
    private Boolean multipleSources;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    private Dictionary destinationDictionary;

    @Column(name = "multiple_destinations")
    private Boolean multipleDestinations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getMultipleSources() {
        return multipleSources;
    }

    public void setMultipleSources(Boolean multipleSources) {
        this.multipleSources = multipleSources;
    }

    public Boolean getMultipleDestinations() {
        return multipleDestinations;
    }

    public void setMultipleDestinations(Boolean multipleDestinations) {
        this.multipleDestinations = multipleDestinations;
    }

    public Dictionary getSourceDictionary() {
        return sourceDictionary;
    }

    public void setSourceDictionary(Dictionary sourceDictionary) {
        this.sourceDictionary = sourceDictionary;
    }

    public Dictionary getDestinationDictionary() {
        return destinationDictionary;
    }

    public void setDestinationDictionary(Dictionary destinationDictionary) {
        this.destinationDictionary = destinationDictionary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DictionaryRelation dictionaryRelation = (DictionaryRelation) o;

        if (id != null ? !id.equals(dictionaryRelation.id) : dictionaryRelation.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "DictionaryRelation{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", required='" + required + "'" +
            ", multipleSources='" + multipleSources + "'" +
            ", multipleDestinations='" + multipleDestinations + "'" +
            '}';
    }
}
