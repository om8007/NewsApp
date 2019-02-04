package com.example.lenovo.newsapp;


import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v7.preference.*;
import android.support.v4.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    Preference minNews;
    Preference orderBy;
    Preference section;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_screen,rootKey);


        minNews = findPreference(getString(R.string.settings_min_news_key));
        bindPreferenceSummaryToValue(minNews);

        orderBy = findPreference(getString(R.string.settings_order_by_key));
        bindPreferenceSummaryToValue(orderBy);

        section = findPreference(getString(R.string.settings_section_news_key));
        bindPreferenceSummaryToValue(section);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
        String stringValue = newValue.toString();
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                CharSequence[] labels = listPreference.getEntries();
                CharSequence[] values = listPreference.getEntryValues();
                preference.setSummary(labels[prefIndex]);
                 // editing with new value
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString(preference.getKey(),(values[prefIndex]).toString());
                edit.apply();
            }
        } else {
            preference.setSummary(stringValue);
            // editing with new value
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString(preference.getKey(),stringValue);
            edit.apply();
       }
            return true;
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
        String preferenceString = preferences.getString(preference.getKey(),"");

        onPreferenceChange(preference, preferenceString);
    }

}


