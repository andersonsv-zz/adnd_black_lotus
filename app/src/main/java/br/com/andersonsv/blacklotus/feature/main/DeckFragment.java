package br.com.andersonsv.blacklotus.feature.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.firebase.Deck;

public class DeckFragment extends Fragment {

    public static DeckFragment newInstance() {
        return new DeckFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_deck, container, false);

        Query query = FirebaseFirestore.getInstance()
                .collection("develop")
                .document("6Dfh0Shkwz0ds2Ch6EHa")
                .collection("decks")
                .whereEqualTo("userId", "jyZhuGIdUUNpI9dXnbYYMID65P13")
                .limit(50);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null) {
                    return;
                }
                List<Deck> decks = queryDocumentSnapshots.toObjects(Deck.class);
            }
        });


        return rootView;
    }


}
