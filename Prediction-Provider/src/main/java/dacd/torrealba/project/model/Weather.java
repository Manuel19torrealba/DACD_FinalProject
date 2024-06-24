package dacd.torrealba.project.model;

import java.time.Instant;

public class Weather {
    private Instant ts;
    private String ss;
    private Instant predictionTime;
    private Location location;
    private double rain;
    private double temp;
    private double wind;
    private int humidity;

    public Weather(String ss, Instant predictionTime, Location location, double rain, double temp, double wind, int humidity) {
        this.ts = Instant.now();
        this.ss = ss;
        this.predictionTime = predictionTime;
        this.location = location;
        this.rain = rain;
        this.temp = temp;
        this.wind = wind;
        this.humidity = humidity;
    }

    public Instant getTs() {
        return ts;
    }

    public void setTs(Instant ts) {
        this.ts = ts;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Instant getPredictionTime() {
        return predictionTime;
    }

    public void setPredictionTime(Instant predictionTime) {
        this.predictionTime = predictionTime;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }
}
