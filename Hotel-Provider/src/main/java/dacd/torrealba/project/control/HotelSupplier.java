package dacd.torrealba.project.control;

import dacd.torrealba.project.model.Hotel;
import dacd.torrealba.project.model.HotelInformation;

import java.util.List;

public interface HotelSupplier {
    List<Hotel> getHotel(HotelInformation hotelInformation);
}
