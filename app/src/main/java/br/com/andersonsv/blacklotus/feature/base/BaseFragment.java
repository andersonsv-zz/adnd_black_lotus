package br.com.andersonsv.blacklotus.feature.base;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
}
