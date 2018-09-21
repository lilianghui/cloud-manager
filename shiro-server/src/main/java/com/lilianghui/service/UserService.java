package com.lilianghui.service;

import com.lilianghui.entity.User;
import com.lilianghui.framework.core.service.impl.AbstractBaseMapperService;
import com.lilianghui.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends AbstractBaseMapperService<User, UserMapper> {

    public List<User> findAll() {
        return mapper.findAll();
    }

}
