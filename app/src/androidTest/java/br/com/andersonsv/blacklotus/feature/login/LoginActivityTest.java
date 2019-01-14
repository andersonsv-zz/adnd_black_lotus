package br.com.andersonsv.blacklotus.feature.login;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;

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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static br.com.andersonsv.blacklotus.util.ConstantsTest.TEXT_MSG_EMAIL;
import static br.com.andersonsv.blacklotus.util.ConstantsTest.TEXT_MSG_REQUIRED;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest extends BaseActivityTest {

    @Rule
    public final ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void whenEmailPasswordIsEmpty_andClickOnLogin_shouldDisplayErrors() {

        String msgEmail = TEXT_MSG_REQUIRED.concat("\n").concat(TEXT_MSG_EMAIL);
        String msgPassword = TEXT_MSG_REQUIRED.concat("\n").concat(mActivityTestRule.getActivity().getResources().getString(R.string.login_auth_password_error));

        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withId(R.id.textInputLayoutEmail)).check(matches(hasTextInputLayoutHintText(msgEmail)));
        onView(withId(R.id.textInputLayoutPassword)).check(matches(hasTextInputLayoutHintText(msgPassword)));
    }

    @Test
    public void whenEmailInvalidPasswordIsEmpty_andOnClickLogin_shouldDisplayErrors() {

        String msgPassword = TEXT_MSG_REQUIRED.concat("\n").concat(mActivityTestRule.getActivity().getResources().getString(R.string.login_auth_password_error));

        onView(withId(R.id.textInputEditTextEmail)).perform(typeText("aaaaa"),closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withId(R.id.textInputLayoutEmail)).check(matches(hasTextInputLayoutHintText(TEXT_MSG_EMAIL)));
        onView(withId(R.id.textInputLayoutPassword)).check(matches(hasTextInputLayoutHintText(msgPassword)));
    }

    @Test
    public void whenPasswordInvalidLength_andClickOnLogin_shouldDisplayErrors() {

        String msgPassword = mActivityTestRule.getActivity().getResources().getString(R.string.login_auth_password_error);

        onView(withId(R.id.textInputEditTextEmail)).perform(typeText("aaaa@aaa.com"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextPassword)).perform(typeText("12345"),closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withId(R.id.textInputLayoutPassword)).check(matches(hasTextInputLayoutHintText(msgPassword)));
    }

    @Test
    public void whenAuthError_andClickOnLoginButton_shouldDisplayDialog() {
        onView(withId(R.id.textInputEditTextEmail)).perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextPassword)).perform(typeText("123456aaaaaa"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withText("Error")).check(matches(isDisplayed()));
    }

    @Test
    public void whenAuthOk_andClickOnLoginButton_shouldDecksActivity() {
        onView(withId(R.id.textInputEditTextEmail)).perform(typeText("test@test.com"), closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextPassword)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withText("Decks")).check(matches(isDisplayed()));

        FirebaseAuth.getInstance().signOut();
    }

}