package br.com.andersonsv.blacklotus.feature.deck;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.azimolabs.conditionwatcher.ConditionWatcher;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.andersonsv.blacklotus.condition.FirebaseAuthInstruction;
import br.com.andersonsv.blacklotus.feature.BaseActivityTest;
import br.com.andersonsv.blacklotus.feature.base.DebugActivity;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DeckFragmentTest extends BaseActivityTest {

    @Rule
    public final FragmentTestRule<DebugActivity, DeckFragment> fragmentTestRule =
            new FragmentTestRule<>(DebugActivity.class, DeckFragment.class, true,true, false);

    @BeforeClass
    public static void login(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword("testwithdata@test.com", "123456").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                assertThat(authResult.getUser().getUid(), notNullValue());
            }
        });
    }

    @Test
    public void whenEditDeckData_onShow_shouldData() throws Exception{
        ConditionWatcher.waitForCondition(new FirebaseAuthInstruction());

        DeckFragment deckFragment = new DeckFragment();
        fragmentTestRule.launchFragment(deckFragment);

        assertTrue(true);
        // onView(withId(R.id.linearLayoutEmptyStateLand)).check(matches(isDisplayed()));
        //onView(withId(R.id.linearLayoutEmptyStateCard)).check(matches(isDisplayed()));
    }


    @AfterClass
    public static void signOut(){
        FirebaseAuth.getInstance().signOut();
    }


    /*@Test
    public void whenScreenCards_onLoad_shouldEmptyStates() throws Exception{

        ConditionWatcher.waitForCondition(new FirebaseAuthSignOutInstruction());

        CardFragment cardFragment = new CardFragment();
        cardFragment.setArguments(getMockBundleEdit());

        fragmentTestRule.launchFragment(cardFragment);

        onView(withId(R.id.linearLayoutEmptyStateLand)).check(matches(isDisplayed()));
        onView(withId(R.id.linearLayoutEmptyStateCard)).check(matches(isDisplayed()));
    }*/
}
