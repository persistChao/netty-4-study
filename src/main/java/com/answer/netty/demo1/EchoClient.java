package com.answer.netty.demo1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Author: answer
 * @Date: 2018/10/30 11 01
 * @Descreption:
 */
public class EchoClient {

//    static final boolean SSL = System.getProperty("ssl") != null;
//    static final String HOST = System.getProperty("host" , "127.0.0.1");
//    static final int PORT = Integer.parseInt(System.getProperty("port" , "8007"));
//    static final int SIZE = Integer.parseInt(System.getProperty("size" , "256"));

    private final String host;
    private final int port;

    public EchoClient(int port , String host) {
        this.port = port;
        this.host = host;
    }

//    static final String HOST = System.getProperty("host" , "127.0.0.1");
//    static final int PORT = Integer.parseInt(System.getProperty("port" ,"8040")) ;

    public void start() throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host , port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHanlder());
                        }
                    });
            ChannelFuture future = bootstrap.connect(host , port).sync();
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {

        new EchoClient(8040 , "127.0.0.1").start();
    }
}
