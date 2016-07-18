package com.itgfirm.docengine.types.jpa;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.itgfirm.docengine.types.Document;
import com.itgfirm.docengine.types.DocumentInstance;

/**
 * @author Justin Scott
 * 
 * DocumentJpaImpl InstanceJpaImpl Data Model
 */
@JsonIdentityInfo( property = "@id",
		generator = ObjectIdGenerators.IntSequenceGenerator.class )
@Entity @DiscriminatorValue( TypeConstants.JPA_DISCRIMINATOR_DOCUMENT )
public class DocumentInstanceJpaImpl extends InstanceJpaImpl implements DocumentInstance {
	private static final Logger LOG = LogManager.getLogger(DocumentInstanceJpaImpl.class);

	public DocumentInstanceJpaImpl() { }
	
	public DocumentInstanceJpaImpl(Document document, String projectId) {
		super(document);
		LOG.debug("Instantiating DocumentJpaImpl InstanceJpaImpl.");
		this.setProjectId(projectId);
		this.setContent(document);
	}
	
	public DocumentInstanceJpaImpl(Document document, String projectId, String body) {
		this(document, projectId);
		this.setBody(body);
	}
	
	public DocumentInstanceJpaImpl(Document document, String projectId, String body, 
			boolean isAdHoc) {
		this(document, projectId, body);
		this.setAdHoc(isAdHoc);
	}
}