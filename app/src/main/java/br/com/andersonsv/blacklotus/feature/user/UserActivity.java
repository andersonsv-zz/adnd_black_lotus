package br.com.andersonsv.blacklotus.feature.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.base.BaseActivity;
import br.com.andersonsv.blacklotus.feature.main.MainActivity;
import br.com.andersonsv.blacklotus.util.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.andersonsv.blacklotus.util.Constants.VALIDATION_APP_COMPAT_EDIT_TEXT;
import static br.com.andersonsv.blacklotus.util.Constants.VALIDATION_TEXT_INPUT_EDIT_TEXT;

public class UserActivity extends BaseActivity implements Validator.ValidationListener {

    @BindView(R.id.textInputEditTextName)
    @NotEmpty
    TextInputEditText mName;

    @BindView(R.id.textInputEditTextEmail)
    @NotEmpty(sequence = 0)
    @Email(messageResId = R.string.default_email_error, sequence = 1)
    TextInputEditText mEmail;

    @BindView(R.id.textInputEditTextPassword)
    @Password
    TextInputEditText mPassword;

    @BindView(R.id.textInputEditTextPasswordConfirmation)
    @ConfirmPassword
    @NotEmpty
    TextInputEditText mPasswordConfirmation;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.layoutCreateUser)
    CoordinatorLayout layout;

    private Validator mValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        initValidator();
        setSupportActionBar(mToolbar);
    }

    private void initValidator(){
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
    }

    @OnClick(R.id.buttonSignUp)
    public void signUp(View view){
        mValidator.validate();
    }

    private void createUser(final String email, final String password, final String name){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mProgressBar.setVisibility(View.GONE);
                        snack(layout, e.getLocalizedMessage());
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name).build();
                                user.updateProfile(profileUpdates);

                                openActivity(MainActivity.class);
                            }
                        }
                    }
                );
    }

    @Override
    public void onValidationSucceeded() {
        if (!NetworkUtils.isNetworkConnected(this)) {
            Snackbar snackbar = Snackbar.make(layout, R.string.offline_no_internet, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.offline_no_internet_retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            signUp(view);
                        }
                    });
            snackbar.show();
        }else {

            mProgressBar.setVisibility(View.VISIBLE);

            final String email = mEmail.getText().toString();
            final String password = mPassword.getText().toString();
            final String name = mName.getText().toString();

            createUser(email, password, name);
        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view.getClass().getSimpleName().equalsIgnoreCase(VALIDATION_APP_COMPAT_EDIT_TEXT)) {
                ((AppCompatEditText) view).setError(message);
            } else if (view.getClass().getSimpleName().equalsIgnoreCase(VALIDATION_TEXT_INPUT_EDIT_TEXT)) {
                TextInputLayout textInputLayout = (TextInputLayout) view.getParent().getParent();
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(message);
            }
        }
    }
}
