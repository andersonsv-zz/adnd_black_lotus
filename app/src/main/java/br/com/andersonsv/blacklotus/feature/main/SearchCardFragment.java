package br.com.andersonsv.blacklotus.feature.main;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.adapter.CardSearchAdapter;
import br.com.andersonsv.blacklotus.data.Card;
import br.com.andersonsv.blacklotus.data.Cards;
import br.com.andersonsv.blacklotus.feature.base.BaseFragment;
import br.com.andersonsv.blacklotus.holder.CardsSearchViewHolder;
import br.com.andersonsv.blacklotus.network.CardService;
import br.com.andersonsv.blacklotus.network.RetrofitClientInstance;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchCardFragment extends BaseFragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, CardsSearchViewHolder.ClickListener {

    private CardSearchAdapter mAdapter;
    private Context mContext;

    @BindView(R.id.recyclerViewSearchCard)
    RecyclerView mRecyclerCard;

    @BindView(android.R.id.empty)
    TextView mEmptyTextView;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    public static SearchCardFragment newInstance() {
        return new SearchCardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_card, container, false);

        ButterKnife.bind(this, rootView);

        mAdapter = new CardSearchAdapter(mContext, null);

        setLinearLayoutVerticalWithDivider(mRecyclerCard);
        mRecyclerCard.setAdapter(mAdapter);

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        //TODO - alterar para string xml
        searchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText == null || newText.trim().isEmpty()) {
            return false;
        }

        if(newText.length() > 3){

            mProgressBar.setVisibility(View.VISIBLE);

            CardService service = RetrofitClientInstance.getRetrofitInstance().create(CardService.class);

            Call<Cards> call = service.getCards(newText.toLowerCase(), 10);
            call.enqueue(new Callback<Cards>() {
                @Override
                public void onResponse(Call<Cards> call, Response<Cards> response) {
                    if (response.isSuccessful()){
                        Log.d("OK", "ok" + response.body().getCards().size());
                        mAdapter.setCards(response.body().getCards());
                        mProgressBar.setVisibility(View.GONE);
                        mEmptyTextView.setVisibility(View.GONE);

                        if (response.body().getCards().size() > 0) {
                            mEmptyTextView.setVisibility(View.VISIBLE);
                        }

                    }
                }

                @Override
                public void onFailure(Call<Cards> call, Throwable t) {
                    Log.e("ERROR", t.getLocalizedMessage());
                    mProgressBar.setVisibility(View.GONE);
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
