package br.com.andersonsv.blacklotus.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.common.primitives.Ints;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.firebase.DeckModel;
import br.com.andersonsv.blacklotus.util.ColorDeckUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DeckAdapter extends FirestoreRecyclerAdapter<DeckModel, DeckAdapter.ViewHolder> {

    private Context mContext;
    private ProgressBar mProgressBar;
    private LinearLayout mEmptyState;
    private List<DeckModel> mData;

    private LayoutInflater mInflater;
    private final DeckAdapter.DeckRecyclerOnClickHandler mClickHandler;

    public interface DeckRecyclerOnClickHandler {
        void onClick(DeckModel deck);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        this.mInflater = LayoutInflater.from(mContext);

        View view = mInflater
                .inflate(R.layout.item_deck, parent, false);

        mContext = view.getContext();

        return new ViewHolder(view);
    }

    public DeckAdapter(FirestoreRecyclerOptions recyclerOptions, @NonNull ProgressBar progressBar, LinearLayout emptyState, DeckRecyclerOnClickHandler clickHandler) {
        super(recyclerOptions);
        this.mProgressBar = progressBar;
        this.mEmptyState = emptyState;
        this.mClickHandler = clickHandler;
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, final DeckModel model) {
        mProgressBar.setVisibility(View.GONE);

        final String docId = getSnapshots().getSnapshot(position).getId();
        model.setId(docId);

        holder.mDeckName.setText(model.getName());
        holder.mDeckDescription.setText(model.getDescription());

        String numberOfCards = model.getNumberOfCards() != null ? model.getNumberOfCards().toString() : "0";
        holder.mNumberOfCards.setText(String.format(mContext.getString(R.string.decks_number_cards), numberOfCards));

        List<Integer> colors = new ArrayList<>();

        if (model.getColor1() != null && model.getColor2() == null) {

            int colorValue = Color.parseColor(model.getColor1());
            ColorDeckUtil.setOneColor(colorValue, holder.mColor);
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
            holder.mColor.setImageDrawable(gradientDrawable);
        } else {
            ColorDeckUtil.setOneColor(Color.GRAY, holder.mColor);
        }

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                colorArray);

        gradientDrawable.setShape(GradientDrawable.OVAL);
        holder.mColor.setImageDrawable(gradientDrawable);
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        Log.e("error", e.getMessage());
    }

    @Override
    public void onDataChanged() {
        mProgressBar.setVisibility(View.VISIBLE);
        notifyDataSetChanged();
        mData = getSnapshots();
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textViewDeckName)
        TextView mDeckName;

        @BindView(R.id.textViewDeckDescription)
        TextView mDeckDescription;

        @BindView(R.id.textViewNumberOfCards)
        TextView mNumberOfCards;

        @BindView(R.id.imageViewColor)
        ImageView mColor;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            DeckModel deck = mData.get(adapterPosition);
            mClickHandler.onClick(deck);
        }
    }
}
