package br.com.andersonsv.blacklotus.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.firebase.Deck;
import br.com.andersonsv.blacklotus.holder.DecksViewHolder;
import br.com.andersonsv.blacklotus.util.ShaderUtils;

import static android.graphics.Color.BLACK;
import static com.google.common.primitives.Floats.min;

public class DeckAdapter extends FirestoreRecyclerAdapter<Deck, DecksViewHolder> {

    private Context context;
    private Boolean isEmpty = false;

    public DeckAdapter(FirestoreRecyclerOptions recyclerOptions) {
        super(recyclerOptions);
    }

    @Override
    protected void onBindViewHolder(DecksViewHolder holder, int position, Deck model) {
        holder.getmDeckName().setText(model.getName());
        holder.getmDeckDescription().setText(model.getDescription());

        String numberOfCards = model.getNumberOfCards() != null ? model.getNumberOfCards().toString() : "0";
        holder.getmNumberOfCards().setText(String.format(context.getString(R.string.decks_number_cards), numberOfCards));
        int [] colors = new int[] { 0xff0000ff, 0xff00ff00, 0xffff0000 };
        Float positionX = 0.5F;
        Float positionY = 0.5F;
        Float size = 1.0F;

        //holder.getmColor().setImageDrawable(ShaderUtils.radialGradientBackground(colors, positionX, positionY, size));
        ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {

                int [] colors = new int[] { Color.RED, Color.BLACK, Color.WHITE };
                float [] positions = new float[] { 0.0f, 0.5f, 1.0f};



                RadialGradient rg = new  RadialGradient(
                        width * 1.0f,
                        height * 1.0f,
                        min(width, height) * 1.0f,
                        colors,
                        null,
                        Shader.TileMode.CLAMP);


                return rg;
            }
        };

        ShapeDrawable badge = new ShapeDrawable (new OvalShape());
        badge.setIntrinsicWidth (200);
        badge.setIntrinsicHeight (200);
        badge.getPaint().setColor(Color.RED);

        badge.setShaderFactory(sf);
        holder.getmColor().setImageDrawable (badge);
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        Log.e("error", e.getMessage());
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public DecksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deck, parent, false);

        context = view.getContext();

        return new DecksViewHolder(view);
    }

    @Override
    public void onDataChanged() {
        notifyDataSetChanged();
        if (getItemCount() == 0) {
            isEmpty = true;
        } else {
            isEmpty = false;
        }
    }

    public Boolean getEmpty() {
        return isEmpty;
    }
}
