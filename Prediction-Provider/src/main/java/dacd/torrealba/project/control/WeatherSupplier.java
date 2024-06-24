package dacd.torrealba.project.control;

import dacd.torrealba.project.model.Location;
import dacd.torrealba.project.model.Weather;

import java.time.Instant;
import java.util.List;

public interface WeatherSupplier {
    List<Weather> getWeather(Location location, Instant ts);
}
