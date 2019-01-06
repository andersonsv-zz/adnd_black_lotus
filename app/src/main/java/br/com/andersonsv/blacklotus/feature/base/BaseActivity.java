package br.com.andersonsv.blacklotus.feature.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.List;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.login.LoginActivity;

import static br.com.andersonsv.blacklotus.util.Constants.VALIDATION_APP_COMPAT_EDIT_TEXT;
import static br.com.andersonsv.blacklotus.util.Constants.VALIDATION_TEXT_INPUT_EDIT_TEXT;

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

    public void checkFormValidation(List<ValidationError> errors){
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

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void alertDialogMessage(Activity activity, String title, String message){
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

    public static void removeErrorTextInputLayout(TextInputEditText textInputEditText){
        TextInputLayout textInputLayout = (TextInputLayout) textInputEditText.getParent().getParent();
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError("");
    }
}
