package com.example.gonzaloenrique.combi_aqpnuevo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class ContactanosFragment extends Fragment {

    public ContactanosFragment(){}
    private WebView mWebView;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       //etUserVisibleHint(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_contactanos, container, false);




        return rootView;
    }
    private MainActivity mainActivity;



}
