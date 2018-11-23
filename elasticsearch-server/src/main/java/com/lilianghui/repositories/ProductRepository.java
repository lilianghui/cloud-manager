package com.lilianghui.repositories;

import com.lilianghui.entity.Product;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
	
	//https://www.cnblogs.com/guozp/archive/2018/04/02/8686904.html
	
    @Query("{\"bool\" : {\"should\" : [{\"term\" : {\"name\" : \"?0\"}}, {\"term\" : {\"description\" : \"?0\"}}]}}}")
    List<Product> findByNameOrDescription(String query);
}
