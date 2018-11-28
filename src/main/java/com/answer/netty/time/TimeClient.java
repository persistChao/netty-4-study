package com.answer.netty.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @descreption
 * @Author answer
 * @Date 2018/11/28 10 42
 */
public class TimeClient {
    private int port ;
    private String host;

    public TimeClient(String host,int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE , true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });

            //启动客户端
            Channel f = b.connect(host,port).sync().channel();
            f.writeAndFlush("hello netty!");
            f.writeAndFlush("hello netty!");
            f.writeAndFlush("hello netty!");
            //等待连接关闭
//            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new TimeClient("127.0.0.1" , 9999).run();
    }
}
