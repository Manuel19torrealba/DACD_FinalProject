package dacd.torrealba.project.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import dacd.torrealba.project.model.Hotel;
import dacd.torrealba.project.model.Weather;

import java.io.File;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SQLBusinessUnitStore implements BusinessUnitStore {
    private final String path;
    private final Connection connection;

    public SQLBusinessUnitStore(String path) {
        try {
            this.path = path;
            String dbPath = path + File.separator + "/datamartSQL.db";
            new File(dbPath).getParentFile().mkdirs();
            this.connection = conectDB(dbPath);
            this.connection.setAutoCommit(false);
            System.out.println("Conexión a la base de datos establecida.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(String string) {
        try {

            JsonObject object = JsonParser.parseString(string).getAsJsonObject();
            String topic = object.get("ss").getAsString();
            if (topic.equals("weather-provider")) {
                insertTableWeather(string);
            } else {
                insertTableHotel(string);
            }
            connection.commit();
        } catch (JsonSyntaxException | SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    private Connection conectDB(String Path) {
        try {
            String DB = "jdbc:sqlite:" + Path;
            Connection connection1 = DriverManager.getConnection(DB);
            return connection1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Weather constructWeatherObject(String jsonData) {
        JsonObject object = new Gson().fromJson(jsonData, JsonObject.class);
        Instant ts = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(object.get("predictionTime").getAsString()));
        String location = object.getAsJsonObject("location").get("island").getAsString();
        double temp = object.get("temp").getAsDouble();
        double rain = object.get("rain").getAsDouble();
        double wind = object.get("temp").getAsDouble();
        int humidity = object.get("humidity").getAsInt();
        return new Weather(ts, location, temp, rain, wind, humidity);
    }

    private void insertTableWeather(String jsonString) {
        Weather weatherData = constructWeatherObject(jsonString);

        String names = getTableNameWeather(weatherData.getLocation());
        createTableWeatherIfNotExits(names);

        String selectQuery = "SELECT * FROM " + names + " WHERE Date = ?";

        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            selectStatement.setString(1, dateString(weatherData.getTs()));
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String updateQuery = "UPDATE " + names + " SET Temperature = ?, Rain = ?, " +
                        "Humidity = ?, Wind = ? WHERE Date = ?";

                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setDouble(1, weatherData.getTemp());
                    updateStatement.setDouble(2, weatherData.getRain());
                    updateStatement.setInt(3, weatherData.getHumidity());
                    updateStatement.setDouble(4, weatherData.getWind());
                    updateStatement.setString(5, dateString(weatherData.getTs()));
                    updateStatement.executeUpdate();
                }
            } else {

                String insertQuery = "INSERT INTO " + names + " (Date, Location, Temperature, " +
                        "Rain, Humidity, Wind) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                    insertStatement.setString(1, dateString(weatherData.getTs()));
                    insertStatement.setString(2, weatherData.getLocation());
                    insertStatement.setDouble(3, weatherData.getTemp());
                    insertStatement.setDouble(4, weatherData.getRain());
                    insertStatement.setInt(5, weatherData.getHumidity());
                    insertStatement.setDouble(6, weatherData.getWind());
                    insertStatement.executeUpdate();
                }
            }
            deleteOldPredictions(names);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void createTableWeatherIfNotExits(String nameTable) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + nameTable + " (" +
                "Date TEXT," +
                "Location TEXT," +
                "Temperature REAL," +
                "Rain REAL," +
                "Humidity INTEGER," +
                "Wind REAL)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteOldPredictions(String tableName) {
        String deleteQuery = "DELETE FROM " + tableName + " WHERE Date < ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setString(1, LocalDate.now().toString());
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Hotel constructHotelObject(String jsonData) {
        JsonObject object = new Gson().fromJson(jsonData, JsonObject.class);
        String name = object.getAsJsonObject("hotelInformation").get("name").getAsString();
        String island = object.getAsJsonObject("hotelInformation").get("island").getAsString();
        String location = object.getAsJsonObject("hotelInformation").get("location").getAsString();
        String chk_in = object.get("chk_in").getAsString();
        String chk_out = object.get("chk_out").getAsString();
        String reserve_company = object.get("reserve_company").getAsString();
        double price = object.get("price").getAsDouble();
        return new Hotel(name, island, location, chk_in, chk_out, reserve_company, price);
    }
    private void insertTableHotel(String jsonString) {
        Hotel hotelData = constructHotelObject(jsonString);

        String location = hotelData.getIsland();
        String names = getTableNameHotel(location);
        createTableHotelIfNotExits(names);

        String insertQuery = "INSERT INTO " + names + " (Name, Check_in, Check_out, " +
                "Company, Price, Location, Island) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setString(1, hotelData.getName());
            insertStatement.setString(2, hotelData.getChk_in());
            insertStatement.setString(3, hotelData.getChk_out());
            insertStatement.setString(4, hotelData.getReserve_company());
            insertStatement.setDouble(5, hotelData.getPrice());
            insertStatement.setString(6, hotelData.getLocation());
            insertStatement.setString(7, hotelData.getIsland());
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        deleteOldHotelEntries(names);
    }

    private void deleteOldHotelEntries(String tableName) {
        String deleteQuery = "DELETE FROM " + tableName + " WHERE Check_in <= ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setString(1, LocalDate.now().toString());
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTableHotelIfNotExits(String nameTable) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + nameTable + " (" +
                "Name TEXT," +
                "Check_in TEXT," +
                "Check_out TEXT," +
                "Company TEXT," +
                "Price REAL," +
                "Location TEXT," +
                "Island TEXT)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public String dateString(Instant ts) {
        LocalDateTime localDate = ts.atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(format);
    }
    private String getTableNameWeather(String island) {
        return "Weather_" + island.replace(" ", "_");
    }

    private String getTableNameHotel(String island) {
        return "Hotel_" + island.replace(" ", "_");
    }

}

