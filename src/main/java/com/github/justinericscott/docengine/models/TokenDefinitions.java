package com.github.justinericscott.docengine.models;

import java.util.Collection;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TokenDefinitions {

	@JsonDeserialize(contentAs = TokenDefinition.class)
	@JsonSerialize(contentAs = TokenDefinition.class)
	private TokenDefinition[] tokens;

	public TokenDefinitions() {
		// Default constructor for Spring
	}

	public TokenDefinitions(final TokenDefinition[] tokens) {
		this.tokens = tokens;
	}

	public TokenDefinitions(final Collection<TokenDefinition> tokens) {
		setTokens(tokens.toArray(new TokenDefinition[tokens.size()]));
	}

	public TokenDefinition[] getTokens() {
		return tokens;
	}

	public void setTokens(TokenDefinition[] tokens) {
			this.tokens = tokens;
	}
}