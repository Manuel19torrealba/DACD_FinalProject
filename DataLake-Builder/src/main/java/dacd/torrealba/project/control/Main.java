package dacd.torrealba.project.control;

public class Main {
    public static void main(String[] args) {
        DataLake weatherStore = new DataLakeBuilder(args[0], "prediction.Weather");
        SubscriberSupplier subscriberWeatherSupplier = new Subscriber("tcp://localhost:61616",
                "prediction.Weather", "weather-provider", weatherStore);
        subscriberWeatherSupplier.start();

        DataLake hotelStore = new DataLakeBuilder(args[0], "prediction.Hotel");
        SubscriberSupplier subscriberHotelSupplier = new Subscriber("tcp://localhost:61616",
                "prediction.Hotel", "hotel-provider", hotelStore);
        subscriberHotelSupplier.start();
    }
}