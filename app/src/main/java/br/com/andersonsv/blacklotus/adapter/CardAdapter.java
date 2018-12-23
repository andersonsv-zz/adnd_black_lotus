package br.com.andersonsv.blacklotus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        holder.setOnClickListener(new CardsViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(view.getContext(), "Card id" + docId, Toast.LENGTH_LONG).show();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        List<String> costs = new ArrayList<>();
        //List<String> items = Arrays.asList(model.getCost().split(","));

        holder.getmRecyclerCost().setLayoutManager(linearLayoutManager);

        costs.add("{2}");
        costs.add("{B}");

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
