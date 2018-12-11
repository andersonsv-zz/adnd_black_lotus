package br.com.andersonsv.blacklotus.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import br.com.andersonsv.blacklotus.model.Rarity;

/*
     "name": "Bontu the Glorified",
     "manaCost": "{2}{B}",
        "cmc": 3,
        "colors": [
            "Black"
        ],
        "colorIdentity": [
            "B"
        ],
        "type": "Legendary Creature — God",
        "supertypes": [
            "Legendary"
        ],
        "types": [
            "Creature"
        ],
        "subtypes": [
            "God"
        ],
        "rarity": "Mythic Rare",
        "set": "AKH",
        "setName": "Amonkhet",
        "text": "Menace, indestructible\nBontu the Glorified can't attack or block unless a creature died under your control this turn.\n{1}{B}, Sacrifice another creature: Scry 1. Each opponent loses 1 life and you gain 1 life.",
        "artist": "Chase Stone",
        "number": "82",
        "power": "4",
        "toughness": "6",
        "layout": "normal",
        "multiverseid": 426784,
        "imageUrl": "http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=426784&type=card",
        "rulings": [
            {
                "date": "2017-04-18",
                "text": "The creature that died doesn't have to still be in its owner's graveyard to satisfy Bontu's combat restriction."
            },
            {
                "date": "2017-04-18",
                "text": "Creature tokens that die are put into your graveyard as normal (and cease to exist soon after). If one died this turn, it satisfies Bontu's combat restriction."
            },
            {
                "date": "2017-04-18",
                "text": "All attackers are chosen at once. You can't attack with one, sacrifice it to satisfy Bontu's combat restriction, and then attack with Bontu."
            },
            {
                "date": "2017-04-18",
                "text": "In a Two-Headed Giant game, Bontu's activated ability causes the opposing team to lose 2 life and you gain 1 life."
            }
        ],
        "foreignNames": [
            {
                "name": "Bontu die Vielgepriesene",
                "imageUrl": "http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=427053&type=card",
                "language": "German",
                "multiverseid": 427053
            },
            {
                "name": "Bontu, la Venerada",
                "imageUrl": "http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=427322&type=card",
                "language": "Spanish",
                "multiverseid": 427322
            },
            {
                "name": "Bontu la Glorifiée",
                "imageUrl": "http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=427591&type=card",
                "language": "French",
                "multiverseid": 427591
            },
            {
                "name": "Bontu la Gloriosa",
                "imageUrl": "http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=427860&type=card",
                "language": "Italian",
                "multiverseid": 427860
            },
            {
                "name": "栄光の神バントゥ",
                "imageUrl": "http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=428129&type=card",
                "language": "Japanese",
                "multiverseid": 428129
            },
            {
                "name": "영광의 본투",
                "imageUrl": "http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=428398&type=card",
                "language": "Korean",
                "multiverseid": 428398
            },
            {
                "name": "Bontu, a Glorificada",
                "imageUrl": "http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=428667&type=card",
                "language": "Portuguese (Brazil)",
                "multiverseid": 428667
            },
            {
                "name": "Бонту Прославленная",
                "imageUrl": "http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=428936&type=card",
                "language": "Russian",
                "multiverseid": 428936
            },
            {
                "name": "荣光神芭图",
                "imageUrl": "http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=429205&type=card",
                "language": "Chinese Simplified",
                "multiverseid": 429205
            },
            {
                "name": "榮光神芭圖",
                "imageUrl": "http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=429474&type=card",
                "language": "Chinese Traditional",
                "multiverseid": 429474
            }
        ],
        "printings": [
            "AKH",
            "MPS_AKH"
        ],
        "originalText": "Menace, indestructible\nBontu the Glorified can't attack or block unless a creature died under your control this turn.\n{1}{B}, Sacrifice another creature: Scry 1. Each opponent loses 1 life and you gain 1 life.",
        "originalType": "Legendary Creature — God",
        "legalities": [
            {
                "format": "Brawl",
                "legality": "Legal"
            },
            {
                "format": "Commander",
                "legality": "Legal"
            },
            {
                "format": "Legacy",
                "legality": "Legal"
            },
            {
                "format": "Modern",
                "legality": "Legal"
            },
            {
                "format": "Standard",
                "legality": "Legal"
            },
            {
                "format": "Vintage",
                "legality": "Legal"
            }
        ],
        "id": "c296e737311de2a17d061d7453d330a900595e13"
 */

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

    private Card(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.type = in.readString();
        this.rarity = Rarity.valueOf(in.readString());
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
    }
}
