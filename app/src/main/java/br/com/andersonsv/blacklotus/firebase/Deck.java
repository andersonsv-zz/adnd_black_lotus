package br.com.andersonsv.blacklotus.firebase;

import com.google.firebase.firestore.IgnoreExtraProperties;
@IgnoreExtraProperties
public class Deck {

    private String userId;
    private String name;
    private Integer numberOfCards;
    private String description;
    private Boolean changeDeck;
    private String color1;
    private String color2;
    private String color3;
    private String color4;
    private String color5;

    public Deck() { }

    public Deck(String userId, String name, Integer numberOfCards,
                String description, Boolean changeDeck,
                String color1, String color2, String color3,
                String color4, String color5) {
        this.userId = userId;
        this.name = name;
        this.numberOfCards = numberOfCards;
        this.description = description;
        this.changeDeck = changeDeck;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
        this.color5 = color5;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getChangeDeck() {
        return changeDeck;
    }

    public String getColor1() {
        return color1;
    }

    public String getColor2() {
        return color2;
    }

    public String getColor3() {
        return color3;
    }

    public String getColor4() {
        return color4;
    }

    public String getDescription() {
        return description;
    }

    public Integer getNumberOfCards() {
        return numberOfCards;
    }

    public String getColor5() {
        return color5;
    }

    public void setNumberOfCards(Integer numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChangeDeck(Boolean changeDeck) {
        this.changeDeck = changeDeck;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public void setColor3(String color3) {
        this.color3 = color3;
    }

    public void setColor4(String color4) {
        this.color4 = color4;
    }

    public void setColor5(String color5) {
        this.color5 = color5;
    }
}
