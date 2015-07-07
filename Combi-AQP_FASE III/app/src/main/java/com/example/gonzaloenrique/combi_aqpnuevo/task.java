package com.example.gonzaloenrique.combi_aqpnuevo;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by ramiro on 17/05/2015.
 */
class task extends AsyncTask<String, String, Void>

{
   // private ProgressDialog progressDialog = new ProgressDialog(this);
    InputStream is = null ;
    String result = "";
   /* protected void onPreExecute() {
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();
        progressDialog.setOnCancelListener(new SearchManager.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface arg0) {
                task.this.cancel(true);
            }
        });
    }*/
    @Override
    protected Void doInBackground(String... params) {
        String url_select = "http://www.combiaqp.16mb.com/NombresCombis.php";

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url_select);

        ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("email", "-16.407070"));
        param.add(new BasicNameValuePair("message","-71.539212"));
        //param.add(new BasicNameValuePair("message","-71.539212"));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(param));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();

            //read content
            is =  httpEntity.getContent();

        } catch (Exception e) {

            Log.e("log_tag", "Error in http connection " + e.toString());
            //Toast.makeText(MainActivity.this, "Please Try Again", Toast.LENGTH_LONG).show();
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while((line=br.readLine())!=null)
            {
                sb.append(line+"\n");
            }
            is.close();
            result=sb.toString();

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "Error converting result "+e.toString());
        }

        return null;

    }
    protected void onPostExecute(Void v) {

        // ambil data dari Json database
        try {
            String[] NombresCombis= new String[200];
            JSONArray Jarray = new JSONArray(result);
            for(int i=0;i<Jarray.length();i++)
            {
                JSONObject Jasonobject = null;
                //text_1 = (TextView)findViewById(R.id.txt1);
                Jasonobject = Jarray.getJSONObject(i);

                //get an output on the screen
                //String id = Jasonobject.getString("id");
                String IN_CodCombi = Jasonobject.getString("IN_CodCombi");


                //String DA_Latitud="";
                NombresCombis[i]=Jasonobject.getString("DA_NomCombi");
                String et= "124";
                if(et.equalsIgnoreCase(IN_CodCombi)) {

                    //text.setText(NombresCombis[i]);
                    break;
                }
            }
            //this.progressDialog.dismiss();

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "Error parsing data "+e.toString());
        }
    }
}

