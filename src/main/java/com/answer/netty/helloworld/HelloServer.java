package com.answer.netty.helloworld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author: answer
 * @Date: 2018/11/27 11 07
 * @Descreption:
 */
public class HelloServer {
    /**
     * 服务端监听的端口地址
     */
    private static final int portNumber = 7878;

    public static void main(String[] args) {
        //Worker线程用于管理线程为Boss线程服务。
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            //这里 server服务端 使用ServerBootstrap  / client 客户端应该用 Bootstrap
            //server服务端 使用.childHandler /client 客户端使用 .handler
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new HelloServerInitializer());

            //绑定端口 建立连接 等待客户端连接和发送消息
            ChannelFuture f = bootstrap.bind(portNumber).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
