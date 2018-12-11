package br.com.andersonsv.blacklotus.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cards {
    @SerializedName("cards")
    private List<Card> cards;

    public List<Card> getCards() {
        return cards;
    }
}
