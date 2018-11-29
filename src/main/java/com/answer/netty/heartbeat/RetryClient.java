package com.answer.netty.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @descreption
 * @Author answer
 * @Date 2018/11/29 16 01
 */
public class RetryClient {
    private NioEventLoopGroup workGroup = new NioEventLoopGroup();
    private Channel channel;
    private Bootstrap bootstrap;

    public static void main(String[] args) throws Exception {
        RetryClient client = new RetryClient();
        client.start();
        client.sendData();
    }

    public void sendData() throws Exception {
        Random random = new Random(System.currentTimeMillis());
        if (channel != null && channel.isActive()) {
            for (int i = 0; i < 10000; i++) {
                String content = " Client Msg " + i;
                ByteBuf buf = channel.alloc().buffer(5 + content.getBytes().length);
                buf.writeByte(CustomHeartbeatHandler.CUSTOM_MSG);
                buf.writeBytes(content.getBytes());
                channel.writeAndFlush(buf);
            }
        }
        Thread.sleep(20000);
    }

    public void start() {
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new IdleStateHandler(0, 0, 5));
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, -4, 0));
                            pipeline.addLast(new RetryClientHandler(RetryClient.this));
                        }
                    });
            doConnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }
        ChannelFuture future = bootstrap.connect("127.0.0.1", 8880);
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    channel = future.channel();
                    System.out.println("connect to server successfully!");
                } else {
                    System.out.println("Failed to connect to server, try connect after 10s");
                    future.channel().eventLoop().schedule(new Runnable() {
                        public void run() {
                            doConnect();
                        }
                    }, 10, TimeUnit.SECONDS);
                }
            }
        });
    }

    ;
}
