package br.com.andersonsv.blacklotus.feature.setting;

import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import br.com.andersonsv.blacklotus.R;
import br.com.andersonsv.blacklotus.util.Constants;

public class SettingPreferencesFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.app_preferences, s);

        ListPreference secondLanguage = (ListPreference) findPreference(Constants.KEY_SECOND_LANGUAGE);

        if(secondLanguage.getValue() != null){
            secondLanguage.setTitle(secondLanguage.getValue());
        }
        secondLanguage.setOnPreferenceChangeListener(onPreferenceChangeListener);
    }

    private static final Preference.OnPreferenceChangeListener onPreferenceChangeListener
            = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if(preference instanceof EditTextPreference) {
                preference.setTitle(stringValue);
            }

            if(preference instanceof ListPreference) {
                preference.setTitle(stringValue);
            }
            return true;
        }
    };
}