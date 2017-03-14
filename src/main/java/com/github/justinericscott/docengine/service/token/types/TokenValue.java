package com.github.justinericscott.docengine.service.token.types;

import static com.github.justinericscott.docengine.util.Utils.RomanNumber.toRoman;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.justinericscott.docengine.models.TokenDefinition;

public class TokenValue {
	private static final Logger LOG = LoggerFactory.getLogger(TokenValue.class);
	public static final String NOT_DEFINED_TXT = "No Alt Text";
	public static final String XABLE_TXT = "XX";
	private final TokenDefinition definition;
	private final Object value;
	private String displayValue;

	public TokenValue(TokenDefinition definition, Object value) {
		if (definition == null)
			throw new IllegalArgumentException("TokenDefinition Object Must Not Be Null!");
		// FIXME
		// if ("INTEGER".equalsIgnoreCase(def.getDatatype()) ||
		// "DECIMAL".equalsIgnoreCase(def.getDatatype())) {
		// this.defaultValue = 0;
		// } else {
		// this.defaultValue = null;
		// }
		this.definition = definition;
		this.value = value;
	}

	public TokenDefinition getDefinition() {
		return definition;
	}

	public String getDisplayValue() {
		String name = definition.getName();
		boolean isXable = definition.isXable();
		boolean isRoman = definition.isRoman();
		if (displayValue == null) {
			if (isXable) {
				displayValue = XABLE_TXT;
			} else if (value == null) {
				displayValue = getAlternativeText();
			} else if (isRoman) {
				if (value instanceof String) {
					displayValue = toRoman(Integer.parseInt((String) value));
				} else if (value instanceof Number) {
					Number num = (Number) value;
					displayValue = toRoman(num.intValue());
				}
			} else if (value instanceof String) {
				displayValue = (String) value;
				if (StringUtils.left(name, 2).equals("is")) {
					if (displayValue.equals("0")) {
						displayValue = "False";
					} else if (displayValue.equals("1")) {
						displayValue = "True";
					}
				} else {
					if (StringUtils.isBlank(displayValue)) {
						displayValue = getAlternativeText();
					}
				}
			} else if (value instanceof Number) {
				Number num = (Number) value;
				if ((num.intValue() >= 0 || num.doubleValue() >= 0) && !StringUtils.left(name, 2).equals("is")) {
					double d = Double.parseDouble(num.toString());
					displayValue = NumberFormat.getNumberInstance(Locale.US).format(d);
				} else if (num.intValue() >= 0 && StringUtils.left(name, 2).equals("is")) {
					if (num.intValue() == 0) {
						LOG.trace("Found instance of a False Boolean for :" + name);
						displayValue = "False";
					} else if (num.intValue() == 1) {
						LOG.trace("Found instance of a True Boolean for :" + name);
						displayValue = "True";
					}
				}
			} else {
				displayValue = value.toString();
				if (StringUtils.isBlank(displayValue)) {
					displayValue = getAlternativeText();
				}
			}
		}
		return displayValue;
	}

	public Object getValue() {
		if (value == null) {
			return (Object) null;
		}
		return value;
	}

	private String getAlternativeText() {
		String altText = definition.getAltText();
		if (altText != null && !altText.isEmpty()) {
			return altText;
		} else {
			return "[" + NOT_DEFINED_TXT + ": " + definition.getName() + "]";
		}
	}
}