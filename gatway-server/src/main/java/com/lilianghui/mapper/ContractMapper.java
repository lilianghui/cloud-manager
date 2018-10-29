package com.lilianghui.mapper;

import com.lilianghui.entity.Contract;
import com.lilianghui.framework.core.mapper.Mapper;

import java.util.List;

public interface ContractMapper extends Mapper<Contract> {

    List<Contract> selectContract(Contract contract);

}
