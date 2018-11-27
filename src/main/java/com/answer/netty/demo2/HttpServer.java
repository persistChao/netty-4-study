package com.answer.netty.demo2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

import java.net.InetSocketAddress;


/**
 * @Author: answer
 * @Date: 2018/11/2 14 47
 * @Descreption:
 */
public class HttpServer {
    private final int port;

    private final String host;

    public HttpServer(String host , int port) {
        this.port = port;
        this.host = host;
    }

    public static void main(String[] args) throws InterruptedException {
        new HttpServer("localhost" , 8090).start();

    }

    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(host , port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                  @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                      System.out.println("initChannel ch:" + ch);
                      ch.pipeline().addLast("decoder", new HttpRequestDecoder())
                              .addLast("encoder", new HttpResponseDecoder())
                              .addLast("aggregator", new HttpObjectAggregator(512*1024))
                              .addLast("handler" , new HttpHandler());
                    }
                }).option(ChannelOption.SO_BACKLOG ,128).childOption(ChannelOption.SO_KEEPALIVE , Boolean.TRUE);

        bootstrap.bind(host ,port).sync();
    }
}
