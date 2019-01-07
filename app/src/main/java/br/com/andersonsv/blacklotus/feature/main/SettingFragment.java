package br.com.andersonsv.blacklotus.feature.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.base.BaseFragment;
import br.com.andersonsv.blacklotus.feature.login.LoginActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingFragment extends BaseFragment {

    @BindView(R.id.textViewName)
    TextView mName;

    @BindView(R.id.textViewEmail)
    TextView mEmail;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, rootView);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.navigation_settings);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            mName.setText(firebaseUser.getDisplayName());
            mEmail.setText(firebaseUser.getEmail());
        }

        if (savedInstanceState == null) {
            Fragment preferenceFragment = new SettingPreferencesFragment();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.add(R.id.sharedPreferences, preferenceFragment);
            ft.commit();
        }

        return rootView;
    }

    @OnClick(R.id.buttonSignOut)
    public void signOut(View v){
       FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
       mFirebaseAuth.signOut();
       openActivity(LoginActivity.class);
    }
}
