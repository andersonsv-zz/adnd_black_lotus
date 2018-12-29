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
    private Integer quantity;
    private String cost;
    private String rarity;
    private String type;
    private String image;
    private String text;
    private String power;
    private String toughness;
    private String setName;

    private CardModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.quantity = in.readInt();
        this.cost = in.readString();
        this.rarity = in.readString();
        this.type = in.readString();
        this.image = in.readString();
        this.text = in.readString();
        this.power = in.readString();
        this.toughness = in.readString();
        this.setName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeInt(quantity);
        parcel.writeString(cost);
        parcel.writeString(rarity);
        parcel.writeString(type);
        parcel.writeString(image);
        parcel.writeString(text);
        parcel.writeString(power);
        parcel.writeString(toughness);
        parcel.writeString(setName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public CardModel() {}

    public CardModel(String name,
                     Integer numberOfCards,
                    String cost,
                     String rarity,
                     String type,
                     String image,
                     String text,
                     String power,
                     String toughness,
                     String setName) {
        this.name = name;
        this.quantity = numberOfCards;
        this.cost = cost;
        this.rarity = rarity;
        this.type  = type;
        this.image = image;
        this.text = text;
        this.power = power;
        this.toughness = toughness;
        this.setName = setName;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getToughness() {
        return toughness;
    }

    public void setToughness(String toughness) {
        this.toughness = toughness;
    }

    public String getSetName() {
        return setName;
    }

    public void setSetName(String setName) {
        this.setName = setName;
    }
}
