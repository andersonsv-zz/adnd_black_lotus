package br.com.andersonsv.blacklotus.feature.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.andersonsv.blacklotus.R;

import static br.com.andersonsv.blacklotus.util.Constants.DECK_ID;

public class CardFragment extends Fragment {

    private String deckId;

    public static CardFragment newInstance() {
        return new CardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            deckId = bundle.getString(DECK_ID, "");
        }

        return rootView;
    }
}