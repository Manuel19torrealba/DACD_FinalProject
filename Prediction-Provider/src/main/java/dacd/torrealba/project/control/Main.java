package dacd.torrealba.project.control;

import dacd.torrealba.project.control.resouerces.IslandsCoords;
import dacd.torrealba.project.model.Location;

import java.time.Instant;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {

        List<Location> locations = IslandsCoords.getLocations();

        WeatherSupplier weatherSupplier = new OpenWeatherMapSupplier(args[0], locations);
        WeatherStore weatherStore = new ActiveMQPublisher("tcp://localhost:61616", "prediction.Weather");
        WeatherController weatherController = new WeatherController(weatherSupplier, weatherStore);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                for (Location location : locations) {
                    weatherController.execute(location, Instant.now());
                }
            }
        };

        long delay = 0;
        long period = 6 * 60 * 60 * 1000;

        timer.scheduleAtFixedRate(task, delay, period);

    }
}