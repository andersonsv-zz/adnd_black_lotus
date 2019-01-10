package br.com.andersonsv.blacklotus.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.firebase.CardModel;
import br.com.andersonsv.blacklotus.model.Rarity;
import butterknife.BindView;
import butterknife.ButterKnife;

import static br.com.andersonsv.blacklotus.util.StringUtils.replaceTypetImgSrc;

public class CardNewAdapter extends FirestoreAdapter<CardNewAdapter.ViewHolder> implements Html.ImageGetter {

    private Context mContext;

    @Override
    public Drawable getDrawable(String source) {
        return null;
    }

    public interface OnCardSelectedListener {
        void onSelected(DocumentSnapshot card);
    }

    private OnCardSelectedListener mListener;

    public CardNewAdapter(Query query, CardNewAdapter.OnCardSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public CardNewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CardNewAdapter.ViewHolder(inflater.inflate(R.layout.item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(CardNewAdapter.ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewCardName)
        TextView mCardName;

        @BindView(R.id.imageViewCardImage)
        ImageView mCardImage;

        @BindView(R.id.textViewQuantity)
        TextView mQuantity;

        @BindView(R.id.textViewRarity)
        TextView mRarity;

        @BindView(R.id.textViewType)
        TextView mType;

        @BindView(R.id.textViewCost)
        TextView mCost;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final CardNewAdapter.OnCardSelectedListener listener) {

            final CardModel model = snapshot.toObject(CardModel.class);
            model.setId(snapshot.getId());

            Resources resources = itemView.getResources();

            mCardName.setText(model.getName());
            mQuantity.setText(model.getQuantity().toString());
            mType.setText(model.getType());

            Rarity rarity = Rarity.getByType(model.getRarity());

            if(rarity != null){
                mRarity.setText(rarity.getTypeId());

                int color = resources.getColor(rarity.getColor());
                mRarity.setTextColor(color);
            }

            if (model.getImage() != null) {
                Picasso.with(itemView.getContext())
                        .load(model.getImage())
                        .into(mCardImage);
            }

            mCardImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    View layoutDialog = View.inflate(itemView.getContext(), R.layout.dialog_card_image, null);
                    ImageView imgRefInflated = layoutDialog.findViewById(R.id.imageViewDialog);

                    if (model.getImage() != null){
                        Picasso.with(itemView.getContext()).load(model.getImage()).into(imgRefInflated);
                    } else {
                        imgRefInflated.setImageDrawable(itemView.getContext().getDrawable(R.drawable.ic_image_not_found));
                    }

                    final Dialog dialog = new Dialog(itemView.getContext(),android.R.style.Theme_Light_NoTitleBar_Fullscreen); //default fullscreen titlebar

                    dialog.setContentView(layoutDialog);
                    dialog.show();

                    imgRefInflated.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View paramView) {
                            dialog.cancel();
                        }
                    });
                }
            });

            if( model.getCost() != null) {
                String text = replaceTypetImgSrc(model.getCost());
                Spanned spanned = Html.fromHtml(text);
                mCost.setText(spanned);
            }

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onSelected(snapshot);
                    }
                }
            });
        }
    }
}
