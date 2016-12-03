package com.itgfirm.docengine.types.jpa;

import static com.itgfirm.docengine.types.jpa.AbstractJpaModel.ModelConstants.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author Justin Scott
 * 
 *         DocumentJpaImpl InstanceJpaImpl Data Model
 */
@Entity
@DiscriminatorValue(JPA_DSCRMNTR_DOCUMENT)
@JsonIdentityInfo(property = JSON_PROP_ID, generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class DocumentInstanceJpaImpl extends InstanceJpaImpl {
	private static final Logger LOG = LoggerFactory.getLogger(DocumentInstanceJpaImpl.class);

	public DocumentInstanceJpaImpl() {
		// Default constructor for Spring
	}

	public DocumentInstanceJpaImpl(final DocumentJpaImpl document, final String projectId) {
		super(document);
		LOG.debug("Instantiating DocumentJpaImpl InstanceJpaImpl.");
		this.setProjectId(projectId);
		this.setContent(document);
	}

	public DocumentInstanceJpaImpl(final DocumentJpaImpl document, final String projectId, final String body) {
		this(document, projectId);
		this.setBody(body);
	}

	public DocumentInstanceJpaImpl(final DocumentJpaImpl document, final String projectId, final String body, final boolean isAdHoc) {
		this(document, projectId, body);
		this.setAdHoc(isAdHoc);
	}
}