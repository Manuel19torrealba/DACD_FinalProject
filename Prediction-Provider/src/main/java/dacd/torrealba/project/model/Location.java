package dacd.torrealba.project.model;

public class Location {
    private String island;
    private double lat;
    private double lon;

    public Location(String island, double lat, double lon){
        this.island = island;
        this.lat = lat;
        this.lon = lon;
    }

    public String getIsland() {
        return island;
    }

    public void setIsland(String island) {
        this.island = island;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}