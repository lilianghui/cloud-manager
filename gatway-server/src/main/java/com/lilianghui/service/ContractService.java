package com.lilianghui.service;

import com.google.common.collect.Sets;
import com.lilianghui.entity.Contract;
import com.lilianghui.entity.User;
import com.lilianghui.framework.core.entity.MergeEntity;
import com.lilianghui.framework.core.protobuf.ProtocbufRedisSerializer;
import com.lilianghui.framework.core.rocketmq.transaction.SpringTransactionMQProducer;
import com.lilianghui.framework.core.service.impl.AbstractBaseMapperService;
import com.lilianghui.mapper.ContractMapper;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ContractService extends AbstractBaseMapperService<Contract, ContractMapper> {

    @Resource
    private SpringTransactionMQProducer springTransactionMQProducer;
    private ProtocbufRedisSerializer protocbufRedisSerializer = new ProtocbufRedisSerializer();

    public void transactional() throws Exception {
        Contract contract = new Contract();
        contract.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        contract.setContractCode("785458715878");
        //
        User user = new User();
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setTelPhone("13120931905");

        MergeEntity<User,Contract> mergeEntity=new MergeEntity<User,Contract>();
        mergeEntity.setMainEntity(user);
        mergeEntity.setDeletePrimaryKeys(Sets.newHashSet("1","2"));
        mergeEntity.setInsertEntity(Arrays.asList(contract));
        mergeEntity.setUpdateEntity(Arrays.asList(contract));

        Message msg = new Message(springTransactionMQProducer.getRocketMQConfig().getTopic(),
                springTransactionMQProducer.getRocketMQConfig().getTag(), user.getId(), protocbufRedisSerializer.serialize(user));

        springTransactionMQProducer.sendMessageInTransaction(mergeEntity, (Object) null, new TransactionListener() {

            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                mapper.insert(contract);
                return LocalTransactionState.COMMIT_MESSAGE;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                return null;
            }
        });
//        springTransactionMQProducer.sendMessageInTransaction(msg, (Object) null, new TransactionListener() {
//
//            @Override
//            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
//                mapper.insert(contract);
//                return LocalTransactionState.COMMIT_MESSAGE;
//            }
//
//            @Override
//            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
//                return null;
//            }
//        });

    }
}
