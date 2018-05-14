package com.example.a1512572.mobileminiproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.location.LocationManager.PASSIVE_PROVIDER;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private static final String DIRECTION_API_KEY = "AIzaSyBv_krOVfS_4axcL8u4ivtj-SaKFxz4E8M";

    private GoogleMap mMap;
    private LatLng myPosition;
    private MarkerOptions myPosMarkerOpt;
    private Marker myMarker;

    ArrayList<Marker> markerList;

    Polyline way;
    PolylineOptions wayopt;

    DatabaseHelper db;

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = new DatabaseHelper(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Set map theme
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {showText("Không thể tải style cho map!");}
        } catch (Resources.NotFoundException e) {showText("Không thể tải style cho map!");}

        //Check permission
        checkPermission();

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
            mMap.animateCamera( CameraUpdateFactory.zoomTo( 14.0f ) );
        }

        locationManager.requestLocationUpdates(PASSIVE_PROVIDER, 5000, 5, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                myPosition = new LatLng(latitude, longitude);

                myMarker.setPosition(myPosition);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
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

        loadCH();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                          @Override
                                          public boolean onMarkerClick(Marker marker) {
                                              markerClickEvent(marker);
                                              return false;
                                          }
                                      });

    }

    public void loadCH(){
        Cursor result = db.getAllCH();
        if (result.getCount()==0){
            showText("Danh sách cửa hàng trống!");
            return;
        }
        while (result.moveToNext()){
            if (!result.getString(10).equals("0"))
                if (Integer.parseInt(result.getString(9)) > 0)
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(result.getString(4)),Double.parseDouble(result.getString(5)))).title(result.getString(1)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))).setSnippet(result.getString(3));
                else
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(result.getString(4)),Double.parseDouble(result.getString(5)))).title(result.getString(1)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).setSnippet(result.getString(3));
        }

    }

    private void showDirection(LatLng from, LatLng to){
        GoogleDirection.withServerKey(DIRECTION_API_KEY)
                .from(from)
                .to(to)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if(direction.isOK()) {
                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> pointList = leg.getDirectionPoint();

                            if (way!=null)
                            way.remove();
                            wayopt = DirectionConverter.createPolyline(MapsActivity.this, pointList, 10, Color.MAGENTA);
                            way = mMap.addPolyline(wayopt);
                        }
                        else
                            showText("Xuất hiện lỗi khi gửi yêu cầu tìm đường!");
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        showText("Xuất hiện lỗi khi gửi yêu cầu tìm đường!");
                    }
                });
    }

    private void markerClickEvent(final Marker marker){
        if (marker.getTitle().equals(myMarker.getTitle()))
            return;

        Cursor result = db.getSpecCH(marker.getTitle());
        result.moveToNext();
        final String id = result.getString(0);
        final String name = marker.getTitle();
        final String desc = result.getString(2);
        final String addr = marker.getSnippet();
        final String lat = result.getString(4);
        final String lng = result.getString(5);
        final String open = result.getString(6);
        final String close = result.getString(7);
        final String phone = result.getString(8);
        final String type = result.getString(9);
        final String status = result.getString(10);
        final String imageURL = result.getString(11);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogContent =  inflater.inflate(R.layout.infobox, null);

        TextView ifOpen = (TextView) dialogContent.findViewById(R.id.isOpen);
        TextView openTime = (TextView) dialogContent.findViewById(R.id.openTime);
        TextView title = (TextView) dialogContent.findViewById(R.id.CHname);
        TextView addrC = (TextView) dialogContent.findViewById(R.id.addrContent);
        TextView descC = (TextView) dialogContent.findViewById(R.id.descContent);
        ImageView imageView = (ImageView) dialogContent.findViewById(R.id.CHimage);
        builder.setCancelable(true);
        Picasso.get().load(imageURL).into(imageView);
        title.setText(name);
        addrC.setText(addr);
        String openOrClose = "Đang mở cửa";
        if (isOpen(open, close)){
            ifOpen.setTextColor(Color.GREEN);
            openOrClose = "Đang mở cửa";
            ifOpen.setText(openOrClose);
        }
        else {
            ifOpen.setTextColor(Color.RED);
            openOrClose = "Đóng cửa";
            ifOpen.setText(openOrClose);
        }
        String ot = open + " : " + close;
        openTime.setText(ot);
        String updating = "Đang cập nhật mô tả";
        if (desc.equals(""))
            descC.setText(updating);
        else
            descC.setText(desc);
        builder.setView(dialogContent);

        if (type.equals("0")){
            builder.setPositiveButton("Yêu thích", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    db.updateCH(id, name, desc, addr, lat, lng, open, close, phone, "1", status, imageURL);
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                }
            });
        }
        else
        {
            builder.setPositiveButton("Hủy yêu thích", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    db.updateCH(id, name, desc, addr, lat, lng, open, close, phone, "0", status, imageURL);
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }
            });
        }

        builder.setNegativeButton("Gọi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });

        builder.setNeutralButton("Dẫn đường", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showDirection(myPosition, marker.getPosition());
            }
        });


        builder.show();

    }

    private boolean isOpen(String open, String close){

        int openh = Integer.parseInt(open.subSequence(0,2).toString());
        int openm = Integer.parseInt(open.subSequence(3,5).toString());

        int closeh = Integer.parseInt(close.subSequence(0,2).toString());
        int closem = Integer.parseInt(close.subSequence(3,5).toString());

        Calendar time = Calendar.getInstance();
        int hour = time.get(Calendar.HOUR_OF_DAY);
        int min = time.get(Calendar.MINUTE);

        if (((closeh > openh) || (closeh == openh && closem > openm)) && ((hour > openh) || (hour == openh && min >= openm)) && ((hour < closeh) || (hour == closeh && min <closem)))
            return true;

        if ((closeh < openh || (closeh == openh && closem < openm)) && !((hour > closeh) || (hour == closeh && min > closem)) && !((hour < openh) || (hour == openh && min <openm)))
            return true;

        return false;

    }


    //////

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_REQUEST_CODE);
            }}
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

    private void showText(String text){
        Toast.makeText(MapsActivity.this, text, Toast.LENGTH_LONG);
    }
}