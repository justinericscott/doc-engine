package com.itgfirm.docengine.types.jpa;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Document;

/**
 * @author Justin Scott
 * 
 *         DocumentJpaImpl Data Model
 */
@Entity
@DiscriminatorValue(TypeConstants.JPA_DISCRIMINATOR_DOCUMENT)
@JsonIdentityInfo(property = "@id", generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class DocumentJpaImpl extends ContentJpaImpl implements Document {

	public DocumentJpaImpl() {}

	public DocumentJpaImpl(String body) {
		super(body);
	}

	public DocumentJpaImpl(String contentCd, String body) {
		this(body);
		this.setContentCd(contentCd);
	}

	public DocumentJpaImpl(Content content) {
		super(content);
	}

	public DocumentJpaImpl(Content content, String contentCd) {
		this(content);
		this.setContentCd(contentCd);
	}

	public DocumentJpaImpl(Document document) {
		super(document);
	}

	public DocumentJpaImpl(Document document, String contentCd) {
		this(document);
		this.setContentCd(contentCd);
	}
}