package com.youshusoft.ack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 本地版消息确认器
 */
public class LocalMsgAck implements MsgAck {
    private static LocalMsgAck localMsgAck = new LocalMsgAck();
    public static LocalMsgAck getInstance(){
        return localMsgAck;
    }

    private LocalMsgAck() {
    }

    private Map<String,Note> map = new ConcurrentHashMap<String, Note>();
    public boolean await(String msgId, int time) {
        Note note = map.get(msgId);
        if(note == null){
            synchronized (this){
                note = map.get(msgId);
                if(note == null){
                    note = new Note();
                    map.put(msgId,note);
                }
            }
        }
        synchronized (note){
            try {
                note.wait(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        boolean bool = note.state;
        map.remove(msgId);
        return bool;
    }

    public boolean await(String msgId) {
        return await(msgId,0);
    }

    public void signal(String msgId) {
        Note note = map.get(msgId);
        if(note != null){
            note.setState(true);
            synchronized (note){
                note.notify();
            }
            
        }
        
    }

    public void signalAll(String msgId) {
        Note note = map.get(msgId);
        if(note != null){
            note.setState(true);
            synchronized (note){
                note.notifyAll();
            }
            
        }
    }

    public static void main(String[] args) throws Exception{
        new Thread(new Runnable() {
            public void run() {
                    System.out.println("msg id 1");
                   boolean bool =  LocalMsgAck.getInstance().await("1",10000);
                    System.out.println("msg id 1 " + bool + "|" + System.currentTimeMillis());
            }
        }).start();
        System.out.println("=================================");
        new Thread(new Runnable() {
            public void run() {
                    System.out.println("msg id 2");
                    boolean bool =  LocalMsgAck.getInstance().await("2",10000);
                    System.out.println("msg id 2 " + bool + "|" + System.currentTimeMillis());
            }
        }).start();
        TimeUnit.SECONDS.sleep(2);
        LocalMsgAck.getInstance().signal("1");
        TimeUnit.SECONDS.sleep(1);
        LocalMsgAck.getInstance().signal("2");
    }
    
    public static class Note{
        
        private boolean state;

        public boolean isState() {
            return state;
        }

        public void setState(boolean state) {
            this.state = state;
        }
    }
}
