package br.com.andersonsv.blacklotus.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
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
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.firebase.CardModel;
import br.com.andersonsv.blacklotus.model.CardColor;
import br.com.andersonsv.blacklotus.model.Rarity;
import br.com.andersonsv.blacklotus.widget.TextDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CardAdapter extends FirestoreRecyclerAdapter<CardModel, CardAdapter.ViewHolder> implements Html.ImageGetter {
    private ProgressBar mProgressBar;
    private LinearLayout mEmptyState;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<CardModel> mData;

    private final CardAdapter.CardRecyclerOnClickHandler mClickHandler;

    public CardAdapter(FirestoreRecyclerOptions recyclerOptions, @NonNull ProgressBar progressBar, LinearLayout emptyState, CardRecyclerOnClickHandler clickHandler) {
        super(recyclerOptions);
        this.mProgressBar = progressBar;
        this.mEmptyState = emptyState;
        mClickHandler = clickHandler;
    }

    public interface CardRecyclerOnClickHandler {
        void onClick(CardModel cardModel);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        this.mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.item_card, parent, false);

        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, final CardModel model) {
        mProgressBar.setVisibility(View.GONE);

        final String docId = getSnapshots().getSnapshot(position).getId();
        model.setId(docId);

        holder.mCardName.setText(model.getName());
        holder.mQuantity.setText(model.getQuantity().toString());
        holder.mType.setText(model.getType());

        Rarity rarity = Rarity.getByType(model.getRarity());

        if(rarity != null){
            holder.mRarity.setText(rarity.getTypeId());

            int color = mInflater.getContext().getResources().getColor(rarity.getColor());
            holder.mRarity.setTextColor(color);
        }

        Picasso.with(mInflater.getContext())
                .load(model.getImage())
                .into(holder.mCardImage);

        holder.mCardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View layoutDialog = View.inflate(mContext, R.layout.dialog_card_image, null);
                ImageView imgRefInflated = layoutDialog.findViewById(R.id.imageViewDialog);
                Picasso.with(mInflater.getContext()).load(model.getImage()).into(imgRefInflated);

                final Dialog dialog = new Dialog(mContext,android.R.style.Theme_Light_NoTitleBar_Fullscreen); //default fullscreen titlebar

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
            Spanned spanned = Html.fromHtml(text, this, null);
            holder.mCost.setText(spanned);
        }
        //holder.Cost.setLayoutManager(linearLayoutManager);

        //CostAdapter costAdapter = new CostAdapter(mContext, costs);
       // holder.mRecyclerCost.setAdapter(costAdapter);
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

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            CardModel movie = mData.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }

    private String replaceTypetImgSrc(String textToReplace){
        return textToReplace.replaceAll("\\{([^}]*)\\}", "<img src='$1'>");
    }

    @Override
    public Drawable getDrawable(String name) {

        CardColor cardColor = CardColor.getById(name);
        int size = mContext.getResources().getInteger(R.integer.card_cost_list);

        if (cardColor != null) {
            LevelListDrawable d = new LevelListDrawable();

            Drawable empty = mContext.getResources().getDrawable(cardColor.getImage());
            d.addLevel(0, 0, empty);
            d.setBounds(0, 0, size, size);

            return d;

        } else {
            //Copied by - https://github.com/devunwired/textdrawable
            TextDrawable textDrawable = new TextDrawable(mContext);

            textDrawable.setText(name);
            textDrawable.setTextColor(Color.BLACK);

            GradientDrawable gD = new GradientDrawable();
            gD.setColor(Color.GRAY);
            gD.setShape(GradientDrawable.OVAL);
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{gD, textDrawable});

            layerDrawable.setBounds(0, 0, size, size);

            return layerDrawable;
        }
    }
}
