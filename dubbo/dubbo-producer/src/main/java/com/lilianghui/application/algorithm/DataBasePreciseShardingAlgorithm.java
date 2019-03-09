package com.lilianghui.application.algorithm;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
public class DataBasePreciseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        try {
            Long id = Long.valueOf(String.valueOf(preciseShardingValue.getValue()));
            long mod = id % collection.size();
            for (String name : collection) {
                if (name.endsWith(String.valueOf(mod))) {
                    return name;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return collection.iterator().next();
    }
}