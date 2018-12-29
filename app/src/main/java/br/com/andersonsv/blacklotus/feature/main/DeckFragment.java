package br.com.andersonsv.blacklotus.feature.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import br.com.andersonsv.blacklotus.firebase.DeckModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.andersonsv.blacklotus.util.Constants.DECK_ID;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_LIST;
import static br.com.andersonsv.blacklotus.util.Constants.USER_ID;

public class DeckFragment extends BaseFragment {

    private FirestoreRecyclerAdapter mAdapter;

    @BindView(R.id.recyclerViewDeck)
    RecyclerView mDeckRecycler;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.linearLayoutEmptyState)
    LinearLayout mEmptyState;

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
        FirebaseFirestore mDb = FirebaseFirestore.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        setLinearLayoutVerticalWithDivider(mDeckRecycler);

        mProgressBar.setVisibility(View.VISIBLE);

        Query query = mDb.collection(BuildConfig.FIREBASE_COLLECTION)
                .document(BuildConfig.FIREBASE_DOCUMENT)
                .collection(DECK_LIST)
                .whereEqualTo(USER_ID, mUser.getUid());

        FirestoreRecyclerOptions<DeckModel> response = new FirestoreRecyclerOptions.Builder<DeckModel>()
                .setQuery(query, DeckModel.class)
                .build();

        mAdapter = new DeckAdapter(response, mProgressBar, mEmptyState, this.getFragmentManager().beginTransaction());
        mAdapter.notifyDataSetChanged();
        mDeckRecycler.setAdapter(mAdapter);
    }


    @OnClick(R.id.fabAddDeck)
    public void addDeck(View view){
        Fragment addDeckFragment = AddDeckFragment.newInstance();

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, addDeckFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
