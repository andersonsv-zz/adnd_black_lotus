package br.com.andersonsv.blacklotus.feature.user;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.auth.util.ui.fieldvalidators.BaseValidator;
import com.firebase.ui.auth.util.ui.fieldvalidators.EmailFieldValidator;
import com.firebase.ui.auth.util.ui.fieldvalidators.PasswordFieldValidator;
import com.firebase.ui.auth.viewmodel.email.EmailProviderResponseHandler;
import com.google.firebase.auth.FirebaseAuth;

import br.com.andersonsv.blacklotus.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserActivity extends AppCompatActivity {

    private EmailProviderResponseHandler mHandler;
    private EmailFieldValidator mEmailFieldValidator;
    private PasswordFieldValidator mPasswordFieldValidator;
    private BaseValidator mNameValidator;

    private User mUser;
    private FirebaseAuth auth;

    @BindView(R.id.textInputEditTextEmail)
    TextInputEditText mEmail;

    @BindView(R.id.textInputEditTextPassword)
    TextInputEditText mPassword;

    @BindView(R.id.textInputEditTextPasswordConfirmation)
    TextInputEditText mPasswordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.buttonSignUp)
    public void signUp(View view) {

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String name = mPasswordConfirmation.getText().toString();

       // boolean emailValid = mEmailFieldValidator.validate(email);
       // boolean passwordValid = mPasswordFieldValidator.validate(password);
       // boolean nameValid = mNameValidator.validate(name);

    }
}
