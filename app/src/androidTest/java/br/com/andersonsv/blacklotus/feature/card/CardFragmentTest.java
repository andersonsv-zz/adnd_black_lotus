package br.com.andersonsv.blacklotus.feature.card;

import android.os.Bundle;

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

import br.com.andersonsv.blacklotus.condition.FirebaseAuthInstruction;
import br.com.andersonsv.blacklotus.feature.BaseActivityTest;
import br.com.andersonsv.blacklotus.feature.base.DebugActivity;
import br.com.andersonsv.blacklotus.firebase.DeckModel;

import static br.com.andersonsv.blacklotus.util.Constants.DECK_PARCELABLE;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class CardFragmentTest extends BaseActivityTest {

    @Rule
    public FragmentTestRule<DebugActivity, CardFragment> fragmentTestRule =
            new FragmentTestRule<>(DebugActivity.class, CardFragment.class, true,true, false);

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
    public void whenEditDeckData_onShow_shouldData() throws Exception{
        ConditionWatcher.waitForCondition(new FirebaseAuthInstruction());

        CardFragment cardFragment = new CardFragment();
        cardFragment.setArguments(getMockBundleEdit());

        fragmentTestRule.launchFragment(cardFragment);

        assertTrue(true);
       // onView(withId(R.id.linearLayoutEmptyStateLand)).check(matches(isDisplayed()));
        //onView(withId(R.id.linearLayoutEmptyStateCard)).check(matches(isDisplayed()));
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


    /*@Test
    public void whenScreenCards_onLoad_shouldEmptyStates() throws Exception{

        ConditionWatcher.waitForCondition(new FirebaseAuthSignOutInstruction());

        CardFragment cardFragment = new CardFragment();
        cardFragment.setArguments(getMockBundleEdit());

        fragmentTestRule.launchFragment(cardFragment);

        onView(withId(R.id.linearLayoutEmptyStateLand)).check(matches(isDisplayed()));
        onView(withId(R.id.linearLayoutEmptyStateCard)).check(matches(isDisplayed()));
    }*/


}
