package com.youshusoft.ack;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * redis版消息确认器
 */
public class RedisMsgAck implements MsgAck{
    public RedisMsgAck(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private RedisTemplate<String,String> redisTemplate;
    public boolean await(String msgId, int time) {
        String val = redisTemplate.opsForList().leftPop("ma:" + msgId,time, TimeUnit.MILLISECONDS);
        return val != null;
    }

    public boolean await(String msgId) {
        String val = redisTemplate.opsForList().leftPop("ma:" + msgId);
        return val != null;
    }
    public void signal(String msgId){
        signal(msgId,1L,TimeUnit.MINUTES);
    }
    public void signal(String msgId,Long timeout,TimeUnit timeUnit) {
        String key = "ma:" + msgId;
        redisTemplate.opsForList().leftPush(key,"1");
        redisTemplate.expire(key,timeout,timeUnit);
    }

    public void signalAll(String msgId) {

    }
}
