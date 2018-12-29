
package com.lilianghui.spring.starter.netty.rpc.entity;

import com.lilianghui.spring.starter.netty.rpc.entity.MessageRequest;
import com.lilianghui.spring.starter.netty.rpc.entity.MessageResponse;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MessageCallBack {

    private MessageRequest request;
    private MessageResponse response;
    private int httpTimeout;
    private Lock lock = new ReentrantLock();
    private Condition finish = lock.newCondition();

    public MessageCallBack(MessageRequest request, int httpTimeout) {
        this.request = request;
        this.httpTimeout = httpTimeout;
    }

    public Object start() throws InterruptedException {
        try {
            lock.lock();
            //设定一下超时时间，rpc服务器太久没有相应的话，就默认返回空吧。
            finish.await(this.httpTimeout, TimeUnit.SECONDS);
            if (this.response != null) {
                return this.response.getResult();
            } else {
                return null;
            }
        } finally {
            lock.unlock();
        }
    }

    public void over(MessageResponse reponse) {
        try {
            lock.lock();
            this.response = reponse;
            finish.signal();
        } finally {
            lock.unlock();
        }
    }
}