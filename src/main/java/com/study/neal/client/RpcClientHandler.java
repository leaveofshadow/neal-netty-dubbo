package com.study.neal.client;

import com.study.neal.protol.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author yedunyao
 * @since 2020/11/10 0:49
 */
public class RpcClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public  void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = (Response) msg;
        System.out.println("收到消息：" + msg);
        RpcFuture.received(ctx.channel(), response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
