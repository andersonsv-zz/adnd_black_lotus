package br.com.andersonsv.blacklotus.feature.main;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.andersonsv.blacklotus.R;
import io.victoralbertos.device_animation_test_rule.DeviceAnimationTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DeckFragmentTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @ClassRule
    public static DeviceAnimationTestRule
            deviceAnimationTestRule = new DeviceAnimationTestRule();

    private IdlingResource mIdlingResource;


    @Before
    public void setUp() {

        DeckFragment fragment = new DeckFragment();
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void whenDeckListIsEmpty_onLoadFragment_shouldDisplayEmptyStates(){
        onView(withId(R.id.linearLayoutEmptyState)).check(matches(isDisplayed()));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
