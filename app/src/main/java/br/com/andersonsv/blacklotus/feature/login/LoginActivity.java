package br.com.andersonsv.blacklotus.feature.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.base.BaseActivity;
import br.com.andersonsv.blacklotus.feature.main.MainActivity;
import br.com.andersonsv.blacklotus.feature.user.UserActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements Validator.ValidationListener{

    @BindView(R.id.layout_login)
    ConstraintLayout mLayout;

    @Order(1)
    @NotEmpty(sequence = 1)
    @Email(sequence = 2)
    @BindView(R.id.textInputEditTextEmail)
    TextInputEditText mEmail;

    @Order(2)
    @NotEmpty
    @Password(messageResId = R.string.login_auth_password_error)
    @BindView(R.id.textInputEditTextPassword)
    TextInputEditText mPassword;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private Validator mValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            openActivity(MainActivity.class);
        } else {
            initValidator();
        }
    }

    private void initValidator(){
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        mValidator.setViewValidatedAction(new Validator.ViewValidatedAction() {
            @Override
            public void onAllRulesPassed(View view) {
            removeErrorTextInputLayout(mEmail);
            removeErrorTextInputLayout(mPassword);
            }
        });
    }

    @OnClick(R.id.textViewSignUp)
    public void signUp(View view){
        Class destinationActivity = UserActivity.class;
        openActivity(destinationActivity);
    }

    @OnClick(R.id.textViewForgotPassword)
    public void forgotPassword(View view){
        Class destinationActivity = ForgotPasswordActivity.class;
        openActivity(destinationActivity);
    }

    @OnClick(R.id.buttonLogin)
    public void login(View view){
        hideKeyboardFrom(this, mLayout);
        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {

        mProgressBar.setVisibility(View.VISIBLE);

        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if (authResult.getUser() != null) {
                        mProgressBar.setVisibility(View.GONE);
                        openActivity(MainActivity.class);
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e){
                    mProgressBar.setVisibility(View.GONE);
                    String error = getString(R.string.login_auth_error);
                    alertDialogMessage(LoginActivity.this, getResources().getString(R.string.default_error_title), error);

                }
            });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        checkFormValidation(errors);
    }
}
