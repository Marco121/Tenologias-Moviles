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
    String[] combis = {
            "15 de Agosto S.A.",
            "24 De Diciembre S.R.L.",
            "3 de Octubre S.A.",
            "6 de Diciembre S.A.",
            "Alborada Transasil S.A.",
            "Alto la Luna S.A",
            "Amaru Sociedad Anonima",
            "Angeles Del Misti S.A.",
            "Angeles Del Sur (Etrasur S.A.)",
            "Audaces Tours S.A.",
            "Bella Vista Combi Express S.C.R.",
            "Benedicto Xvi S.A.",
            "Bus S.A.C. (Emsebus S.A.C.)",
            "Bus Service Independencia",
            "Buss Arequipa S.A.",
            "C.O.T.U.M. S.A.",
            "Campano Velarde y CIA S.A.",
            "Cerro Colorado S.A.C",
            "Cettar S.A.",
            "Characato S.A.",
            "Chavez Z E Hijos S.A.C.",
            "Christus S.A.C.",
            "Ciudad Municipal Arequipa S.A.",
            "Conan S.A.",
            "Continental S.A. (Transconsa)",
            "Corporacion Palermo Automotores S.A.",
            "COTAP S.A.",
            "COTASMIGPUA S.A.-ETCOS S.A.",
            "COTASPA S.A.",
            "Dean Valdivia S.A.",
            "Divino Ni �o Jesus San Camilo",
            "ECOPTRA S.A.",
            "El Calero S.R.Ltda",
            "El Chalan S.A.",
            "El Correcamino S.A.",
            "El Pacific S.A.",
            "El Rapido De Selva Alegre",
            "El Trome Guillen S.C.R.L.",
            "Escorpiones De Bustamante",
            "Espiritu Santo",
            "ETCOSAC",
            "ETMASA",
            "ETUPSSA S.A.",
            "Exenatru Sociedad Anonima",
            "Florian S.A.",
            "Garcilazo De La Vega",
            "Inka Terra Tour S.R.L.",
            "Inka Tours Express S.R.L.",
            "Joyino S.A.",
            "Juan XXIII S.R.LTDA.",
            "La Joya Del Sur S.A.C.",
            "La Perla S.R.LTDA.",
            "Las Begonias 1 S.A.",
            "Laser Jorge Chavez S.A.",
            "Litoral S.A. (Tralsa)",
            "Los Angeles Blancos S.R.L.",
            "Los Angeles De Ciudad De Dios",
            "Los Angeles De Villaba S.A",
            "Los Angeles Dorados De Hunter",
            "Los Ases Del Timon",
            "Los Canarios De Socabaya S.A.",
            "Los Claveles S.R.LTDA",
            "Los Delfines Azules S.A.",
            "Los Escorpiones S.A",
            "Los Pioneros",
            "Los Primeros Pioneros S.A.",
            "Los Ruise�ores S.A.",
            "Los Tres Volcanes S.A.",
            "Lutte E.I.R.Ltda",
            "Madariaga S.A.",
            "Marfil S.R.LTDA. S.A.",
            "Mariano Bustamante",
            "Miguel Grau Campi�a S.A.",
            "Miguel Grau S.A.",
            "Miraflores S.A.-ETRAMPU S.A.",
            "Monterrey A S.A.",
            "Monterrey C S.A.",
            "Monterrey Vallecito S.A.",
            "Multiples De La Dorada S.A.",
            "Multiples Milagrosa De Chapi",
            "Multiples Virgen Adorada S.A.",
            "Nuestra Se�ora Virgen de los Remedios S.A.",
            "Nuevo Horizonte S.A. (Etnhsa).",
            "Nuevo Peru S.A.-ETRANPESA",
            "Ojo del Milagro",
            "Oriol S.A.",
            "Pais S.A. (Esm Transpais S.A.)",
            "Pampas De Polanco S.A.",
            "Peruarbo Buss S.A.",
            "Primavera-Tahuaycani S.A.",
            "Primor S.R.L.",
            "R.Coaguila Coaguila",
            "Rapido Kombis Independencia",
            "Reyes S.A.C.",
            "San Antonio De Padua",
            "San Isidro La Cano S.A.",
            "San Martin 43 S.A",
            "San Martin de Socabaya S.A.",
            "San Martin Express Bus S.A.",
            "San Martin S.A.",
            "Santa Clara S.R.L.",
            "Santa Rita De Siguas S.A.C.",
            "Santillana S.R.LTDA.",
            "Santillana Tours S.R.L",
            "Santo Domingo",
            "Santo Toribio De Mogrovejo",
            "Servico Especial Miraflores S.A.",
            "Se�or de la Amargura S.A.",
            "Se�or De Lampa S.A. Translampa",
            "Se�or de Luren S.A.",
            "Se�or del Gran Poder S.A.",
            "Sideral Tours Srl",
            "Sociedad Anonima Emtranpa S.A.",
            "SR. De La Joya S.R.LTDA",
            "Tiabaya S.A.",
            "Tiburcio Choque",
            "Tours F&F Srl",
            "TRANSMAPA S.A.",
            "Travic S.A.",
            "Turismo Queque�a S.A.C.",
            "Turismo San Isidro S.R.L.",
            "Turismo Y Expreso Florida",
            "Umachiri",
            "Unidos De Hunter S.A. Etunhsa",
            "Union Libertad S.A.C. Etulsac",
            "Urbanos La Joya S.A.C.",
            "Villa America del Sur del Peru S.A.A.",
            "Virgen Copacabana 2012 S.A",
            "Virgen de la candelaria S.A.",
            "Virgen De La Gloria S.A.",
            "Virgen De Polanco S.A.",
            "Volant Bus S.A.",
            "Waldos Inversiones Scrl.",
            "Yaquesan Sac",
            "Zamacola S.A.",
    };
    public CombisFragment(){}
    //ESTE INT ES EL TAMANIO DEL ARREGLO
    int arreglo_lenght = 0;
    //AQUI GUARDA EL NOMBRE DE LAS COMBIS
    String[] Nombres_coombis = new String[200];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_combis, container, false);
        task tarea1 = new task();
        tarea1.execute();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,combis);

        AutoCompleteTextView textView = (AutoCompleteTextView)rootView.findViewById(R.id.autoCompleteTextView);

        textView.setThreshold(3);
        textView.setAdapter(adapter);
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
            //param.add(new BasicNameValuePair("message","-71.539212"));

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

                this.progressDialog.dismiss();
                // mostrar();

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data " + e.toString());
            }


        }

    }
}
