package com.itgfirm.docengine.types;

import java.util.Collection;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
		setDefinitions(definitions.toArray(new TokenDefinitionJpaImpl[definitions.size()]));
	}

	public TokenDefinitionJpaImpl[] getDefinitions() {
		return _definitions;
	}

	public void setDefinitions(TokenDefinitionJpaImpl[] definitions) {
			this._definitions = definitions;
	}
}