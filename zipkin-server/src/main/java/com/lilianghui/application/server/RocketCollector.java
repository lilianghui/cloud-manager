/*
 * Copyright 2015-2018 The OpenZipkin Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.lilianghui.application.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zipkin2.CheckResult;
import zipkin2.collector.Collector;
import zipkin2.collector.CollectorComponent;
import zipkin2.collector.CollectorMetrics;
import zipkin2.collector.CollectorSampler;
import zipkin2.storage.SpanConsumer;
import zipkin2.storage.StorageComponent;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This collector polls a Kafka topic for messages that contain TBinaryProtocol big-endian encoded
 * lists of spans. These spans are pushed to a {@link SpanConsumer#accept span consumer}.
 *
 * <p>This collector uses a Kafka 0.10+ consumer.
 */
public final class RocketCollector extends CollectorComponent {
    private static final Logger LOG = LoggerFactory.getLogger(RocketCollector.class);

    public static Builder builder(ZipkinRocketCollectorProperties properties) {
        return new Builder(properties);
    }

    /**
     * Configuration including defaults needed to consume spans from a Kafka topic.
     */
    public static final class Builder extends CollectorComponent.Builder {
        ZipkinRocketCollectorProperties properties;
        Collector.Builder delegate = Collector.newBuilder(RocketCollector.class);
        CollectorMetrics metrics = CollectorMetrics.NOOP_METRICS;
        int streams = 1;

        public Builder(ZipkinRocketCollectorProperties properties) {
            this.properties = properties;
        }

        @Override
        public Builder storage(StorageComponent storage) {
            delegate.storage(storage);
            return this;
        }

        @Override
        public Builder sampler(CollectorSampler sampler) {
            delegate.sampler(sampler);
            return this;
        }

        @Override
        public Builder metrics(CollectorMetrics metrics) {
            if (metrics == null) {
                throw new NullPointerException("metrics == null");
            }
            this.metrics = metrics.forTransport("kafka");
            delegate.metrics(this.metrics);
            return this;
        }


        /**
         * Count of threads consuming the topic. Defaults to 1
         */
        public Builder streams(int streams) {
            this.streams = streams;
            return this;
        }

        @Override
        public RocketCollector build() {
            return new RocketCollector(this);
        }

    }

    final LazyRocketWorkers kafkaWorkers;
    final ZipkinRocketCollectorProperties properties;

    RocketCollector(Builder builder) {
        kafkaWorkers = new LazyRocketWorkers(builder);
        properties = builder.properties;
    }

    @Override
    public RocketCollector start() {
        kafkaWorkers.get();
        return this;
    }

    @Override
    public CheckResult check() {
        try {
            CheckResult failure = kafkaWorkers.failure.get(); // check the kafka workers didn't quit
            if (failure != null) {
                return failure;
            }
//            KafkaFuture<String> maybeClusterId = getAdminClient().describeCluster().clusterId();
//            maybeClusterId.get(1, TimeUnit.SECONDS);
            return CheckResult.OK;
        } catch (Exception e) {
            return CheckResult.failed(e);
        }
    }


    @Override
    public void close() {
        kafkaWorkers.close();
    }

    static final class LazyRocketWorkers {
        final int streams;
        final Builder builder;
        final AtomicReference<CheckResult> failure = new AtomicReference<>();
        final CopyOnWriteArrayList<RocketCollectorWorker> workers = new CopyOnWriteArrayList<>();
        volatile ExecutorService pool;

        LazyRocketWorkers(Builder builder) {
            this.streams = builder.streams;
            this.builder = builder;
        }

        ExecutorService get() {
            if (pool == null) {
                synchronized (this) {
                    if (pool == null) {
                        pool = compute();
                    }
                }
            }
            return pool;
        }

        void close() {
            ExecutorService maybePool = pool;
            if (maybePool == null) return;
            maybePool.shutdownNow();
            try {
                maybePool.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                // at least we tried
            }
        }

        ExecutorService compute() {
            ExecutorService pool =
                    streams == 1
                            ? Executors.newSingleThreadExecutor()
                            : Executors.newFixedThreadPool(streams);

            for (int i = 0; i < streams; i++) {
                RocketCollectorWorker worker = new RocketCollectorWorker(builder);
                workers.add(worker);
                pool.execute(guardFailures(worker));
            }

            return pool;
        }

        Runnable guardFailures(final Runnable delegate) {
            return () -> {
                try {
                    delegate.run();
                } catch (RuntimeException e) {
                    LOG.error("Kafka worker exited with exception", e);
                    failure.set(CheckResult.failed(e));
                } catch (Error e) {
                    LOG.error("Kafka worker exited with error", e);
                    failure.set(CheckResult.failed(new RuntimeException(e)));
                }
            };
        }
    }
}
