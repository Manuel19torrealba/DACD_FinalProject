package dacd.torrealba.project.view;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class HotelInformation {
    private final Connection connection;

    public HotelInformation(String path) {
        String dbPath = path + File.separator + "/datamartSQL.db";
        this.connection = conectDB(dbPath);
    }

    public String extractHotelInformation(String island, String checkIn, String checkOut) {
        boolean dataAvailable = false;

        if (!tableExists(getTableNameHotel(island))) {
            return "No hay hoteles disponibles";
        }

        String sql = "SELECT h.Name, h.Location, h.Company, h.Price " +
                "FROM " + getTableNameHotel(island) + " h " +
                "JOIN (" +
                "    SELECT Name, MIN(Price) AS MinPrice " +
                "    FROM " + getTableNameHotel(island) +
                "    WHERE check_in = ? " +
                "    GROUP BY Name" +
                ") AS sub " +
                "ON h.Name = sub.Name AND h.Price = sub.MinPrice " +
                "WHERE h.check_in = ?" +
                "ORDER BY h.Price";
        StringBuilder bookingResult = new StringBuilder();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, checkIn);
            preparedStatement.setString(2, checkIn);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    return "No hay hoteles disponibles para esta fecha.";
                }
                while (resultSet.next()) {
                    dataAvailable = true;
                    String hotel = resultSet.getString("Name");
                    String location = resultSet.getString("Location");
                    String company = resultSet.getString("Company");
                    double price = resultSet.getDouble("Price");

                    LocalDate check_In = LocalDate.parse(checkIn);
                    LocalDate check_Out = LocalDate.parse(checkOut);
                    long duracion = ChronoUnit.DAYS.between(check_In, check_Out);

                    double totalPrice = price * duracion;

                    bookingResult.append(hotel)
                            .append(", Ubicacion: ").append(location)
                            .append(", Reserva: ").append(company)
                            .append(", Precio para ").append(duracion).append(" noches: ").append(totalPrice).append("\n");

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error processing booking data.");
        }

        if (!dataAvailable) {
            return "No hay hoteles disponibles para esta fecha.";
        }

        return String.valueOf(bookingResult);
    }

    private String getTableNameHotel(String island) {
        return "Hotel_" + island.replace(" ", "_");
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

    private boolean tableExists(String tableName) {
        try (ResultSet resultSet = connection.getMetaData().getTables(null, null, tableName, null)) {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
