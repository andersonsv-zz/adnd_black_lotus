package br.com.andersonsv.blacklotus.feature.base;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    public void snack(View view, String message){

        Snackbar.make(view,
                message,
                Snackbar.LENGTH_INDEFINITE)
                .setAction("Action", null).show();
    }

    public void toast(Context context, String message){
        Toast.makeText(context,
                message,
                Toast.LENGTH_LONG)
                .show();
    }
}
