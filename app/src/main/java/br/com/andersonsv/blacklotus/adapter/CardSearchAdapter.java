package br.com.andersonsv.blacklotus.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.data.Card;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CardSearchAdapter extends RecyclerView.Adapter<CardSearchAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<Card> mData;
    private String mDeckId;
    private final CardSearchRecyclerOnClickHandler mClickHandler;

    public CardSearchAdapter(List<Card> data, String deckId, CardSearchRecyclerOnClickHandler clickHandler) {
        mData = data;
        mDeckId = deckId;
        mClickHandler = clickHandler;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        this.mInflater = LayoutInflater.from(parent.getContext());
        View view = mInflater.inflate(R.layout.item_search_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {

        final Card card = mData.get(i);

        holder.mCardName.setText(card.getName());

        if (card.getImage() != null) {
            Picasso.with(mInflater.getContext())
                    .load(card.getImage())
                    .into(holder.mCardImage);
        }
        holder.mRarity.setText(card.getRarity().getTypeId());
        holder.mRarity.setTextColor(card.getRarity().getColor());

        /*holder.setOnClickListener(new CardsSearchViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Fragment cardEditorFragment = CardEditorFragment.newInstance();

                Bundle bundle = new Bundle();
                bundle.putParcelable(CARD_PARCELABLE, card);
                bundle.putString(DECK_ID, mDeckId);
                cardEditorFragment.setArguments(bundle);
                mFragmentTransaction.replace(R.id.container, cardEditorFragment);
                mFragmentTransaction.addToBackStack(null);
                mFragmentTransaction.commit();
            }
        });*/
    }

    public void setCards(List<Card> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    public interface CardSearchRecyclerOnClickHandler {
        void onClick(Card card);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textViewCardName)
        TextView mCardName;

        @BindView(R.id.imageViewCard)
        ImageView mCardImage;

        @BindView(R.id.textViewRarity)
        TextView mRarity;

        @BindView(R.id.textViewType)
        TextView mType;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Card movie = mData.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }
}
