package com.youshusoft.ack;

/**
 * 消息响应确认器
 */
public interface MsgAck {
    /**
     * 等待响应,指定超时时间
     * @param msgId
     * @param time
     * @return
     */
    boolean await(String msgId,int time);

    /**
     * 等待响应
     * @param msgId
     * @return
     */
    boolean await(String msgId);

    /**
     * 响应通知
     * @param msgId
     */
    void signal(String msgId);
    /**
     * 响应通知
     * @param msgId
     */
    void signalAll(String msgId);
}
