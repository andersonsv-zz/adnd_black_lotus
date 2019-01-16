package br.com.andersonsv.blacklotus.feature.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mobsandgeeks.saripaar.ValidationError;

import java.util.List;

import br.com.andersonsv.blacklotus.R;

import static br.com.andersonsv.blacklotus.util.Constants.VALIDATION_APP_COMPAT_EDIT_TEXT;
import static br.com.andersonsv.blacklotus.util.Constants.VALIDATION_TEXT_INPUT_EDIT_TEXT;

public class BaseActivity extends AppCompatActivity {

    protected void openActivity(Class destination){
        Intent intent = new Intent(this, destination);
        startActivity(intent);
        finish();
    }

    protected void checkFormValidation(List<ValidationError> errors){
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

    protected static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected static void alertDialogMessage(Activity activity, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setNeutralButton(activity.getResources().getString(R.string.default_close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected static void removeErrorTextInputLayout(TextInputEditText textInputEditText){
        TextInputLayout textInputLayout = (TextInputLayout) textInputEditText.getParent().getParent();
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError("");
    }

    protected void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
