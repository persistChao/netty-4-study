package com.answer.netty.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Random;

/**
 * @descreption
 * @Author answer
 * @Date 2018/11/29 11 47
 */
public class Client {

    private int port;
    private Bootstrap b;

    public Client(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Random random = new Random(System.currentTimeMillis());
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    /**
                     * 我们给 pipeline 添加了三个 Handler, IdleStateHandler 这个 handler 是心跳机制的核心,
                     * 我们为客户端端设置了读写 idle 超时, 时间间隔是5s,
                     * 即如果客户端在间隔 5s 后都没有收到服务器的消息或向服务器发送消息, 则产生 ALL_IDLE 事件.
                     */
                    pipeline.addLast(new IdleStateHandler(0, 0, 5));
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, -4, 0));
                    pipeline.addLast(new ClientHandler());
                }
            });
            Channel ch = b.remoteAddress("127.0.0.1", port).connect().sync().channel();
            for (int i = 0; i < 10; i++) {
                String content = "客戶端 " + i;
                ByteBuf buf = ch.alloc().buffer();
                buf.writeInt(5 + content.getBytes().length);
                buf.writeByte(CustomHeartbeatHandler.CUSTOM_MSG);
                buf.writeBytes(content.getBytes());
                ch.writeAndFlush(buf);
                Thread.sleep(random.nextInt(20000));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Client(8880).run();
    }
}
