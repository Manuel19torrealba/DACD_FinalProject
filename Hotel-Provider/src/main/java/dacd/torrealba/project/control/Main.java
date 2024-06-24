package dacd.torrealba.project.control;


import dacd.torrealba.project.control.resource.HotelsInformation;
import dacd.torrealba.project.model.HotelInformation;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) {
        List<HotelInformation> hotelInformations = HotelsInformation.getHotels();

        HotelSupplier hotelSupplier = new XoteloHotelSupplier();
        HotelStore hotelStore = new HotelPublisher("tcp://localhost:61616", "prediction.Hotel");
        HotelController hotelController = new HotelController(hotelSupplier, hotelStore);


        Timer timer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                for (HotelInformation hotelInformation : hotelInformations) {
                    hotelController.execute(hotelInformation);
                }
            }
        };

        long delay = 0;
        long period = 12 * 60 * 60 * 1000;

        timer.scheduleAtFixedRate(task, delay, period);

    }
}