package com.itsv.FSZHZX.model;

public class Profile {

    private String word;
    private String content;

    public String getWord() {
        return word;
    }

    public String getContent() {
        return content;
    }

    public Profile(String word, String content) {
        this.word = word;
        this.content = content;
    }
}
