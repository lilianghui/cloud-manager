package com.lilianghui.config.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 */
public class ResponseFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(ResponseFilter.class);

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        try (InputStream responseDataStream = ctx.getResponseDataStream()) {
//            ResponseModel<Object> responseModel = new ResponseModel<>();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//            mapper.configure(MapperFeature.FAIL_ON_EMPTY_BEANS, false);
//
            if (responseDataStream == null) {
//                ctx.setResponseBody(mapper.writeValueAsString(new ResponseModel()));
                return null;
            }
            final String responseData = CharStreams.toString(new InputStreamReader(responseDataStream, "UTF-8"));
            int statusCode = ctx.getResponseStatusCode();
//
//
            if (statusCode >= 400 && statusCode < 500) {
                JsonNode jsonNode = mapper.readTree(responseData);
                JsonNode message = jsonNode.get("message");
                if (message == null) {
//                    responseModel.setMessage("");
                } else {
//                    responseModel.setMessage(message.textValue());
                }
//                responseModel.setData(new Object());
            } else if (statusCode >= 500) {
//                responseModel.setMessage("系统繁忙，请稍后重试");
//                responseModel.setData(new Object());
            } else {
//                responseModel.setData(jsonNode);
            }
            ctx.setSendZuulResponse(true);
//            ctx.setResponseBody(mapper.writeValueAsString(responseModel));

        } catch (IOException e) {
            log.warn("Error reading body", e);
        }
        return null;
    }
}

