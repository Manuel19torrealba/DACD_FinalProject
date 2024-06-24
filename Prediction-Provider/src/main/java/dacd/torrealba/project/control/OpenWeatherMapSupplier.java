package dacd.torrealba.project.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dacd.torrealba.project.model.Location;
import dacd.torrealba.project.model.Weather;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherMapSupplier implements WeatherSupplier {
    private final String apiKey;
    private final List<Location> locations;

    public OpenWeatherMapSupplier(String apiKey, List<Location> locations) {
        this.apiKey = apiKey;
        this.locations = locations;
    }


    @Override
    public List<Weather> getWeather(Location location, Instant ts) {
        String url = buildApiUrl(location);

        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet =new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                return parseJsonResponse(jsonResponse);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return null;
    }

    private List<Weather> parseJsonResponse(String jsonResponse) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        JsonObject cityObject = jsonObject.getAsJsonObject("city");
        double lat = cityObject.getAsJsonObject("coord").get("lat").getAsDouble();
        double lon = cityObject.getAsJsonObject("coord").get("lon").getAsDouble();
        String islandName = findIslandName(lat, lon);

        JsonArray weather = jsonObject.getAsJsonArray("list");
        List<Weather> weatherList = new ArrayList<>();

        for (JsonElement element : weather) {
            JsonObject listObject = element.getAsJsonObject();
            long dt = listObject.get("dt").getAsLong();

            if (isMidNight(Instant.ofEpochSecond(dt))) {
                JsonObject mainObject = listObject.getAsJsonObject("main");
                double temperature = mainObject.get("temp").getAsDouble();
                double wind = listObject.getAsJsonObject("wind").get("speed").getAsDouble();
                int humidity = mainObject.get("humidity").getAsInt();
                double rain = listObject.get("pop").getAsDouble();

                weatherList.add(new Weather("weather-provider", Instant.ofEpochSecond(dt),
                        new Location(islandName, lat, lon), rain, temperature, wind, humidity));
            }
        }
        return weatherList;
    }

    private String buildApiUrl(Location location) {
        return "https://api.openweathermap.org/data/2.5/forecast?lat=" + location.getLat() +
                "&lon=" + location.getLon() + "&appid=" + apiKey + "&units=metric";
    }

    private String findIslandName(double lat, double lon) {
        for (Location location : locations) {
            if (location.getLat() == lat && location.getLon() == lon) {
                return location.getIsland();
            }
        }
        return null;
    }

    private boolean isMidNight(Instant ts) {
        LocalDateTime localDateTime = ts.atZone(ZoneId.of("UTC")).toLocalDateTime();
        return localDateTime.getHour() == 0 && localDateTime.getMinute() == 0 && localDateTime.getSecond() == 0;
    }
}
