package ro.teamnet.bootstrap.dictionary.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Collection;

/**
 * A dictionary collection.
 * Used to define a hierarchy of dictionaries and other dictionary collections.
 */
@Entity
@Table(name = "T_DICTIONARY_COLLECTION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DictionaryCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name="parent_collection_id", nullable=true)
    private DictionaryCollection parentCollection;

    @OneToMany(mappedBy = "parentCollection")
    private Collection<DictionaryCollection> childCollections;

    @OneToMany(mappedBy = "collection")
    private Collection<Dictionary> dictionaries;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DictionaryCollection getParentCollection() {
        return parentCollection;
    }

    public void setParentCollection(DictionaryCollection parentCollection) {
        this.parentCollection = parentCollection;
    }

    public Collection<DictionaryCollection> getChildCollections() {
        return childCollections;
    }

    public void setChildCollections(Collection<DictionaryCollection> childCollections) {
        this.childCollections = childCollections;
    }

    public Collection<Dictionary> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(Collection<Dictionary> dictionaries) {
        this.dictionaries = dictionaries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DictionaryCollection dictionaryCollection = (DictionaryCollection) o;

        if (id != null ? !id.equals(dictionaryCollection.id) : dictionaryCollection.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "DictionaryCollection{" +
            "id=" + id +
            ", code='" + code + "'" +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
