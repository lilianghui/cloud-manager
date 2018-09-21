package com.lilianghui.config;


import com.lilianghui.utils.ProtobufUtils;
import org.apache.commons.lang.ArrayUtils;
import org.assertj.core.util.Lists;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 存在问题,必须设置对应type,这样的话RedisTemplate单例就没有办法是用了!,是用多例
 */
public class ProtocbufRedisSerializer implements RedisSerializer {


    @Override
    public byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        try {
            byte[] sampleNameByte = t.getClass().getName().getBytes();//类名
            byte[] data = ProtobufUtils.serialize((Object) t, (Class<Object>) t.getClass());//数据
            byte[] legth = intToBytes2(sampleNameByte.length);//类名长度
            return ArrayUtils.addAll(ArrayUtils.addAll(legth, sampleNameByte), data);
        } catch (Exception ex) {
            throw new SerializationException("Cannot serialize", ex);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (ArrayUtils.isEmpty(bytes)) {
            return null;
        }
        try {
            LinkedList<Byte> linkedList = new LinkedList<>();
            for (int i = 0; i < bytes.length; i++) {
                linkedList.add(new Byte(bytes[i]));
            }
            int length = byteToInt2(pop(linkedList, 4));//取长度
            String className = new String(pop(linkedList, length));//取类名
            byte[] data = pop(linkedList, null);//取数据
            return ProtobufUtils.deSerialize(data, Class.forName(className));
        } catch (Exception ex) {
            throw new SerializationException("Cannot deserialize", ex);
        }
    }

    private byte[] pop(LinkedList<Byte> linkedList, Integer length) {
        length = length == null ? linkedList.size() : length;
        byte[] data = new byte[length];
        for (int i = 0; i < length; i++) {
            data[i] = linkedList.pop();
        }
        return data;
    }


    private int byteToInt2(byte[] b) {

        int mask = 0xff;
        int temp = 0;
        int n = 0;
        for (int i = 0; i < b.length; i++) {
            n <<= 8;
            temp = b[i] & mask;
            n |= temp;
        }
        return n;
    }

    private byte[] intToBytes2(int n) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (n >> (24 - i * 8));

        }
        return b;
    }
}