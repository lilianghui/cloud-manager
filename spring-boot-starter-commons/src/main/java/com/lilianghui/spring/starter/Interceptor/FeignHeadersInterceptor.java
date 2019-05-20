package com.lilianghui.spring.starter.Interceptor;

import brave.Span;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static brave.internal.HexCodec.writeHexLong;

@Component
public class FeignHeadersInterceptor implements RequestInterceptor {

    private static final String LEGACY_EXPORTABLE_NAME = "X-Span-Export";
    private static final String LEGACY_PARENT_ID_NAME = "X-B3-ParentSpanId";
    private static final String LEGACY_TRACE_ID_NAME = "X-B3-TraceId";
    private static final String LEGACY_SPAN_ID_NAME = "X-B3-SpanId";
    private static final String LEGACY_SAMPLE_ID_NAME = "X-B3-Sampled";


    @Resource
    private brave.Tracing tracing;

    private Set<String> ignoreHeaders = new HashSet<>();

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest request = RequestContextHolder.getHttpServletRequest();
        Span span = this.tracing.tracer().nextSpan();


        Optional.ofNullable(request.getHeader(LEGACY_SAMPLE_ID_NAME)).orElseGet(() -> {
            requestTemplate.header(LEGACY_SAMPLE_ID_NAME, span.context().sampled() ? "1" : "0");
            return "";
        });
        Optional.ofNullable(request.getHeader(LEGACY_TRACE_ID_NAME)).orElseGet(() -> {
            requestTemplate.header(LEGACY_TRACE_ID_NAME, getId(span.context().traceId()));
            return "";
        });
        Optional.ofNullable(request.getHeader(LEGACY_PARENT_ID_NAME)).orElseGet(() -> {
            requestTemplate.header(LEGACY_PARENT_ID_NAME, getId(span.context().parentId()));
            return "";
        });
        Optional.ofNullable(request.getHeader(LEGACY_SPAN_ID_NAME)).orElseGet(() -> {
            requestTemplate.header(LEGACY_SPAN_ID_NAME, getId(span.context().spanId()));
            return "";
        });

//        Optional.ofNullable(request).map(httpServletRequest -> httpServletRequest.getHeaderNames())
//                .map(Collections::list).filter(key -> !ignoreHeaders.contains(key))
//                .orElseGet(ArrayList::new)
//                .forEach(name -> requestTemplate.header(name, request.getHeader(name)));
    }

    private String getId(long id) {
        char[] result = new char[16];
        writeHexLong(result, 0, id);
        return new String(result);
    }

}
