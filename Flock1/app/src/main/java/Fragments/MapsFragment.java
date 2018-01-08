package Fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.natia.flock1.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Model.Markers;
import UI.CustomInfoWindow;
import Util.Trains;


public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private BufferedReader in;
    private MapView mapView;
    private Double myLongitude = 0.00;
    private Double myLattitude = 0.00;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Markers markers = new Markers();
    private Trains trains = new Trains();
    private RequestQueue queue;
    private Context c;
    private BufferedReader bufferedReader = null;
    private LocationListener locationListener;
    private final static int MY_PERMISSINON_FINE_LOCATION = 101;
    protected final static String TAG = "MapsFragment";


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        View rootView = inflater.inflate(R.layout.map, container, false);


        //initialize map
        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstaceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setInfoWindowAdapter(new CustomInfoWindow(getContext()));
                mMap.setOnMarkerClickListener(MapsFragment.this);
                mMap.setOnInfoWindowClickListener(MapsFragment.this);


                if (ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    mMap.setMyLocationEnabled(true);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSINON_FINE_LOCATION);
                    }
                }



                //Add a markers for current location and move the camera
                //LatLng myLocation = new LatLng(myLattitude, myLongitude);
                //mMap.addMarker(new MarkerOptions().position(myLocation).title("My Location"));
                //mMap.moveCamera(CameraUpdateFactory.);
                getTrainStations();

            }
        });



        //Build GoogleAPIClient, tells it that we are going to user location services
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(15 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //queue = Volley.newRequestQueue(getContext());
        //ArrayList<String> stations = getTrainStations();

        //Log.d("arraydata2", String.valueOf(stations.subList(1,1)));
        return rootView;
    }



    private void getTrainStations() {
        JSONArray jsonArray = null;
        ArrayList<String> cList = new ArrayList<String>();

        try {
            InputStream is = getResources().getAssets().open("stations.json");
            bufferedReader = new BufferedReader(new InputStreamReader(is));

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";

            while((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            String finalJson = stringBuffer.toString();

            JSONObject jsonObject = new JSONObject(finalJson);

            int size = jsonObject.length();
            JSONArray keys = jsonObject.names();


            for (int i = 0; i < keys.length(); i++){
                String key = keys.getString(i);
                String value = jsonObject.getJSONObject(key).getString("POSITION");
                String array1[]= value.split(" ");
                myLattitude = Double.parseDouble(array1[0]);
                myLongitude = Double.parseDouble(array1[1]);

                //Set the markers values
                markers.setName(key);
                markers.setLongitude(myLongitude);
                markers.setLattitude(myLattitude);
                markers.setLines(jsonObject.getJSONObject(key).getString("LINE"));
                //int train = tra
                //markers.setLinesPic(train);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                markerOptions.title(markers.getName())
                        .position(new LatLng(myLongitude,myLattitude));
                markerOptions.snippet("Lines: " + markers.getLines());

                Marker marker = mMap.addMarker(markerOptions);



                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLongitude,myLattitude), 15));
            }




        } catch (IOException|JSONException ex){
            ex.printStackTrace();
        }

        //JSONObject jsonObject = new JSONObject()
    }

    @Override
    //Called after onStart
    public void onResume() {
        mapView.onResume();

        if(googleApiClient.isConnected()){
            requestLocationUpdates();
        }

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSINON_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(getContext(), "This app requires location permission to be granted",
                            Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,
                    this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection Failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Log.d(TAG, "We made it here");
        Toast.makeText(getContext(),marker.getSnippet().toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        
    }
}