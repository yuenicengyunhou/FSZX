package com.itsv.FSZHZX.model;

public class MyParticipant  {
    private String nickname;
    private String endpoint;
    private boolean isHost;
    private boolean isRaiseHand;
    private boolean getMuteMic;
    private boolean getVideoStatus;
    private String jid;
    private String photo;
    private boolean stage;
    private long userStartTime;
//    private boolean isSpeaker;
    private boolean isModerator;
/*boolean isSpeaker,*/
    public MyParticipant(String nickname, String endpoint, boolean isHost, boolean isRaiseHand, boolean getMuteMic, boolean getVideoStatus, String jid, String photo, boolean stage, long userStartTime,boolean isModerator) {
        this.nickname = nickname;
        this.endpoint = endpoint;
        this.isHost = isHost;
        this.isRaiseHand = isRaiseHand;
        this.getMuteMic = getMuteMic;
        this.getVideoStatus = getVideoStatus;
        this.jid = jid;
        this.photo = photo;
        this.stage = stage;
        this.userStartTime = userStartTime;
//        this.isSpeaker = isSpeaker;
        this.isModerator = isModerator;
    }

    public void setModerator(boolean moderator) {
        isModerator = moderator;
    }

    public boolean isModerator() {
        return isModerator;
    }

//    public boolean isSpeaker() {
//        return isSpeaker;
//    }

    public String getNickname() {
        return nickname;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public boolean isHost() {
        return isHost;
    }

    public boolean isRaiseHand() {
        return isRaiseHand;
    }

    public boolean isGetMuteMic() {
        return getMuteMic;
    }

    public boolean isGetVideoStatus() {
        return getVideoStatus;
    }

    public String getJid() {
        return jid;
    }

    public String getPhoto() {
        return photo;
    }

    public boolean isStage() {
        return stage;
    }

    public long getUserStartTime() {
        return userStartTime;
    }
}
