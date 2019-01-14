package br.com.andersonsv.blacklotus.feature.card;

import android.support.test.uiautomator.UiDevice;

import com.android21buttons.fragmenttestrule.FragmentTestRule;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;

import br.com.andersonsv.blacklotus.feature.BaseActivityTest;
import br.com.andersonsv.blacklotus.feature.base.DebugActivity;
import br.com.andersonsv.blacklotus.feature.card.CardEditorFragment;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class SearchCardFragmentTest extends BaseActivityTest {

    UiDevice mDevice;

    @Rule
    public FragmentTestRule<DebugActivity, CardEditorFragment> fragmentTestRule =
            new FragmentTestRule<>(DebugActivity.class, CardEditorFragment.class);

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

    @AfterClass
    public static void signOut(){
        FirebaseAuth.getInstance().signOut();
    }
}
