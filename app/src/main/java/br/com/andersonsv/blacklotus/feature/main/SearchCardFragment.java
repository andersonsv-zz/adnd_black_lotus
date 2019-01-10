package br.com.andersonsv.blacklotus.feature.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.adapter.CardSearchAdapter;
import br.com.andersonsv.blacklotus.data.Card;
import br.com.andersonsv.blacklotus.data.Cards;
import br.com.andersonsv.blacklotus.feature.base.BaseFragment;
import br.com.andersonsv.blacklotus.firebase.DeckModel;
import br.com.andersonsv.blacklotus.network.CardService;
import br.com.andersonsv.blacklotus.network.RetrofitClientInstance;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static br.com.andersonsv.blacklotus.util.Constants.CARD_DATA;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_PARCELABLE;

public class SearchCardFragment extends BaseFragment implements SearchView.OnQueryTextListener, CardSearchAdapter.CardSearchRecyclerOnClickHandler{

    private CardSearchAdapter mAdapter;
    private DeckModel mDeck;

    @BindView(R.id.recyclerViewSearchCard)
    RecyclerView mRecyclerCard;

    @BindView(android.R.id.empty)
    TextView mEmptyTextView;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private SearchView searchView;

    private boolean secondLanguageActive = false;

    public static SearchCardFragment newInstance() {
        return new SearchCardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_card, container, false);

        ButterKnife.bind(this, rootView);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mDeck = bundle.getParcelable(DECK_PARCELABLE);
        }

        mAdapter = new CardSearchAdapter(null, mDeck.getId(), this);

        setLinearLayoutVerticalWithDivider(mRecyclerCard);
        mRecyclerCard.setAdapter(mAdapter);

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        searchView.setQueryHint(getString(R.string.default_search));
        searchView.setQuery("Black Lotus",false);
        searchView.setFocusable(true);
        searchView.requestFocusFromTouch();

        MenuItem item = menu.findItem(R.id.app_bar_switch);
        item.setActionView(R.layout.switch_item);

        final Switch switchSecondLanguage = item.getActionView().findViewById(R.id.switchSecondLanguage);
        switchSecondLanguage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    secondLanguageActive = true;
                } else {
                    secondLanguageActive = false;
                }
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        if (query == null || query.trim().isEmpty()) {
            return false;
        }

        if(query.length() > 3){

            //close keyboard
            searchView.clearFocus();

            mProgressBar.setVisibility(View.VISIBLE);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String secondLanguage = prefs.getString("key_second_language_api", "");

            //second language
            if (secondLanguageActive && !"".equalsIgnoreCase(secondLanguage)){
                searchSecondLanguage(query, secondLanguage);
            } else {
                searchDefaultLanguage(query);
            }


            return true;
        }
        return true;
    }

    private void searchSecondLanguage(String query, String secondLanguage) {

        CardService service = RetrofitClientInstance.getRetrofitInstance().create(CardService.class);

        Call<Cards> call = service.getCardsByLanguage(query.toLowerCase(), 10, secondLanguage);
        call.enqueue(new Callback<Cards>() {
            @Override
            public void onResponse(Call<Cards> call, Response<Cards> response) {
                if (response.isSuccessful()){
                    Log.d("OK", "ok" + response.body().getCards().size());
                    mAdapter.setCards(response.body().getCards());
                    mProgressBar.setVisibility(View.GONE);
                    mEmptyTextView.setVisibility(View.GONE);

                    if (response.body().getCards().size() <= 0) {
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
    }


    public void searchDefaultLanguage(String query) {
        CardService service = RetrofitClientInstance.getRetrofitInstance().create(CardService.class);

        Call<Cards> call = service.getCards(query.toLowerCase(), 10);
        call.enqueue(new Callback<Cards>() {
            @Override
            public void onResponse(Call<Cards> call, Response<Cards> response) {
                if (response.isSuccessful()){
                    Log.d("OK", "ok" + response.body().getCards().size());
                    mAdapter.setCards(response.body().getCards());
                    mProgressBar.setVisibility(View.GONE);
                    mEmptyTextView.setVisibility(View.GONE);

                    if (response.body().getCards().size() <= 0) {
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
    }



    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onClick(Card card) {
        Fragment cardAddFragment = CardEditorFragment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putParcelable(CARD_DATA, card);
        bundle.putParcelable(DECK_PARCELABLE, mDeck);
        cardAddFragment.setArguments(bundle);

        openFragment(cardAddFragment);
    }

}
