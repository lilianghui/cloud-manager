package com.lilianghui.service;

import com.google.common.collect.Sets;
import com.lilianghui.entity.Contract;
import com.lilianghui.entity.User;
import com.lilianghui.framework.core.entity.MergeEntity;
import com.lilianghui.framework.core.protobuf.ProtocbufRedisSerializer;
import com.lilianghui.framework.core.service.impl.AbstractBaseMapperService;
import com.lilianghui.mapper.ContractMapper;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.starter.core.RocketMQTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ContractService extends AbstractBaseMapperService<Contract, ContractMapper> {

    public static final String TX_PRODUCER_GROUP = "transaction-message";
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    private ProtocbufRedisSerializer protocbufRedisSerializer = new ProtocbufRedisSerializer();

    public void transactional() throws Exception {
        Contract contract = new Contract();
        contract.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        contract.setContractCode("785458715878");
        //
        User user = new User();
        user.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        user.setTelPhone("13120931905");

        MergeEntity<User, Contract> mergeEntity = new MergeEntity<User, Contract>();
        mergeEntity.setMainEntity(user);
        mergeEntity.setDeletePrimaryKeys(Sets.newHashSet("1", "2"));
        mergeEntity.setInsertEntity(Arrays.asList(contract));
        mergeEntity.setUpdateEntity(Arrays.asList(contract));

        String topic = "message-ext-topic";
        String tags = "tag1";
        Message msg = new Message(topic,
                tags, user.getId(), protocbufRedisSerializer.serialize(user));

        rocketMQTemplate.sendMessageInTransaction(TX_PRODUCER_GROUP, msg, null);

//        springTransactionMQProducer.sendMessageInTransaction(mergeEntity, (Object) null, new TransactionListener() {
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
