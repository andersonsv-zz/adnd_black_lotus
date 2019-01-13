package br.com.andersonsv.blacklotus.feature.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.andersonsv.blacklotus.BuildConfig;
import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.adapter.CardAdapter;
import br.com.andersonsv.blacklotus.feature.base.BaseFragment;
import br.com.andersonsv.blacklotus.firebase.CardModel;
import br.com.andersonsv.blacklotus.firebase.DeckModel;
import br.com.andersonsv.blacklotus.provider.DeckWidgetProvider;
import br.com.andersonsv.blacklotus.util.CsvWriter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.andersonsv.blacklotus.util.Constants.CARD_LAND;
import static br.com.andersonsv.blacklotus.util.Constants.CARD_LIST;
import static br.com.andersonsv.blacklotus.util.Constants.CARD_MODEL;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_ID;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_PARCELABLE;

public class CardFragment extends BaseFragment implements CardAdapter.OnCardSelectedListener {

    private DeckModel mDeck;
    private FirebaseFirestore mDb;
    private CardAdapter mLandAdapter;
    private CardAdapter mCardAdapter;

    @BindView(R.id.deckName)
    TextView mDeckName;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.linearLayoutEmptyStateLand)
    LinearLayout mLandEmptyState;

    @BindView(R.id.linearLayoutEmptyStateCard)
    LinearLayout mCardEmptyState;

    @BindView(R.id.recyclerViewCardLand)
    RecyclerView mCardLandRecycler;

    @BindView(R.id.recyclerViewCard)
    RecyclerView mCardRecycler;

    @BindView(R.id.cardLayout)
    ConstraintLayout layout;

    private List<CardModel> cardModelList = new ArrayList<>();

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

    public static CardFragment newInstance() {
        return new CardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card, container, false);

        ButterKnife.bind(this, rootView);

        mProgressBar.setVisibility(View.VISIBLE);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mDeck = bundle.getParcelable(DECK_PARCELABLE);
            mDeckName.setText(mDeck.getName());
        }

        mDb = FirebaseFirestore.getInstance();

        getLandList();
        getCardList();

        sendCardsToWidget();

        setHasOptionsMenu(true);

        return rootView;
    }

    private void getCardList() {

        Query query = mDb.collection(BuildConfig.FIREBASE_COLLECTION)
                .document(BuildConfig.FIREBASE_DOCUMENT)
                .collection(CARD_LIST)
                .whereEqualTo(DECK_ID, mDeck.getId())
                .whereEqualTo(CARD_LAND, false);

        mCardAdapter = new CardAdapter(query, this) {
            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    mCardEmptyState.setVisibility(View.VISIBLE);

                } else {
                    mCardEmptyState.setVisibility(View.GONE);
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                snack(layout, String.format(getString(R.string.default_error), e.getMessage()));
            }
        };

        setLinearLayoutVerticalWithDivider(mCardRecycler);
        mCardRecycler.setAdapter(mCardAdapter);
    }

    private void getLandList() {
        setLinearLayoutVerticalWithDivider(mCardLandRecycler);

        Query query = mDb.collection(BuildConfig.FIREBASE_COLLECTION)
                .document(BuildConfig.FIREBASE_DOCUMENT)
                .collection(CARD_LIST)
                .whereEqualTo(DECK_ID, mDeck.getId())
                .whereEqualTo(CARD_LAND, true);

        mLandAdapter = new CardAdapter(query, this) {
            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    mLandEmptyState.setVisibility(View.VISIBLE);

                } else {
                    mLandEmptyState.setVisibility(View.GONE);
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                snack(layout, String.format(getString(R.string.default_error), e.getMessage()));
            }
        };

        setLinearLayoutVerticalWithDivider(mCardLandRecycler);
        mCardLandRecycler.setAdapter(mLandAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mLandAdapter.startListening();
        mCardAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLandAdapter.stopListening();
        mCardAdapter.stopListening();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.deck_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @OnClick(R.id.fabAddCard)
    public void addCard(View view){
        Fragment searchCardFragment = SearchCardFragment.newInstance();

        Bundle bundle = new Bundle();
        bundle.putParcelable(DECK_PARCELABLE, mDeck);
        searchCardFragment.setArguments(bundle);

        openFragment(searchCardFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.deck_share:

                int writeExternalStoragePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                } else {
                    File target = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    String outString = new SimpleDateFormat(getResources().getString(R.string.date_convert_short)).format(new Date());

                    CsvWriter.generateCsvFile(target, outString, cardModelList, getResources());

                    Uri contentUri = FileProvider.getUriForFile(getActivity(), "br.com.andersonsv.blacklotus.app.fileprovider", target);

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    sendIntent.setType(CsvWriter.TYPE_CSV);
                    startActivity(sendIntent);
                }
             break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSelected(DocumentSnapshot card) {
        Fragment cardEditorFragment = CardEditorFragment.newInstance();
        CardModel cardModel  = card.toObject(CardModel.class);
        cardModel.setId(card.getId());

        Bundle bundle = new Bundle();
        bundle.putParcelable(CARD_MODEL, cardModel);
        bundle.putParcelable(DECK_PARCELABLE, mDeck);
        cardEditorFragment.setArguments(bundle);
        openFragment(cardEditorFragment);
    }

    private void sendCardsToWidget(){
        mDb.collection(BuildConfig.FIREBASE_COLLECTION)
            .document(BuildConfig.FIREBASE_DOCUMENT)
            .collection(CARD_LIST)
            .whereEqualTo(DECK_ID, mDeck.getId())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            cardModelList.add(document.toObject(CardModel.class));
                        }

                        Intent intent = new Intent(getActivity(), DeckWidgetProvider.class);

                        intent.putExtra(DECK_PARCELABLE, mDeck);
                        intent.putParcelableArrayListExtra(CARD_LIST,  (ArrayList<? extends Parcelable>) cardModelList);
                        getActivity().sendBroadcast(intent);

                    }
                }
            });
    }
}