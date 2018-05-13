package com.example.a1512572.mobileminiproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import static android.location.LocationManager.PASSIVE_PROVIDER;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private static final String DIRECTION_API_KEY = "AIzaSyBv_krOVfS_4axcL8u4ivtj-SaKFxz4E8M";

    private GoogleMap mMap;
    private LatLng myPosition;
    private MarkerOptions myPosMarkerOpt;
    private Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Set map theme
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {}
        } catch (Resources.NotFoundException e) {}

        //Check permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_REQUEST_CODE);
            }}

        mMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(PASSIVE_PROVIDER);

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            myPosition = new LatLng(latitude, longitude);

            myPosMarkerOpt = new MarkerOptions().position(myPosition).title("Vị trí của bạn");
            myPosMarkerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            myMarker = mMap.addMarker(myPosMarkerOpt);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
            mMap.animateCamera( CameraUpdateFactory.zoomTo( 16.0f ) );
        }

        locationManager.requestLocationUpdates(PASSIVE_PROVIDER, 5000, 5, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                myPosition = new LatLng(latitude, longitude);

                //myPosMarkerOpt.position(myPosition);
                myMarker.setPosition(myPosition);
                //mMap.addMarker(new MarkerOptions().position(myPosition).title("Vị trí của bạn"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
                mMap.animateCamera( CameraUpdateFactory.zoomTo( 16.0f ) );
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
                Toast.makeText(MapsActivity.this, "Provider enabled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderDisabled(String s) {
                Toast.makeText(MapsActivity.this, "Provider disabled", Toast.LENGTH_LONG).show();
            }
        });

        /*LatLng destination = new LatLng(10.762427, 106.681228);
        GoogleDirection.withServerKey(DIRECTION_API_KEY)
                .from(myPosition)
                .to(destination)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if(direction.isOK()) {
                            Toast.makeText(MapsActivity.this,"Gửi yêu cầu tìm đường thành công",Toast.LENGTH_LONG).show();
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> pointList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(MapsActivity.this, pointList, 10, Color.MAGENTA);
                            mMap.addPolyline(polylineOptions);
                        }
                        else
                            Toast.makeText(MapsActivity.this,"Xuất hiện lỗi khi gửi yêu cầu tìm đường!",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Toast.makeText(MapsActivity.this,"Xuất hiện lỗi khi gửi yêu cầu tìm đường!",Toast.LENGTH_LONG).show();
                    }
                });*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
            else {
                Toast.makeText(this,"Ứng dụng không thể hoạt động nếu không cho phép truy cập vị trí của thiết bị",Toast.LENGTH_LONG).show();
            }
        }
    }
}