package br.com.andersonsv.blacklotus.feature.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.Arrays;

import br.com.andersonsv.blacklotus.BuildConfig;
import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.data.Card;
import br.com.andersonsv.blacklotus.feature.base.BaseFragment;
import br.com.andersonsv.blacklotus.firebase.CardModel;
import br.com.andersonsv.blacklotus.firebase.DeckModel;
import br.com.andersonsv.blacklotus.model.Rarity;
import br.com.andersonsv.blacklotus.util.ImageHtmlUtil;
import br.com.andersonsv.blacklotus.util.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.andersonsv.blacklotus.util.Constants.CARD_DATA;
import static br.com.andersonsv.blacklotus.util.Constants.CARD_LIST;
import static br.com.andersonsv.blacklotus.util.Constants.CARD_MODEL;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_PARCELABLE;
import static br.com.andersonsv.blacklotus.util.StringUtils.replaceTypetImgSrc;

public class CardEditorFragment extends BaseFragment {

    public static CardEditorFragment newInstance() {
        return new CardEditorFragment();
    }

    private DeckModel mDeck;
    private CardModel mCard;

    @BindView(R.id.imageViewCard)
    ImageView mCardImage;

    @BindView(R.id.textViewCardName)
    TextView mCardName;

    @BindView(R.id.textViewType)
    TextView mCardType;

    @BindView(R.id.textViewRarity)
    TextView mCardRarity;

    @BindView(R.id.textViewSetName)
    TextView mCardSet;

    @BindView(R.id.textViewCost)
    TextView mCost;

    @BindView(R.id.textViewPowerToughness)
    TextView mPowerToughness;

    @BindView(R.id.seekBarQuantity)
    DiscreteSeekBar mQuantity;

    @BindView(R.id.textViewDescription)
    TextView mDescription;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.textViewQuantityInfo)
    TextView mQuantityInfo;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_card_editor, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            mDeck = bundle.getParcelable(DECK_PARCELABLE);

            if (bundle.containsKey(CARD_DATA)) {
                Card card = bundle.getParcelable(CARD_DATA);
                mCard = convertDataToModel(card);

            }
            if (bundle.containsKey(CARD_MODEL)) {
                mCard = bundle.getParcelable(CARD_MODEL);
            }
            setupView();
        }
        return rootView;
    }

    private CardModel convertDataToModel(Card card) {
        CardModel cardModel = new CardModel();

        cardModel.setId(card.getId());
        cardModel.setName(card.getName());
        cardModel.setCost(card.getManaCost());
        cardModel.setImage(card.getImage());
        cardModel.setQuantity(1);
        cardModel.setRarity(card.getRarity().getTypeId());
        cardModel.setType(card.getType());
        cardModel.setText(card.getText());
        cardModel.setPower(card.getPower());
        cardModel.setToughness(card.getToughness());
        cardModel.setSetName(card.getSetName());
        cardModel.setLand(card.getLand());

        return cardModel;
    }

    private void setupView() {
        if (mCard.getImage() != null) {
            Picasso.with(getContext())
                    .load(mCard.getImage())
                    .into(mCardImage);
        }

        mCardName.setText(mCard.getName());
        mCardType.setText(mCard.getType());

        Rarity rarity = Rarity.getByType(mCard.getRarity());

        mCardRarity.setText(rarity.getTypeId());
        mCardRarity.setTextColor(rarity.getColor());

        if (mCard.getSetName() != null) {
            mCardSet.setText(mCard.getSetName());
        }

        int power = mCard.getPower() != null ? Integer.valueOf(mCard.getPower()) : 0;
        int toughness = mCard.getToughness() != null ? Integer.valueOf(mCard.getToughness()) : 0;

        String powerToughness = "-";
        if (power != 0 && toughness != 0){
            powerToughness = StringUtils.formatStringInt(getContext().getString(R.string.card_editor_format_power_toughness), Arrays.asList( power, toughness));
        }

        mPowerToughness.setText(powerToughness);

        if (mCard.getText() != null) {
            String text = replaceTypetImgSrc(mCard.getText());

            Html.ImageGetter imageGetter =  new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                    return new ImageHtmlUtil(getContext()).generate(source);
                }
            };

            Spanned spanned = Html.fromHtml(text, imageGetter, null);
            mDescription.setText(spanned);
        }

        if (mCard.getCost() != null) {
            String cost = replaceTypetImgSrc(mCard.getCost());

            Html.ImageGetter imageGetter =  new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                    return new ImageHtmlUtil(getContext()).generate(source);
                }
            };

            Spanned spannedCost = Html.fromHtml(cost, imageGetter, null);
            mCost.setText(spannedCost);
        }

        mQuantityInfo.setText(mCard.getQuantity().toString());
        mQuantity.setProgress(mCard.getQuantity());

        mQuantity.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                mQuantityInfo.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) { }
        });

    }

    @OnClick(R.id.buttonSaveCard)
    public void saveCard(View view){

        mProgressBar.setVisibility(View.VISIBLE);
        mCard.setQuantity(mQuantity.getProgress());

        if (mCard.getId() == null) {
            insertDocument();
        } else {
            updateDocument();
        }
    }

    private void insertDocument() {
        FirebaseFirestore mDb = FirebaseFirestore.getInstance();

        mDb.collection(BuildConfig.FIREBASE_COLLECTION)
            .document(BuildConfig.FIREBASE_DOCUMENT)
            .collection(CARD_LIST)
            .add(mCard.objectMap(null))
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Fragment cardFragment = CardFragment.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(DECK_PARCELABLE, mDeck);
                    cardFragment.setArguments(bundle);
                    openFragment(cardFragment);

                    mProgressBar.setVisibility(View.GONE);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showSaveDialog(getString(R.string.card_error_title),getString(R.string.card_error_message));
                    mProgressBar.setVisibility(View.GONE);
                }
            });
    }

    private void updateDocument() {
        FirebaseFirestore mDb = FirebaseFirestore.getInstance();

        mDb.collection(BuildConfig.FIREBASE_COLLECTION)
            .document(BuildConfig.FIREBASE_DOCUMENT)
            .collection(CARD_LIST)
            .document(mCard.getId())
            .set(mCard.objectMap(mDeck.getId()))
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Fragment cardFragment = CardFragment.newInstance();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(DECK_PARCELABLE, mDeck);
                    cardFragment.setArguments(bundle);
                    openFragment(cardFragment);

                    mProgressBar.setVisibility(View.GONE);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showSaveDialog(getString(R.string.card_error_title),getString(R.string.card_error_message));
                    mProgressBar.setVisibility(View.GONE);
                }
            });
    }
}

