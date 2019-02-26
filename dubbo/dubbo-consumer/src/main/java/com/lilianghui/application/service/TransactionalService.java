package com.lilianghui.application.service;

import com.lilianghui.entity.Item;
import com.lilianghui.entity.User;

public interface TransactionalService {

    int save(User user, Item item);

}
