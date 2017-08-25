package com.lukederrynz.android_test;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;


public class WeatherActivity extends AppCompatActivity implements LocationListener {

    private TextView cityField, detailsField, currentTemperatureField, humidityField, pressureField, weatherIcon, updatedField, textView_Location;
    private Typeface weatherFont;
    private LocationManager locationManager;
    private Button button_getLocation;

    //region On Events

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initializeControls();
    }

    //endregion

    private void initializeControls() {

        button_getLocation = (Button)findViewById(R.id.Weather_button_getWeather);

        requestLocationAccess();

        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        cityField = (TextView)findViewById(R.id.Weather_textView_city_field);
        updatedField = (TextView)findViewById(R.id.Weather_textView_updated_field);
        detailsField = (TextView)findViewById(R.id.Weather_textView_details_field);
        currentTemperatureField = (TextView)findViewById(R.id.Weather_textView_current_temperature_field);
        humidityField = (TextView)findViewById(R.id.Weather_textView_humidity_field);
        pressureField = (TextView)findViewById(R.id.Weather_textView_pressure_field);
        weatherIcon = (TextView)findViewById(R.id.Weather_textView_weather_icon);
        weatherIcon.setTypeface(weatherFont);



        // Set location button click event
        button_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getLocation();
            }
        });
    }

    // Ensure we have access to FINE_LOCATION
    private void requestLocationAccess() {

        // If we don't have access
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // Request it
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
            Location l = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            weather.placeIdTask asyncTask = new weather.placeIdTask(new weather.AsyncResponse() {
                public void processFinish(String weather_city, String weather_description, String weather_temperature,
                                          String weather_humidity, String weather_pressure, String weather_updatedOn,
                                          String weather_iconText, String sun_rise) {
                    cityField.setText(weather_city);
                    updatedField.setText(weather_updatedOn);
                    detailsField.setText(weather_description);
                    currentTemperatureField.setText(weather_temperature);
                    humidityField.setText("Humidity: "+weather_humidity);
                    pressureField.setText("Pressure: "+weather_pressure);
                    weatherIcon.setText(Html.fromHtml(weather_iconText)); //TODO DEPRECATED
                }
            });
            String latitude = String.valueOf(l.getLatitude());
            String longitude = String.valueOf(l.getLongitude());
            asyncTask.execute(latitude, longitude);
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override public void onLocationChanged(Location location) {
        getLocation();
    }

    @Override public void onProviderDisabled(String provider) {
        Toast.makeText(WeatherActivity.this, "Please enable GPS and Internet.", Toast.LENGTH_SHORT).show();
    }

    @Override public void onStatusChanged(String provider, int status, Bundle extras) {
        //
    }

    @Override public void onProviderEnabled(String provider) {
        //
    }




}
