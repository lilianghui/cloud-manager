package com.lilianghui.client.fallback;

import com.lilianghui.client.IndexFeignClient;
import feign.hystrix.FallbackFactory;

import java.util.HashMap;
import java.util.Map;

public class IndexFeignClientFallbackFactory implements FallbackFactory<IndexFeignClient>, IndexFeignClient {
    private Throwable throwable;

    @Override
    public Map<String, Object> weather(String city) {
        return new HashMap<String, Object>(){{
            put("error","error"+throwable.getMessage());
        }};
    }

    @Override
    public IndexFeignClient create(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }
}
