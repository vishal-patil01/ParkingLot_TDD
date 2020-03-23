package com.parkinglot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLotSystem {
    private int actualCapacity;
    private List<ParkingLotObservers> observersList;
    public List vehicles;
    private int slot = 0;

    public ParkingLotSystem(int actualCapacity) {
        setActualCapacity(actualCapacity);
        this.observersList = new ArrayList<>();
    }

    public int initializeParkingLot() {
        this.vehicles = new ArrayList();
        IntStream.range(0, this.actualCapacity).forEach(slots -> vehicles.add(null));
        return vehicles.size();
    }

    public void setActualCapacity(int capacity) {
        this.actualCapacity = capacity;
        initializeParkingLot();
    }

    public void park(Object vehicle) throws ParkingLotException {
        if (isVehicleParked(vehicle))
            throw new ParkingLotException("vehicle already parked", ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED);
        if (vehicles.size() == actualCapacity && !vehicles.contains(null)) {
            for (ParkingLotObservers observer : observersList)
                observer.setCapacityFull();
            throw new ParkingLotException("parkinglot is full", ParkingLotException.ExceptionTypes.PARKING_LOT_FULL);
        }
        parkVehicle(slot++, vehicle);
    }

    public boolean unPark(Object vehicle) {
        if (this.vehicles.contains(vehicle)) {
            this.vehicles.set(this.vehicles.indexOf(vehicle), null);
            return true;
        }
        return false;
    }

    public void parkVehicle(int slot, Object vehicle) throws ParkingLotException {
        if (isVehicleParked(vehicle)) {
            throw new ParkingLotException("VEHICLE ALREADY PARK", ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED);
        }
        this.vehicles.set(slot, vehicle);
    }

    public ArrayList getSlot() {
        ArrayList<Integer> emptySlots = new ArrayList();
        for (int slot = 0; slot < this.actualCapacity; slot++) {
            if (this.vehicles.get(slot) == null)
                emptySlots.add(slot);
        }
        return emptySlots;
    }

    public boolean isVehicleParked(Object vehicle) {
        return this.vehicles.contains(vehicle);
    }

    public void registerObserver(ParkingLotObservers observer) {
        observersList.add(observer);
    }
}
