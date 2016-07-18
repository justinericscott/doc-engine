package com.itgfirm.docengine.token;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itgfirm.docengine.types.TokenDefinition;

class DefaultTokenDataImpl implements TokenData {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(DefaultTokenDataImpl.class);
	private final String name;
	private final List<TokenValue> values = new ArrayList<TokenValue>();

	public DefaultTokenDataImpl(TokenDefinition def) {
		this.name = def.getName();
	}

	@Override
	public void addValue(TokenValue value) {
		getValues().add(value);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public TokenValue getValue() {
		if (getValues().size() > 0) {
			return getValues().get(0);
		}
		return null;
	}

	@Override
	public TokenValue getValue(String phase) {
		for (TokenValue v : values) {
			if (v.getDefinition().getPhase().equals(phase)) {
				return v;
			}
		}
		return null;
	}

	@Override
	public List<TokenValue> getValues() {
		return values;
	}

	@Override
	public List<TokenValue> getValues(String phase) {
		List<TokenValue> returnValues = new ArrayList<TokenValue>();
		for (TokenValue v : values) {
			if (v.getDefinition().getPhase().equals(phase)) {
				returnValues.add(v);
			}
		}
		return returnValues;
	}
}