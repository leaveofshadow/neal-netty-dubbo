package com.study.neal.protol;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

@ChannelHandler.Sharable
public class PacketCodecHandler extends MessageToMessageCodec<ByteBuf, Packet> {
    public static final PacketCodecHandler INSTANCE = new PacketCodecHandler();

    private PacketCodecHandler() {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        byte type = byteBuf.readByte();
        Class<? extends Packet> clazz = Response.class;
        if (type == 1) {
            clazz = Request.class;
        }

        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Object obj = JSON.parseObject(bytes, clazz);
        out.add(obj);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> out) {
        ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
        byte[] bytes = JSON.toJSONBytes(packet);

        // 类型
        if (packet instanceof Request) {
            byteBuf.writeByte(1);
        } else {
            byteBuf.writeByte(0);
        }

        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        out.add(byteBuf);
    }
}
