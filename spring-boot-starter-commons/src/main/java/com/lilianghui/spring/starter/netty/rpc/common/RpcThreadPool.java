/**
 * @filename:RpcThreadPool.java
 *
 * Newland Co. Ltd. All rights reserved.
 *
 * @Description:rpc线程池封装
 * @author tangjie
 * @version 1.0
 *
 */
package com.lilianghui.spring.starter.netty.rpc.common;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RpcThreadPool {

    //独立出线程池主要是为了应对复杂耗I/O操作的业务，不阻塞netty的handler线程而引入
    //当然如果业务足够简单，把处理逻辑写入netty的handler（ChannelInboundHandlerAdapter）也未尝不可
    public static Executor getExecutor(int threads, int queues) {
        String name = "RpcThreadPool";
        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
                queues == 0 ? new SynchronousQueue<Runnable>()
                        : (queues < 0 ? new LinkedBlockingQueue<Runnable>()
                                : new LinkedBlockingQueue<Runnable>(queues)),
                new NamedThreadFactory(name, true), new AbortPolicyWithReport(name));
    }
}