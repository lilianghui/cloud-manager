package com.lilianghui.controller;

import com.lilianghui.entity.Contract;
import com.lilianghui.repositories.ContractRepository;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/elastic/contract")
public class ContractController {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    private ContractRepository contractRepository;

    @RequestMapping("initContract")
    public boolean initContract(@RequestBody List<Contract> contracts) {
        contractRepository.saveAll(contracts);
        return true;
    }
}
