package com.youshusoft.ack;

/**
 * 消息响应确认器
 */
public interface MsgAck {
    boolean await(String msgId,int time);

    boolean await(String msgId);

    void signal(String msgId);

    void signalAll(String msgId);
}
