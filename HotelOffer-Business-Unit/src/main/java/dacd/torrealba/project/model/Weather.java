package dacd.torrealba.project.model;

import java.time.Instant;

public class Weather {
    private Instant ts;
    private String location;
    private double temp;

    private double rain;
    private double wind;
    private int humidity;

    public Weather(Instant ts, String location, double temp, double rain, double wind, int humidity) {
        this.ts = ts;
        this.location = location;
        this.temp = temp;
        this.rain = rain;
        this.wind = wind;
        this.humidity = humidity;
    }

    public Instant getTs() {
        return ts;
    }

    public String getLocation() {
        return location;
    }

    public double getTemp() {
        return temp;
    }

    public double getRain() {
        return rain;
    }

    public double getWind() {
        return wind;
    }

    public int getHumidity() {
        return humidity;
    }
}
