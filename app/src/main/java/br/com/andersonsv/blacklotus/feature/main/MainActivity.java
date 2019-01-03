package br.com.andersonsv.blacklotus.feature.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.base.BaseActivity;
import br.com.andersonsv.blacklotus.provider.DeckWidgetProvider;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mNavigation.setOnNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle("Decks");
        Fragment deckFragment = DeckFragment.newInstance();
        openFragment(deckFragment);
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                getSupportActionBar().setTitle("Decks");
                Fragment decksFragment = DeckFragment.newInstance();
                openFragment(decksFragment);
                return true;
            case R.id.navigation_dashboard:
                getSupportActionBar().setTitle("Cards");
                Fragment cardsFragment = CardFragment.newInstance();
                openFragment(cardsFragment);
                return true;
            case R.id.navigation_notifications:
                getSupportActionBar().setTitle("Settings");
                Fragment settingFragment = SettingFragment.newInstance();
                openFragment(settingFragment);
                return true;
        }
        return true;
    }
}
