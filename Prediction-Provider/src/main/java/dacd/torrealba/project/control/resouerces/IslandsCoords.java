package dacd.torrealba.project.control.resouerces;

import dacd.torrealba.project.model.Location;

import java.util.ArrayList;
import java.util.List;

public class IslandsCoords {
    public static List<Location> getLocations() {
        List<Location> locations = new ArrayList<>();
        locations.add(new Location("La Palma", 28.65, -17.91));
        locations.add(new Location("La Gomera", 28.08, -17.33));
        locations.add(new Location("El Hierro", 27.75, -18.01));
        locations.add(new Location("Tenerife", 28.46, -16.24));
        locations.add(new Location("Las Palmas", 28.13, -15.43));
        locations.add(new Location("Fuerteventura", 28.50, -13.86));
        locations.add(new Location("Lanzarote", 28.96, -13.55));
        locations.add(new Location("La Graciosa", 29.23, -13.50));
        return locations;
    }
}
