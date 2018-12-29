package br.com.andersonsv.blacklotus.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
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

    private Context context;
    private ProgressBar mProgressBar;
    private LinearLayout mEmptyState;
    private List<DeckModel> mData;
    private final DeckAdapter.DeckRecyclerOnClickHandler mClickHandler;

    public interface DeckRecyclerOnClickHandler {
        void onClick(DeckModel deck);
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
        holder.mNumberOfCards.setText(String.format(context.getString(R.string.decks_number_cards), numberOfCards));

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

        gradientDrawable.setCornerRadii(new float[] { 50, 50, 50, 50, 50, 50, 50, 50 });
        holder.mColor.setImageDrawable(gradientDrawable);

        /*holder.setOnClickListener(new DecksViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Fragment cardFragment = CardFragment.newInstance();

                Bundle bundle = new Bundle();
                bundle.putString(DECK_ID, docId);
                bundle.putParcelable(DECK_PARCELABLE, model);
                cardFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, cardFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });*/
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        Log.e("error", e.getMessage());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deck, parent, false);

        context = view.getContext();

        return new ViewHolder(view);
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
