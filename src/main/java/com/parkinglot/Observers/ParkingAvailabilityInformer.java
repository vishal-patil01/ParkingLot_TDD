package com.parkinglot.Observers;

import java.util.ArrayList;
import java.util.List;

public class ParkingAvailabilityInformer {
    static List<ParkingLotObservers> observersList;

    public ParkingAvailabilityInformer() {
        observersList = new ArrayList<>();
    }

    public void notifyParkingFull() {
        for (ParkingLotObservers observers : observersList)
            observers.setParkingLotFull();
    }

    public void notifyParkingAvailable() {
        for (ParkingLotObservers element : observersList)
            element.setParkingAvailable();
    }

    public void register(ParkingLotObservers observers) {
        observersList.add(observers);
    }
}
