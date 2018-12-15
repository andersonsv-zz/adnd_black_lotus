package br.com.andersonsv.blacklotus.feature.main;

import android.graphics.Color;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import br.com.andersonsv.blacklotus.BuildConfig;
import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.firebase.Deck;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DeckFragment extends Fragment {

    private FirebaseUser mUser;
    private FirebaseFirestore mDb;
    private String mUserUid;
    private FirestoreRecyclerAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;

    @BindView(R.id.recyclerViewDeck)
    RecyclerView mDeckRecycler;

    public static DeckFragment newInstance() {
        return new DeckFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_deck, container, false);

        ButterKnife.bind(this, rootView);

        mDb = FirebaseFirestore.getInstance();

        getDeckList();


        return rootView;
    }

    private void getDeckList() {

        linearLayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mDeckRecycler.getContext(),
                linearLayoutManager.getOrientation());
        mDeckRecycler.addItemDecoration(dividerItemDecoration);
        mDeckRecycler.setLayoutManager(linearLayoutManager);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser != null){
            mUserUid = mUser.getUid();
        } else {
            //TODO - usuario nao identificado, escrever mensagem
        }

        Query query = mDb.collection(BuildConfig.FIREBASE_COLLECTION)
                .document(BuildConfig.FIREBASE_DOCUMENT)
                .collection("decks")
                .whereEqualTo("userId", mUserUid);

        FirestoreRecyclerOptions<Deck> response = new FirestoreRecyclerOptions.Builder<Deck>()
                .setQuery(query, Deck.class)
                .build();

        mAdapter = new FirestoreRecyclerAdapter<Deck, DecksHolder>(response) {
            @Override
            public void onBindViewHolder(DecksHolder holder, int position, Deck model) {

                holder.mDeckName.setText(model.getName());
                holder.mDeckDescription.setText(model.getDescription());

                String numberOfCards = model.getNumberOfCards() != null ? model.getNumberOfCards().toString() : "0";
                holder.mNumberOfCards.setText(String.format(getString(R.string.decks_number_cards), numberOfCards));

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

                holder.mColor.setImageDrawable (badge);


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
            public DecksHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_deck, group, false);

                return new DecksHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        mAdapter.notifyDataSetChanged();
        mDeckRecycler.setAdapter(mAdapter);
    }

    public class DecksHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewDeckName)
        TextView mDeckName;
        @BindView(R.id.textViewDeckDescription)
        TextView mDeckDescription;
        @BindView(R.id.textViewNumberOfCards)
        TextView mNumberOfCards;
        @BindView(R.id.imageViewColor)
        ImageView mColor;

        public DecksHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
