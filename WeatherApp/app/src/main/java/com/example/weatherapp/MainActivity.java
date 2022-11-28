package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    EditText etCity, etCountry;
    TextView tvResult, tvResult2, tvResult3;
    Button btnGet;
    ImageView imgstate;
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "1fabc292552ddeafbc7932f59a1be2d1";
    DecimalFormat df = new DecimalFormat("#.#");

    //https://api.openweathermap.org/data/2.5/weather?q=Ankara&appid=1fabc292552ddeafbc7932f59a1be2d1
    //https://api.openweathermap.org/data/2.5/weather?lat=39.8620745&lon=32.7361142&appid=1fabc292552ddeafbc7932f59a1be2d1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);
        tvResult2 = findViewById(R.id.tvResult2);
        tvResult3 = findViewById(R.id.tvResult3);
        btnGet=findViewById(R.id.btnGet);
        imgstate=findViewById(R.id.imgstate);
    }

    public void getWeatherDetails(View view) {
        String tempurl = "";
        String city = etCity.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        if (city.equals("")) {
            Toast.makeText(getBaseContext(), "Şehir adı alanı boş geçilemez", Toast.LENGTH_SHORT).show();

        } else {
            if (!country.equals("")) {
                tempurl = url + "?q=" + city + "," + country + "&appid=" + appid;
            } else {
                tempurl = url + "?q=" + city + "&appid=" + appid;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Log.d("response",response);
                    String output = "";
                    String Temp="";
                    String Humidity="";
                    String Cloudiness="";
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityname = jsonResponse.getString("name");
                        Temp+=df.format(temp);
                        Humidity+=df.format(humidity);
                        Cloudiness+=clouds;
                        output += "Current weather of" + cityname + "(" + countryName + ")"
                                + "\n Temp: " + df.format(temp) + "C"
                                + "\n Fells like:" + df.format(feelsLike) + "C"
                                + "\n Humidity:" + humidity + "%"
                                + "\n Description:" + description
                                + "\n Wind Speed:" + wind + "m/s(meters per second)"
                                + "\n Cloudiness:" + clouds + "%"
                                + "\n Pressure:" + pressure + "hPa";
                        tvResult.setText("Sıcaklık: " +Temp+" C");
                        tvResult2.setText("Bulut Seviyesi: % "+ Cloudiness);
                        tvResult3.setText("Nem: % " +Humidity);
                        //tvResult3.setText(output);
                        if(temp>=25){
                            imgstate.setImageResource(R.drawable.sunweather);
                        }
                        else if(temp<=25){
                            imgstate.setImageResource(R.drawable.cloudy_icon2);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}