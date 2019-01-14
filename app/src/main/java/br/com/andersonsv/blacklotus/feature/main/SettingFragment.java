package br.com.andersonsv.blacklotus.feature.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.andersonsv.blacklotus.BuildConfig;
import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.feature.base.BaseFragment;
import br.com.andersonsv.blacklotus.feature.login.LoginActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.andersonsv.blacklotus.util.Constants.CARD_LIST;
import static br.com.andersonsv.blacklotus.util.Constants.DECK_PARCELABLE;

public class SettingFragment extends BaseFragment {

    @BindView(R.id.textViewName)
    TextView mName;

    @BindView(R.id.textViewEmail)
    TextView mEmail;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, rootView);

        getActivity().setTitle(R.string.navigation_settings);

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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.sign_out_title));
        builder.setMessage(getString(R.string.sign_out_confirm));
        builder.setPositiveButton(getString(R.string.default_signout), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                mFirebaseAuth.signOut();
                openActivity(LoginActivity.class);
            }
        });
        builder.setNegativeButton(getString(R.string.default_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }
}
