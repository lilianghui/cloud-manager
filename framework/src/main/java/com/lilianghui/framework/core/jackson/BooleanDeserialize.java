package com.lilianghui.framework.core.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class BooleanDeserialize extends JsonDeserializer<Boolean> {

	@Override
	public Boolean deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String text = p.getText();
		if (StringUtils.isBlank(text)) {
			return null;
		}
		if ("1".equalsIgnoreCase(text) || "Y".equalsIgnoreCase(text)|| "是".equalsIgnoreCase(text)) {
			return Boolean.TRUE;
		} else if ("0".equalsIgnoreCase(text) || "N".equalsIgnoreCase(text)|| "否".equalsIgnoreCase(text)) {
			return Boolean.FALSE;
		}
		return Boolean.valueOf(text);
	}
}
