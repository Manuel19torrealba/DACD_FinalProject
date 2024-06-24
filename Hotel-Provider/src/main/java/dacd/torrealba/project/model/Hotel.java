package dacd.torrealba.project.model;

import java.time.Instant;

public class Hotel {
    private Instant ts;
    private String ss;
    private HotelInformation hotelInformation;
    private String chk_in;
    private String chk_out;
    private String reserve_company;
    private double price;

    public Hotel(HotelInformation hotelInformation, String chk_in,
                 String chk_out, String reserve_company, double price) {
        this.ts = Instant.now();
        this.ss = "hotel-provider";
        this.hotelInformation = hotelInformation;
        this.chk_in = chk_in;
        this.chk_out = chk_out;
        this.reserve_company = reserve_company;
        this.price = price;
    }

    public Instant getTs() {
        return ts;
    }

    public String getSs() {
        return ss;
    }

    public HotelInformation getHotelInformation() {
        return hotelInformation;
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
