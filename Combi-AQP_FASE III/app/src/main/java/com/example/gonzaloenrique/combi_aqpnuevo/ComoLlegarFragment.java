package com.example.gonzaloenrique.combi_aqpnuevo;

import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
public class ComoLlegarFragment extends FragmentActivity implements
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
    private static final double [] cotaspa_lati= {-16.384240,-16.383684,-16.382037,-16.382367,-16.375038,
            -16.374008,-16.380020,-16.379690,-16.383293,-16.388007,-16.392134,-16.396869,-16.397013,-16.396190,
            -16.404238,-16.404773,-16.404259,-16.403538,-16.404321,-16.404238,-16.403003,-16.403435,-16.405288,
            -16.405885,-16.408293,-16.409569,-16.417144,-16.420540,-16.421940,-16.425295,-16.426962,-16.426982,
            -16.430131,-16.430564,-16.441842,-16.443180,-16.444867,-16.446184, -16.447193,-16.453325,-16.455013,
            -16.455610,-16.456345};
    private static final double [] cotaspa_longi= {-71.569208, -71.568092 , -71.568157, -71.569101
            , -71.569272, -71.560410, -71.558930, -71.556891, -71.556333, -71.552321, -71.540631, -71.542648
            , -71.543399, -71.544944, -71.547926, -71.546531, -71.546210, -71.545094, -71.543055, -71.542433
            , -71.540931, -71.540394, -71.540738, -71.540008, -71.538313, -71.536618, -71.532884, -71.531404
            , -71.531404, -71.533271, -71.533163, -71.533936, -71.534408, -71.532906, -71.529000, -71.529022
            , -71.530266, -71.530588, -71.531361, -71.538656, -71.536596, -71.537519, -71.539434};

    private static final double [] quinceagosto_lati = {-16.405870,-16.406858,-16.404388,-16.406467,
            -16.407558,-16.408278,-16.403132,-16.405386,-16.404604,-16.415389,-16.416913,-16.416480,-16.415307,
            -16.414957,-16.414299};

    private static final double [] quinceagosto_lonti = {-71.531698, -71.530753, -71.527470, -71.525217
            , -71.527106, -71.526634, -71.516570,-71.515424, -71.513836, -71.497142, -71.496541,-71.494095
            , -71.494288, -71.492464, -71.492486};
    private static final float DEFAULTZOOM = 15;
    @SuppressWarnings("unused")
    private static final String LOGTAG = "Maps";

    GoogleApiClient mLocationClient;
    Marker marker1;
    Marker marker2;
    Polyline line;
    Marker marker3;




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
                        Geocoder gc = new Geocoder(ComoLlegarFragment.this);
                        List<Address> list = null;

                        try {
                            list = gc.getFromLocation(ll.latitude, ll.longitude, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }

                        Address add = list.get(0);
                        ComoLlegarFragment.this.setMarker(add.getLocality(), add.getCountryName(),
                                ll.latitude, ll.longitude);

                    }
                });

                mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String msg = marker.getTitle() + " (" + marker.getPosition().latitude +
                                "," + marker.getPosition().longitude + ")";
                        Toast.makeText(ComoLlegarFragment.this, msg, Toast.LENGTH_LONG).show();
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
                        Geocoder gc = new Geocoder(ComoLlegarFragment.this);
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
        String locality = add.getLocality();
        //		Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

        double lat = add.getLatitude();
        double lng = add.getLongitude();

        gotoLocation(lat, lng, DEFAULTZOOM);

        setMarker(locality, add.getCountryName(), lat, lng);

        double menor=0;
        double menorlon=0;
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);
        //Location currentLocation = new Location("");
        Location targetLocation = new Location("");
        //currentLocation.setLatitude(-16.405583);
        //currentLocation.setLongitude(-71.548836);
        double latactual = currentLocation.getLatitude();
        double lonactual = currentLocation.getLongitude();
        double men = 0;

        Toast.makeText(this, "Actual " + currentLocation.getLatitude() + "," +  currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        int decide;
        for (int i = 0; i < cotaspa_lati.length ; i++) {

            if (i==0) {
                targetLocation.setLatitude(cotaspa_lati[i]);
                targetLocation.setLongitude(cotaspa_longi[i]);
                men = currentLocation.distanceTo(targetLocation);
                menor=cotaspa_lati[i];
                menorlon=cotaspa_longi[i];
            }
            targetLocation.setLatitude(cotaspa_lati[i]);
            targetLocation.setLongitude(cotaspa_longi[i]);
            if(currentLocation.distanceTo(targetLocation)<men)
            {
                men=currentLocation.distanceTo(targetLocation);
                menor=cotaspa_lati[i];
                menorlon=cotaspa_longi[i];
            }
        }
        decide=0;
        for (int i = 0; i < quinceagosto_lati.length ; i++) {

            targetLocation.setLatitude(quinceagosto_lati[i]);
            targetLocation.setLongitude(quinceagosto_lati[i]);
            if(currentLocation.distanceTo(targetLocation)<men)
            {
                men=currentLocation.distanceTo(targetLocation);
                menor=quinceagosto_lati[i];
                menorlon=quinceagosto_lati[i];
                decide=1;
            }

        }

        setMarker("Tomar Carro", "",
                menor,
                menorlon);
        setMarker("Posicion actual", "",
                currentLocation.getLatitude(),
                currentLocation.getLongitude());

        drawLine(decide);

    }
    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    private void drawLine(int decide) {

        PolylineOptions options;
        PolylineOptions options_re;
        if(decide ==1)
        {/*PolylineOptions options = new PolylineOptions()
			.add(marker1.getPosition())
			.add(marker2.getPosition())
			.color(Color.YELLOW);*/

            options = new PolylineOptions()
                    .add(new LatLng(-16.414873, -71.492476),new LatLng(-16.415182, -71.494321),
                            new LatLng(-16.416520, -71.494064),new LatLng(-16.416852, -71.496641),new LatLng(-16.415470, -71.497218),
                            new LatLng(-16.415470, -71.497218),new LatLng(-16.399510, -71.521655),
                            new LatLng(-16.408793, -71.532921),new LatLng(-16.407661, -71.533972),
                            new LatLng(-16.405870, -71.531698))
                    .width(5)
                    .color(Color.RED);

            options_re = new PolylineOptions()
                    .add(new LatLng(-16.405870, -71.531698),new LatLng(-16.406858, -71.530753),
                            new LatLng(-16.404388, -71.527470),new LatLng(-16.406467, -71.525217),
                            new LatLng(-16.407558, -71.527106),new LatLng(-16.408278, -71.526634),
                            new LatLng(-16.403132, -71.516570),
                            new LatLng(-16.405386, -71.515424),new LatLng(-16.404604, -71.513836),
                            new LatLng(-16.415389, -71.497142),new LatLng(-16.416913, -71.496541),
                            new LatLng(-16.416480, -71.494095),new LatLng(-16.415307, -71.494288),
                            new LatLng(-16.414957, -71.492464),new LatLng(-16.414299, -71.492486)
                    )
                    .color(Color.BLUE)
                    .width(5);
        }
        else
        {
            options = new PolylineOptions()
                    .add(new LatLng(-16.384240, -71.569208),new LatLng(-16.383684, -71.568092),
                            new LatLng(-16.382037, -71.568157),new LatLng(-16.382367, -71.569101),
                            new LatLng(-16.375038, -71.569272),new LatLng(-16.374008, -71.560410),
                            new LatLng(-16.380020, -71.558930),new LatLng(-16.379690, -71.556891),
                            new LatLng(-16.383293, -71.556333),new LatLng(-16.388007, -71.552321),
                            new LatLng(-16.392134, -71.540631),new LatLng(-16.396869, -71.542648),
                            new LatLng(-16.397013, -71.543399),
                            new LatLng(-16.396190, -71.544944),new LatLng(-16.404238, -71.547926),
                            new LatLng(-16.404773, -71.546531),new LatLng(-16.404259, -71.546210),
                            new LatLng(-16.403538, -71.545094),new LatLng(-16.404321, -71.543055),
                            new LatLng(-16.404238, -71.542433),new LatLng(-16.403003, -71.540931),
                            new LatLng(-16.403435, -71.540394),new LatLng(-16.405288, -71.540738),
                            new LatLng(-16.405885, -71.540008),new LatLng(-16.408293, -71.538313),
                            new LatLng(-16.409569, -71.536618),new LatLng(-16.417144, -71.532884),
                            new LatLng(-16.420540, -71.531404),new LatLng(-16.421940, -71.531404),
                            new LatLng(-16.425295, -71.533271),new LatLng(-16.426962, -71.533163),
                            new LatLng(-16.426982, -71.533936),new LatLng(-16.430131, -71.534408),
                            new LatLng(-16.430564, -71.532906),new LatLng(-16.441842, -71.529000),
                            new LatLng(-16.443180, -71.529022),new LatLng(-16.444867, -71.530266),
                            new LatLng(-16.446184, -71.530588),new LatLng(-16.447193, -71.531361),
                            new LatLng(-16.453325, -71.538656),new LatLng(-16.455013, -71.536596),
                            new LatLng(-16.455610, -71.537519),new LatLng(-16.456345, -71.539434))
                    .width(8)
                    .color(Color.RED);


            options_re = new PolylineOptions()
                    .add(
                            new LatLng(-16.456345, -71.539434),new LatLng(-16.455610, -71.537519),
                            new LatLng(-16.455013, -71.536596),new LatLng(-16.453325, -71.538656),
                            new LatLng(-16.447193, -71.531361),new LatLng(-16.446184, -71.530588),
                            new LatLng(-16.444867, -71.530266),new LatLng(-16.443180, -71.529022),
                            new LatLng(-16.441842, -71.529000),new LatLng(-16.430564, -71.532906),
                            new LatLng(-16.430131, -71.534408),new LatLng(-16.426678, -71.533995),
                            new LatLng(-16.424528, -71.533062),new LatLng(-16.421940, -71.531404),
                            new LatLng(-16.420540, -71.531404),new LatLng(-16.417144, -71.532884),
                            new LatLng(-16.409569, -71.536618),new LatLng(-16.408293, -71.538313),
                            new LatLng(-16.406816, -71.539504),new LatLng(-16.408741, -71.541253),
                            new LatLng(-16.407804, -71.542315),new LatLng(-16.406652, -71.541853),
                            new LatLng(-16.404707, -71.542572),new LatLng(-16.404737, -71.542797),
                            new LatLng(-16.404336, -71.543023),new LatLng(-16.403539, -71.544896),
                            new LatLng(-16.404146, -71.545604),new LatLng(-16.404208, -71.546269),
                            new LatLng(-16.403776, -71.547707),new LatLng(-16.399864, -71.546377),
                            new LatLng(-16.391373, -71.543115),new LatLng(-16.387816, -71.552658),
                            new LatLng(-16.384296, -71.555844),new LatLng(-16.380333, -71.556767),
                            new LatLng(-16.380395, -71.557497),new LatLng(-16.379901, -71.557711),
                            new LatLng(-16.379880, -71.557883),new LatLng(-16.378882, -71.558151),
                            new LatLng(-16.378995, -71.558945),new LatLng(-16.378017, -71.559213),
                            new LatLng(-16.378048, -71.559460),new LatLng(-16.373972, -71.560436),
                            new LatLng(-16.374672, -71.564095),new LatLng(-16.374980, -71.566917),
                            new LatLng(-16.375269, -71.567979),new LatLng(-16.375114, -71.569288),
                            new LatLng(-16.379602, -71.568869),new LatLng(-16.384306, -71.569320))
                    .color(Color.BLUE)
                    .width(5);
        }
        line = mMap.addPolyline(options);
        line = mMap.addPolyline(options_re);

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

        MapStateManager mgr = new  MapStateManager(this);
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
        }
        else {
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
                .anchor(.5f, .5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mapmarker))
                        //	.icon(BitmapDescriptorFactory.defaultMarker())
                .draggable(true);
        if (country.length() > 0) {
            options.snippet(country);
        }

        if (marker1 == null ) {
            marker1 = mMap.addMarker(options);
            drawLine();
        }
		/*else if (marker2 == null) {
			marker2 = mMap.addMarker(options);
			drawLine();
		}
		else {
			removeEverything();
			marker1 = mMap.addMarker(options);
		}*/

    }

    private void drawLine() {
		/*PolylineOptions options = new PolylineOptions()
		.add(marker1.getPosition())
		.add(marker2.getPosition())
		.color(Color.YELLOW);*/

        PolylineOptions options = new PolylineOptions()
                .add(new LatLng(-16.414873, -71.492476),new LatLng(-16.415182, -71.494321),
                        new LatLng(-16.416520, -71.494064),new LatLng(-16.416852, -71.496641),new LatLng(-16.415470, -71.497218),
                        new LatLng(-16.415470, -71.497218),new LatLng(-16.399510, -71.521655),
                        new LatLng(-16.408793, -71.532921),new LatLng(-16.407661, -71.533972),
                        new LatLng(-16.405870, -71.531698))
                .color(Color.RED);
        line = mMap.addPolyline(options);

    }

    private void removeEverything() {
        marker1.remove();
        marker1 = null;
        marker2.remove();
        marker2 = null;
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
}
