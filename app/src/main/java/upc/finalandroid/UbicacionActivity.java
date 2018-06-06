package upc.finalandroid;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class UbicacionActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private static final int REQUEST_CODE = 1000;
    private TextView txtvLat, txtvLon;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    private Button btnLocation, btnStop;
    String lon = "";
    String lat = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        txtvLat = (TextView) findViewById(R.id.etxt_lat);
        txtvLon = (TextView) findViewById(R.id.etxt_lon);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnLocation = (Button) findViewById(R.id.btn_location);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //Check Permission
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            //if permission is granted
            buildLocationRequest();
            buildLocationCallBack();


            btnLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ActivityCompat.checkSelfPermission(UbicacionActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UbicacionActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(UbicacionActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                        return;
                    }
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                    btnLocation.setEnabled(!btnLocation.isEnabled());
                    btnStop.setEnabled(!btnStop.isEnabled());

                }
            });

            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ActivityCompat.checkSelfPermission(UbicacionActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UbicacionActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(UbicacionActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                        return;
                    }
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback);

                    btnLocation.setEnabled(!btnLocation.isEnabled());
                    btnStop.setEnabled(!btnStop.isEnabled());

                }
            });

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        txtvLat.setText(String.valueOf(location.getLatitude()));
                        txtvLon.setText(String.valueOf(location.getLongitude()));
                        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(txtvLat.getText().toString()), Double.parseDouble(txtvLon.getText().toString()))).title("Localizacion seleccionada"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(txtvLat.getText().toString()), Double.parseDouble(txtvLon.getText().toString()))));

                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });

        }

    private void buildLocationCallBack() {

        locationCallback= new LocationCallback(){
            @Override

            public void onLocationResult(LocationResult locationResult) {
                for(Location location :  locationResult.getLocations() ){
                    txtvLat.setText (String.valueOf(location.getLatitude()));
                    txtvLon.setText(String.valueOf(location.getLongitude()));

                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(txtvLat.getText().toString()), Double.parseDouble(txtvLon.getText().toString()))).title("Localizacion seleccionada"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(txtvLat.getText().toString()), Double.parseDouble(txtvLon.getText().toString()))));


                }

            }

        };

    }

    private void buildLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);

    }
}
