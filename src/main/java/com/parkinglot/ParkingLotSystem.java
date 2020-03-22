package com.parkinglot;

import java.util.ArrayList;
import java.util.List;

public class ParkingLotSystem {
    private int actualCapacity;
    private List<ParkingLotObservers> observersList;
    private List vehicles;

    public ParkingLotSystem(int actualCapacity) {
        this.actualCapacity = actualCapacity;
        this.vehicles = new ArrayList();
        this.observersList = new ArrayList<>();
    }

    public void setActualCapacity(int capacity) {
        this.actualCapacity = capacity;
    }

    public void park(Object vehicle) throws ParkingLotException {
        if (isVehicleParked(vehicle))
            throw new ParkingLotException("vehicle already parked",ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED);
        if (vehicles.size()==actualCapacity) {
            for (ParkingLotObservers observer : observersList)
                observer.setCapacityFull();
            throw new ParkingLotException("parkinglot is full",ParkingLotException.ExceptionTypes.PARKING_LOT_FULL);
        }
        this.vehicles.add(vehicle);
    }

    public boolean unPark(Object vehicle) {
        if (this.vehicles.contains(vehicle)) {
            this.vehicles.remove(vehicle);
            return true;
        }
        return false;
    }

    public boolean isVehicleParked(Object vehicle) {
        return this.vehicles.contains(vehicle);
    }

    public void registerObserver(ParkingLotObservers observer) {
        observersList.add(observer);
    }
}
