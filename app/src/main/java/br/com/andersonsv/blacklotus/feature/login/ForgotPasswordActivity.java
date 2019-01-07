package br.com.andersonsv.blacklotus.feature.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.List;

import br.com.andersonsv.blacklotus.util.EspressoIdlingResource;
import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends BaseActivity implements Validator.ValidationListener{

    @BindView(R.id.layout_forgot_password)
    ConstraintLayout mLayout;

    @Order(1)
    @NotEmpty(sequence = 1)
    @Email(sequence = 2)
    @BindView(R.id.textInputEditTextEmail)
    TextInputEditText mEmail;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private Validator mValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        initValidator();
    }

    private void initValidator(){
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        mValidator.setViewValidatedAction(new Validator.ViewValidatedAction() {
            @Override
            public void onAllRulesPassed(View view) {
                removeErrorTextInputLayout(mEmail);
            }
        });
    }

    @OnClick(R.id.buttonRecover)
    public void recover(View view){
        hideKeyboardFrom(this, mLayout);
        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        mProgressBar.setVisibility(View.VISIBLE);

        final String email = mEmail.getText().toString();

        EspressoIdlingResource.increment();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    EspressoIdlingResource.decrement();
                    mProgressBar.setVisibility(View.GONE);
                    alertDialogMessage(ForgotPasswordActivity.this, getResources().getString(R.string.forgot_dialog_title), getResources().getString(R.string.forgot_dialog_message));
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgressBar.setVisibility(View.GONE);
                    alertDialogMessage(ForgotPasswordActivity.this, getResources().getString(R.string.default_error_title), e.getMessage());
                }
            });

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        checkFormValidation(errors);
    }
}
