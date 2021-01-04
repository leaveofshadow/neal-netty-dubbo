package com.study.neal.proxy;

import com.study.neal.client.RpcFuture;
import com.study.neal.protol.Request;
import com.study.neal.protol.Response;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author yedunyao
 * @since 2020/11/10 0:13
 */
public class ServiceInvokeHandler implements InvocationHandler {

    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public <T> T bind(Class<T> tClass) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{tClass}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        long requestId = RpcFuture.REQUEST_ID.getAndIncrement();
        request.setId(requestId);
        request.setMethodName(method.getName());
        request.setParams(args);
        System.out.println("发送请求：" + request);
        RpcFuture rpcFuture = new RpcFuture(channel, request);
        channel.writeAndFlush(request).sync();
        return ((Response) rpcFuture.get()).getResult();
    }

}
