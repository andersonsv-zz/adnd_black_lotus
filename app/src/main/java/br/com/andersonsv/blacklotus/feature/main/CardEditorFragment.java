package br.com.andersonsv.blacklotus.feature.main;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.Arrays;

import br.com.andersonsv.blacklotus.BuildConfig;
import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.data.Card;
import br.com.andersonsv.blacklotus.feature.base.BaseFragment;
import br.com.andersonsv.blacklotus.firebase.CardModel;
import br.com.andersonsv.blacklotus.model.CardColor;
import br.com.andersonsv.blacklotus.model.Rarity;
import br.com.andersonsv.blacklotus.util.StringUtils;
import br.com.andersonsv.blacklotus.widget.TextDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.andersonsv.blacklotus.util.Constants.CARD_DATA;
import static br.com.andersonsv.blacklotus.util.Constants.CARD_LIST;
import static br.com.andersonsv.blacklotus.util.Constants.CARD_MODEL;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_ID;
import static br.com.andersonsv.blacklotus.util.StringUtils.replaceTypetImgSrc;

public class CardEditorFragment extends BaseFragment implements Html.ImageGetter {

    public static CardEditorFragment newInstance() {
        return new CardEditorFragment();
    }

    private String mDeckId;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_card_editor, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mDeckId = bundle.getString(DECK_ID, "");

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
        } else {
            //TODO - setar imagem de vazio
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
            //powerToughness = String.format(getContext().getString(R.string.card_editor_format_power_toughness), power, toughness);
        }

        mPowerToughness.setText(powerToughness);

        if (mCard.getText() != null) {
            String text = replaceTypetImgSrc(mCard.getText());
            Spanned spanned = Html.fromHtml(text, this, null);
            mDescription.setText(spanned);
        }

        if (mCard.getCost() != null) {
            String cost = replaceTypetImgSrc(mCard.getCost());
            Spanned spannedCost = Html.fromHtml(cost, this, null);
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

    @Override
    public Drawable getDrawable(String name) {

        CardColor cardColor = CardColor.getById(name);
        int size = getResources().getInteger(R.integer.card_cost_list);

        if (cardColor != null) {
            LevelListDrawable d = new LevelListDrawable();

            Drawable empty = getResources().getDrawable(cardColor.getImage());
            d.addLevel(0, 0, empty);
            d.setBounds(0, 0, size, size);

            return d;

        } else {
            //Copied by - https://github.com/devunwired/textdrawable
            TextDrawable textDrawable = new TextDrawable(getContext());

            textDrawable.setText(name);
            textDrawable.setTextColor(Color.BLACK);
            //textDrawable.setTextSize(12);
            textDrawable.setTextAlign(Layout.Alignment.ALIGN_NORMAL);

            GradientDrawable gD = new GradientDrawable();
            gD.setColor(Color.GRAY);
            gD.setShape(GradientDrawable.OVAL);
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{gD, textDrawable});

            layerDrawable.setBounds(0, 0, size, size);

            return layerDrawable;
        }
    }

    @OnClick(R.id.buttonSaveCard)
    public void saveCard(View view){

        mProgressBar.setVisibility(View.VISIBLE);
        mCard.setQuantity(mQuantity.getProgress());

        FirebaseFirestore mDb = FirebaseFirestore.getInstance();

        mDb.collection(BuildConfig.FIREBASE_COLLECTION)
                .document(BuildConfig.FIREBASE_DOCUMENT)
                .collection(CARD_LIST)
                .document(mCard.getId())
                .set(mCard.objectMap(mDeckId))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showSaveDialog(getString(R.string.card_save_title),getString(R.string.card_save_message));
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

