package com.itgfirm.docengine.types.jpa;

import static com.itgfirm.docengine.types.jpa.TypeConstants.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author Justin Scott
 * 
 *         DocumentJpaImpl Data Model
 */
@Entity
@DiscriminatorValue(JPA_DSCRMNTR_DOCUMENT)
@JsonIdentityInfo(property = JSON_PROP_ID, generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class DocumentJpaImpl extends ContentJpaImpl {

}