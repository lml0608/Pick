package com.example.android.pick;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by zengzhi on 2017/7/21.
 */

public class MapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int REQUEST_ERROR = 0;
    private final String LOG_TAG = "LaurenceTestApp";

    private TextView txtOutput;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private static final String[] LOCATION_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final int REQUEST_LOCATION_PERMISSIONS = 1;


    @Override
    protected void onResume() {
        super.onResume();


        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);

        Log.i(LOG_TAG, String.valueOf(errorCode)+ "nihao");
        if (errorCode != ConnectionResult.SUCCESS) {

            Dialog errorDialog = apiAvailability.getErrorDialog(this,
                    errorCode,
                    REQUEST_ERROR,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }
                    });
            errorDialog.show();


        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(LOCATION_PERMISSIONS,
                    REQUEST_LOCATION_PERMISSIONS);
        }

        txtOutput = (TextView) findViewById(R.id.txt);

        //使用位置服务LocationServices，添加几个监听
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启连接
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        //关闭连接
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //-------------------------------------------------------
    //GoogleApiClient.ConnectionCallbacks

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        //服务连接上

        //建立位置请求，mLocationRequest
        //set it's priority to hight accuracy
        //set it to update every second(1000 ms)
        //Call requestLocationUpdate in the Api with this request

        mLocationRequest = LocationRequest.create();

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //this = new LocationListener()
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

        //先前已连接，突然暂停了


        Log.i(LOG_TAG, "GoogleApiClient connection has been suspend");

    }

    //GoogleApiClient.OnConnectionFailedListener
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        //连接失败

        Log.i(LOG_TAG, "GoogleApiClient connection has failed");
    }


    //LocationListener

    @Override
    public void onLocationChanged(Location location) {
        //位置改变

        Log.i(LOG_TAG, location.toString());
        //txtOutput.setText(location.toString());

        txtOutput.setText(Double.toString(location.getLatitude()));
    }

    //---------------------------------------------------------------


    private boolean hasLocationPermission() {

        int result = ContextCompat.checkSelfPermission(this, LOCATION_PERMISSIONS[0]);
        return  result == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case REQUEST_LOCATION_PERMISSIONS:
                if (hasLocationPermission()) {

                    //findImage();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }

    }
}
