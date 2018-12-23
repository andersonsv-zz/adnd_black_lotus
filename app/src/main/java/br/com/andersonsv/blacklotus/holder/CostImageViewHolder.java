package br.com.andersonsv.blacklotus.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import br.com.andersonsv.blacklotus.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CostImageViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.imageViewCostImage)
    ImageView mCostImage;

    public CostImageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public ImageView getmCostImage() {
        return mCostImage;
    }
}
