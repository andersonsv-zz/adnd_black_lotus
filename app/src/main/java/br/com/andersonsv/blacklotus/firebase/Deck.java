package br.com.andersonsv.blacklotus.firebase;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Deck {

    private String mUserId;
    private String mName;
    private Boolean mChangeDeck;
    private String mColor1;
    private String mColor2;
    private String mColor3;
    private String mColor4;
    private String mColor5;

    private Date mTimestamp;

    public Deck() { } // Needed for Firebase

    public Deck(String userId, String name, Boolean changeDeck, String color1, String color2, String color3, String color4, String color5) {
        mUserId = userId;
        mName = name;
        mChangeDeck = changeDeck;
        mColor1 = color1;
        mColor2 = color2;
        mColor3 = color3;
        mColor4 = color4;
        mColor5 = color5;

    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public Boolean getmChangeDeck() {
        return mChangeDeck;
    }

    public void setmChangeDeck(Boolean mChangeDeck) {
        this.mChangeDeck = mChangeDeck;
    }

    public String getmColor1() {
        return mColor1;
    }

    public void setmColor1(String mColor1) {
        this.mColor1 = mColor1;
    }

    public String getmColor2() {
        return mColor2;
    }

    public void setmColor2(String mColor2) {
        this.mColor2 = mColor2;
    }

    public String getmColor3() {
        return mColor3;
    }

    public void setmColor3(String mColor3) {
        this.mColor3 = mColor3;
    }

    public String getmColor4() {
        return mColor4;
    }

    public void setmColor4(String mColor4) {
        this.mColor4 = mColor4;
    }

    public String getmColor5() {
        return mColor5;
    }

    public void setmColor5(String mColor5) {
        this.mColor5 = mColor5;
    }

    public Date getmTimestamp() {
        return mTimestamp;
    }

    public void setmTimestamp(Date mTimestamp) {
        this.mTimestamp = mTimestamp;
    }

    @ServerTimestamp
    public Date getTimestamp() { return mTimestamp; }

    public void setTimestamp(Date timestamp) { mTimestamp = timestamp; }
}
