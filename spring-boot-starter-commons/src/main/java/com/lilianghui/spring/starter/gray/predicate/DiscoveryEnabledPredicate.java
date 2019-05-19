package com.lilianghui.spring.starter.gray.predicate;

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import javax.annotation.Nullable;

public abstract class DiscoveryEnabledPredicate extends AbstractServerPredicate {
    public DiscoveryEnabledPredicate() {
    }

    public boolean apply(@Nullable PredicateKey input) {
        return input != null && input.getServer() instanceof DiscoveryEnabledServer && this.apply((DiscoveryEnabledServer)input.getServer());
    }

    protected abstract boolean apply(DiscoveryEnabledServer var1);
}
