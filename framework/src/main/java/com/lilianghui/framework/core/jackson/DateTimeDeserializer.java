package com.lilianghui.framework.core.jackson;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.lilianghui.framework.core.entity.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DateTimeDeserializer extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			if(StringUtils.isBlank(p.getText())){
				return null;
			}
			return DateUtils.parseDate(p.getText(), Constant.DATE_FORMAT_ARRAY);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new JsonParseException(p, "解析日期格式字符串出错", e);
		}
	}

}