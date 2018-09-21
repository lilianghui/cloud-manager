package com.lilianghui.mapper;

import com.lilianghui.entity.User;
import com.lilianghui.framework.core.mapper.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {
    List<User> findAll();
}
