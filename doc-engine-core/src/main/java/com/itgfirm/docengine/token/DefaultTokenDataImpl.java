package com.itgfirm.docengine.token;

import java.util.ArrayList;
import java.util.List;

import com.itgfirm.docengine.types.jpa.TokenDefinitionJpaImpl;

class DefaultTokenDataImpl {
	private final String name;
	private final List<DefaultTokenValueImpl> values = new ArrayList<DefaultTokenValueImpl>();

	public DefaultTokenDataImpl(TokenDefinitionJpaImpl def) {
		this.name = def.getName();
	}

	public void addValue(DefaultTokenValueImpl value) {
		getValues().add(value);
	}

	public String getName() {
		return name;
	}

	public DefaultTokenValueImpl getValue() {
		if (getValues().size() > 0) {
			return getValues().get(0);
		}
		return null;
	}

	public DefaultTokenValueImpl getValue(String phase) {
		for (DefaultTokenValueImpl v : values) {
			if (v.getDefinition().getPhase().equals(phase)) {
				return v;
			}
		}
		return null;
	}

	public List<DefaultTokenValueImpl> getValues() {
		return values;
	}

	public List<DefaultTokenValueImpl> getValues(String phase) {
		List<DefaultTokenValueImpl> returnValues = new ArrayList<DefaultTokenValueImpl>();
		for (DefaultTokenValueImpl v : values) {
			if (v.getDefinition().getPhase().equals(phase)) {
				returnValues.add(v);
			}
		}
		return returnValues;
	}
}