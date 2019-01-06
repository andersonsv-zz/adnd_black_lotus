package br.com.andersonsv.blacklotus.feature.login;

import android.support.design.widget.TextInputLayout;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.andersonsv.blacklotus.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class LoginActivityTest {

   @Rule
   public final ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void required_email_and_password() {
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withId(R.id.textInputEditTextEmail)).check(matches(hasTextInputLayoutHintText("This field has required")));


       // onView(withId(R.id.textInputEditTextEmail)).check(matches(hasDescendant(withText(NUTELLA_PIE))));
        //onView(withId(R.id.rvRecipe)).perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLER_VIEW_FIRST_ITEM, click()));
        //onView(withText(NUTELLA_PIE_STEP)).check(matches(isDisplayed()));
    }

    public static Matcher<View> hasTextInputLayoutHintText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) view).getHint();

                if (error == null) {
                    return false;
                }

                String hint = error.toString();

                return expectedErrorText.contains(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}
