package br.com.andersonsv.blacklotus.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.common.primitives.Ints;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.main.CardFragment;
import br.com.andersonsv.blacklotus.firebase.Deck;
import br.com.andersonsv.blacklotus.holder.DecksViewHolder;
import br.com.andersonsv.blacklotus.util.ColorDeckUtil;
import br.com.andersonsv.blacklotus.util.ShaderUtils;

import static android.graphics.Color.BLACK;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_ID;
import static com.google.common.primitives.Floats.min;

public class DeckAdapter extends FirestoreRecyclerAdapter<Deck, DecksViewHolder> {

    private Context context;
    private ProgressBar mProgressBar;
    private LinearLayout mEmptyState;
    private FragmentTransaction fragmentTransaction;

    public DeckAdapter(FirestoreRecyclerOptions recyclerOptions, @NonNull ProgressBar progressBar, LinearLayout emptyState, FragmentTransaction fragmentTransaction) {
        super(recyclerOptions);
        this.mProgressBar = progressBar;
        this.mEmptyState = emptyState;
        this.fragmentTransaction = fragmentTransaction;
    }

    @Override
    protected void onBindViewHolder(DecksViewHolder holder, int position, final Deck model) {
        mProgressBar.setVisibility(View.GONE);

        final String docId = getSnapshots().getSnapshot(position).getId();

        holder.getmDeckName().setText(model.getName());
        holder.getmDeckDescription().setText(model.getDescription());

        String numberOfCards = model.getNumberOfCards() != null ? model.getNumberOfCards().toString() : "0";
        holder.getmNumberOfCards().setText(String.format(context.getString(R.string.decks_number_cards), numberOfCards));

        List<Integer> colors = new ArrayList<>();

        if (model.getColor1() != null && model.getColor2() == null) {

            int colorValue = Color.parseColor(model.getColor1());
            ColorDeckUtil.setOneColor(colorValue, holder.getmColor());
            return;

        } else {
            ColorDeckUtil.addColorIfNonNull(model.getColor1(), colors);
            ColorDeckUtil.addColorIfNonNull(model.getColor2(), colors);
            ColorDeckUtil.addColorIfNonNull(model.getColor3(), colors);
            ColorDeckUtil.addColorIfNonNull(model.getColor4(), colors);
            ColorDeckUtil.addColorIfNonNull(model.getColor5(), colors);
        }

        int[] colorArray = Ints.toArray(colors);

        if (colorArray.length > 1){
            GradientDrawable gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    colorArray);
            gradientDrawable.setShape(GradientDrawable.OVAL);
            holder.getmColor().setImageDrawable(gradientDrawable);
        } else {
            ColorDeckUtil.setOneColor(Color.GRAY, holder.getmColor());
        }

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                colorArray);

        gradientDrawable.setCornerRadii(new float[] { 50, 50, 50, 50, 50, 50, 50, 50 });
        holder.getmColor().setImageDrawable(gradientDrawable);

        holder.setOnClickListener(new DecksViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Fragment cardFragment = CardFragment.newInstance();

                Bundle bundle = new Bundle();
                bundle.putString(DECK_ID, docId);
                cardFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, cardFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        Log.e("error", e.getMessage());
    }

    @Override
    public DecksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deck, parent, false);

        context = view.getContext();

        return new DecksViewHolder(view);
    }

    @Override
    public void onDataChanged() {
        mProgressBar.setVisibility(View.VISIBLE);
        notifyDataSetChanged();
        if (getItemCount() == 0) {
            mEmptyState.setVisibility(View.VISIBLE);
        } else {
            mEmptyState.setVisibility(View.GONE);
        }
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());

    }
}
