package br.com.andersonsv.blacklotus.feature.base;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import br.com.andersonsv.blacklotus.R;

public class BaseFragment extends Fragment {

    public void setLinearLayoutVerticalWithDivider(final RecyclerView recyclerView){
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
