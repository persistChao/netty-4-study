package com.answer.netty.transfer;

import java.util.Date;

/**
 * @descreption
 * @Author answer
 * @Date 2018/11/28 18 38
 */
public class UnixTime {
    private final int value;

    public UnixTime(int value) {
        this.value = value;
    }

    public UnixTime() {
        this((int) (System.currentTimeMillis() / 1000L + 2208988800L));
    }

    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return new Date((value() - 2208988800L) * 1000L).toString();
    }

}
