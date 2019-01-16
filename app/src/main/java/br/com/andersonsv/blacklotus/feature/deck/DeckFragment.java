package br.com.andersonsv.blacklotus.feature.deck;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import br.com.andersonsv.blacklotus.BuildConfig;
import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.adapter.DeckAdapter;
import br.com.andersonsv.blacklotus.feature.base.BaseFragment;
import br.com.andersonsv.blacklotus.feature.card.CardFragment;
import br.com.andersonsv.blacklotus.feature.setting.SettingFragment;
import br.com.andersonsv.blacklotus.firebase.DeckModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.andersonsv.blacklotus.util.Constants.CARD_LIST;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_ID;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_LIST;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_PARCELABLE;
import static br.com.andersonsv.blacklotus.util.Constants.USER_ID;

public class DeckFragment extends BaseFragment implements DeckAdapter.OnDeckSelectedListener {


    @BindView(R.id.recyclerViewDeck)
    RecyclerView mDeckRecycler;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.linearLayoutEmptyState)
    LinearLayout mEmptyState;

    private Menu mMenu;

    private DeckModel mDeck;

    private DeckAdapter mAdapter;

    @BindView(R.id.deckLayout)
    ConstraintLayout layout;

    public static DeckFragment newInstance() {
        return new DeckFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deck, container, false);
        ButterKnife.bind(this, rootView);

        getActivity().setTitle(R.string.navigation_decks);

        FirebaseFirestore.setLoggingEnabled(true);
        setHasOptionsMenu(true);

        getDeckList();

        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.edit_deck).setVisible(false);
        menu.findItem(R.id.delete_deck).setVisible(false);
        menu.findItem(R.id.cancel_edit).setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mMenu = menu;
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void getDeckList() {

        FirebaseFirestore mDb = FirebaseFirestore.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        mProgressBar.setVisibility(View.VISIBLE);

        Query query = mDb.collection(BuildConfig.FIREBASE_COLLECTION)
                .document(BuildConfig.FIREBASE_DOCUMENT)
                .collection(DECK_LIST)
                .whereEqualTo(USER_ID, mUser.getUid());

        mAdapter = new DeckAdapter(query, this) {
            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    mEmptyState.setVisibility(View.VISIBLE);

                } else {
                    mEmptyState.setVisibility(View.GONE);
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                snack(layout, String.format(getString(R.string.default_error), e.getMessage()));
            }
        };

        setLinearLayoutVerticalWithDivider(mDeckRecycler);
        mDeckRecycler.setAdapter(mAdapter);
    }


    @OnClick(R.id.fabAddDeck)
    public void addDeck(View view){
        Fragment deckEditorFragment = DeckEditorFragment.newInstance();
        openFragment(deckEditorFragment);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings:
                Fragment settingFragment = SettingFragment.newInstance();
                openFragment(settingFragment);
                break;
            case R.id.cancel_edit:
                mMenu.findItem(R.id.edit_deck).setVisible(false);
                mMenu.findItem(R.id.delete_deck).setVisible(false);
                mMenu.findItem(R.id.cancel_edit).setVisible(false);
                mMenu.findItem(R.id.settings).setVisible(true);
                break;
            case R.id.edit_deck:
                Fragment deckEditorFragment = DeckEditorFragment.newInstance();
                Bundle bundle = new Bundle();

                bundle.putParcelable(DECK_PARCELABLE, mDeck);
                deckEditorFragment.setArguments(bundle);

                openFragment(deckEditorFragment);
                break;
            case R.id.delete_deck:
                deleteDeckAndCards();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteDeckAndCards() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.deck_delete_title));
        builder.setMessage(String.format(getString(R.string.deck_delete_title_confirm), mDeck.getName()));
        builder.setPositiveButton(getString(R.string.deck_delete_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                FirebaseFirestore mDb = FirebaseFirestore.getInstance();

                mDb.collection(BuildConfig.FIREBASE_COLLECTION)
                   .document(BuildConfig.FIREBASE_DOCUMENT)
                   .collection(CARD_LIST)
                   .whereEqualTo(DECK_ID, mDeck.getId())
                   .get()
                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task != null && task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().delete();
                                }
                            }
                        }
                   });

                mDb.collection(BuildConfig.FIREBASE_COLLECTION)
                    .document(BuildConfig.FIREBASE_DOCUMENT)
                    .collection(DECK_LIST).document(mDeck.getId())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mMenu.findItem(R.id.edit_deck).setVisible(false);
                            mMenu.findItem(R.id.delete_deck).setVisible(false);
                            mMenu.findItem(R.id.cancel_edit).setVisible(false);
                            mMenu.findItem(R.id.settings).setVisible(true);
                            showSaveDialog(getString(R.string.default_deleted), getString(R.string.deck_delete_confirm_msg));
                        }
                });
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.default_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onDeckSelected(DocumentSnapshot deck) {
        mDeck = deck.toObject(DeckModel.class);
        mDeck.setId(deck.getId());

        Fragment cardFragment = CardFragment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putParcelable(DECK_PARCELABLE, mDeck);
        cardFragment.setArguments(bundle);
        openFragment(cardFragment);
    }

    @Override
    public void onDeckEdited(DocumentSnapshot deck) {
        mMenu.findItem(R.id.edit_deck).setVisible(true);
        mMenu.findItem(R.id.delete_deck).setVisible(true);
        mMenu.findItem(R.id.cancel_edit).setVisible(true);
        mMenu.findItem(R.id.settings).setVisible(false);

        mDeck = deck.toObject(DeckModel.class);
        mDeck.setId(deck.getId());
    }
}
