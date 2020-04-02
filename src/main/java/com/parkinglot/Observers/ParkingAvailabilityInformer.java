package com.parkinglot.Observers;

import java.util.ArrayList;
import java.util.List;

public class ParkingAvailabilityInformer {
    List<ParkingLotObservers> observersList;
    static ParkingAvailabilityInformer instance;

    public static ParkingAvailabilityInformer getInstance() {
        if (instance == null)
            instance = new ParkingAvailabilityInformer();
        return instance;
    }

    private ParkingAvailabilityInformer() {
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
