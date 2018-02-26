package com.ziniuyimeixiang.imanager;

/**
 * Created by j_mei on 2018-02-25.
 */

public class WeatherData extends Model {

    String city;
    String country;
    String region;

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

    String defaultCityRegion;


    public WeatherData() {
        defaultCityRegion = "Waterloo, ON";
    }

    public String getDefaultCityRegion() {
        return defaultCityRegion;
    }

    public void setDefaultCityRegion(String defaultCityRegion) {
        this.defaultCityRegion = defaultCityRegion;
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
}
