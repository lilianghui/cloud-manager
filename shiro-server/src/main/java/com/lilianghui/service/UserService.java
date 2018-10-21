package com.lilianghui.service;

import com.lilianghui.entity.User;
import com.lilianghui.framework.core.service.impl.AbstractBaseMapperService;
import com.lilianghui.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService extends AbstractBaseMapperService<User, UserMapper> {

    public List<User> findAll() {
        return mapper.findAll();
    }
}
