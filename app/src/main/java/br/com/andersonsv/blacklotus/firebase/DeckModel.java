package br.com.andersonsv.blacklotus.firebase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class DeckModel implements Parcelable {
    public static final Creator<DeckModel> CREATOR = new Creator<DeckModel>() {
        @Override
        public DeckModel createFromParcel(Parcel in) {
            return new DeckModel(in);
        }

        @Override
        public DeckModel[] newArray(int size) {
            return new DeckModel[size];
        }
    };

    private String id;
    private String name;
    private Integer numberOfCards;
    private String description;
    private Boolean changeDeck;
    private String color1;
    private String color2;
    private String color3;
    private String color4;
    private String color5;

    private DeckModel(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.numberOfCards = in.readInt();
        this.description = in.readString();
        this.changeDeck = in.readByte() != 0;
        this.color1 = in.readString();
        this.color2 = in.readString();
        this.color3 = in.readString();
        this.color4 = in.readString();
        this.color5 = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeInt(numberOfCards);
        parcel.writeString(description);
        parcel.writeByte((byte) (changeDeck ? 1: 0));
        parcel.writeString(color1);
        parcel.writeString(color2);
        parcel.writeString(color3);
        parcel.writeString(color4);
        parcel.writeString(color5);
    }

    public DeckModel() { }

    public DeckModel(String name, Integer numberOfCards,
                     String description, Boolean changeDeck,
                     String color1, String color2, String color3,
                     String color4, String color5) {
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

    public String getId() {
        return id;
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

    public void setId(String id) {
        this.id = id;
    }

    // Mapper to insert / update
    public Map<String, Object> objectMap (String userId){
        Map<String, Object> deck = new HashMap<>();
        deck.put("name", name);

        if (numberOfCards != null)
            deck.put("numberOfCards", numberOfCards);

        if (description != null)
            deck.put("description", description);

        deck.put("changeDeck", changeDeck);
        deck.put("color1", color1);

        if (color2 != null)
            deck.put("color2", color2);
        if (color3 != null)
            deck.put("color3", color3);
        if (color4 != null)
            deck.put("color4", color4);
        if (color5 != null)
            deck.put("color5", color5);

        deck.put("userId", userId);

        return deck;
    }
}
