package com.example.gonzaloenrique.combi_aqpnuevo;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CombisFragment extends Fragment {

    public CombisFragment(){}
    //ESTE INT ES EL TAMANIO DEL ARREGLO
    int arreglo_lenght = 0;
    //AQUI GUARDA EL NOMBRE DE LAS COMBIS
    String[] Nombres_coombis = new String[200];
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         rootView = inflater.inflate(R.layout.fragment_combis, container, false);
        task tarea1 = new task();
        tarea1.execute();


        return rootView;
    }

  /*  public void mostrar()
    {
        //AQUI LAS MUESTRAAAAA
        for(int i =0;i<arreglo_lenght;i++)
          //  Toast.makeText(getActivity(), Nombres_coombis[i], Toast.LENGTH_SHORT).show();
    }*/
    class task extends AsyncTask<String, String, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(getActivity());
        InputStream is = null;
        String result = "";
        String latitud = "-16.405698";
        String longitud = "-71.548877";
        int opcion = 0;
        protected void onPreExecute() {
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
            /*progressDialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    task.this.cancel(true);
                }
            });*/
        }

        @Override
        protected Void doInBackground(String... params) {
            String url_select = "http://www.combiaqp.16mb.com/combidos.php";

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url_select);

            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("email", latitud));
            param.add(new BasicNameValuePair("message", longitud));


            try {
                httpPost.setEntity(new UrlEncodedFormEntity(param));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                //read content
                is = httpEntity.getContent();

            } catch (Exception e) {

                Log.e("log_tag", "Error in http connection " + e.toString());
                //Toast.makeText(MainActivity.this, "Please Try Again", Toast.LENGTH_LONG).show();
            }
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error converting result " + e.toString());
            }

            return null;

        }

        protected void onPostExecute(Void v) {

            // ambil data dari Json database
            try {
                String[] NombresCombis = new String[200];
                JSONArray Jarray = new JSONArray(result);
                for(int i=0;i<200;i++)
                    NombresCombis[i]="";
                arreglo_lenght=Jarray.length();
                for (int i = 0; i < Jarray.length(); i++) {
                    JSONObject Jasonobject = null;
                    //text_1 = (TextView)findViewById(R.id.txt1);
                    Jasonobject = Jarray.getJSONObject(i);

                    //get an output on the screen
                    //String id = Jasonobject.getString("id");

                    //String IN_CodCombi = Jasonobject.getString("IN_CodCombi");


                    //String DA_Latitud="";
                    NombresCombis[i] = Jasonobject.getString("DA_NomCombi");

                   /* if (et.getText().toString().equalsIgnoreCase(IN_CodCombi)) {

                        text.setText(NombresCombis[i]);
                        break;
                    }*/
                }
                Nombres_coombis= NombresCombis;

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,Nombres_coombis);

                AutoCompleteTextView textView = (AutoCompleteTextView)rootView.findViewById(R.id.autoCompleteTextView);

                textView.setThreshold(3);
                textView.setAdapter(adapter);
                this.progressDialog.dismiss();
               // mostrar();

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data " + e.toString());
            }


        }

    }
}
