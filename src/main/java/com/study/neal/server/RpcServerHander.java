package com.study.neal.server;

import com.study.neal.protol.Request;
import com.study.neal.protol.Response;
import com.study.neal.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author yedunyao
 * @since 2020/11/10 0:35
 */
public class RpcServerHander extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request) msg;
        String method = request.getMethodName();
        String param = request.getParams()[0].toString();

        String result = "Not found!";
        HelloServiceImpl helloService = new HelloServiceImpl();
        if ("sayHello".equals(method)) {
            result = helloService.sayHello(param);
        } else if ("sayBye".equals(method)) {
            result = helloService.sayBye(param);
        }
        System.out.println("发送结果: " + result);

        Response response = new Response();
        response.setId(request.getId());
        response.setResult(result);
        ctx.writeAndFlush(response);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


    private static String getStringParam(String request) {
        int i = request.lastIndexOf("+");
        return request.substring(i + 1);
    }

    private static String getMethod(String request) {
        int i = request.lastIndexOf("+");
        return request.substring(0, i);
    }
}
