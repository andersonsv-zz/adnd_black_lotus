package br.com.andersonsv.blacklotus.feature.deck;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.azimolabs.conditionwatcher.ConditionWatcher;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.condition.FirebaseAuthInstruction;
import br.com.andersonsv.blacklotus.feature.BaseActivityTest;
import br.com.andersonsv.blacklotus.feature.base.DebugActivity;
import br.com.andersonsv.blacklotus.feature.card.CardFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DeckFragmentTest extends BaseActivityTest {

    @Rule
    public final FragmentTestRule<DebugActivity, DeckFragment> fragmentTestRule =
            new FragmentTestRule<>(DebugActivity.class, DeckFragment.class, true,true, false);

    @BeforeClass
    public static void login(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword("testwithdata@test.com", "123456").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                assertThat(authResult.getUser().getUid(), notNullValue());
            }
        });
    }

    @Test
    public void whenCardsLoad_onLoad_shouldDisplayItems() throws Exception{
        ConditionWatcher.waitForCondition(new FirebaseAuthInstruction());

        DeckFragment deckFragment = new DeckFragment();
        fragmentTestRule.launchFragment(deckFragment);

        onView(withId(R.id.textViewDeckName)).check(matches(withText(containsString("Deck"))));
        onView(withId(R.id.textViewDeckDescription)).check(matches(withText(containsString("Lorem"))));
    }

    //@Test
    public void whenEditDeckData_onDeleteCancel_shouldData() throws Exception{
        ConditionWatcher.waitForCondition(new FirebaseAuthInstruction());

        DeckFragment deckFragment = new DeckFragment();
        fragmentTestRule.launchFragment(deckFragment);
        Thread.sleep(3000);



        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.recyclerViewDeck),
                        childAtPosition(
                                withId(R.id.recyclerViewCardLand),
                                0),
                        isDisplayed()));
        appCompatImageView.perform(longClick());


       /* ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.delete_deck), withContentDescription("Delete deck"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        1),
                                2),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(android.R.id.button2), withText("Cancel"),
                        childAtPosition(
                                allOf(withClassName(is("com.android.internal.widget.ButtonBarLayout")),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                3)),
                                2),
                        isDisplayed()));
        appCompatButton4.perform(click());*/

        /*ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.cancel_edit), withContentDescription("Cancel"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView2.perform(click());*/
    }


    @AfterClass
    public static void signOut(){
        FirebaseAuth.getInstance().signOut();
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
