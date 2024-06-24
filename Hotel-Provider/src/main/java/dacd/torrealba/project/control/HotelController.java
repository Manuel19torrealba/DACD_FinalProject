package dacd.torrealba.project.control;

import dacd.torrealba.project.model.Hotel;
import dacd.torrealba.project.model.HotelInformation;

import java.util.List;

public class HotelController {
    private final HotelSupplier hotelSupplier;
    private final HotelStore hotelStore;

    public HotelController(HotelSupplier hotelSupplier, HotelStore hotelStore) {
        this.hotelSupplier = hotelSupplier;
        this.hotelStore = hotelStore;
    }

    public void execute(HotelInformation hotelInformation) {
        List<Hotel> hotelList = hotelSupplier.getHotel(hotelInformation);

        for (Hotel hotel: hotelList) {
            hotelStore.save(hotel);
        }

        System.out.println("Predicciones de los Hoteles de " + hotelInformation.getIsland() + " guardadas en ActiveMQ");
        System.out.println("--------------------------------------------------");
    }
}
