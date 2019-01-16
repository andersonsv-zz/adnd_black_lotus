package br.com.andersonsv.blacklotus.feature.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mobsandgeeks.saripaar.ValidationError;

import java.util.List;

import br.com.andersonsv.blacklotus.R;

import static br.com.andersonsv.blacklotus.util.Constants.VALIDATION_APP_COMPAT_EDIT_TEXT;
import static br.com.andersonsv.blacklotus.util.Constants.VALIDATION_TEXT_INPUT_EDIT_TEXT;

public class BaseFragment extends Fragment {

    protected void setLinearLayoutVerticalWithDivider(final RecyclerView recyclerView){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void snack(View view, String message){

        Snackbar.make(view,
                message,
                Snackbar.LENGTH_INDEFINITE)
                .setAction("Action", null).show();
    }

    protected void showSaveDialog(String title, String message){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(title);
        builder.setMessage(message);

        builder.setNeutralButton(getContext().getResources().getString(R.string.default_close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        }).show();

    }

    protected void openActivity(Class destination){
        Intent intent = new Intent(getActivity(), destination);
        startActivity(intent);
        getActivity().finish();
    }

    protected void checkFormValidation(List<ValidationError> errors){
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getActivity());

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

    protected static void removeErrorTextInputLayout(TextInputEditText textInputEditText){
        TextInputLayout textInputLayout = (TextInputLayout) textInputEditText.getParent().getParent();
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError("");
    }

    protected void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
