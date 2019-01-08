package br.com.andersonsv.blacklotus.feature.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import static br.com.andersonsv.blacklotus.util.Constants.DECK_LIST;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_PARCELABLE;
import static br.com.andersonsv.blacklotus.util.Constants.USER_ID;

public class DeckFragment extends BaseFragment implements DeckAdapter.DeckRecyclerOnClickHandler{

    private FirestoreRecyclerAdapter mAdapter;

    @BindView(R.id.recyclerViewDeck)
    RecyclerView mDeckRecycler;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.linearLayoutEmptyState)
    LinearLayout mEmptyState;

    private Menu mMenu;

    public static DeckFragment newInstance() {
        return new DeckFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deck, container, false);
        ButterKnife.bind(this, rootView);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.navigation_decks);

        setupView();
        getDeckList();

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mMenu = menu;
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void setupView() {
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

        mAdapter = new DeckAdapter(response, mProgressBar, mEmptyState, this);
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

    @Override
    public void onClick(DeckModel deck) {
        Fragment cardFragment = CardFragment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putParcelable(DECK_PARCELABLE, deck);
        cardFragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, cardFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLongClick(DeckModel deck) {
        showMenu(mDeckRecycler);

    }

    private void showMenu(View view){


        mMenu.removeGroup(R.menu.deck_menu);
       /* PopupMenu popup = new PopupMenu(getContext(),view);

        popup.getMenuInflater()
                .inflate(R.menu.deck_edit_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        popup.show();*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings:
                Fragment settingFragment = SettingFragment.newInstance();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, settingFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
