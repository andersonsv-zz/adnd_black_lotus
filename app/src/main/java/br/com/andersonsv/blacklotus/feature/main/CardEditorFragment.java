package br.com.andersonsv.blacklotus.feature.main;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.data.Card;
import br.com.andersonsv.blacklotus.feature.base.BaseFragment;
import br.com.andersonsv.blacklotus.model.CardColor;
import butterknife.BindView;
import butterknife.ButterKnife;

import static br.com.andersonsv.blacklotus.util.Constants.CARD_PARCELABLE;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_ID;

public class CardEditorFragment extends BaseFragment implements Html.ImageGetter {

    public static CardEditorFragment newInstance() {
        return new CardEditorFragment();
    }

    private String mDeckId;
    private Card mCard;

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

    @BindView(R.id.recyclerViewCost)
    RecyclerView mRecyclerCost;

    @BindView(R.id.textViewPowerToughness)
    TextView mPowerToughness;

    @BindView(R.id.seekBarQuantity)
    SeekBar mQuantity;

    @BindView(R.id.textViewDescription)
    TextView mDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_card_editor, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mDeckId = bundle.getString(DECK_ID, "");
            mCard = bundle.getParcelable(CARD_PARCELABLE);

            setupView();
        }

        return rootView;
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

        mCardRarity.setText(mCard.getRarity().getTypeId());
        mCardRarity.setTextColor(mCard.getRarity().getColor());

        if (mCard.getSetName() != null) {
            mCardSet.setText(mCard.getSetName());
        }

        int power = mCard.getPower() != null ? Integer.valueOf(mCard.getPower()) : 0;
        int toughness = mCard.getToughness() != null ? Integer.valueOf(mCard.getToughness()) : 0;

        String powerToughness = "-";
        if (power != 0 && toughness != 0){
            powerToughness = String.format(getContext().getString(R.string.card_editor_format_power_toughness), power, toughness);
        }

        mPowerToughness.setText(powerToughness);

        String text = replaceTypetImgSrc(mCard.getText());

        Spanned spanned = Html.fromHtml(text, this, null);
        mDescription.setText(spanned);
    }

    @Override
    public Drawable getDrawable(String name) {

        CardColor cardColor = CardColor.getById(name);
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty;

        if (cardColor != null) {
            empty = getResources().getDrawable(cardColor.getImage());

        } else {
            empty = getResources().getDrawable(R.drawable.ic_add_circle_24dp);

        }
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, 32, 32);

        return d;
    }

    private String replaceTypetImgSrc(String textToReplace){
        return textToReplace.replaceAll("\\{([^}]*)\\}", "<img src='$1'>");
    }
}
