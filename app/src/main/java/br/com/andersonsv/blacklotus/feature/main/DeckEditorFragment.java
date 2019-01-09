package br.com.andersonsv.blacklotus.feature.main;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.primitives.Ints;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.ArrayList;
import java.util.List;

import br.com.andersonsv.blacklotus.BuildConfig;
import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.base.BaseFragment;
import br.com.andersonsv.blacklotus.firebase.DeckModel;
import br.com.andersonsv.blacklotus.util.ColorDeckUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.andersonsv.blacklotus.util.Constants.DECK_LIST;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_PARCELABLE;

public class DeckEditorFragment extends BaseFragment implements Validator.ValidationListener {

    public static DeckEditorFragment newInstance() {
        return new DeckEditorFragment();
    }

    @BindView(R.id.imageViewColor)
    ImageView mColor;

    @BindView(R.id.layout_add_deck)
    ConstraintLayout layout;

    @Order(1)
    @NotEmpty
    @BindView(R.id.textInputEditTextName)
    TextInputEditText mName;

    @BindView(R.id.textInputEditTextDescription)
    TextInputEditText mDescription;

    @BindView(R.id.switchDeckChange)
    Switch mDeckChange;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.textViewErrorColor)
    TextView mColorError;

    private DeckModel mDeck;

    private FirebaseFirestore mDb;
    private String mUserUid;

    private Validator mValidator;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_deck, container, false);

        ButterKnife.bind(this, rootView);
        mDeck = new DeckModel();

        Bundle args = getArguments();
        if (args != null) {
            mDeck = args.getParcelable(DECK_PARCELABLE);
            mName.setText(mDeck.getName());
            mDescription.setText(mDeck.getDescription());
            mDeckChange.setChecked(mDeck.getChangeDeck());
        }

        mDb = FirebaseFirestore.getInstance();

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            mUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        updateColor();
        initValidator();

        return rootView;
    }

    private void initValidator(){
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        mValidator.setViewValidatedAction(new Validator.ViewValidatedAction() {
            @Override
            public void onAllRulesPassed(View view) {
                removeErrorTextInputLayout(mName);
            }
        });
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
        mDeck.setColor1(null);
        mDeck.setColor2(null);
        mDeck.setColor3(null);
        mDeck.setColor4(null);
        mDeck.setColor5(null);

        updateColor();
    }

    @OnClick(R.id.buttonSaveDeck)
    public void saveDeck() {
        mValidator.validate();
    }

    private boolean validateColor(){
        boolean valid = true;

        if (mDeck.getColor1() == null) {
            valid = false;
        }
        return valid;
    }

    private void checkNextColor(int color){

        //TODO - transformar em método
        String colorHex = "#".concat(Integer.toHexString(color).toUpperCase());

        boolean existsColor = checkColorExists(colorHex);

        if (existsColor) {
            Toast.makeText(getContext(), R.string.deck_color_exists_error, Toast.LENGTH_LONG).show();
            return;
        }

        if (mDeck.getColor1() == null) {
            mDeck.setColor1(colorHex);
        } else if (mDeck.getColor2() == null) {
            mDeck.setColor2(colorHex);
        } else if (mDeck.getColor3() == null) {
            mDeck.setColor3(colorHex);
        } else if (mDeck.getColor4() == null) {
            mDeck.setColor4(colorHex);
        } else {
            mDeck.setColor5(colorHex);
        }
        updateColor();
    }

    private boolean checkColorExists(String color) {
        if (mDeck.getColor1() != null && mDeck.getColor1().equals(color)) {
            return true;
        } else if (mDeck.getColor2() != null && mDeck.getColor2().equals(color)) {
            return true;
        } else if (mDeck.getColor3() != null && mDeck.getColor3().equals(color)) {
            return true;
        } else if (mDeck.getColor4() != null && mDeck.getColor4().equals(color)) {
            return true;
        } else return mDeck.getColor5() != null && mDeck.getColor5().equals(color);
    }

    private void updateColor(){

        List<Integer> colors = new ArrayList<>();

        if (mDeck.getColor1() == null) {
            ColorDeckUtil.setOneColor(Color.GRAY, mColor);
            return;
        } else if (mDeck.getColor1() != null && mDeck.getColor2() == null) {

            int colorValue = Color.parseColor(mDeck.getColor1());
            ColorDeckUtil.setOneColor(colorValue, mColor);
            return;

        } else {
            ColorDeckUtil.addColorIfNonNull(mDeck.getColor1(), colors);
            ColorDeckUtil.addColorIfNonNull(mDeck.getColor2(), colors);
            ColorDeckUtil.addColorIfNonNull(mDeck.getColor3(), colors);
            ColorDeckUtil.addColorIfNonNull(mDeck.getColor4(), colors);
            ColorDeckUtil.addColorIfNonNull(mDeck.getColor5(), colors);
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

    @Override
    public void onValidationSucceeded() {
        if (!validateColor()) {
            mColorError.setVisibility(View.VISIBLE);
            return;
        } else {
            mColorError.setVisibility(View.INVISIBLE);
        }

        mProgressBar.setVisibility(View.VISIBLE);

        if(mName.getText() != null)
            mDeck.setName(mName.getText().toString());

        if(mDescription.getText() != null)
            mDeck.setDescription(mDescription.getText().toString());

        mDeck.setChangeDeck(mDeckChange.isChecked());
        mDeck.setNumberOfCards(0);

        if (mDeck.getId() == null) {
            insertDocument();
        } else {
            updateDocument();
        }
    }

    public void insertDocument() {
        mDb.collection(BuildConfig.FIREBASE_COLLECTION)
                .document(BuildConfig.FIREBASE_DOCUMENT)
                .collection(DECK_LIST)
                .add(mDeck.objectMap(mUserUid))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Fragment cardFragment = CardFragment.newInstance();

                        mDeck.setId(documentReference.getId());

                        Bundle bundle = new Bundle();
                        bundle.putParcelable(DECK_PARCELABLE, mDeck);
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

    public void updateDocument() {
        mDb.collection(BuildConfig.FIREBASE_COLLECTION)
                .document(BuildConfig.FIREBASE_DOCUMENT)
                .collection(DECK_LIST)
                .document(mDeck.getId())
                .set(mDeck.objectMap(mUserUid))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Fragment cardFragment = CardFragment.newInstance();

                        mDeck.setId(mDeck.getId());

                        Bundle bundle = new Bundle();
                        bundle.putParcelable(DECK_PARCELABLE, mDeck);
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

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        checkFormValidation(errors);
    }
}
