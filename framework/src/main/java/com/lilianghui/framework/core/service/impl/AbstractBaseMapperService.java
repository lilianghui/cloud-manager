package com.lilianghui.framework.core.service.impl;

import com.github.pagehelper.Page;
import com.lilianghui.framework.core.entity.BaseEntity;
import com.lilianghui.framework.core.mapper.Mapper;
import com.lilianghui.framework.core.service.BaseMapperService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractBaseMapperService<T extends BaseEntity, M extends Mapper<T>> implements BaseMapperService<T> {

    @Autowired
    protected M mapper;

    protected Class<T> clazz;

    public AbstractBaseMapperService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.clazz = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Override
    public T selectOne(T record) {
        return mapper.selectOne(record);
    }

    @Override
    public List<T> select(T record) {
        return mapper.select(record);
    }

    @Override
    public List<T> selectAll() {
        return mapper.selectAll();
    }

    @Override
    public int selectCount(T record) {
        return mapper.selectCount(record);
    }

    @Override
    public T selectByPrimaryKey(Object key) {
        return mapper.selectByPrimaryKey(key);
    }

    @Override
    public int insert(T record) {
        return mapper.insert(record);
    }

    @Override
    public int insertSelective(T record) {
        return mapper.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKey(T record) {
        return mapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateForFieldByExample(T record, Example example, String... fields) {
        return mapper.updateForFieldByExample(record, example, fields);
    }

    @Override
    public int updateForFieldByPrimaryKey(T record, String... fields) {
        return mapper.updateForFieldByPrimaryKey(record, fields);
    }

    @Override
    public int updateByPrimaryKeySelective(T record) {
        return mapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int delete(T record) {
        return mapper.delete(record);
    }

    @Override
    public int deleteByPrimaryKey(Object key) {
        return mapper.deleteByPrimaryKey(key);
    }

    @Override
    public int deleteByPrimaryKeys(Serializable[] keys) {
        return mapper.deleteByPrimaryKeys(keys);
    }

    @Override
    public List<T> selectByPrimaryKeys(Serializable[] keys) {
        return mapper.selectByPrimaryKeys(keys);
    }

    @Override
    public List<T> selectByExample(Object example) {
        return mapper.selectByExample(example);
    }

    @Override
    public int selectCountByExample(Object example) {
        return mapper.selectCountByExample(example);
    }

    @Override
    public int deleteByExample(Object example) {
        return mapper.deleteByExample(example);
    }

    @Override
    public int updateByExample(T record, Object example) {
        return mapper.updateByExample(record, example);
    }

    @Override
    public int updateByExampleSelective(T record, Object example) {
        return mapper.updateByExampleSelective(record, example);
    }

    @Override
    public List<T> selectByExampleAndRowBounds(Object example, RowBounds rowBounds) {
        return mapper.selectByExampleAndRowBounds(example, rowBounds);
    }

    @Override
    public List<T> selectByRowBounds(T record, RowBounds rowBounds) {
        return mapper.selectByRowBounds(record, rowBounds);
    }

    @Override
    public Page<T> selectByRowBounds(T record) {
//		Page<T> list = (Page<T>) mapper.selectByRowBounds(record, new RowBounds(record.getStartRow(), record.getPageSize()));
//		record.setTotalCount(Long.valueOf(list.getTotal()).intValue());
        return null;
    }

    @Override
    public int mergeIntoForField(T record, String[] updateFields, String... fields) {
        return mapper.mergeIntoForField(record, updateFields, fields);
    }

    @Override
    public int mergeInto(T record, String... fields) {
        return mapper.mergeInto(record, fields);
    }

    @Override
    public int mergeIntoSelective(T record, String... fields) {
        return mapper.mergeIntoSelective(record, fields);
    }

    @Override
    public int insertList(List<T> recordList) {
        return mapper.insertList(recordList);
    }

    @Override
    public boolean existsWithPrimaryKey(Object key) {
        return mapper.existsWithPrimaryKey(key);
    }

    @Override
    public int duplicateSelectiveByPrimaryKey(List<T> recordList, String... fields) {
        return mapper.duplicateSelectiveByPrimaryKey(recordList, fields);
    }

    @Override
    public void saveOrUpdate(T record) throws Exception {
        if (null == record) {
            return;
        }
//		EntityColumn pkColumn = BridgeHelper.getPKColumn(clazz);
//		if (PlatformUtils.getPropertyValue(record, pkColumn.getProperty()) != null) {
//			updateByPrimaryKey(record);
//		} else {
//			insert(record);
//		}
    }

    @Override
    public void saveOrUpdate(List<T> records) throws Exception {
        if (CollectionUtils.isNotEmpty(records)) {
//			EntityColumn pkColumn = BridgeHelper.getPKColumn(clazz);
//			List<T> insert = new LinkedList<>();
//			List<T> update = new LinkedList<>();
//			for (T t : records) {
//				if (PlatformUtils.getPropertyValue(t, pkColumn.getProperty()) != null) {
//					update.add(t);
//				} else {
//					insert.add(t);
//				}
//			}
//			if (CollectionUtils.isNotEmpty(insert)) {
//				insertList(insert);
//			}
//			for (T t : update) {
//				updateByPrimaryKey(t);
//			}
        }
    }

    @Override
    public T selectOneByExample(Object example) {
        return mapper.selectOneByExample(example);
    }

    @Override
    public <T> T getMapper() {
        return (T) mapper;
    }
}
