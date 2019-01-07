package br.com.andersonsv.blacklotus.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Nullable;

import br.com.andersonsv.blacklotus.model.Rarity;
@IgnoreExtraProperties
public class Card implements Parcelable {
    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    @SerializedName("id")
    private final String id;

    @SerializedName("name")
    private final String name;

    @SerializedName("type")
    private final String type;

    @SerializedName("rarity")
    private final Rarity rarity;

    @Nullable
    @SerializedName("imageUrl")
    private final String image;

    @SerializedName("manaCost")
    private final String manaCost;

    @SerializedName("text")
    private final String text;

    @SerializedName("originalType")
    private final String originalType;

    @SerializedName("power")
    private final String power;

    @SerializedName("toughness")
    private final String toughness;

    @SerializedName("setName")
    private final String setName;

    @SerializedName("types")
    private final List<String> types;

    private Card(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.type = in.readString();
        this.rarity = Rarity.valueOf(in.readString());
        this.image = in.readString();
        this.manaCost = in.readString();
        this.text = in.readString();
        this.originalType = in.readString();
        this.power = in.readString();
        this.toughness = in.readString();
        this.setName = in.readString();
        this.types = in.readArrayList(List.class.getClassLoader());

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(type);
        parcel.writeString(rarity.name());
        parcel.writeString(image);
        parcel.writeString(manaCost);
        parcel.writeString(text);
        parcel.writeString(originalType);
        parcel.writeString(power);
        parcel.writeString(toughness);
        parcel.writeString(setName);
        parcel.writeStringList(types);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Rarity getRarity() {
        return rarity;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    public String getManaCost() {
        return manaCost;
    }

    public String getText() {
        return text;
    }

    public String getOriginalType() {
        return originalType;
    }

    public String getPower() {
        return power;
    }

    public String getSetName() {
        return setName;
    }

    public String getToughness() {
        return toughness;
    }

    public boolean getLand(){
        if (types != null){
            for (String type : types) {
                if (type.contains("Land")) {
                    return true;
                }
            }
        }

        return false;
    }
}