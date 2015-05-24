package com.example.gonzaloenrique.combi_aqpnuevo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InicioFragment extends Fragment {

    public InicioFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_inicio, container, false);

        return rootView;
    }



}

