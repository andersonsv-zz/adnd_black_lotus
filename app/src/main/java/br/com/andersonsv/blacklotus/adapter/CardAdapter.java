package br.com.andersonsv.blacklotus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.firebase.CardModel;
import br.com.andersonsv.blacklotus.holder.CardsViewHolder;

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

        Picasso.with(mInflater.getContext())
                .load(model.getImage())
                .into(holder.getmCardImage());

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
