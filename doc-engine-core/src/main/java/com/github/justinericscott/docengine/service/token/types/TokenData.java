package com.github.justinericscott.docengine.service.token.types;

import java.util.ArrayList;
import java.util.Collection;

import com.github.justinericscott.docengine.types.TokenDefinitionJpaImpl;

public class TokenData {
	private final String name;
	private final Collection<TokenValue> values = new ArrayList<TokenValue>();

	public TokenData(final TokenDefinitionJpaImpl def) {
		this.name = def.getName();
	}

	public final void addValue(final TokenValue value) {
		values.add(value);
	}

	public final String getName() {
		return name;
	}

	public final TokenValue getValue() {
		if (values.size() > 0) {
			return values.iterator().next();
		}
		return null;
	}

	public final TokenValue getValue(final String phase) {
		for (final TokenValue v : values) {
			if (v.getDefinition().getPhase().equals(phase)) {
				return v;
			}
		}
		return null;
	}

	public final Iterable<TokenValue> getValues() {
		return values;
	}

	public final Iterable<TokenValue> getValues(final String phase) {
		final Collection<TokenValue> returnValues = new ArrayList<TokenValue>();
		for (final TokenValue v : values) {
			if (v.getDefinition().getPhase().equals(phase)) {
				returnValues.add(v);
			}
		}
		return returnValues;
	}
}