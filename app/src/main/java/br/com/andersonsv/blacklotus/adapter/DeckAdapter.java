package br.com.andersonsv.blacklotus.adapter;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.primitives.Ints;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.firebase.DeckModel;
import br.com.andersonsv.blacklotus.util.ColorDeckUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DeckAdapter extends FirestoreAdapter<DeckAdapter.ViewHolder> {

    public interface OnDeckSelectedListener {
        void onDeckSelected(DocumentSnapshot deck);
        void onDeckEdited(DocumentSnapshot deck);
    }

    private OnDeckSelectedListener mListener;

    public DeckAdapter(Query query, OnDeckSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_deck, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewDeckName)
        TextView mDeckName;

        @BindView(R.id.textViewDeckDescription)
        TextView mDeckDescription;

        @BindView(R.id.imageViewColor)
        ImageView mColor;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnDeckSelectedListener listener) {

            DeckModel model = snapshot.toObject(DeckModel.class);
            model.setId(snapshot.getId());

            Resources resources = itemView.getResources();

            mDeckName.setText(model.getName());
            mDeckDescription.setText(model.getDescription());

            List<Integer> colors = new ArrayList<>();

            if (model.getColor1() != null && model.getColor2() == null) {
                int colorValue = Color.parseColor(model.getColor1());
                ColorDeckUtil.setOneColor(colorValue, mColor);
            } else {
                ColorDeckUtil.addColorIfNonNull(model.getColor1(), colors);
                ColorDeckUtil.addColorIfNonNull(model.getColor2(), colors);
                ColorDeckUtil.addColorIfNonNull(model.getColor3(), colors);
                ColorDeckUtil.addColorIfNonNull(model.getColor4(), colors);
                ColorDeckUtil.addColorIfNonNull(model.getColor5(), colors);

                int[] colorArray = Ints.toArray(colors);

                if (colorArray.length > 1) {
                    GradientDrawable gradientDrawable = new GradientDrawable(
                            GradientDrawable.Orientation.TOP_BOTTOM,
                            colorArray);
                    gradientDrawable.setShape(GradientDrawable.OVAL);
                    mColor.setImageDrawable(gradientDrawable);
                } else {
                    ColorDeckUtil.setOneColor(Color.GRAY, mColor);
                }

                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        colorArray);

                gradientDrawable.setShape(GradientDrawable.OVAL);
                mColor.setImageDrawable(gradientDrawable);
            }

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onDeckSelected(snapshot);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        listener.onDeckEdited(snapshot);
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}
