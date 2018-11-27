package com.answer.netty.helloworld;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author: answer
 * @Date: 2018/11/27 14 07
 * @Descreption:
 */
public class HelloClient {
    private static final String host = "127.0.0.1";

    private static final int port = 7878;

    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
            .channel(NioSocketChannel.class)
            .handler(new HelloClientInitializer());

            //连接服务
            Channel channel = b.connect(host, port).sync().channel();
            //控制台输入
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {
                String line = in.readLine();
                if (line == null) {
                    continue;
                }

                channel.writeAndFlush(line + "\r\n");
            }
        }finally {
            group.shutdownGracefully();
        }
    }
}
