package com.lilianghui.spring.starter.brave.rocket;

import brave.Span;
import brave.SpanCustomizer;
import brave.Tracer;
import brave.Tracing;
import brave.kafka.clients.KafkaTags;
import brave.propagation.TraceContextOrSamplingFlags;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.common.message.MessageExt;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.lilianghui.spring.starter.brave.rocket.SleuthRocketPropagation.ROCKET_KEY_TAG;
import static com.lilianghui.spring.starter.brave.rocket.SleuthRocketPropagation.ROCKET_TOPIC_TAG;

/**
 * TracingProducer
 */
@Aspect
public class SleuthRocketListenerAspect extends AbstractSleuthRocket {

    public SleuthRocketListenerAspect(Tracing tracing) {
        super(tracing);

    }

    @Pointcut("execution(public * org.apache.rocketmq.client.consumer.DefaultMQPushConsumer.setMessageListener(..))")
    private void anyCreateListenerContainer() {
    } // NOSONAR

    @Around("anyCreateListenerContainer()")
    public Object wrapListenerContainerCreation(ProceedingJoinPoint pjp) throws Throwable {
        Result result = find(pjp.getArgs(), MessageListener.class);
        result.set(createProxy(result.getValue()));
        return pjp.proceed();
    }

    Object createProxy(Object bean) {
        ProxyFactoryBean factory = new ProxyFactoryBean();
        factory.setProxyTargetClass(true);
        factory.addAdvice(new MessageListenerMethodInterceptor(this.tracing));
        factory.setTarget(bean);
        return factory.getObject();
    }

    class MessageListenerMethodInterceptor<T extends MessageListener> implements MethodInterceptor {


        private final Tracing tracing;
        private final Tracer tracer;

        MessageListenerMethodInterceptor(Tracing tracing) {
            this.tracing = tracing;
            this.tracer = tracing.tracer();
        }

        @Override
        public Object invoke(MethodInvocation invocation)
                throws Throwable {
            if (!"onMessage".equals(invocation.getMethod().getName())) {
                return invocation.proceed();
            }

            Result<List> result = SleuthRocketListenerAspect.this.find(invocation.getArguments(), List.class);
            MessageExt messageExt = (MessageExt) result.getValue().get(0);

            Span span = nextSpan(messageExt).name("on-message").start();
            try (Tracer.SpanInScope ws = this.tracer.withSpanInScope(span)) {
                return invocation.proceed();
            } catch (RuntimeException | Error e) {
                String message = e.getMessage();
                if (message == null) message = e.getClass().getSimpleName();
                span.tag("error", message);
                throw e;
            } finally {
                span.finish();
            }
        }

        public Span nextSpan(MessageExt messageExt) {
            TraceContextOrSamplingFlags extracted = extractAndClearHeaders(messageExt);
            Span result = tracing.tracer().nextSpan(extracted);
            if (extracted.context() == null && !result.isNoop()) {
                addTags(messageExt, result);
            }
            return result;
        }

        TraceContextOrSamplingFlags extractAndClearHeaders(MessageExt messageExt) {
            TraceContextOrSamplingFlags extracted = extractor.extract(messageExt.getProperties());
            // clear propagation headers if we were able to extract a span
            if (extracted != TraceContextOrSamplingFlags.EMPTY) {
                tracing.propagation().keys().forEach(key -> messageExt.getProperties().remove(key));
            }
            return extracted;
        }

        /**
         * When an upstream context was not present, lookup keys are unlikely added
         */
        void addTags(MessageExt messageExt, SpanCustomizer result) {
//            if (record.key() instanceof String && !"".equals(record.key())) {
//                result.tag(ROCKET_KEY_TAG, record.key().toString());
//            }
//            result.tag(ROCKET_TOPIC_TAG, record.topic());
        }
    }
}
