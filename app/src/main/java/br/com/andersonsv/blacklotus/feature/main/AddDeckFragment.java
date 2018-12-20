package br.com.andersonsv.blacklotus.feature.main;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.primitives.Ints;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.ArrayList;
import java.util.List;

import br.com.andersonsv.blacklotus.BuildConfig;
import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.base.BaseFragment;
import br.com.andersonsv.blacklotus.firebase.Deck;
import br.com.andersonsv.blacklotus.util.ColorDeckUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.andersonsv.blacklotus.util.Constants.DECK_ID;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_LIST;

public class AddDeckFragment extends BaseFragment {

    public static AddDeckFragment newInstance() {
        return new AddDeckFragment();
    }

    @BindView(R.id.imageViewColor)
    ImageView mColor;

    @BindView(R.id.containerAddDeck)
    ConstraintLayout containerAddDeck;

    @BindView(R.id.textInputLayoutName)
    TextInputLayout mLayoutName;

    @BindView(R.id.textInputEditTextName)
    TextInputEditText mName;

    @BindView(R.id.textInputEditTextDescription)
    TextInputEditText mDescription;

    @BindView(R.id.switchDeckChange)
    Switch mDeckChange;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private Deck deck;

    private FirebaseFirestore mDb;
    private String mUserUid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_deck, container, false);

        ButterKnife.bind(this, rootView);
        deck = new Deck();

        mDb = FirebaseFirestore.getInstance();
        mUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

    @OnClick(R.id.textViewClear)
    public void clearColors(View view){
        deck.setColor1(null);
        deck.setColor2(null);
        deck.setColor3(null);
        deck.setColor4(null);
        deck.setColor5(null);

        updateColor();
    }

    @OnClick(R.id.buttonSaveDeck)
    public void saveDeck() {

        mProgressBar.setVisibility(View.VISIBLE);

        if (validateForm()){
            deck.setName(mName.getText().toString());
            deck.setDescription(mDescription.getText().toString());
            deck.setChangeDeck(mDeckChange.isChecked());
            deck.setNumberOfCards(0);


            mDb.collection(BuildConfig.FIREBASE_COLLECTION)
                .document(BuildConfig.FIREBASE_DOCUMENT)
                .collection(DECK_LIST)
                .add(deck.objectMap(mUserUid))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Fragment cardFragment = CardFragment.newInstance();

                        Bundle bundle = new Bundle();
                        bundle.putString(DECK_ID, documentReference.getId());
                        cardFragment.setArguments(bundle);

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, cardFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                        mProgressBar.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), R.string.default_error_save, Toast.LENGTH_LONG).show();
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
        }
    }

    private boolean validateForm(){
        boolean valid;
        valid = validate(mLayoutName, mName);

        if (deck.getColor1() == null) {

        }

        return valid;
    }

    private void checkNextColor(int color){

        String colorHex = "#".concat(Integer.toHexString(color).toUpperCase());

        boolean existsColor = checkColorExists(colorHex);

        if (existsColor) {
            Toast.makeText(getContext(), R.string.deck_color_exists_error, Toast.LENGTH_LONG).show();
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
            ColorDeckUtil.setOneColor(Color.GRAY, mColor);
            return;
        } else if (deck.getColor1() != null && deck.getColor2() == null) {

            int colorValue = Color.parseColor(deck.getColor1());
            ColorDeckUtil.setOneColor(colorValue, mColor);
            return;

        } else {
            ColorDeckUtil.addColorIfNonNull(deck.getColor1(), colors);
            ColorDeckUtil.addColorIfNonNull(deck.getColor2(), colors);
            ColorDeckUtil.addColorIfNonNull(deck.getColor3(), colors);
            ColorDeckUtil.addColorIfNonNull(deck.getColor4(), colors);
            ColorDeckUtil.addColorIfNonNull(deck.getColor5(), colors);
        }

        int[] colorArray = Ints.toArray(colors);

        if (colorArray.length > 1){
            GradientDrawable gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    colorArray);
            gradientDrawable.setShape(GradientDrawable.OVAL);
            mColor.setImageDrawable(gradientDrawable);
        } else {
            ColorDeckUtil.setOneColor(Color.GRAY, mColor);
        }
    }
}
