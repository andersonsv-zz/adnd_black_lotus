package br.com.andersonsv.blacklotus.feature.main;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.firebase.DeckModel;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DeckFragmentTest {

    @Rule public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    UiDevice mDevice;

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(getInstrumentation());
        assertThat(mDevice, notNullValue());

        FirebaseAuth.getInstance().signInWithEmailAndPassword("test@test.com", "123456").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                assertThat(authResult.getUser().getUid(), notNullValue());
            }
        });
    }

    @Test
    public void whenDeckListIsEmpty_onLoadFragment_shouldDisplayEmptyStates(){
        mActivityTestRule.launchActivity(new Intent());
        onView(withId(R.id.linearLayoutEmptyState)).check(matches(isDisplayed()));
    }

    @Test
    public void whenDeckNotEmpty_onLoadFragment_shouldDisplayItems(){
        mActivityTestRule.launchActivity(new Intent());

        DeckModel deckModel = new DeckModel();
        deckModel.setName("test");

        onData(allOf(is(instanceOf(DeckModel.class)), is(deckModel))).perform(click());
        onView(withId(R.id.recyclerViewDeck)).check(matches(withText(containsString("test"))));

        //onView(withId(R.id.linearLayoutEmptyState)).check(matches(isDisplayed()));
    }

}
