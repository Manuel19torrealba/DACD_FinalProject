package dacd.torrealba.project.control;

import dacd.torrealba.project.model.Location;
import dacd.torrealba.project.model.Weather;

import java.time.Instant;
import java.util.List;

public class WeatherController {
    private final WeatherSupplier weatherSupplier;
    private final WeatherStore weatherStore;

    public WeatherController(WeatherSupplier weatherSupplier, WeatherStore weatherStore) {
        this.weatherSupplier = weatherSupplier;
        this.weatherStore = weatherStore;
    }

    public void execute(Location location, Instant ts) {
        List<Weather> weatherList = weatherSupplier.getWeather(location, ts);

        if (weatherList == null) {
            System.out.println("La lista de weather es null. Verifique la inicialización.");
            return; // O maneja el error de acuerdo a tu lógica
        }

        for (Weather weather: weatherList) {
            weatherStore.save(weather);
        }

        System.out.println("Predicciones del Weather de " + location.getIsland() + " guardadas en ActiveMQ");
        System.out.println("--------------------------------------------------");
    }
}
