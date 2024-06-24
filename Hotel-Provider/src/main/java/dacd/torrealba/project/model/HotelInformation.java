package dacd.torrealba.project.model;

public class HotelInformation {
    private String name;
    private String island;
    private String location;
    private String key;

    public HotelInformation(String name, String island, String location, String key) {
        this.name = name;
        this.island = island;
        this.location = location;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getIsland() {
        return island;
    }

    public String getLocation() {
        return location;
    }

    public String getKey() {
        return key;
    }
}
