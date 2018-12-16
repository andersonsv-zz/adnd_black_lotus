package br.com.andersonsv.blacklotus.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.andersonsv.blacklotus.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DecksViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textViewDeckName)
    TextView mDeckName;

    @BindView(R.id.textViewDeckDescription)
    TextView mDeckDescription;

    @BindView(R.id.textViewNumberOfCards)
    TextView mNumberOfCards;

    @BindView(R.id.imageViewColor)
    ImageView mColor;

    public DecksViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public TextView getmDeckName() {
        return mDeckName;
    }

    public TextView getmDeckDescription() {
        return mDeckDescription;
    }

    public TextView getmNumberOfCards() {
        return mNumberOfCards;
    }

    public ImageView getmColor() {
        return mColor;
    }
}