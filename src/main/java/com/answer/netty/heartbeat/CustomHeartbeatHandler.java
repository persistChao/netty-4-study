package com.answer.netty.heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @descreption
 * @Author answer
 * @Date 2018/11/29 11 16
 */
public abstract class CustomHeartbeatHandler extends SimpleChannelInboundHandler<ByteBuf> {

    public static final byte PING_MSG = 1;
    public static final byte PONG_MSG = 2;
    public static final byte CUSTOM_MSG = 3;
    protected String name;
    private int heartbeatCount = 0;

    public CustomHeartbeatHandler(String name) {
        this.name = name;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        if (msg.getByte(4) == PING_MSG) {
            sendPongMsg(ctx);
        } else if (msg.getByte(4) == PONG_MSG) {
            System.out.println(name + " get pong msg from " + ctx.channel().remoteAddress());
        } else {
            handleData(ctx, msg);
        }
    }

    protected void sendPingMsg(ChannelHandlerContext ctx) {
        ByteBuf buf = ctx.alloc().buffer(5);
        buf.writeInt(5);
        buf.writeByte(PING_MSG);
        ctx.writeAndFlush(buf);
        heartbeatCount++;
        System.out.println(name + " send pong msg to " + ctx.channel().remoteAddress() + ",count:" + heartbeatCount);

    }

    protected void sendPongMsg(ChannelHandlerContext ctx) {
        ByteBuf buf = ctx.alloc().buffer(5);
        buf.writeInt(5);
        buf.writeByte(PONG_MSG);
        ctx.writeAndFlush(buf);
        heartbeatCount++;
        System.out.println(name + " send ping msg to " + ctx.channel().remoteAddress() + ",count:" + heartbeatCount);
    }

    /**
     * 如果客户端触发时间在 IdleStateEvent中（READER_IDLE WRITER_IDLE ALL_IDLE）中 则执行下边的方法
     * 在5秒钟没有收到服务器发来的消息或者没有客户端没有发送消息则触发事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // IdleStateHandler 所产生的 IdleStateEvent 的处理逻辑.
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent)evt;
            switch (e.state()) {
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    protected abstract void handleData(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("---" + ctx.channel().remoteAddress() + " is active---");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println("---" + ctx.channel().remoteAddress() + " is inactive---");
    }

    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        System.err.println("---READER_IDLE---");
    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        System.err.println("---WRITER_IDLE---");
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) {
        System.err.println("---ALL_IDLE---");
    }
}
