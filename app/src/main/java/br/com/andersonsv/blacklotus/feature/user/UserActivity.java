package br.com.andersonsv.blacklotus.feature.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.base.BaseActivity;
import br.com.andersonsv.blacklotus.util.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserActivity extends BaseActivity {

    private User mUser;
    private FirebaseAuth mFirebaseAuth;

    @BindView(R.id.textInputLayoutName)
    TextInputLayout mLayoutName;

    @BindView(R.id.textInputEditTextName)
    TextInputEditText mName;

    @BindView(R.id.textInputLayoutEmail)
    TextInputLayout mLayoutEmail;

    @BindView(R.id.textInputEditTextEmail)
    TextInputEditText mEmail;

    @BindView(R.id.textInputLayoutPassword)
    TextInputLayout mLayoutPassword;

    @BindView(R.id.textInputEditTextPassword)
    TextInputEditText mPassword;

    @BindView(R.id.textInputLayoutPasswordConfirmation)
    TextInputLayout mLayoutPasswordConfirmation;

    @BindView(R.id.textInputEditTextPasswordConfirmation)
    TextInputEditText mPasswordConfirmation;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.layoutCreateUser)
    CoordinatorLayout layout;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        this.context = this;

        setSupportActionBar(mToolbar);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mName.addTextChangedListener(new MyTextWatcher(mName));
        mEmail.addTextChangedListener(new MyTextWatcher(mEmail));
        mPassword.addTextChangedListener(new MyTextWatcher(mPassword));
        mPasswordConfirmation.addTextChangedListener(new MyTextWatcher(mPasswordConfirmation));
    }

    @OnClick(R.id.buttonSignUp)
    public void signUp(View view) {

        NetworkUtils.isNetworkConnected(view.getContext());

        if (!NetworkUtils.isNetworkConnected(this)) {
            Snackbar snackbar = Snackbar.make(layout, R.string.offline_no_internet, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.offline_no_internet_retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            signUp(view);
                        }
                    });
            snackbar.show();
        }else{

            mProgressBar.setVisibility(View.VISIBLE);

            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();
            final String name = mName.getText().toString();

            if(validateForm()){
               createUser(email, password, name);
            }else{
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void createUser(final String email, final String password, final String name){
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
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

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                    user.updateProfile(profileUpdates);
                                }
                            }
                        }
                );
    }

    private boolean validateForm(){
        boolean valid = true;

        valid = validate(mLayoutName, mName);
        valid = validateEmail(mLayoutEmail, mEmail);
        valid = validate(mLayoutPassword, mPassword);
        valid = validate(mLayoutPasswordConfirmation, mPasswordConfirmation);

        if (mPassword.getText().toString().trim().equals(mPasswordConfirmation.getText().toString().trim())) {

        }

        return valid;
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
