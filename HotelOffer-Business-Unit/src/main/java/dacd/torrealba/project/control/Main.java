package dacd.torrealba.project.control;

import dacd.torrealba.project.view.InterfaceCLI;

public class Main {

    public static void main(String[] args) {
        SubscriberSupplier subscriberSupplier = new SubscriberBusinessUnit("tcp://localhost:61616",
                "prediction.Weather", "prediction.Hotel", "Hotel-Weather",
                new SQLBusinessUnitStore(args[0]));
        subscriberSupplier.start();

        new InterfaceCLI(args[0]).start();
    }
}