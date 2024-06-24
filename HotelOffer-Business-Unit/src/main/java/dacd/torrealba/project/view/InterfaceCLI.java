package dacd.torrealba.project.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InterfaceCLI {
    private Scanner scan;
    private HotelInformation hotelInformation;
    private WeatherInformation weatherInformation;
    private static String path;


    public InterfaceCLI(String path) {
        this.path = path;
        this.scan = new Scanner(System.in);
        this.hotelInformation = new HotelInformation(path);

    }

    public void start() {
        System.out.println("Bienvenido al Buscador de los mejores precios en hoteles en las Islas Canarias");

        System.out.print("Indique el nombre de la isla a la que desea viajar (Tenerife/La Palma/La Gomera/El Hierro/Las Palmas/Fuerteventura/Lanzarote/La Graciosa): ");
        String selectedIsland = scan.nextLine();

        System.out.println("En " + selectedIsland + " los proximos 5 dias hara: ");
        new WeatherInformation(path, selectedIsland);

        String checkInDate;
        while (true) {
            System.out.print("Introduzca su fecha de llegada para los proximos 5 dias (yyyy-MM-dd): ");
            checkInDate = scan.nextLine();
            if (isValidCheckInDate(checkInDate)) {
                System.out.print("Introduzca hasta que fecha de salida (yyyy-MM-dd): ");
                String checkOutDate = scan.nextLine();
                System.out.println(hotelInformation.extractHotelInformation(selectedIsland, checkInDate, checkOutDate));
                break;
            } else {
                System.out.println("Fecha inválida. Debe ser entre mañana y los próximos 5 días.");
                break;
            }
        }


    }

    private boolean isValidCheckInDate(String checkInDate) {
        try {
            LocalDate checkIn = LocalDate.parse(checkInDate, DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            LocalDate maxDate = today.plusDays(5);
            return !checkIn.isBefore(tomorrow) && !checkIn.isAfter(maxDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static void main(String[] args)  {
        InterfaceCLI cli = new InterfaceCLI(path);
        cli.start();
    }
}

