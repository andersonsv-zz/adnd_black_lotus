package br.com.andersonsv.blacklotus.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
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

        ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {

                int [] colors = new int[] { 0xff0000ff, 0xff00ff00, 0xffff0000 };
                float [] positions = new float[] { 0.4f, 0.7f, 0.9f };
                RadialGradient rg = new RadialGradient(0, 0, height, colors, positions, Shader.TileMode.CLAMP);

                return rg;
                       /* LinearGradient lg = new LinearGradient(0, 0, width, height,
                                new int[]{Color.GREEN, Color.WHITE, Color.BLACK, Color.RED},
                                new float[]{0,0.5f,.55f,1}, Shader.TileMode.REPEAT);
                        return lg;*/

                        /*int [] colors = new int[] { 0xff0000ff, 0xff00ff00, 0xffff0000 };
                        float [] positions = new float[] { 0.4f, 0.7f, 0.9f };

                        RadialGradient gradient = new RadialGradient(50, 50, 50, colors, positions, TileMode.CLAMP);
                        mPaint = new Paint();
                        mPaint.setDither(true);
                        mPaint.setShader(gradient);*/
            }
        };

        ShapeDrawable badge = new ShapeDrawable (new OvalShape());
        badge.setIntrinsicWidth (200);
        badge.setIntrinsicHeight (200);
        badge.getPaint().setColor(Color.RED);

        badge.setShaderFactory(sf);
        holder.getmColor().setImageDrawable (badge);


                /*addView (image);

                ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
                    @Override
                    public Shader resize(int width, int height) {
                        LinearGradient lg = new LinearGradient(0, 0, width, height,
                                new int[]{Color.GREEN, Color.WHITE, Color.BLACK, Color.RED},
                                new float[]{0,0.5f,.55f,1}, Shader.TileMode.REPEAT);
                        return lg;
                    }
                };

                PaintDrawable p= new PaintDrawable();
                p.setShape(new RectShape());
                p.setShaderFactory(sf);

                holder.mColor.setImageDrawable(p);*/

                /*progressBar.setVisibility(View.GONE);
                holder.textName.setText(model.getName());
                holder.textTitle.setText(model.getTitle());
                holder.textCompany.setText(model.getCompany());
                Glide.with(getApplicationContext())
                        .load(model.getImage())
                        .into(holder.imageView);

                holder.itemView.setOnClickListener(v -> {
                    Snackbar.make(friendList, model.getName()+", "+model.getTitle()+" at "+model.getCompany(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                });*/
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        Log.e("error", e.getMessage());
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
