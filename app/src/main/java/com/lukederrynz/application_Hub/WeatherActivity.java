package com.lukederrynz.application_Hub;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
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

import com.lukederrynz.android_test.R;


/**
 * Created by Luke Derry on 1/09/2017
 *
 * Works on actual devices. Crashes emulator.
 */
public class WeatherActivity extends AppCompatActivity implements LocationListener {

    private TextView cityField, detailsField, currentTemperatureField, humidityField, pressureField, weatherIcon, updatedField, textView_Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initializeControls();
    }


    /**
     * Initialize our UI elements.
     *
     */
    private void initializeControls() {

        // Request location
        requestLocationAccess();

        Typeface weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        cityField = (TextView)findViewById(R.id.Weather_textView_city_field);
        updatedField = (TextView)findViewById(R.id.Weather_textView_updated_field);
        detailsField = (TextView)findViewById(R.id.Weather_textView_details_field);
        currentTemperatureField = (TextView)findViewById(R.id.Weather_textView_current_temperature_field);
        humidityField = (TextView)findViewById(R.id.Weather_textView_humidity_field);
        pressureField = (TextView)findViewById(R.id.Weather_textView_pressure_field);
        weatherIcon = (TextView)findViewById(R.id.Weather_textView_weather_icon);
        weatherIcon.setTypeface(weatherFont);

        // Set location button click event
        Button button_getLocation = (Button)findViewById(R.id.Weather_button_getWeather);
        button_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getWeather();
            }
        });
    }


    /**
     * Ensure we have access to FINE_LOCATION.
     *
     */
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


    /**
     * Retrieve the weather.
     *
     */
    private void getWeather() {

        // If we still do not have location access we should quit.
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Unable to Retrieve Weather", Toast.LENGTH_SHORT).show();
            finish();
        }

        try {
            LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
            Location l = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            // New async task Get fields
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


    /**
     *
     * @param location - Location : The detected location
     */
    @Override public void onLocationChanged(Location location) {
        getWeather();
    }


    /**
     * If GPS or internet is disabled.
     *
     * @param provider - String : Our provider
     */
    @Override public void onProviderDisabled(String provider) {
        Toast.makeText(WeatherActivity.this, "Please enable GPS and Internet.", Toast.LENGTH_SHORT).show();
    }


    /**
     * If the Status changes.
     *
     * @param provider - String : Our provider
     * @param status - int : It's status
     * @param extras - Bundle : Unknown
     */
    @Override public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO: Complete me.
    }


    /**
     *
     * @param provider - String : Our provider
     */
    @Override public void onProviderEnabled(String provider) {
        // TODO: Complete me.
    }




}
