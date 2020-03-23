package com.parkinglot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLotSystem {
    ParkingLotOwner owner = new ParkingLotOwner();
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

    public int park(Object vehicle, int... slots) throws ParkingLotException {
        if (isVehicleParked(vehicle))
            throw new ParkingLotException("vehicle already parked", ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED);
        if (vehicles.size() == actualCapacity && !vehicles.contains(null)) {
            for (ParkingLotObservers observer : observersList)
                observer.setCapacityFull();
            throw new ParkingLotException("parkinglot is full", ParkingLotException.ExceptionTypes.PARKING_LOT_FULL);
        }
        int parkingNumber = (slots.length == 0) ? slot++ : slots[0];
        this.vehicles.set(parkingNumber, vehicle);
        observersList.get(0).setParkingTime(LocalDateTime.now().getMinute());
        return LocalDateTime.now().getMinute();
    }

    public boolean unPark(Object vehicle) {
        if (this.vehicles.contains(vehicle)) {
            this.vehicles.set(this.vehicles.indexOf(vehicle), null);
            return true;
        }
        return false;
    }

    public ArrayList getEmptyParkingSlot() {
        ArrayList<Integer> emptySlots = new ArrayList();
        IntStream.range(0, this.actualCapacity).filter(slot -> vehicles.get(slot) == null).forEach(slot -> emptySlots.add(slot));
        return emptySlots;
    }

    public int findVehicle(Object vehicle) throws ParkingLotException {
        if (this.vehicles.contains(vehicle))
            return this.vehicles.indexOf(vehicle);
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public boolean isVehicleParked(Object vehicle) {
        return this.vehicles.contains(vehicle);
    }

    public void registerObserver(ParkingLotObservers observer) {
        observersList.add(observer);
    }
}
