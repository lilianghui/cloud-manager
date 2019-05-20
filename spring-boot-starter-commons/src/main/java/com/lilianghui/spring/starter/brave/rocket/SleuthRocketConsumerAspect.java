package com.lilianghui.spring.starter.brave.rocket;

import brave.Span;
import brave.Tracing;
import brave.propagation.TraceContextOrSamplingFlags;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import zipkin2.Endpoint;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TracingProducer
 */
@Aspect
public class SleuthRocketConsumerAspect extends AbstractSleuthRocket{


    public SleuthRocketConsumerAspect(Tracing tracing) {
        super(tracing);

    }

/*
    @Pointcut("execution(public * org.springframework.kafka.core.ConsumerFactory.createConsumer(..))")
    private void anyConsumerFactory() {
    } // NOSONAR


    @Around("anyConsumerFactory()")
    public Object wrapConsumerFactory(ProceedingJoinPoint pjp) throws Throwable {
        ConsumerRecords<K, V> records = delegate.poll(timeout);
        if (records.isEmpty() || tracing.isNoop()) return records;
        Map<String, Span> consumerSpansForTopic = new LinkedHashMap<>();
        for (TopicPartition partition : records.partitions()) {
            String topic = partition.topic();
            List<ConsumerRecord<K, V>> recordsInPartition = records.records(partition);
            for (int i = 0, length = recordsInPartition.size(); i < length; i++) {
                ConsumerRecord<K, V> record = recordsInPartition.get(i);
                TraceContextOrSamplingFlags extracted = extractor.extract(record.headers());

                // If we extracted neither a trace context, nor request-scoped data (extra),
                // make or reuse a span for this topic
                if (extracted.samplingFlags() != null && extracted.extra().isEmpty()) {
                    Span consumerSpanForTopic = consumerSpansForTopic.get(topic);
                    if (consumerSpanForTopic == null) {
                        consumerSpansForTopic.put(topic,
                                consumerSpanForTopic = tracing.tracer().nextSpan(extracted).name("poll")
                                        .kind(Span.Kind.CONSUMER)
                                        .tag(KafkaTags.KAFKA_TOPIC_TAG, topic)
                                        .start());
                    }
                    // no need to remove propagation headers as we failed to extract anything
                    injector.inject(consumerSpanForTopic.context(), record.headers());
                } else { // we extracted request-scoped data, so cannot share a consumer span.
                    Span span = tracing.tracer().nextSpan(extracted);
                    if (!span.isNoop()) {
                        span.name("poll").kind(Span.Kind.CONSUMER).tag(KafkaTags.KAFKA_TOPIC_TAG, topic);
                        if (remoteServiceName != null) {
                            span.remoteEndpoint(Endpoint.newBuilder().serviceName(remoteServiceName).build());
                        }
                        span.start().finish(); // span won't be shared by other records
                    }
                    // remove prior propagation headers from the record
                    tracing.propagation().keys().forEach(key -> record.headers().remove(key));
                    injector.inject(span.context(), record.headers());
                }
            }
        }
        consumerSpansForTopic.values().forEach(span -> {
            if (remoteServiceName != null) {
                span.remoteEndpoint(Endpoint.newBuilder().serviceName(remoteServiceName).build());
            }
            span.finish();
        });
        return this.kafkaTracing.consumer(consumer);
    }*/
}
