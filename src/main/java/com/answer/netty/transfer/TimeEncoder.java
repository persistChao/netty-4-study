package com.answer.netty.transfer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @descreption
 * @Author answer
 * @Date 2018/11/28 19 37
 */
public class TimeEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        UnixTime time = (UnixTime)msg;
        ByteBuf encode = ctx.alloc().buffer(4);
        encode.writeInt(time.value());
        ctx.write(encode, promise);
    }
}
