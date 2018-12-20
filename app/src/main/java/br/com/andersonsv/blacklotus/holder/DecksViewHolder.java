package br.com.andersonsv.blacklotus.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.andersonsv.blacklotus.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DecksViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.textViewDeckName)
    TextView mDeckName;

    @BindView(R.id.textViewDeckDescription)
    TextView mDeckDescription;

    @BindView(R.id.textViewNumberOfCards)
    TextView mNumberOfCards;

    @BindView(R.id.imageViewColor)
    ImageView mColor;

    private DecksViewHolder.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnClickListener(DecksViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public DecksViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });
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