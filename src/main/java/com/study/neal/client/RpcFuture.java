package com.study.neal.client;

import com.study.neal.protol.Request;
import com.study.neal.protol.Response;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yedunyao
 * @since 2020/12/18 14:22
 */
public class RpcFuture {

    private static final Map<Long, Channel> CHANNELS = new ConcurrentHashMap();

    private static final Map<Long, RpcFuture> FUTURES = new ConcurrentHashMap();

    public static final AtomicLong REQUEST_ID = new AtomicLong(0L);

    private final long id;
    private final Channel channel;

    private final Lock lock = new ReentrantLock();
    private final Condition done = lock.newCondition();

    private volatile Object response;

    public RpcFuture(Channel channel, Request request) {
        this.channel = channel;
        this.id = request.getId();
        // put into waiting map.
        FUTURES.put(id, this);
        CHANNELS.put(id, channel);
    }

    public Object get() {
        lock.lock();
        try {
            while (response == null) {
                done.await();
                if (response != null) {
                    break;
                }
            }
            System.out.println("被唤醒 结果：" + response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return response;
    }

    public static void received(Channel channel, Response response) {
        try {
            RpcFuture future = FUTURES.remove(response.getId());
            if (future != null) {
                future.doReceived(response);
            }
        } finally {
            CHANNELS.remove(response.getId());
        }
    }

    public void doReceived(Response res) {
        lock.lock();
        try {
            response = res;
            if (done != null) {
                System.out.println("返回参数唤醒");
                done.signal();
            }
        } finally {
            lock.unlock();
        }
    }

}
