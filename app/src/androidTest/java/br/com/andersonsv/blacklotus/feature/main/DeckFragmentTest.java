package br.com.andersonsv.blacklotus.feature.main;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.andersonsv.blacklotus.util.EspressoIdlingResource;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DeckFragmentTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void whenDeckListIsEmpty_onLoadFragment_shouldDisplayEmptyStates(){

    }
}
