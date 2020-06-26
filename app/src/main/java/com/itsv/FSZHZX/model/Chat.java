package com.itsv.FSZHZX.model;

public class Chat {

   public String message;
   public String name;
   public String fromJid;
   public String toJid;
   public int isMe; //0 他人

    public Chat(String message, String name, String fromJid, String toJid, int isMe) {
        this.message = message;
        this.name = name;
        this.fromJid = fromJid;
        this.toJid = toJid;
        this.isMe = isMe;
    }
}
