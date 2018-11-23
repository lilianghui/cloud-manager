package com.lilianghui.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "product", type = "simple", shards = 1, replicas = 1, refreshInterval = "-1")
@Data
public class Product {

    private Long id;

    @Field(type = FieldType.Text)
    private String sku;

    private String name;

    private String description;

}
