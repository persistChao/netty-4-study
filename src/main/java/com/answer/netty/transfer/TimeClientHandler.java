package com.answer.netty.transfer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * @descreption
 * @Author answer
 * @Date 2018/11/28 10 28
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter{

    private ByteBuf buf;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        buf = ctx.alloc().buffer(4);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        buf.release();
        buf = null;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf)msg;
//        try {
//            long currentMillions = (byteBuf.readUnsignedInt() - 2208988800L)*1000L;
//            System.out.println(new Date(currentMillions));
//            ctx.close();
//        }finally {
//            byteBuf.release();
//        }
        /////////////////////////////////////
//        ByteBuf m = (ByteBuf)msg;
//        buf.writeBytes(m);
//        m.release();
//        if (buf.readableBytes() == 4) {
//            long currentTimeMillis = (buf.readInt() - 2208988800L) * 1000L;
//            System.out.println(new Date(currentTimeMillis));
//            ctx.close();
//        }
        UnixTime time = (UnixTime)msg;
        System.out.println(time);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
