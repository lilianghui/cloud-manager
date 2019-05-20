package com.lilianghui.spring.starter.brave.rocket;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.internal.Nullable;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.sleuth.instrument.messaging.SleuthMessagingProperties;
import org.springframework.messaging.Message;
import zipkin2.Endpoint;

import javax.annotation.Resource;

import static com.lilianghui.spring.starter.brave.rocket.SleuthRocketPropagation.ROCKET_TOPIC_TAG;

/**
 * TracingProducer
 */
@Aspect
public class SleuthRocketProducerAspect extends AbstractSleuthRocket implements BeanPostProcessor, MethodInterceptor {

    @Resource
    private SleuthMessagingProperties properties;

    public SleuthRocketProducerAspect(Tracing tracing) {
        super(tracing);

    }

    @Pointcut("execution(public * org.apache.rocketmq.spring.starter.core.ProducerBeanFactory.create*(..))")
    private void anyProducerFactory() {
    }

    @Around("anyProducerFactory()")
    public Object wrapProducerFactory(ProceedingJoinPoint pjp) throws Throwable {
        return createProxy(pjp.proceed());
    }

//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        if (bean instanceof DefaultMQProducer) {
//            return createProxy(bean);
//        }
//        return bean;
//    }

    public Object createProxy(Object bean) {
        ProxyFactoryBean factory = new ProxyFactoryBean();
        factory.setProxyTargetClass(true);
        factory.addAdvice(this);
        factory.setTarget(bean);
        return factory.getObject();
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        if (methodInvocation.getMethod().getName().startsWith("send")) {
            Span span = tracing.tracer().nextSpan();
            Result<Message> result = find(methodInvocation.getArguments(), Message.class);
            if (result.isSuccess()) {
                Message message = result.getValue();
                tracing.propagation().keys().forEach(key -> message.getHeaders().remove(key));
                injector.inject(span.context(), message.getHeaders());
                if (!span.isNoop()) {
//                if (record.key() instanceof String && !"".equals(record.key())) {
//                    span.tag(KafkaTags.KAFKA_KEY_TAG, record.key().toString());
//                }
                    String remoteServiceName = properties.getMessaging().getKafka().getRemoteServiceName();
                    if (remoteServiceName != null) {
                        span.remoteEndpoint(Endpoint.newBuilder().serviceName(remoteServiceName).build());
                    }
                    span.tag(ROCKET_TOPIC_TAG, "").name("send").kind(Span.Kind.PRODUCER).start();
                }
            }
            Result<SendCallback> callBack = find(methodInvocation.getArguments(), SendCallback.class);
            callBack.set(new TracingCallback(span, (SendCallback) callBack.getValue()));

            try (Tracer.SpanInScope ws = tracing.tracer().withSpanInScope(span)) {
                return methodInvocation.proceed();
            } catch (RuntimeException | Error e) {
                span.error(e).finish(); // finish as an exception means the callback won't finish the span
                throw e;
            }
        }
        return methodInvocation.proceed();
    }


    class TracingCallback implements SendCallback {
        final Span span;
        @Nullable
        final SendCallback wrappedCallback;


        TracingCallback(Span span, @Nullable SendCallback wrappedCallback) {
            this.span = span;
            this.wrappedCallback = wrappedCallback;
        }

        @Override
        public void onSuccess(SendResult sendResult) {
            span.finish();
            if (wrappedCallback != null) {
                wrappedCallback.onSuccess(sendResult);
            }
        }

        @Override
        public void onException(Throwable exception) {
            if (exception != null) span.error(exception);
            span.finish();
            if (wrappedCallback != null) {
                wrappedCallback.onException(exception);
            }
        }
    }

}
