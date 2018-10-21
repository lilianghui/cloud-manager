package com.lilianghui.framework.core.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class MergeEntity<M, S> {
    private M mainEntity;
    private List<S> insertEntity = Lists.newArrayList();
    private List<S> updateEntity = Lists.newArrayList();
    private Set<String> deletePrimaryKeys = Sets.newHashSet();
    private Object extra;
}
