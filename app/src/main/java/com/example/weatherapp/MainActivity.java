package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView cityNameText, weatherInfoText;
    Button displayButton;

    public class DownloadTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while(data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            }
            catch (Exception e){
                e.printStackTrace();
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                String description = null, temperature = null, humidity = null, info = "";
                JSONObject jsonObject = new JSONObject(s);
                info = jsonObject.getString("weather");
                JSONArray weatherInfoArray = new JSONArray(info);
                for(int i=0; i<weatherInfoArray.length(); i++){
                    JSONObject object1 = weatherInfoArray.getJSONObject(i);
                    description = object1.getString("description");
                }

                info = "[" + jsonObject.getString("main") + "]";
                JSONArray mainInfoArray = new JSONArray(info);
                for(int i=0; i<mainInfoArray.length(); i++){
                    JSONObject object2 = mainInfoArray.getJSONObject(i);
                    temperature = object2.getString("temp");
                    humidity = object2.getString("humidity");
                }

                char firstLetter = description.charAt(0);
                firstLetter = (char) ((int)firstLetter - 32);
                String infoText = firstLetter + description.substring(1) + ".\nTemperature: " + temperature + "°C\nHumidity: " + humidity + "%";
                weatherInfoText.setText(infoText);
                Log.i("entered", description);

                Log.i("temperature", temperature);
                Log.i("humidity", humidity);
                Log.i("finalString", infoText);




            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Enter a proper city name", Toast.LENGTH_SHORT).show();
                weatherInfoText.setText("");
            }

        }
    }

    public void buttonClicked(View view){
        String cityName = cityNameText.getText().toString();
        DownloadTask task = new DownloadTask();
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        try{
            task.execute("https://openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=439d4b804bc8187953eb36d2a8c26a02");
        }
        catch (Exception e){

        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityNameText = findViewById(R.id.cityNameText);
        weatherInfoText = findViewById(R.id.weatherInfoText);
        displayButton = findViewById(R.id.displayButton);

    }
}
