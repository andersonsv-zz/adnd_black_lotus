package br.com.andersonsv.blacklotus.feature.user;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import br.com.andersonsv.blacklotus.EspressoIdlingResource;
import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.BaseActivityTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static br.com.andersonsv.blacklotus.util.ConstantsTest.TEXT_MSG_EMAIL;
import static br.com.andersonsv.blacklotus.util.ConstantsTest.TEXT_MSG_PASSWORD_CONFIRMATION;
import static br.com.andersonsv.blacklotus.util.ConstantsTest.TEXT_MSG_REQUIRED;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserActivityTest extends BaseActivityTest {

    @Rule
    public final ActivityTestRule<UserActivity> mActivityTestRule = new ActivityTestRule<>(UserActivity.class);

    @Before
    public void setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getIdlingResource());
    }

    @Test
    public void show_required_all_inputs() {

        String msgEmail = TEXT_MSG_REQUIRED.concat("\n").concat(TEXT_MSG_EMAIL);
        String msgPassword = TEXT_MSG_REQUIRED.concat("\n").concat(mActivityTestRule.getActivity().getResources().getString(R.string.login_auth_password_error));

        onView(withId(R.id.buttonSignUp)).perform(click());
        onView(withId(R.id.textInputLayoutName)).check(matches(hasTextInputLayoutHintText(TEXT_MSG_REQUIRED)));
        onView(withId(R.id.textInputLayoutEmail)).check(matches(hasTextInputLayoutHintText(msgEmail)));
        onView(withId(R.id.textInputLayoutPassword)).check(matches(hasTextInputLayoutHintText(msgPassword)));
        onView(withId(R.id.textInputLayoutPasswordConfirmation)).check(matches(hasTextInputLayoutHintText(TEXT_MSG_REQUIRED)));
    }

    @Test
    public void show_required_all_inputs_name_ok() {

        String msgEmail = TEXT_MSG_REQUIRED.concat("\n").concat(TEXT_MSG_EMAIL);
        String msgPassword = TEXT_MSG_REQUIRED.concat("\n").concat(mActivityTestRule.getActivity().getResources().getString(R.string.login_auth_password_error));

        onView(withId(R.id.textInputEditTextName)).perform(typeText("Name"),closeSoftKeyboard());
        onView(withId(R.id.buttonSignUp)).perform(click());
        onView(withId(R.id.textInputLayoutEmail)).check(matches(hasTextInputLayoutHintText(msgEmail)));
        onView(withId(R.id.textInputLayoutPassword)).check(matches(hasTextInputLayoutHintText(msgPassword)));
        onView(withId(R.id.textInputLayoutPasswordConfirmation)).check(matches(hasTextInputLayoutHintText(TEXT_MSG_REQUIRED)));
    }

    @Test
    public void show_invalid_email_and_required_other_inputs_name_ok() {

        String msgPassword = TEXT_MSG_REQUIRED.concat("\n").concat(mActivityTestRule.getActivity().getResources().getString(R.string.login_auth_password_error));

        onView(withId(R.id.textInputEditTextName)).perform(typeText("Name"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextEmail)).perform(typeText("aaaaa"),closeSoftKeyboard());
        onView(withId(R.id.buttonSignUp)).perform(click());
        onView(withId(R.id.textInputLayoutEmail)).check(matches(hasTextInputLayoutHintText(TEXT_MSG_EMAIL)));
        onView(withId(R.id.textInputLayoutPassword)).check(matches(hasTextInputLayoutHintText(msgPassword)));
        onView(withId(R.id.textInputLayoutPasswordConfirmation)).check(matches(hasTextInputLayoutHintText(TEXT_MSG_REQUIRED)));

    }

    @Test
    public void show_invalid_and_required_password_confirm_password_name_email_ok() {

        String msgPassword = mActivityTestRule.getActivity().getResources().getString(R.string.login_auth_password_error);

        String msgPasswordConfirmation = TEXT_MSG_REQUIRED.concat("\n").concat(TEXT_MSG_PASSWORD_CONFIRMATION);

        onView(withId(R.id.textInputEditTextName)).perform(typeText("Name"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextEmail)).perform(typeText("aaaa@aaa.com"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextPassword)).perform(typeText("12345"),closeSoftKeyboard());
        onView(withId(R.id.buttonSignUp)).perform(click());

        onView(withId(R.id.textInputLayoutPassword)).check(matches(hasTextInputLayoutHintText(msgPassword)));
        onView(withId(R.id.textInputLayoutPasswordConfirmation)).check(matches(hasTextInputLayoutHintText(msgPasswordConfirmation)));

    }

    @Test
    public void show_invalid_and_required_confirm_password_name_email_password_ok() {

        String msgPasswordConfirmation = TEXT_MSG_REQUIRED.concat("\n").concat(TEXT_MSG_PASSWORD_CONFIRMATION);

        onView(withId(R.id.textInputEditTextName)).perform(typeText("Name"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextEmail)).perform(typeText("aaaa@aaa.com"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextPassword)).perform(typeText("123456"),closeSoftKeyboard());
        onView(withId(R.id.buttonSignUp)).perform(click());

        onView(withId(R.id.textInputLayoutPasswordConfirmation)).check(matches(hasTextInputLayoutHintText(msgPasswordConfirmation)));

    }

    @Test
    public void show_invalid_and_dont_match_confirm_password_name_email_password_ok() {

        onView(withId(R.id.textInputEditTextName)).perform(typeText("Name"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextEmail)).perform(typeText("aaaa@aaa.com"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextPassword)).perform(typeText("123456"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextPasswordConfirmation)).perform(typeText("12345899"),closeSoftKeyboard());
        onView(withId(R.id.buttonSignUp)).perform(click());

        onView(withId(R.id.textInputLayoutPasswordConfirmation)).check(matches(hasTextInputLayoutHintText(TEXT_MSG_PASSWORD_CONFIRMATION)));
    }

    @Test
    public void insert_new_user() {

        String email = "user" + randomInt() + "@test.com";

        onView(withId(R.id.textInputEditTextName)).perform(typeText("Name"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextEmail)).perform(typeText(email),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextPassword)).perform(typeText("123456"),closeSoftKeyboard());
        onView(withId(R.id.textInputEditTextPasswordConfirmation)).perform(typeText("123456"),closeSoftKeyboard());
        onView(withId(R.id.buttonSignUp)).perform(click());
    }

    private String randomInt() {
        return String.valueOf(((new Random()).nextInt(100000)));
    }
}
