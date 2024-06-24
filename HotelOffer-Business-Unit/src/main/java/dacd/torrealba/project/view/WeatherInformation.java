package dacd.torrealba.project.view;

import java.io.File;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class WeatherInformation {
    private final Connection connection;

    public WeatherInformation(String path, String island) {
        String dbPath = path + File.separator + "/datamartSQL.db";
        this.connection = conectDB(dbPath);

        if (validIslandName(island)) {
            extracInformation(island);
        } else {
            throw new IllegalArgumentException("El nombre de la isla '" + island + "' no es válido.");
        }

    }

    private void extracInformation(String island) {
        String sql = "SELECT Date, Location, Temperature, Rain, Humidity, Wind " +
                "FROM " + getTableNameWeather(island);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String date = resultSet.getString("Date");
                    String location = resultSet.getString("Location");
                    double temp = resultSet.getDouble("Temperature");
                    double rain = resultSet.getDouble("Rain");
                    int humidity = resultSet.getInt("Humidity");
                    double wind = resultSet.getDouble("Wind");

                    System.out.println(informationWeather(date, location, temp, rainProb(rain), evalHumidity(humidity), windSpeed(wind)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error processing weather data.");
        }
    }



    private String informationWeather(String date, String location, double temp, String rain, String humidity, String wind) {
        return "Temperatura: " + temp + "°C" +
                ", con una " + rain +
                ", con " + humidity +
                " y " + wind;
    }

    private Connection conectDB(String Path) {
        try {
            String DB = "jdbc:sqlite:" + Path;
            Connection con = DriverManager.getConnection(DB);
            return con;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String rainProb(double rain) {
        if (rain == 0.0) {
            return "Probabilidad de lluvia muy baja o nula";
        } else if (rain <= 0.2) {
            return "Probabilidad de lluvia baja";
        } else if (rain <= 0.5) {
            return "Probabilidad de lluvia moderada";
        } else if (rain <= 0.8) {
            return "Probabilidad de lluvia alta";
        } else {
            return "Probabilidad de lluvia muy alta";
        }
    }

    private String windSpeed(double wind) {
        if (wind <= 10.0) {
            return "Viento bajo";
        } else if (wind >= 10.0 && wind < 20.0) {
            return "Viento moderado";
        } else if (wind >= 20.0 && wind <= 40.0) {
            return "Viento alto";
        } else {
            return "Viento muy alto";
        }
    }

    private String evalHumidity(int humidity) {
        if (humidity < 40) {
            return "Humedad buena";
        } else if (humidity < 60) {
            return "Humedad media";
        } else {
            return "Humedad alta";
        }
    }



    private String getTableNameWeather(String island) {
        return "Weather_" + island.replace(" ", "_");
    }

    private boolean validIslandName(String island) {
        List<String> islands = Arrays.asList("Las Palmas", "Tenerife", "La Palma", "La Gomera",
                "El Hierro", "Lanzarote", "Fuerteventura", "La Graciosa");
        return islands.contains(island);
    }
}
