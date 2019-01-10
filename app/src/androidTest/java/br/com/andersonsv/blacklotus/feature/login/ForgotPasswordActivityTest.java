package br.com.andersonsv.blacklotus.feature.login;

import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.BaseActivityTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static br.com.andersonsv.blacklotus.util.ConstantsTest.TEXT_MSG_EMAIL;
import static br.com.andersonsv.blacklotus.util.ConstantsTest.TEXT_MSG_REQUIRED;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class ForgotPasswordActivityTest extends BaseActivityTest {
    @Rule
    public final ActivityTestRule<ForgotPasswordActivity> mActivityTestRule = new ActivityTestRule<>(ForgotPasswordActivity.class);

    @Before
    public void setUp() {

    }

    @Test
    public void whenEmailIsEmpty_andClickOnRecover_shouldDisplayErrors() {
        String msgEmail = TEXT_MSG_REQUIRED.concat("\n").concat(TEXT_MSG_EMAIL);
        onView(withId(R.id.buttonRecover)).perform(click());
        onView(withId(R.id.textInputLayoutEmail)).check(matches(hasTextInputLayoutHintText(msgEmail)));
    }

    @Test
    public void whenEmailInvalidPasswordIsEmpty_andOnClickLogin_shouldDisplayErrors() {
        onView(withId(R.id.textInputEditTextEmail)).perform(typeText("aaaaa"),closeSoftKeyboard());
        onView(withId(R.id.buttonRecover)).perform(click());
        onView(withId(R.id.textInputLayoutEmail)).check(matches(hasTextInputLayoutHintText(TEXT_MSG_EMAIL)));
    }
}
