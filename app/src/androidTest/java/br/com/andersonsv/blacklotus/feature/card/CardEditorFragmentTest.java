package br.com.andersonsv.blacklotus.feature.card;

import android.os.Bundle;
import android.os.Parcel;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.azimolabs.conditionwatcher.ConditionWatcher;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;

import javax.annotation.Nullable;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.condition.FirebaseAuthInstruction;
import br.com.andersonsv.blacklotus.data.Card;
import br.com.andersonsv.blacklotus.feature.BaseActivityTest;
import br.com.andersonsv.blacklotus.feature.base.DebugActivity;
import br.com.andersonsv.blacklotus.feature.main.MainActivity;
import br.com.andersonsv.blacklotus.firebase.CardModel;
import br.com.andersonsv.blacklotus.firebase.DeckModel;
import br.com.andersonsv.blacklotus.model.Rarity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.andersonsv.blacklotus.util.Constants.CARD_DATA;
import static br.com.andersonsv.blacklotus.util.Constants.CARD_MODEL;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_PARCELABLE;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class CardEditorFragmentTest extends BaseActivityTest {


    @Rule
    public FragmentTestRule<DebugActivity, CardEditorFragment> fragmentTestRule =
            new FragmentTestRule<>(DebugActivity.class, CardEditorFragment.class, true,true, false);

    @Before
    public void init(){
    }

    @BeforeClass
    public static void login(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword("test@test.com", "123456").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                assertThat(authResult.getUser().getUid(), notNullValue());
            }
        });
    }

    @Test
    public void whenEditCardData_onShow_shouldData() throws Exception{
        ConditionWatcher.waitForCondition(new FirebaseAuthInstruction());

        CardEditorFragment cardEditorFragment = new CardEditorFragment();
        cardEditorFragment.setArguments(getMockBundleEdit());

        fragmentTestRule.launchFragment(cardEditorFragment);

        onView(withId(R.id.textViewSetName)).check(matches(withText(containsString("Set Name"))));
        onView(withId(R.id.textViewCardName)).check(matches(withText(containsString("Test"))));
        onView(withId(R.id.textViewType)).check(matches(withText(containsString("Test"))));
        onView(withId(R.id.textViewQuantityInfo)).check(matches(withText(containsString("2"))));
        onView(withId(R.id.textViewDescription)).check(matches(withText(containsString("Lorem ipsum"))));
    }

    @Test
    public void whenNewCardData_onShow_shouldData() throws Exception{
        ConditionWatcher.waitForCondition(new FirebaseAuthInstruction());

        CardEditorFragment cardEditorFragment = new CardEditorFragment();
        cardEditorFragment.setArguments(getMockBundleInsert());

        fragmentTestRule.launchFragment(cardEditorFragment);

        onView(withId(R.id.textViewSetName)).check(matches(withText(containsString("Set name"))));
        onView(withId(R.id.textViewCardName)).check(matches(withText(containsString("Test"))));
        onView(withId(R.id.textViewType)).check(matches(withText(containsString("Test"))));
        onView(withId(R.id.textViewQuantityInfo)).check(matches(withText(containsString("1"))));
        onView(withId(R.id.textViewDescription)).check(matches(withText(containsString("Lorem ipsum"))));
    }


    @AfterClass
    public static void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    private Bundle getMockBundleEdit() {
        Bundle bundle = new Bundle();

        CardModel cardModel = new CardModel();
        cardModel.setId("43434");
        cardModel.setCost("{W}");
        cardModel.setLand(false);
        cardModel.setName("Test");
        cardModel.setImage("https://image.com");
        cardModel.setPower("0");
        cardModel.setToughness("0");
        cardModel.setQuantity(2);
        cardModel.setRarity("Common");
        cardModel.setText("Lorem ipsum");
        cardModel.setSetName("Set Name");
        cardModel.setType("Test");

        bundle.putParcelable(CARD_MODEL, cardModel);
        bundle.putParcelable(DECK_PARCELABLE, generateDeckData());

        return bundle;
    }

    private Bundle getMockBundleInsert() {
        Bundle bundle = new Bundle();

        Card card = new Card("554545", "Test",
                "Test", Rarity.COMMON,
                null, "{W}",
                "Lorem ipsum", "Test",
                "0", "0",
                "Set name", Arrays.asList("Card"));

        bundle.putParcelable(CARD_DATA, card);
        bundle.putParcelable(DECK_PARCELABLE, generateDeckData());

        return bundle;
    }

    private DeckModel generateDeckData() {
        DeckModel deckModel = new DeckModel();
        deckModel.setId("98989889");
        deckModel.setChangeDeck(false);
        deckModel.setName("Deck Test");
        deckModel.setColor1("");

        return deckModel;
    }
}
