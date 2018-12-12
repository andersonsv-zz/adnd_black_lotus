package br.com.andersonsv.blacklotus.feature.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.Resource;
import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.auth.util.GoogleApiUtils;
import com.firebase.ui.auth.util.ui.fieldvalidators.BaseValidator;
import com.firebase.ui.auth.util.ui.fieldvalidators.EmailFieldValidator;
import com.firebase.ui.auth.util.ui.fieldvalidators.PasswordFieldValidator;
import com.firebase.ui.auth.viewmodel.email.EmailProviderResponseHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Arrays;
import java.util.List;

import br.com.andersonsv.blacklotus.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserActivity extends AppCompatActivity {

    private User mUser;
    private FirebaseAuth mFirebaseAuth;
    private List<AuthUI.IdpConfig> providers;

    @BindView(R.id.textInputLayoutName)
    TextInputLayout mLayoutName;

    @BindView(R.id.textInputEditTextName)
    TextInputEditText mName;

    @BindView(R.id.textInputLayoutEmail)
    TextInputLayout mLayoutEmail;

    @BindView(R.id.textInputEditTextEmail)
    TextInputEditText mEmail;

    @BindView(R.id.textInputEditTextPassword)
    TextInputEditText mPassword;

    @BindView(R.id.textInputEditTextPasswordConfirmation)
    TextInputEditText mPasswordConfirmation;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mName.addTextChangedListener(new MyTextWatcher(mName));
        mEmail.addTextChangedListener(new MyTextWatcher(mEmail));
    }

    @OnClick(R.id.buttonSignUp)
    public void signUp(View view) {

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String passwordConfirmation = mPasswordConfirmation.getText().toString();

        validate(mLayoutName,mName);
        validateEmail(mLayoutEmail,mEmail);

       // mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        //    @Override
        //    public void onComplete(@NonNull Task<AuthResult> task) {
//
       //     }
        //});

        //check email Patterns.EMAIL_ADDRESS.matcher(email).matches()


    }

    private boolean validate(@NonNull TextInputLayout textInputLayout, @NonNull TextInputEditText textInputEditText) {
        if (textInputEditText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(getString(R.string.default_required));
            requestFocus(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail(@NonNull TextInputLayout textInputLayout, @NonNull TextInputEditText textInputEditText) {
        if (textInputEditText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(getString(R.string.default_required));
            requestFocus(textInputEditText);
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(textInputEditText.getText().toString().trim()).matches()){
            textInputLayout.setError(getString(
                        R.string.default_email_error));

        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.name:
                    validate(mLayoutName, mEmail);
                    break;
                case R.id.email:
                    validateEmail(mLayoutEmail, mEmail);
                    break;
                /*case R.id.input_password:
                    validatePassword();
                    break;*/
            }
        }
    }
}
