package br.com.andersonsv.blacklotus.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.data.Card;
import br.com.andersonsv.blacklotus.firebase.CardModel;
import br.com.andersonsv.blacklotus.holder.CardsViewHolder;
import br.com.andersonsv.blacklotus.model.Rarity;

public class CardAdapter extends FirestoreRecyclerAdapter<CardModel, CardsViewHolder> {
    private Context mContext;
    private ProgressBar mProgressBar;
    private LinearLayout mEmptyState;
    private FragmentTransaction fragmentTransaction;
    private final LayoutInflater mInflater;

    public CardAdapter(Context context, FirestoreRecyclerOptions recyclerOptions, @NonNull ProgressBar progressBar, LinearLayout emptyState, FragmentTransaction fragmentTransaction) {
        super(recyclerOptions);
        this.mContext = context;
        this.mProgressBar = progressBar;
        this.mEmptyState = emptyState;
        this.fragmentTransaction = fragmentTransaction;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    protected void onBindViewHolder(CardsViewHolder holder, int position, final CardModel model) {
        mProgressBar.setVisibility(View.GONE);

        final String docId = getSnapshots().getSnapshot(position).getId();

        holder.getmCardName().setText(model.getName());
        holder.getmQuantity().setText(model.getQuantity().toString());
        holder.getmType().setText(model.getType());

        if (model.getCost() != null) {

        }
        Rarity rarity = Rarity.getByType(model.getRarity());

        if(rarity != null){
            holder.getmRarity().setText(rarity.getTypeId());

            int color = mInflater.getContext().getResources().getColor(rarity.getColor());
            holder.getmRarity().setTextColor(color);
        }

        Picasso.with(mInflater.getContext())
                .load(model.getImage())
                .into(holder.getmCardImage());

        holder.getmCardImage().setOnClickListener(new View.OnClickListener() {
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


        holder.setOnClickListener(new CardsViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(view.getContext(), "Card id" + docId, Toast.LENGTH_LONG).show();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        List<String> costs = new ArrayList<>();

        if (model.getCost() != null) {

            Pattern p = Pattern.compile("\\{([^}]*)\\}");
            Matcher m = p.matcher(model.getCost());

            while (m.find()) {
                costs.add(m.group(1));
            }
        }

        holder.getmRecyclerCost().setLayoutManager(linearLayoutManager);

        CostAdapter costAdapter = new CostAdapter(mContext, costs);
        holder.getmRecyclerCost().setAdapter(costAdapter);
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        Log.e("error", e.getMessage());
    }

    @Override
    public CardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_card, parent, false);

        return new CardsViewHolder(view);
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

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());

    }
}
