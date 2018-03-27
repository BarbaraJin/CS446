package com.ziniuyimeixiang.imanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import com.google.gson.JsonParser;
import com.google.gson.JsonSerializer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by j_mei on 2018-02-25.
 */

public class WeatherData extends Model {

    String city;
    String country;
    String region;
    String defaultCityRegion;
    String currentLocation;

    int direction;
    int speed ;

    int humidity;
    float pressure;
    float visibility;

    String sunrise;
    String sunset;

    int currentTemp;
    int currentWeatherCode;
    int highTemp;
    int lowTemp;
    String weatherText;

    Bitmap weatherIcon;

    Context context;

    /**
     * create instance
     */

    private static final WeatherData ourInstance = new WeatherData();
    static WeatherData getInstance()
    {
        return ourInstance;
    }

    /**
     * Constructor, getter and setter
     */
    public WeatherData() {
    }

    public void setWeatherInfo(){
        readWeatherSettingJson();
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{currentLocation});
    }

    public String getDefaultCityRegion() {
        return defaultCityRegion;
    }

    public Bitmap getWeatherIcon() {
        return weatherIcon;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setWeatherIcon(Bitmap weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public void setDefaultCityRegion(String defaultCityRegion) {
        this.defaultCityRegion = defaultCityRegion;
        updateJsonFile_defaultCityRegion();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getVisibility() {
        return visibility;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(int currentTemp) {
        this.currentTemp = currentTemp;
    }

    public int getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(int highTemp) {
        this.highTemp = highTemp;
    }

    public int getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(int lowTemp) {
        this.lowTemp = lowTemp;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }

    public int getCurrentWeatherCode() {
        return currentWeatherCode;
    }

    public void setCurrentWeatherCode(int currentWeatherCode) {
        this.currentWeatherCode = currentWeatherCode;
    }


    /**
     * other function
     */


    /* read data from weather setting json */
    @SuppressWarnings("unchecked")
    private void readWeatherSettingJson() {
        JsonParser parser = new JsonParser();

        try{
            String filePath = context.getFilesDir()+"/assets/weatherSetting.json";
            File file = new File(filePath);
            Object obj = parser.parse(new FileReader(file));
            String temp = obj.toString();
            JSONObject object = new JSONObject(temp);
//            JSONObject object = (JSONObject) obj;
//            JSONObject location = channel.optJSONObject("location");
            defaultCityRegion = object.optString("defaultCityRegion");
//            defaultCityRegion = object.getString("defaultCityRegion");
        } catch(FileNotFoundException e){
            createWeatherSettingJsonFile();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /* if not find json file, then create one */
    private void createWeatherSettingJsonFile() {
        try{
            String fileName = "weatherSetting.json";
//            File file = new File("weatherSetting.json");
//            file.createNewFile();
//            FileWriter writer = new FileWriter(file);
            JSONObject obj = new JSONObject();
            obj.put("defaultCityRegion","Toronto,ON");
            defaultCityRegion = "Toronto,ON";
//            writer.write(obj.toString());
//            writer.flush();
            wrtieFileOnInternalStorage(fileName,obj.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void updateJsonFile_defaultCityRegion() {
        JsonParser parser = new JsonParser();
        try{
            String filePath = context.getFilesDir()+"/assets/weatherSetting.json";
            File file = new File(filePath);
            Object obj = parser.parse(new FileReader(file));

            String temp = obj.toString();
            JSONObject jsonObject = new JSONObject(temp);

//            JSONObject jsonObject = (JSONObject) obj;
            jsonObject.put("defaultCityRegion", defaultCityRegion);

            FileWriter writer = new FileWriter(file);
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void wrtieFileOnInternalStorage(String sFileName, String sBody){
        File file = new File(context.getFilesDir(),"assets");
        if(!file.exists()){
            file.mkdir();
        }

        try{
            File gpxfile = new File(file, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.write(sBody);
            writer.flush();
            writer.close();

        }catch (Exception e){
            e.printStackTrace();

        }
    }


    /**
     * get and read data from web
     */
    public String getDataFromWeb(String location){

        currentLocation = location;
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
    public void parseJsonWeatherData(String stringData){

        try{
            JSONObject data = new JSONObject(stringData);
            JSONObject query = data.getJSONObject("query");

            JSONObject channel = query.optJSONObject("results").optJSONObject("channel");

            // get location
            JSONObject location = channel.optJSONObject("location");
            city = location.optString("city");
            country = location.optString("country");
            region = location.optString("region");

            // get wind
            JSONObject wind = channel.optJSONObject("wind");
            direction = wind.optInt("direction");
            speed = wind.optInt("speed");

            // get atmosphere
            JSONObject atmosphere = channel.optJSONObject("atmosphere");
            humidity = atmosphere.optInt("humidity");
            pressure = (float) atmosphere.optDouble("pressure");
            visibility = (float) atmosphere.optDouble("visibility");

            // get astronomy
            JSONObject astronomy = channel.optJSONObject("astronomy");
            sunrise = astronomy.optString("sunrise");
            sunset =  astronomy.optString("sunset");

            // get item
            JSONObject item = channel.optJSONObject("item");

            // get current condition
            JSONObject condition = item.optJSONObject("condition");
            int currentTempinF = condition.optInt("temp");
            currentTemp = (int) convertFtoC(currentTempinF);
            currentWeatherCode = condition.optInt("code");

            // get today temp
            JSONArray forecast = item.optJSONArray("forecast");
            JSONObject today = forecast.getJSONObject(0); // TODO json array may wrong
            int highTempInF = today.optInt("high");
            int lowTempInF = today.optInt("low");
            weatherText = today.optString("text");

            highTemp = (int) convertFtoC(highTempInF);
            lowTemp = (int) convertFtoC(lowTempInF);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /* convert F to C (temperature) */
    public float convertFtoC(int f){
        float temperature = ((f-32)*5)/9;
        return temperature;
    }

    /**
     * get weather icon from web
     * @param code
     * @return
     */
    public Bitmap downloadIcon(String code){
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
     * get weather data at the back, and change data on UI
     */


    private class WeatherTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            String JsonWeatherData = (getDataFromWeb(strings[0]));
            parseJsonWeatherData(JsonWeatherData);
            int code = getCurrentWeatherCode();
            String stringCode = Integer.toString(code);
            weatherIcon = downloadIcon(stringCode);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setChanged();
            notifyObservers();
        }


    }


}


