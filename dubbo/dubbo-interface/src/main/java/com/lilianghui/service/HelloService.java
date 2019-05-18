package com.lilianghui.service;

import com.lilianghui.entity.User;

import java.util.List;

public interface HelloService {

    int save(User user);

    User selectByPrimaryKey(Object id);

    List<User> selectByRowBounds(int offset, int limit);
}
