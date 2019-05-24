package com.lilianghui.spring.starter.brave.rocket;

import brave.propagation.Propagation;

import java.nio.charset.Charset;
import java.util.Map;

public class SleuthRocketPropagation {


    static final Charset UTF_8 = Charset.forName("UTF-8");

    static final Propagation.Setter<Map<String, String>, String> HEADER_SETTER = (carrier, key, value) -> {
        carrier.remove(key);
        carrier.put(key, value);
    };

    static final Propagation.Getter<Map<String, String>, String> HEADER_GETTER = (carrier, key) -> {
        String header = carrier.get(key);
        if (header == null) return null;
        return header;
    };


    static final String ROCKET_KEY_TAG = "rocket.key";
    static final String ROCKET_TOPIC_TAG = "rocket.topic";
}
