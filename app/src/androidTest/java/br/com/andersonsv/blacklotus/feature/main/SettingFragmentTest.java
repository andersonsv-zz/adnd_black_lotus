package br.com.andersonsv.blacklotus.feature.main;

import android.support.test.uiautomator.UiDevice;

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

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.condition.FirebaseAuthInstruction;
import br.com.andersonsv.blacklotus.feature.BaseActivityTest;
import br.com.andersonsv.blacklotus.feature.base.DebugActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class SettingFragmentTest extends BaseActivityTest {

    UiDevice mDevice;

    @Rule
    public FragmentTestRule<DebugActivity, SettingFragment> fragmentTestRule =
            new FragmentTestRule<>(DebugActivity.class, SettingFragment.class);

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

    @Before
    public void setUp() {
        mDevice = UiDevice.getInstance(getInstrumentation());
        assertThat(mDevice, notNullValue());

    }

    @Test
    public void whenSelectSecondLanguage_onClickSignOut_shouldDialog() throws Exception{
        ConditionWatcher.waitForCondition(new FirebaseAuthInstruction());

        onView(withId(R.id.buttonSignOut)).perform(click());
        onView(withText(R.string.sign_out_confirm)).check(matches(isDisplayed()));

    }


    @AfterClass
    public static void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

}
