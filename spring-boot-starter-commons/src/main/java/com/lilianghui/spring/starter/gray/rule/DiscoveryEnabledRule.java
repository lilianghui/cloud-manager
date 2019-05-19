package com.lilianghui.spring.starter.gray.rule;

import com.lilianghui.spring.starter.gray.predicate.DiscoveryEnabledPredicate;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.AvailabilityPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.PredicateBasedRule;
import org.springframework.util.Assert;

public abstract class DiscoveryEnabledRule extends PredicateBasedRule {
    private final CompositePredicate predicate;

    public DiscoveryEnabledRule(DiscoveryEnabledPredicate discoveryEnabledPredicate) {
        Assert.notNull(discoveryEnabledPredicate, "Parameter 'discoveryEnabledPredicate' can't be null");
        this.predicate = this.createCompositePredicate(discoveryEnabledPredicate, new AvailabilityPredicate(this, (IClientConfig)null));
    }

    public AbstractServerPredicate getPredicate() {
        return this.predicate;
    }

    private CompositePredicate createCompositePredicate(DiscoveryEnabledPredicate discoveryEnabledPredicate, AvailabilityPredicate availabilityPredicate) {
        return CompositePredicate.withPredicates(new AbstractServerPredicate[]{discoveryEnabledPredicate, availabilityPredicate}).build();
    }
}
