package ro.teamnet.bootstrap.dictionary.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A ElementRelation.
 */
@Entity
@Table(name = "T_ELEMENT_RELATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ElementRelation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private DictionaryRelation dictionaryRelation;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private DictionaryElement sourceElement;

    @ManyToOne
    @JoinColumn(name = "destination_id")
    private DictionaryElement destinationElement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DictionaryRelation getDictionaryRelation() {
        return dictionaryRelation;
    }

    public void setDictionaryRelation(DictionaryRelation dictionaryRelation) {
        this.dictionaryRelation = dictionaryRelation;
    }

    public DictionaryElement getSourceElement() {
        return sourceElement;
    }

    public void setSourceElement(DictionaryElement sourceElement) {
        this.sourceElement = sourceElement;
    }

    public DictionaryElement getDestinationElement() {
        return destinationElement;
    }

    public void setDestinationElement(DictionaryElement destinationElement) {
        this.destinationElement = destinationElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ElementRelation elementRelation = (ElementRelation) o;

        if (id != null ? !id.equals(elementRelation.id) : elementRelation.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "ElementRelation{" +
            "id=" + id +
            '}';
    }
}
