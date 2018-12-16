package br.com.andersonsv.blacklotus.feature.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import br.com.andersonsv.blacklotus.BuildConfig;
import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.adapter.DeckAdapter;
import br.com.andersonsv.blacklotus.feature.base.BaseFragment;
import br.com.andersonsv.blacklotus.firebase.Deck;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DeckFragment extends BaseFragment {

    private FirebaseUser mUser;
    private FirebaseFirestore mDb;
    private String mUserUid;
    private FirestoreRecyclerAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;

    private final static String DECK_LIST = "decks";
    private final static String USER_ID = "userId";

    @BindView(R.id.recyclerViewDeck)
    RecyclerView mDeckRecycler;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    public static DeckFragment newInstance() {
        return new DeckFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deck, container, false);
        ButterKnife.bind(this, rootView);

        getDeckList();

        return rootView;
    }

    private void getDeckList() {
        mDb = FirebaseFirestore.getInstance();

        setLinearLayoutVerticalWithDivider(mDeckRecycler);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser != null){
            mUserUid = mUser.getUid();
        } else {

            //snack
            //TODO - usuario nao identificado, escrever mensagem
        }

        Query query = mDb.collection(BuildConfig.FIREBASE_COLLECTION)
                .document(BuildConfig.FIREBASE_DOCUMENT)
                .collection(DECK_LIST)
                .whereEqualTo(USER_ID, mUserUid);

        FirestoreRecyclerOptions<Deck> response = new FirestoreRecyclerOptions.Builder<Deck>()
                .setQuery(query, Deck.class)
                .build();

        mAdapter = new DeckAdapter(response);
        mAdapter.notifyDataSetChanged();
        mDeckRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
