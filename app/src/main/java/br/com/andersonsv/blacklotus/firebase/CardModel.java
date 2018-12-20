package br.com.andersonsv.blacklotus.firebase;

import android.os.Parcel;
import android.os.Parcelable;

public class CardModel implements Parcelable {

    public static final Creator<CardModel> CREATOR = new Creator<CardModel>() {
        @Override
        public CardModel createFromParcel(Parcel in) {
            return new CardModel(in);
        }

        @Override
        public CardModel[] newArray(int size) {
            return new CardModel[size];
        }
    };

    private String id;
    private String name;
    private Integer numberOfCards;
    private String cost;
    private String rarity;
    private String type;
    private String image;

    private CardModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.numberOfCards = in.readInt();
        this.cost = in.readString();
        this.rarity = in.readString();
        this.type = in.readString();
        this.image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeInt(numberOfCards);
        parcel.writeString(cost);
        parcel.writeString(rarity);
        parcel.writeString(type);
        parcel.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public CardModel() {}

    public CardModel(String name, Integer numberOfCards,
                String cost, String rarity, String type, String image) {
        this.name = name;
        this.numberOfCards = numberOfCards;
        this.cost = cost;
        this.rarity = rarity;
        this.type  = type;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(Integer numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
