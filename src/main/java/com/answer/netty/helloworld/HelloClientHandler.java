package com.answer.netty.helloworld;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author: answer
 * @Date: 2018/11/27 14 31
 * @Descreption:
 */
public class HelloClientHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 每次收到消息的时候执行
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("Server say ... " + msg);
    }

    /**
     * 客户端在建立连接的时候 触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client close...");
        super.channelInactive(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client active...");
        super.channelActive(ctx);
    }
}
