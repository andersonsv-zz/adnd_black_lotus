package br.com.andersonsv.blacklotus.feature.deck;

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
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static br.com.andersonsv.blacklotus.util.ConstantsTest.TEXT_MSG_REQUIRED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class DeckEditorFragmentTest extends BaseActivityTest {

    UiDevice mDevice;

    @Rule
    public FragmentTestRule<DebugActivity, DeckEditorFragment> fragmentTestRule =
            new FragmentTestRule<>(DebugActivity.class, DeckEditorFragment.class);

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
    public void whenInsertDeckWithOutDeckName_onClickSave_shouldValidationError() throws Exception{
        ConditionWatcher.waitForCondition(new FirebaseAuthInstruction());

        onView(withId(R.id.buttonSaveDeck)).perform(click());
        onView(withId(R.id.textInputLayoutName)).check(matches(hasTextInputLayoutHintText(TEXT_MSG_REQUIRED)));
    }

    @Test
    public void whenInsertDeckWithNoColorSelected_onClickSave_shouldValidationError() throws Exception{
        ConditionWatcher.waitForCondition(new FirebaseAuthInstruction());

        onView(withId(R.id.textInputEditTextName)).perform(typeText("Deck Test"),closeSoftKeyboard());
        onView(withId(R.id.buttonSaveDeck)).perform(click());
        onView(withId(R.id.textViewErrorColor)).check(matches(isDisplayed()));
    }

    @AfterClass
    public static void signOut(){
        FirebaseAuth.getInstance().signOut();
    }
}
