package com.lilianghui.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String id;
    private String account;
    private String name;
    private int age;
}
