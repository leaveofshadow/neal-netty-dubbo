package com.study.neal.client;

import com.study.neal.api.HelloService;
import com.study.neal.protol.PacketCodecHandler;
import com.study.neal.proxy.ServiceInvokeHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author yedunyao
 * @since 2020/11/10 0:40
 */
public class Client {

    private static  Channel channel;

    public static void main(String[] args) throws Exception {
        startServer();

        // 等待连接
        TimeUnit.SECONDS.sleep(3);

        ServiceInvokeHandler serviceInvokeHandler = new ServiceInvokeHandler();
        serviceInvokeHandler.setChannel(channel);
        HelloService helloService = serviceInvokeHandler.bind(HelloService.class);
        String hello = helloService.sayHello("neal");
        System.out.println("result: " + hello);
        String bye = helloService.sayBye("neal");
        System.out.println("result: " + bye);

        System.in.read(); // 按任意键退出
    }

    static void startServer() {
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)   // 禁用negal算法
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline()
//                                    .addLast(new StringDecoder())
//                                    .addLast(new StringEncoder())
                                    .addLast(PacketCodecHandler.INSTANCE)
                                    .addLast(new RpcClientHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("localhost", 8080).sync();
            channel = channelFuture.channel();
//            channel.closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
//            worker.shutdownGracefully();
        }
    }

    private static void startConsoleThread(Channel channel) {
        Scanner scanner = new Scanner(System.in);

        new Thread(() -> {
            while (!Thread.interrupted()) {
                String line = scanner.nextLine();
                switch (line) {
                    case "sayHello":
                }
            }
        }).start();
    }


  /*  public static Object createProxy(final Class<?> serviceClass) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass}, (proxy, method, args) -> {
                    // 设置参数
                    String param = method.getName() + "+" + args[0];
                    client.setParam(param);
                    System.out.println("param: " + param);
                    return executor.submit(client).get();
                });
    }*/
}
