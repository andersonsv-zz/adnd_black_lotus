package br.com.andersonsv.blacklotus.feature.main;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.primitives.Ints;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.ArrayList;
import java.util.List;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.firebase.Deck;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddDeckFragment extends Fragment {

    public static AddDeckFragment newInstance() {
        return new AddDeckFragment();
    }

    @BindView(R.id.imageViewColor)
    ImageView mColor;

    private Deck deck;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_deck, container, false);

        ButterKnife.bind(this, rootView);
        deck = new Deck();

        updateColor();

        return rootView;
    }

    @OnClick(R.id.buttonAddColor)
    public void addColor(View view){
        new SpectrumDialog.Builder(view.getContext())
            .setColors(R.array.deck_colors)
            .setTitle(R.string.deck_add_color_title)
            .setDismissOnColorSelected(false)
            .setPositiveButtonText(R.string.deck_select_color_confirm)
            .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                @Override public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                    if (positiveResult) {
                        checkNextColor(color);
                    }
                }
            }).build().show(getFragmentManager(), "dialog_demo_1");
    }

    private void checkNextColor(int color){

        String colorHex = "#".concat(Integer.toHexString(color).toUpperCase());

        boolean existsColor = checkColorExists(colorHex);

        if (existsColor) {
            Toast.makeText(getContext(), R.string.deck_color_exists_error, Toast.LENGTH_LONG);
            return;
        }

        if (deck.getColor1() == null) {
            deck.setColor1(colorHex);
        } else if (deck.getColor2() == null) {
            deck.setColor2(colorHex);
        } else if (deck.getColor3() == null) {
            deck.setColor3(colorHex);
        } else if (deck.getColor4() == null) {
            deck.setColor4(colorHex);
        } else {
            deck.setColor5(colorHex);
        }
        updateColor();
    }

    private boolean checkColorExists(String color) {
        if (deck.getColor1() != null && deck.getColor1().equals(color)) {
            return true;
        } else if (deck.getColor2() != null && deck.getColor2().equals(color)) {
            return true;
        } else if (deck.getColor3() != null && deck.getColor3().equals(color)) {
            return true;
        } else if (deck.getColor4() != null && deck.getColor4().equals(color)) {
            return true;
        } else if (deck.getColor5() != null && deck.getColor5().equals(color)) {
            return true;
        }
        return false;
    }

    private void updateColor(){

        List<Integer> colors = new ArrayList<>();

        if (deck.getColor1() == null) {
            setOneColor(Color.GRAY);
            return;
        } else if (deck.getColor1() != null && deck.getColor2() == null) {

            int colorValue = Color.parseColor(deck.getColor1());
            setOneColor(colorValue);
            return;

        } else {
            addColorIfNonNull(deck.getColor1(), colors);
            addColorIfNonNull(deck.getColor2(), colors);
            addColorIfNonNull(deck.getColor3(), colors);
            addColorIfNonNull(deck.getColor4(), colors);
            addColorIfNonNull(deck.getColor5(), colors);
        }

        int[] colorArray = Ints.toArray(colors);

        if (colorArray.length > 1){
            GradientDrawable gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    colorArray);
            gradientDrawable.setShape(GradientDrawable.OVAL);
            mColor.setImageDrawable(gradientDrawable);
        } else {
            setOneColor(Color.GRAY);
        }
    }

    private void setOneColor(int color){
        ShapeDrawable sd = new ShapeDrawable(new OvalShape());
        sd.setIntrinsicHeight(100);
        sd.setIntrinsicWidth(100);
        sd.getPaint().setColor(color);
        mColor.setBackground(sd);
    }

    private void addColorIfNonNull(final String color, final List<Integer> colors){
        if (color != null) {
            int colorValue = Color.parseColor(color);
            colors.add(colorValue);
        }
    }
}
