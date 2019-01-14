package br.com.andersonsv.blacklotus.feature.main;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.BaseActivityTest;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class SettingsFragmentTest extends BaseActivityTest {

    UiDevice mDevice;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

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
    public void whenSelectSecondLanguage_onClickLanguage_shouldDisplayListLanguages(){

        mActivityTestRule.launchActivity(new Intent());
        onView(withId(R.id.buttonRecover)).perform(click());
        onView(withId(R.id.linearLayoutEmptyState)).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(android.R.id.list)).atPosition(1).perform(click());

    }

}
