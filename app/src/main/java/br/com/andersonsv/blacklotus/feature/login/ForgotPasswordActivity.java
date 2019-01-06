package br.com.andersonsv.blacklotus.feature.login;

import android.os.Bundle;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.base.BaseActivity;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }

}
