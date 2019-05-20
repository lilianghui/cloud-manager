package com.lilianghui.spring.starter.brave.rocket;

import brave.Tracing;
import brave.propagation.TraceContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.messaging.MessageHeaders;

public abstract class AbstractSleuthRocket {

    protected Tracing tracing;
    protected TraceContext.Injector<MessageHeaders> injector;
    protected final TraceContext.Extractor<MessageHeaders> extractor;


    protected AbstractSleuthRocket(Tracing tracing) {
        this.tracing = tracing;
        this.injector = tracing.propagation().injector(SleuthRocketPropagation.HEADER_SETTER);
        this.extractor = tracing.propagation().extractor(SleuthRocketPropagation.HEADER_GETTER);

    }

    protected <T> Result<T> find(Object[] args, Class<T> clazz) {
        if (args != null) {
            for (int index = 0; index < args.length; index++) {
                if (args[index] != null && clazz.isAssignableFrom(args[index].getClass())) {
                    return new Result<T>(args, index, (T) args[index]);
                }
            }
        }
        return new Result<T>(args, 0, null);
    }

    @Data
    @AllArgsConstructor
    public class Result<T> {
        private Object[] args;
        private int index;
        private T value;

        public void set(Object value) {
            if (isSuccess()) {
                args[this.index] = value;
            }
        }

        public boolean isSuccess() {
            return value != null;
        }
    }
}
