package com.lilianghui.spring.starter.gray.rule;


import com.lilianghui.spring.starter.gray.predicate.DiscoveryEnabledPredicate;
import com.lilianghui.spring.starter.gray.predicate.MetadataAwarePredicate;

public class MetadataAwareRule extends DiscoveryEnabledRule {
    public MetadataAwareRule() {
        this(new MetadataAwarePredicate());
    }

    public MetadataAwareRule(DiscoveryEnabledPredicate predicate) {
        super(predicate);
    }
}
