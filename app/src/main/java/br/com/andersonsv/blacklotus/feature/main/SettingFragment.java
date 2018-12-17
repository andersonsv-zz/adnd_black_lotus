package br.com.andersonsv.blacklotus.feature.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.andersonsv.blacklotus.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingFragment extends Fragment {

    private FirebaseUser mUser;

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

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mUser != null) {
            mName.setText(mUser.getDisplayName());
            mEmail.setText(mUser.getEmail());
        }

        return rootView;
    }
}
