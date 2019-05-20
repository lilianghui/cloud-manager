package com.lilianghui.spring.starter.brave.rocket;

import brave.propagation.Propagation;
import org.springframework.messaging.MessageHeaders;

import java.nio.charset.Charset;

public class SleuthRocketPropagation {


    static final Charset UTF_8 = Charset.forName("UTF-8");

    static final Propagation.Setter<MessageHeaders, String> HEADER_SETTER = (carrier, key, value) -> {
        carrier.remove(key);
        carrier.put(key, value);
    };

    static final Propagation.Getter<MessageHeaders, String> HEADER_GETTER = (carrier, key) -> {
        Object header = carrier.get(key);
        if (header == null) return null;
        return header.toString();
    };


    static final String ROCKET_KEY_TAG = "rocket.key";
    static final String ROCKET_TOPIC_TAG = "rocket.topic";
}
