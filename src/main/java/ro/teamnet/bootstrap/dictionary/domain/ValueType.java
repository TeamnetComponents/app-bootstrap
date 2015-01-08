package ro.teamnet.bootstrap.dictionary.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Dictionary value types. Each dictionary may define elements of one of these value types.
 */
@Entity
@DiscriminatorValue(value = "D_VALUE_TYPES")
public class ValueType extends DictionaryElement {
}
