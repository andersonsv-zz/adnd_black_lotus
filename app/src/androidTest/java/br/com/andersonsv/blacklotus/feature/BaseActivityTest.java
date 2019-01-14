package br.com.andersonsv.blacklotus.feature;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import br.com.andersonsv.blacklotus.R;

public class BaseActivityTest {
    protected static Matcher<View> hasTextInputLayoutHintText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) view).getError();

                if (error == null) {
                    return false;
                }

                String hint = error.toString();

                return expectedErrorText.equalsIgnoreCase(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}
