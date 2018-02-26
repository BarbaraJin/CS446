package com.ziniuyimeixiang.imanager;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

public class Weather extends AppCompatActivity implements Observer {

    private ImageView weatherIcon;
    private TextView temperature;
    private TextView highLowTemp;
    private TextView cityRegion;
    private TextView wind;
    private TextView humidity;
    private TextView visibility;
    private TextView sunset;
    private TextView sunrise;
    private Bitmap bitmapIcon;

    WeatherData weatherData = new WeatherData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
        temperature = (TextView) findViewById(R.id.currentTempValue);
        highLowTemp = (TextView) findViewById(R.id.highLowTempValue);
        cityRegion = (TextView) findViewById(R.id.locationText);
        wind = (TextView) findViewById(R.id.windValue);
        humidity = (TextView) findViewById(R.id.humidityValue);
        visibility = (TextView) findViewById(R.id.visibilityValue);
        sunset = (TextView) findViewById(R.id.sunsetValue);
        sunrise = (TextView) findViewById(R.id.sunriseValue);

        /* check if network is available */
        if (isNetworkConnected(Weather.this)){
            String defaultCity = weatherData.getDefaultCityRegion();
            doWeatherTask(defaultCity); // TODO add search location function
        }
        else{
            networkFailDialog(Weather.this).show();
        }

//        getCityFromSearchItem();
        // TODO get current location

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ((item.getItemId()) == R.id.app_location_search) {
            getCityFromSearchItem();
//            getsupportMenu
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * check if network is connected
     */
    public boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo lte = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((lte != null && lte.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public AlertDialog.Builder networkFailDialog(Context c) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(c);
        alertBuilder.setTitle("No Internet Connection");
        alertBuilder.setMessage("You need to connect wifi or open mobil data to access. Press OK to Exit.");

        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        return alertBuilder;
    }

    private void getCityFromSearchItem() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Weather.this);
        alertBuilder.setTitle("Change City");

        final EditText cityRegionInput = new EditText(Weather.this);
        cityRegionInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityRegionInput.setHint("Waterloo, ON");
        alertBuilder.setView(cityRegionInput);
        alertBuilder.setPositiveButton("set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String cityRegionString = cityRegionInput.getText().toString();

                /* before get json, check network*/
                if (isNetworkConnected(Weather.this)){
                    doWeatherTask(cityRegionString);
                }
                else{
                    networkFailDialog(Weather.this).show();
                }

            } 
        });
        alertBuilder.show();
    }

    public void doWeatherTask(String city){
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city});
    }

    private class loadIconTask extends AsyncTask<String, Void, Bitmap>{
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            weatherIcon.setImageBitmap(bitmap);
            //super.onPostExecute(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadIcon(strings[0]);
        }
    }

    private class WeatherTask extends AsyncTask<String, Void, WeatherData>{

        @Override
        protected WeatherData doInBackground(String... strings) {
            String JsonWeatherData = (getDataFromWeb(strings[0]));
            weatherData = parseJsonWeatherData(JsonWeatherData);
            int code = weatherData.getCurrentWeatherCode();
            String stringCode = Integer.toString(code);
            new loadIconTask().execute(stringCode);
            return weatherData;
        }

        @Override
        protected void onPostExecute(WeatherData weather) {
            super.onPostExecute(weather);
            cityRegion.setText(weather.getCity() + ", " + weather.getRegion());
            temperature.setText(weather.getCurrentTemp() + "°C");
            highLowTemp.setText("(" + weather.getHighTemp() + "°C — " + weather.getLowTemp() + "°C" +")");
            wind.setText(weather.getSpeed() + " mph");
            humidity.setText(weather.getHumidity() + " %");
            visibility.setText(weather.getVisibility() + " mi");
            sunset.setText(weather.getSunset());
            sunrise.setText(weather.getSunrise());

            // change image


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void update(Observable o, Object arg) {

    }

    private Bitmap downloadIcon(String code){
        String imageUrl = "http://l.yimg.com/a/i/us/we/52/"+ code + ".gif";
        try{
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    /**
     * get and read data from web
     */
    public String getDataFromWeb(String location){
        //init
        HttpURLConnection connection = null;

        //fixed
        String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")", location);
        String yahooEndpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

        try{
            //connect
            connection = (HttpURLConnection) (new URL(yahooEndpoint)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            //read data
            StringBuffer stringBuffer = new StringBuffer();
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;

            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line + "\r\n");
            }
            inputStream.close();
            connection.disconnect();
            return stringBuffer.toString();

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * parse json data
     */
    public WeatherData parseJsonWeatherData(String stringData){

        try{
            JSONObject data = new JSONObject(stringData);
            JSONObject query = data.getJSONObject("query");

            JSONObject channel = query.optJSONObject("results").optJSONObject("channel");

            WeatherData weatherData = new WeatherData();

            // get location
            JSONObject location = channel.optJSONObject("location");
            String city = location.optString("city");
            String country = location.optString("country");
            String region = location.optString("region");
            weatherData.setCity(city);
            weatherData.setCountry(country);
            weatherData.setRegion(region);

            // get wind
            JSONObject wind = channel.optJSONObject("wind");
            int direction = wind.optInt("direction");
            int speed = wind.optInt("speed");
            weatherData.setSpeed(speed);
            weatherData.setDirection(direction);

            // get atmosphere
            JSONObject atmosphere = channel.optJSONObject("atmosphere");
            int humidity = atmosphere.optInt("humidity");
            float pressure = (float) atmosphere.optDouble("pressure");
            float visibility = (float) atmosphere.optDouble("visibility");
            weatherData.setHumidity(humidity);
            weatherData.setPressure(pressure);
            weatherData.setVisibility(visibility);

            // get astronomy
            JSONObject astronomy = channel.optJSONObject("astronomy");
            String sunrise = astronomy.optString("sunrise");
            String sunset =  astronomy.optString("sunset");
            weatherData.setSunrise(sunrise);
            weatherData.setSunset(sunset);

            // get item
            JSONObject item = channel.optJSONObject("item");

            // get current condition
            JSONObject condition = item.optJSONObject("condition");
            int currentTemp = condition.optInt("temp");
            int currentTempInC = (int) convertFtoC(currentTemp);
            int currentWeatherCode = condition.optInt("code");
            weatherData.setCurrentTemp(currentTempInC);
            weatherData.setCurrentWeatherCode(currentWeatherCode);

            // get today temp
            JSONArray forecast = item.optJSONArray("forecast");
            JSONObject today = forecast.getJSONObject(0); // TODO json array may wrong
            int highTemp = today.optInt("high");
            int lowTemp = today.optInt("low");
            String weatherText = today.optString("text");

            int highTempInC = (int) convertFtoC(highTemp);
            int lowTempInC = (int) convertFtoC(lowTemp);
            weatherData.setHighTemp(highTempInC);
            weatherData.setLowTemp(lowTempInC);
            weatherData.setWeatherText(weatherText);

            return weatherData;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /* convert F to C (temperature) */
    public float convertFtoC(int f){
        float temperature = ((f-32)*5)/9;
        return temperature;
    }
}