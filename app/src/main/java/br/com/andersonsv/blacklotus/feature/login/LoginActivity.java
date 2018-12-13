package br.com.andersonsv.blacklotus.feature.login;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.base.BaseActivity;
import br.com.andersonsv.blacklotus.feature.main.MainActivity;
import br.com.andersonsv.blacklotus.feature.user.UserActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    private FirebaseUser mUser;
    private FirebaseAuth mFirebaseAuth;

    @BindView(R.id.textInputLayoutEmail)
    TextInputLayout mLayoutEmail;

    @BindView(R.id.textInputEditTextEmail)
    TextInputEditText mEmail;

    @BindView(R.id.textInputLayoutPassword)
    TextInputLayout mLayoutPassword;

    @BindView(R.id.textInputEditTextPassword)
    TextInputEditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser != null){
            openActivity(MainActivity.class);
        }
    }

    @OnClick(R.id.textViewSignUp)
    public void signUp(View view) {
        Class destinationActivity = UserActivity.class;
        openActivity(destinationActivity);
    }

    @OnClick(R.id.buttonLogin)
    public void login(View view) {

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                if (authResult.getUser() != null) {
                    openActivity(MainActivity.class);
                }
            }
        });
    }
}
