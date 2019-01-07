package br.com.andersonsv.blacklotus.firebase;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.andersonsv.blacklotus.model.Rarity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class CardModelInstrumentedTest {

    @Test
    public void test_card_model_is_parcelable() {
        CardModel cardModel = new CardModel();

        cardModel.setId("43434343");
        cardModel.setQuantity(0);
        cardModel.setName("Black Lotus");
        cardModel.setRarity(Rarity.RARE.getTypeId());
        cardModel.setCost("{0}");
        cardModel.setLand(false);
        cardModel.setText("Lorem ipsum");
        cardModel.setSetName("Set");
        cardModel.setPower("1");
        cardModel.setToughness("2");
        cardModel.setType("Type");
        cardModel.setImage("http://image");

        Parcel parcel = Parcel.obtain();
        cardModel.writeToParcel(parcel, cardModel.describeContents());
        parcel.setDataPosition(0);

        CardModel createdFromParcel = CardModel.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getName(), is("Black Lotus"));
        assertThat(createdFromParcel.getRarity(), is(Rarity.RARE.getTypeId()));
        assertThat(createdFromParcel.getCost(), is("{0}"));
        assertThat(createdFromParcel.getLand(), is(false));
        assertThat(createdFromParcel.getText(), is("Lorem ipsum"));
        assertThat(createdFromParcel.getSetName(), is("Set"));
        assertThat(createdFromParcel.getPower(), is("1"));
        assertThat(createdFromParcel.getToughness(), is("2"));
        assertThat(createdFromParcel.getType(), is("Type"));
        assertThat(createdFromParcel.getImage(), is("http://image"));
    }
}
