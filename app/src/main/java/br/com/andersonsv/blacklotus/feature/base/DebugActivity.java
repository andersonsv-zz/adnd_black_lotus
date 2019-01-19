package br.com.andersonsv.blacklotus.feature.base;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import br.com.andersonsv.blacklotus.R;
import butterknife.ButterKnife;

public class DebugActivity extends AppCompatActivity {

    @VisibleForTesting
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }
}
