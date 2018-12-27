package br.com.andersonsv.blacklotus.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.data.Card;
import br.com.andersonsv.blacklotus.holder.CardsSearchViewHolder;

public class CardSearchAdapter extends RecyclerView.Adapter<CardsSearchViewHolder>{
    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<Card> mData;

    public CardSearchAdapter(Context context, List<Card> data) {
        mContext = context;
        mData = data;
        this.mInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public CardsSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_search_card, parent, false);

        return new CardsSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardsSearchViewHolder holder, int i) {

        Card card = mData.get(i);

        holder.getmCardName().setText(card.getName());

        if (card.getImage() != null) {
            Picasso.with(mInflater.getContext())
                    .load(card.getImage())
                    .into(holder.getmCardImage());
        }
        holder.getmRarity().setText(card.getRarity().getTypeId());
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
}
