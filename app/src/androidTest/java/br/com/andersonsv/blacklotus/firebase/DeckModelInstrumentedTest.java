package br.com.andersonsv.blacklotus.firebase;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DeckModelInstrumentedTest {

    @Test
    public void test_deck_model_is_parcelable() {
        DeckModel deckModel = new DeckModel();

        deckModel.setId("43434343");
        deckModel.setChangeDeck(false);
        deckModel.setName("Deck");
        deckModel.setDescription("Lorem Ipsum");
        deckModel.setColor1("color1");
        deckModel.setColor2("color2");
        deckModel.setColor3("color3");
        deckModel.setColor4("color4");
        deckModel.setColor5("color5");
        deckModel.setNumberOfCards(10);

        Parcel parcel = Parcel.obtain();
        deckModel.writeToParcel(parcel, deckModel.describeContents());
        parcel.setDataPosition(0);

        DeckModel createdFromParcel = DeckModel.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getName(), is("Deck"));
        assertThat(createdFromParcel.getDescription(), is("Lorem Ipsum"));
        assertThat(createdFromParcel.getColor1(), is("color1"));
        assertThat(createdFromParcel.getColor2(), is("color2"));
        assertThat(createdFromParcel.getColor3(), is("color3"));
        assertThat(createdFromParcel.getColor4(), is("color4"));
        assertThat(createdFromParcel.getColor5(), is("color5"));
        assertThat(createdFromParcel.getNumberOfCards(), is(10));
    }
}
