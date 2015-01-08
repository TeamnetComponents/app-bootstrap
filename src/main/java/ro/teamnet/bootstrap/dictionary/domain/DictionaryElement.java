package ro.teamnet.bootstrap.dictionary.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import ro.teamnet.bootstrap.domain.util.CustomLocalDateSerializer;

import javax.persistence.*;
import java.util.Collection;

/**
 * An element (or key) of a dictionary.
 */
@Entity
@Table(name = "T_DICTIONARY_ELEMENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula("(select d.code from T_DICTIONARY d where d.id = dictionary_id)")
public class DictionaryElement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "description")
    private String description;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @Column(name = "activation_date", nullable = false)
    private LocalDate activationDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @Column(name = "inactivation_date", nullable = false)
    private LocalDate inactivationDate;

    @ManyToOne
    @JoinColumn(name="dictionary_id", nullable=false)
    private Dictionary dictionary;

    @OneToMany(mappedBy = "dictionaryElement")
    private Collection<DictionaryValue> values;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(LocalDate activationDate) {
        this.activationDate = activationDate;
    }

    public LocalDate getInactivationDate() {
        return inactivationDate;
    }

    public void setInactivationDate(LocalDate inactivationDate) {
        this.inactivationDate = inactivationDate;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public Collection<DictionaryValue> getValues() {
        return values;
    }

    public void setValues(Collection<DictionaryValue> values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DictionaryElement that = (DictionaryElement) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DictionaryElement{" +
            "id=" + id +
            ", code='" + code + '\'' +
            ", description='" + description + '\'' +
            ", activationDate=" + activationDate +
            ", inactivationDate=" + inactivationDate +
            ", dictionary=" + dictionary +
            '}';
    }
}
