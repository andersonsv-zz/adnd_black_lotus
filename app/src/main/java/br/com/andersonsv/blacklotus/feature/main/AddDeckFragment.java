package br.com.andersonsv.blacklotus.feature.main;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.firebase.Deck;
import butterknife.BindView;
import butterknife.ButterKnife;

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

        return rootView;
    }

    private void updateColor(){

        if (deck.getColor1() == null) {

        }

        /*GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.WHITE,
                        Color.BLUE,
                });

        gradientDrawable.setCornerRadii(new float[] { 50, 50, 50, 50, 50, 50, 50, 50 });
        holder.getmColor().setImageDrawable(gradientDrawable);*/
    }


    /*new SpectrumDialog.Builder(getContext())
            .setColors(R.array.demo_colors)
                .setSelectedColorRes(R.color.md_blue_500)
                .setDismissOnColorSelected(true)
                .setOutlineWidth(2)
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
        @Override public void onColorSelected(boolean positiveResult, @ColorInt int color) {
            if (positiveResult) {
                Toast.makeText(getContext(), "Color selected: #" + Integer.toHexString(color).toUpperCase(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Dialog cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }).build().show(getFragmentManager(), "dialog_demo_1");*/
}
