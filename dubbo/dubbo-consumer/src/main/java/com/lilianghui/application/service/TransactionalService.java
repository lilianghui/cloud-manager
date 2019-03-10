package com.lilianghui.application.service;

import com.lilianghui.entity.Item;
import com.lilianghui.entity.User;

public interface TransactionalService {

    int save(User user, Item item);

    User selectByPrimaryKey(Long id);

    Item selectItemByPrimaryKey(Long itemId);
}
