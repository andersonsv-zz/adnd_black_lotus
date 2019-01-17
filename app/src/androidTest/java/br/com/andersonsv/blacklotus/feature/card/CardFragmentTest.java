package br.com.andersonsv.blacklotus.feature.card;

import android.os.Bundle;
import android.support.test.espresso.ViewInteraction;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.azimolabs.conditionwatcher.ConditionWatcher;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.condition.FirebaseAuthInstruction;
import br.com.andersonsv.blacklotus.feature.BaseActivityTest;
import br.com.andersonsv.blacklotus.feature.base.DebugActivity;
import br.com.andersonsv.blacklotus.firebase.DeckModel;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_PARCELABLE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;

public class CardFragmentTest extends BaseActivityTest {

    @Rule
    public final FragmentTestRule<DebugActivity, CardFragment> fragmentTestRule =
            new FragmentTestRule<>(DebugActivity.class, CardFragment.class, true,true, false);
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
    public void whenEditCardData_onClickImage_shouldShowImageDialog() throws Exception{
        ConditionWatcher.waitForCondition(new FirebaseAuthInstruction());

        CardFragment cardFragment = new CardFragment();
        cardFragment.setArguments(getMockBundleEdit());

        fragmentTestRule.launchFragment(cardFragment);

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.imageViewCardImage), withContentDescription("Card image"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerViewCardLand),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.imageViewDialog), withContentDescription("Card image"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView2.perform(click());
    }

    @Test
    public void whenCardsLoad_onLoad_shouldDisplayItems() throws Exception{
        ConditionWatcher.waitForCondition(new FirebaseAuthInstruction());

        CardFragment cardFragment = new CardFragment();
        cardFragment.setArguments(getMockBundleEdit());

        fragmentTestRule.launchFragment(cardFragment);;

        onView(withId(R.id.recyclerViewCard)).check(matches(hasItem(hasDescendant(withText("Uncommon")))));
    }

    @AfterClass
    public static void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    private Bundle getMockBundleEdit() {
        Bundle bundle = new Bundle();

        bundle.putParcelable(DECK_PARCELABLE, generateDeckData());

        return bundle;
    }

    private DeckModel generateDeckData() {
        DeckModel deckModel = new DeckModel();
        deckModel.setId("wKorovgLoZCdGXk61bSI");
        deckModel.setChangeDeck(false);
        deckModel.setName("Deck Test");
        deckModel.setColor1("#FFFFFF");
        deckModel.setDescription("Lorem Ipsum");
        deckModel.setNumberOfCards(0);

        return deckModel;
    }
}