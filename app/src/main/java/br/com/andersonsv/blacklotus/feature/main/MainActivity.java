package br.com.andersonsv.blacklotus.feature.main;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.base.BaseActivity;
import br.com.andersonsv.blacklotus.feature.deck.DeckFragment;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @VisibleForTesting
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Fragment mainFragment = DeckFragment.newInstance();
        openFragment(mainFragment);
    }
}
