package com.parkinglot;

import com.parkinglot.exceptions.ParkingLotException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLotSystem {
    enum DriverType {NORMAL, HANDICAP}

    private int actualCapacity;
    private List<ParkingLotObservers> observersList;
    public List<Object> vehicles;

    public ParkingLotSystem(int actualCapacity) {
        setActualCapacity(actualCapacity);
        this.observersList = new ArrayList<>();
    }

    public int initializeParkingLot() {
        this.vehicles = new ArrayList<>();
        IntStream.range(0, this.actualCapacity).forEach(slots -> vehicles.add(null));
        return vehicles.size();
    }

    public void setActualCapacity(int capacity) {
        this.actualCapacity = capacity;
        initializeParkingLot();
    }

    public int park(Object vehicle, DriverType driverType, int... slots) throws ParkingLotException {
        if (isVehicleParked(vehicle))
            throw new ParkingLotException("vehicle already parked", ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED);
        if (vehicles.size() == actualCapacity && !vehicles.contains(null)) {
            for (ParkingLotObservers observer : observersList)
                observer.setCapacityFull();
            throw new ParkingLotException("parkinglot is full", ParkingLotException.ExceptionTypes.PARKING_LOT_FULL);
        }
        getAutoParkingLocation(vehicle, driverType, slots);
        observersList.get(0).setParkingTime(LocalDateTime.now().getMinute());
        return LocalDateTime.now().getMinute();
    }

    public ArrayList parkingAttender(DriverType driverType) {
        ArrayList emptyParkingSlotList = getEmptyParkingSlot();
        if (DriverType.HANDICAP.equals(driverType))
          Collections.sort(emptyParkingSlotList);
        else if (DriverType.NORMAL.equals(driverType))
            Collections.sort(emptyParkingSlotList,Collections.reverseOrder());
        return emptyParkingSlotList;
    }

    public void getAutoParkingLocation(Object vehicle, DriverType driverType, int... slots) {
        if (slots.length == 0) {
            int autoParkingLocation = (int) parkingAttender(driverType).get(0);
            this.vehicles.set(autoParkingLocation, vehicle);
            return;
        }
        this.vehicles.set(slots[0], vehicle);
    }

    public boolean unPark(Object vehicle) {
        if (this.vehicles.contains(vehicle)) {
            this.vehicles.set(this.vehicles.indexOf(vehicle), null);
            return true;
        }
        return false;
    }

    public ArrayList getEmptyParkingSlot() {
        ArrayList<Integer> emptySlots = new ArrayList<>();
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
