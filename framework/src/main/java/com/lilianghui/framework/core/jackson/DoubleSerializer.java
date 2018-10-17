package com.lilianghui.framework.core.jackson;

import java.io.IOException;
import java.text.DecimalFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DoubleSerializer extends JsonSerializer<Double> {
	private static DecimalFormat df = null;
	private String format = "######0.00";

	public DoubleSerializer(String format) {
		this.format = format;
	}

	@Override
	public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		if (value != null) {
			gen.writeString(getDecimalFormat().format(value));
		}
	}

	private DecimalFormat getDecimalFormat() {
		if (df == null) {
			synchronized (DoubleSerializer.class) {
				if (df == null) {
					df = new DecimalFormat(format);
				}
			}
		}
		return df;
	}
}
