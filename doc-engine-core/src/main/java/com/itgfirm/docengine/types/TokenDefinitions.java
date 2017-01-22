package com.itgfirm.docengine.types;

import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class TokenDefinitions {

	@JsonDeserialize(contentAs = TokenDefinitionJpaImpl.class)
	@JsonSerialize(contentAs = TokenDefinitionJpaImpl.class)
	private TokenDefinitionJpaImpl[] _definitions;

	public TokenDefinitions() {
		// Default constructor for Spring
	}

	public TokenDefinitions(final TokenDefinitionJpaImpl[] definitions) {
		this._definitions = definitions;
	}

	public TokenDefinitions(final Collection<TokenDefinitionJpaImpl> definitions) {
		setDefinitions(definitions);
	}

	public TokenDefinitionJpaImpl[] getDefinitions() {
		return _definitions;
	}

	@JsonIgnore
	public Collection<TokenDefinitionJpaImpl> getDefinitionsList() {
		if (isNotNullOrEmpty(_definitions)) {
			return Arrays.asList(_definitions);
		}
		return null;
	}

	public void setDefinitions(TokenDefinitionJpaImpl[] definitions) {
			this._definitions = definitions;
	}

	@JsonIgnore
	public void setDefinitions(Collection<TokenDefinitionJpaImpl> definitions) {
		if (isNotNullOrEmpty(definitions)) {
			this._definitions = definitions.toArray(new TokenDefinitionJpaImpl[definitions.size()]);
		}
	}
}