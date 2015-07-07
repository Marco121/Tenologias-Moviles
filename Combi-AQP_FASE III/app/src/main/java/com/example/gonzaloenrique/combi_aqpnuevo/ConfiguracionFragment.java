package com.example.gonzaloenrique.combi_aqpnuevo;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class ConfiguracionFragment extends PreferenceFragment {

    public ConfiguracionFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefe);
    }


}
