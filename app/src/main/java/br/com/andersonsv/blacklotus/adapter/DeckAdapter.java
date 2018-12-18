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
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.firebase.Deck;
import br.com.andersonsv.blacklotus.holder.DecksViewHolder;
import br.com.andersonsv.blacklotus.util.ShaderUtils;

import static android.graphics.Color.BLACK;
import static com.google.common.primitives.Floats.min;

public class DeckAdapter extends FirestoreRecyclerAdapter<Deck, DecksViewHolder> {

    private Context context;
    private ProgressBar mProgressBar;
    private LinearLayout mEmptyState;

    public DeckAdapter(FirestoreRecyclerOptions recyclerOptions, @NonNull ProgressBar progressBar, LinearLayout emptyState) {
        super(recyclerOptions);
        mProgressBar = progressBar;
        mEmptyState = emptyState;
    }

    @Override
    protected void onBindViewHolder(DecksViewHolder holder, int position, Deck model) {
        mProgressBar.setVisibility(View.GONE);

        holder.getmDeckName().setText(model.getName());
        holder.getmDeckDescription().setText(model.getDescription());

        String numberOfCards = model.getNumberOfCards() != null ? model.getNumberOfCards().toString() : "0";
        holder.getmNumberOfCards().setText(String.format(context.getString(R.string.decks_number_cards), numberOfCards));

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.WHITE,
                        Color.BLUE,
                });

        gradientDrawable.setCornerRadii(new float[] { 50, 50, 50, 50, 50, 50, 50, 50 });
        holder.getmColor().setImageDrawable(gradientDrawable);
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
}
