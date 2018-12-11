package br.com.andersonsv.blacklotus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.andersonsv.blacklotus.feature.user.UserActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.buttonUser)
    public void buttonUser(View view) {
        Class destinationActivity = UserActivity.class;
        openActivity(destinationActivity);
    }

    private void openActivity(Class destination){
        Intent intent = new Intent(this, destination);
        startActivity(intent);
    }

}
