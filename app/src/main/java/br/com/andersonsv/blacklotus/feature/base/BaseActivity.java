package br.com.andersonsv.blacklotus.feature.base;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import br.com.andersonsv.blacklotus.R;

public class BaseActivity extends AppCompatActivity {

    public void openActivity(Class destination){
        Intent intent = new Intent(this, destination);
        startActivity(intent);
        finish();
    }

    public void snack(View view, String message){

        Snackbar.make(view,
                message,
                Snackbar.LENGTH_INDEFINITE)
                .setAction("Action", null).show();
    }

    public void toast(Context context, String message){
        Toast.makeText(context,
                message,
                Toast.LENGTH_LONG)
                .show();
    }

    public boolean validate(@NonNull TextInputLayout textInputLayout, @NonNull TextInputEditText textInputEditText) {
        if (textInputEditText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(getString(R.string.default_required));
            requestFocus(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean validateEmail(@NonNull TextInputLayout textInputLayout, @NonNull TextInputEditText textInputEditText) {
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
}
