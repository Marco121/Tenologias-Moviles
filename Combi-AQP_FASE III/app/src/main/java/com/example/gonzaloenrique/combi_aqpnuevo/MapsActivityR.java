// RAMIRO ACA METE CODIGO EL QUE VALE ES MAPSACTIVITYR , LOS QUE NO VALEN SON: COMOLLEGARFRAGMENT Y MAPSACTIVITY


package com.example.gonzaloenrique.combi_aqpnuevo;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.Double;
import java.lang.String;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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

import javax.net.ssl.HttpsURLConnection;

public class MapsActivityR extends FragmentActivity implements
        ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int GPS_ERRORDIALOG_REQUEST = 9001;
    @SuppressWarnings("unused")
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9002;
    GoogleMap mMap;

    @SuppressWarnings("unused")
    private static final double SEATTLE_LAT = 47.60621,
            SEATTLE_LNG = -122.33207,
            SYDNEY_LAT = -33.867487,
            SYDNEY_LNG = 151.20699,
            NEWYORK_LAT = 40.714353,
            NEWYORK_LNG = -74.005973;

    String[] rutas_cercanas_posicion = new String[200];
    String[] rutas_cercanas_destino = new String[200];
    String[] codigo_rutas_cercanas = new String[200];
    String[] latitudes_ruta = new String[800];
    String[] longitud_ruta = new String[800];

    String Nombre_combi = "";

    private static final float DEFAULTZOOM = 15;
    @SuppressWarnings("unused")
    private static final String LOGTAG = "Maps";

    GoogleApiClient mLocationClient;
    Marker marker1;
    Marker marker2;
    Marker marker3;
    Polyline line;
    int flat =0;

    //entoro tiene longitud del arreglo
    int arreglo_lenght =0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (servicesOK()) {
            setContentView(R.layout.activity_maps_prueba);

            if (initMap()) {
                //				mMap.setMyLocationEnabled(true);
                mLocationClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
                mLocationClient.connect();
            } else {
                Toast.makeText(this, "Map not available!", Toast.LENGTH_SHORT).show();
            }
        } else {
            setContentView(R.layout.activity_maps_prueba);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean servicesOK() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, GPS_ERRORDIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to Google Play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean initMap() {
        if (mMap == null) {
            SupportMapFragment mapFrag =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFrag.getMap();

            if (mMap != null) {
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.info_window, null);
                        TextView tvLocality = (TextView) v.findViewById(R.id.tv_locality);
                        TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
                        TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
                        TextView tvSnippet = (TextView) v.findViewById(R.id.tv_snippet);

                        LatLng ll = marker.getPosition();

                        tvLocality.setText(marker.getTitle());
                        tvLat.setText("Latitude: " + ll.latitude);
                        tvLng.setText("Longitude: " + ll.longitude);
                        tvSnippet.setText(marker.getSnippet());

                        return v;
                    }
                });

                mMap.setOnMapLongClickListener(new OnMapLongClickListener() {

                    @Override
                    public void onMapLongClick(LatLng ll) {
                        Geocoder gc = new Geocoder(MapsActivityR.this);
                        List<Address> list = null;

                        try {
                            list = gc.getFromLocation(ll.latitude, ll.longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }

                        Address add = list.get(0);
                        MapsActivityR.this.setMarker(add.getLocality(), add.getCountryName(),
                                ll.latitude, ll.longitude);

                    }
                });

                mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String msg = marker.getTitle() + " (" + marker.getPosition().latitude +
                                "," + marker.getPosition().longitude + ")";
                        Toast.makeText(MapsActivityR.this, msg, Toast.LENGTH_LONG).show();
                        return false;
                    }
                });

                mMap.setOnMarkerDragListener(new OnMarkerDragListener() {

                    @Override
                    public void onMarkerDragStart(Marker arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        Geocoder gc = new Geocoder(MapsActivityR.this);
                        List<Address> list = null;
                        LatLng ll = marker.getPosition();
                        try {
                            list = gc.getFromLocation(ll.latitude, ll.longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }

                        Address add = list.get(0);
                        marker.setTitle(add.getLocality());
                        marker.setSnippet(add.getCountryName());
                        marker.showInfoWindow();
                    }

                    @Override
                    public void onMarkerDrag(Marker arg0) {
                        // TODO Auto-generated method stub

                    }
                });

            }
        }
        return (mMap != null);
    }

    private void gotoLocation(double lat, double lng,
                              float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.moveCamera(update);
    }

    @SuppressWarnings("null")
    public void geoLocate(View v) throws IOException {

        try {
            if (flat != 0)
                removeEverything();
            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();

            EditText et = (EditText) findViewById(R.id.editText1);
            String location = et.getText().toString();
            if (location.length() == 0) {
                Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
                return;
            }

            hideSoftKeyboard(v);

            Geocoder gc = new Geocoder(this);
            List<Address> list = gc.getFromLocationName(location, 1);
            Address add = list.get(0);

            String locality = add.getFeatureName();
            //		Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

            double menor = -16.371829;
            double menorlon = -71.510759;
            Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
            Location targetLocation = new Location("");

            //latitud_longitud posicion actual
            double latactual = currentLocation.getLatitude();
            double lonactual = currentLocation.getLongitude();
            double men = 0;
            String latactual_string = Double.toString(latactual);
            String lonactual_string = Double.toString(lonactual);
            task tarea1 = new task(latactual_string, lonactual_string, 1);
            tarea1.execute();
            // tarea1.
            //latitud longitud posicion de destino
            double lat = add.getLatitude();
            double lng = add.getLongitude();

            String latdestino_string = Double.toString(lat);
            String londestino_string = Double.toString(lng);
            task tarea2 = new task(latdestino_string, londestino_string, 2);
            tarea2.execute();
            // new task(Double.toString(lat),Double.toString(lng),2).execute();

            gotoLocation(lat, lng, DEFAULTZOOM);

            setMarker(locality, add.getCountryName(), lat, lng);

            Toast.makeText(this, "Actual " + currentLocation.getLatitude() + "," + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            int decide;

            /*setMarker("Tomar Carro", "",
                    menor,
                    menorlon);*/
            setMarker("Posicion actual", "",
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude());

            flat = 1;
        }
        catch(Throwable  e)
        {
            Toast.makeText(this, "Excepcion:  " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void drawLine(int decide) {

        //marcador tomar el carro
        Location targetLocation = new Location("");
        //poscicion actual

        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
        //currentLocation.setLatitude(-16.3705211);
        //currentLocation.setLongitude(-71.5131896);
        /*currentLocation.setLatitude(-1639);
        currentLocation.setLongitude();*/
        double latitudes[] = new double[arreglo_lenght];
        double longitudes[] = new double[arreglo_lenght];
        double menor_punto = 0;
        double lati_punto_cercano=0;
        double longi_punto_cercano=0;
        for(int i=0;i<arreglo_lenght;i++)
        {

             latitudes[i]=    Double.parseDouble(latitudes_ruta[i]);
             longitudes[i]=   Double.parseDouble(longitud_ruta[i]);

        }

        // PolylineOptions options;
        PolylineOptions options_re;
        PolylineOptions options = new PolylineOptions().width(5).color(Color.rgb(91,174,255)).geodesic(true);
        for(int i=0;i<arreglo_lenght;i++)
        {
            // if(latitudes[]!==null)
            targetLocation.setLatitude(latitudes[i]);
            targetLocation.setLongitude(longitudes[i]);
            if(i==0)
            {
                menor_punto=currentLocation.distanceTo(targetLocation);
                lati_punto_cercano=targetLocation.getLatitude();
                longi_punto_cercano= targetLocation.getLongitude();


            }
            else
            {
                if(currentLocation.distanceTo(targetLocation)<menor_punto) {
                    menor_punto = currentLocation.distanceTo(targetLocation);
                    lati_punto_cercano=targetLocation.getLatitude();
                    longi_punto_cercano= targetLocation.getLongitude();
                   // Toast.makeText(this,latitudes[i] +  ", " + longitudes[i], Toast.LENGTH_SHORT).show();
                }
            }
           // Toast.makeText(this,latitudes[i] +  ", " + longitudes[i], Toast.LENGTH_SHORT).show();
            LatLng point = new LatLng(latitudes[i],longitudes[i]);
            options.add(point);
        }

        line = mMap.addPolyline(options);
        //line = mMap.addPolyline(options_re);

        //marcador de donde tomar el carro
        setMarker("Tomar Carro", "",
                lati_punto_cercano,
                longi_punto_cercano);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mapTypeNone:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.gotoCurrentLocation:
                gotoCurrentLocation();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();

        MapStateManager mgr = new MapStateManager(this);
        mgr.saveMapState(mMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MapStateManager mgr = new MapStateManager(this);
        CameraPosition position = mgr.getSavedCameraPosition();
        if (position != null) {
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            mMap.moveCamera(update);
            //			This is part of the answer to the code challenge
            mMap.setMapType(mgr.getSavedMapType());
        }

    }

    protected void gotoCurrentLocation() {
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);

        if (currentLocation == null) {
            Toast.makeText(this, "Current location isn't available", Toast.LENGTH_SHORT).show();
        } else {
            LatLng ll = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, DEFAULTZOOM);
            mMap.animateCamera(update);
        }

        setMarker("Current location", "",
                currentLocation.getLatitude(),
                currentLocation.getLongitude());

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
    }

    @Override
    public void onConnected(Bundle arg0) {
        //		Toast.makeText(this, "Connected to location service", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub

    }


    private void setMarker(String locality, String country, double lat, double lng) {


        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .position(new LatLng(lat, lng))
                //.anchor(.5f, .5f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ic_blackmaker))
                        //	.icon(BitmapDescriptorFactory.defaultMarker())
                .draggable(true);
        if (country.length() > 0) {
            options.snippet(country);
        }

        if (marker1 == null) {
            marker1 = mMap.addMarker(options);

        } else if (marker2 == null) {
            marker2 = mMap.addMarker(options);

        } else if (marker3 == null) {
            marker3 = mMap.addMarker(options);
        } else {
            removeEverything();
            marker1 = mMap.addMarker(options);
        }

    }

    private void Elegir_ruta() {

        String Ruta_adecuada = "";
        String Codigo_combi = "";

        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 200; j++) {
                if (rutas_cercanas_posicion[i] != "" || rutas_cercanas_destino[j] != "") {
                    if (rutas_cercanas_posicion[i].equals(rutas_cercanas_destino[j])) {
                        Ruta_adecuada = rutas_cercanas_posicion[i];
                        //Codigo_combi=codigo_rutas_cercanas[i];

                    }
                }
            }
        }

        //Toast.makeText(this, Ruta_adecuada, Toast.LENGTH_LONG).show();

        if(!Ruta_adecuada.equals(""))
        {
           // Toast.makeText(this, "Entra a ruta adecuada", Toast.LENGTH_SHORT).show();
            task tarea3 = new task(Ruta_adecuada, 3);
            tarea3.execute();

        }

    }

    private void mostrar_nombre_combi()
    {
        Toast.makeText(this,Nombre_combi, Toast.LENGTH_LONG).show();
        //marker3.setTitle(marker3.getTitle()+ " " + Nombre_combi);

    }



    private void removeEverything() {
        marker1.remove();
        marker1 = null;
        marker2.remove();
        marker2 = null;
         marker3.remove();
         marker3 = null;
        line.remove();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }


    class task extends AsyncTask<String, String, Void> {
        private ProgressDialog progressDialog = new ProgressDialog(MapsActivityR.this);
        InputStream is = null;
        String result = "";
        String latitud = "-16.405698";
        String longitud = "-71.548877";
        int opcion = 0;
        protected void onPreExecute() {
            progressDialog.setMessage("Cargando rutas ...");
            progressDialog.show();
            /*progressDialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    task.this.cancel(true);
                }
            });*/
        }

        public task(String lat, String lon, int opc)
        {
            latitud=lat;
            longitud=lon;
            opcion=opc;
        }
        public task(String lat, int opc)
        {
            latitud=lat;
            opcion=opc;
        }
        @Override
        protected Void doInBackground(String... params) {
            String url_select = "http://www.combiaqp.16mb.com/demo.php";
            if (opcion==3)
                url_select="http://www.combiaqp.16mb.com/NombresCombis.php";
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
                String[] NombresCombis2 = new String[200];
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
                    if(opcion==3)
                    {

                        latitudes_ruta[i]=Jasonobject.getString("DA_Latitud");
                        longitud_ruta[i]=Jasonobject.getString("DA_Longitud");
                        Nombre_combi=Jasonobject.getString("DA_NomCombi");
                    }

                    else {
                        String IN_CodCombi = Jasonobject.getString("IN_CodCombi");
                        //String DA_Latitud="";
                        NombresCombis[i] = Jasonobject.getString("DA_NRuta");
                       // NombresCombis2[i] = Jasonobject.getString("DA_NomCombi");
                    }
                   /* if (et.getText().toString().equalsIgnoreCase(IN_CodCombi)) {

                        text.setText(NombresCombis[i]);
                        break;
                    }*/
                }
                if(opcion==1)
                    rutas_cercanas_posicion = NombresCombis;

                else {
                    if(opcion==3)
                    {
                        drawLine(1);
                    }
                    codigo_rutas_cercanas=NombresCombis2;
                    rutas_cercanas_destino=NombresCombis;

                }
                Elegir_ruta();
                mostrar_nombre_combi();

                this.progressDialog.dismiss();

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error parsing data " + e.toString());
            }


        }

    }

}