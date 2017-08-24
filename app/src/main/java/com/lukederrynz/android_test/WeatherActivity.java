package com.lukederrynz.android_test;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;

import android.webkit.ConsoleMessage;
import android.widget.EditText;
import android.widget.TextView;


public class WeatherActivity extends AppCompatActivity {

    private EditText editText_City;
    private String url;

    private TextView cityField, detailsField, currentTemperatureField, humidityField, pressureField, weatherIcon, updatedField;
    private Typeface weatherFont;

    //region On Events

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initializeControls();
    }

    //endregion

    private void initializeControls() {

        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        cityField = (TextView)findViewById(R.id.city_field);
        updatedField = (TextView)findViewById(R.id.updated_field);
        detailsField = (TextView)findViewById(R.id.details_field);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        humidityField = (TextView)findViewById(R.id.humidity_field);
        pressureField = (TextView)findViewById(R.id.pressure_field);
        weatherIcon = (TextView)findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);

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
                weatherIcon.setText(Html.fromHtml(weather_iconText)); //TODO DEPRICATED
                //weatherIcon.setText(Html.fromHtml(weather_iconText, 0));

            }
        });
        asyncTask.execute("25.180000", "89.530000");
    }

}
