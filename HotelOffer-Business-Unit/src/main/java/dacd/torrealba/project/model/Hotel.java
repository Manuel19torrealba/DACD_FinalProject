package dacd.torrealba.project.model;

public class Hotel {
    private String name;
    private String island;
    private String location;
    private String chk_in;
    private String chk_out;
    private String reserve_company;
    private double price;

    public Hotel(String name, String island, String location, String chk_in, String chk_out,
                 String reserve_company, double price) {
        this.name = name;
        this.island = island;
        this.location = location;
        this.chk_in = chk_in;
        this.chk_out = chk_out;
        this.reserve_company = reserve_company;
        this.price = price;
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

    public String getChk_in() {
        return chk_in;
    }

    public String getChk_out() {
        return chk_out;
    }

    public String getReserve_company() {
        return reserve_company;
    }

    public double getPrice() {
        return price;
    }
}
