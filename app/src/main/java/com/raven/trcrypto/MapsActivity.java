package com.raven.trcrypto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.Manifest;

import android.util.Log;

import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    String felixsssss;
    String felixsssaaassg;
    String felixssssq22s;
    String felixsssaaa2ssg;
    private GoogleMap mMap;
    private TextView lokasi;
    private ActionBar mActionBar;
    private final float color[] = {BitmapDescriptorFactory.HUE_BLUE,
            BitmapDescriptorFactory.HUE_GREEN,
            BitmapDescriptorFactory.HUE_MAGENTA,
            BitmapDescriptorFactory.HUE_VIOLET,
            BitmapDescriptorFactory.HUE_ROSE,
            BitmapDescriptorFactory.HUE_YELLOW};// 6 warna
    FusedLocationProviderClient FLPC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar=findViewById(R.id.toolbarmap);
        setSupportActionBar(toolbar);
        mActionBar=getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        lokasi = findViewById(R.id.lokasiku);
        Log.e("Tereksekusi", "onCreate");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        } else {
            googleMap.setMyLocationEnabled(true);
            statusCheck();
            //getloc();
        }
        Log.e("Tereksekusi", "onmapready");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.e("tereksekusi", Integer.toString(grantResults.length));

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED&& grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,"Akses diizinkan",Toast.LENGTH_SHORT).show();
                    statusCheck();
                } else {
                    onBackPressed();
                    Toast.makeText(this,"Akses tidak diizinkan",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void getloc() {
        LocationManager loma = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        loma.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 600000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if(location!=null){

                    addmark(location.getLatitude(),location.getLongitude());
                }
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }
        });
        /*
        FLPC = LocationServices.getFusedLocationProviderClient(this);
        FLPC.getLastLocation().addOnSuccessListener(this,new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.e("tereksekusi", "Lokasi tidak null");
                    addmark(location.getLatitude(), location.getLongitude());
                    Log.e("tereksekusi", "Berhasil");

                }else{
                    Log.e("tereksekusi", "Lokasi null");
                }
            }
        });
        */
        /*
            @Override
            public void onComplete(@NonNull Task<android.location.Location> task) {
                android.location.Location location = task.getResult();
                Log.e("masuk","ini program");
                if (location != null) {
                    try {
                        Geocoder gc = new Geocoder(MapsActivity.this, Locale.getDefault());
                        List<Address> alamat = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        addmark(location.getLatitude(), location.getLongitude());
                        Log.e("Executed", "tereksekusi");
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
            }
        });*/
    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }else{
            getloc();
            Toast.makeText(this,"GPS is turned on!",Toast.LENGTH_SHORT).show();
        }
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),1);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        onBackPressed();
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }else{
            /*Intent i = new Intent(this,MapsActivity.class);
            finish();
            startActivity(i);*/
            getloc();
            Toast.makeText(this,"GPS is turned on!",Toast.LENGTH_SHORT).show();
        }
    }


    private void addmark(Double la , Double lo){ //la == latitude dan lo == longitude
        int pick = acak_angka(6);
        float warna = color[pick];
        LatLng x = new LatLng(la,lo);
        String alamat = dapatkan_alamat_lengkap(la,lo);
        MarkerOptions markerOptions =
                new MarkerOptions().position(x).title(alamat);
        mMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.defaultMarker(warna)));
        Log.e("tereksekusi","masuk addmark");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(x, 15.0f));
        lokasi.setText(alamat);
    };

    public int acak_angka(int angka_max){ //mulai dari angka 0 hingga sebelum angka_max
        int temp=angka_max;
        while(temp==angka_max){
            temp = (int) (Math.random()*angka_max);
        }
        return temp;
    };

    private String dapatkan_alamat_lengkap(double LATITUDE, double LONGITUDE) {
        String builder = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            Log.e("tereksekusi", "cobak list alamat geocoder");
            List<Address> alamat = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (alamat != null) {
                Log.e("tereksekusi", "alamat tidak kosong");
                Address returnedAddress = alamat.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                builder = strReturnedAddress.toString();
            } else {
                Log.e("tereksekusi", "alamat kosong jarene");
            }
        } catch (Exception e) {
            e.printStackTrace();

            Log.e("tereksekusi", "koq error");

        }
        return builder;
    }
}