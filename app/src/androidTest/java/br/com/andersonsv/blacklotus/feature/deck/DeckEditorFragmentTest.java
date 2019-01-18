package br.com.andersonsv.blacklotus.feature.deck;

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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_PARCELABLE;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;

public class DeckEditorFragmentTest extends BaseActivityTest {

    @Rule
    public final FragmentTestRule<DebugActivity, DeckEditorFragment> fragmentTestRule =
            new FragmentTestRule<>(DebugActivity.class, DeckEditorFragment.class, true,true, false);

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
    public void whenEditDeckData_onShow_shouldData() throws Exception{
        ConditionWatcher.waitForCondition(new FirebaseAuthInstruction());

        DeckEditorFragment deckEditorFragment = new DeckEditorFragment();
        deckEditorFragment.setArguments(getMockBundleEdit());

        fragmentTestRule.launchFragment(deckEditorFragment);

        onView(withId(R.id.textInputEditTextName)).check(matches(withText(containsString("Deck Test"))));
        onView(withId(R.id.textInputEditTextDescription)).check(matches(withText(containsString("Lorem Ipsum"))));
        onView(withId(R.id.imageViewColor)).check(matches(isDisplayed()));
    }

    @Test
    public void whenEditDeckData_onClickSelectColor_shouldApplyColor() throws Exception{
        ConditionWatcher.waitForCondition(new FirebaseAuthInstruction());

        DeckEditorFragment deckEditorFragment = new DeckEditorFragment();
        deckEditorFragment.setArguments(getMockBundleEdit());

        fragmentTestRule.launchFragment(deckEditorFragment);

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.buttonAddColor), isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction colorItem = onView(
                childAtPosition(
                        childAtPosition(
                                withId(R.id.palette),
                                0),
                        1));
        colorItem.perform(scrollTo(), click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(android.R.id.button1)));
        appCompatButton3.perform(scrollTo(), click());

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
        deckModel.setId("98989889");
        deckModel.setChangeDeck(false);
        deckModel.setName("Deck Test");
        deckModel.setColor1("#FFFFFF");
        deckModel.setDescription("Lorem Ipsum");

        return deckModel;
    }
}
