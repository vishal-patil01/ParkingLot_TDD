package com.parkinglot;

import com.parkinglot.exceptions.ParkingLotException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParkingLotSystem {
    public enum DriverType {NORMAL, HANDICAP}

    private int parkingLotCapacity;
    private ParkingSlot parkingSlot;
    private List<ParkingLotObservers> observersList;
    public List<Object> vehiclesList;

    public ParkingLotSystem(int parkingLotCapacity) {
        setParkingLotCapacity(parkingLotCapacity);
        this.observersList = new ArrayList<>();
    }

    public void setParkingLotCapacity(int capacity) {
        this.parkingLotCapacity = capacity;
        initializeParkingLot();
    }

    public int initializeParkingLot() {
        this.vehiclesList = new ArrayList<>();
        IntStream.range(0, this.parkingLotCapacity).forEach(slots -> vehiclesList.add(null));
        return vehiclesList.size();
    }

    public boolean park(Object vehicle, DriverType driverType) throws ParkingLotException {
        parkingSlot = new ParkingSlot(vehicle);
        if (isVehicleParked(vehicle))
            throw new ParkingLotException("vehicle already parked", ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED);
        if (vehiclesList.size() == parkingLotCapacity && !vehiclesList.contains(null)) {
            observersList.forEach(ParkingLotObservers::setCapacityFull);
            throw new ParkingLotException("parkinglot is full", ParkingLotException.ExceptionTypes.PARKING_LOT_FULL);
        }
        int emptyParkingSlot = getEmptyParkingSlotBasedOnDriverType(driverType);
        this.vehiclesList.set(emptyParkingSlot, parkingSlot);
        return true;
    }

    public boolean isVehicleParked(Object vehicle) {
        parkingSlot = new ParkingSlot(vehicle);
        return this.vehiclesList.contains(parkingSlot);
    }

    public Integer getEmptyParkingSlotBasedOnDriverType(DriverType driverType) {
        if (DriverType.HANDICAP.equals(driverType))
            return getListOfEmptyParkingSlots().stream().sorted().collect(Collectors.toList()).get(0);
        return getListOfEmptyParkingSlots().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList()).get(0);
    }

    public ArrayList<Integer> getListOfEmptyParkingSlots() {
        ArrayList<Integer> emptyParkingSlotList = new ArrayList<>();
        IntStream.range(0, this.parkingLotCapacity).filter(slot -> vehiclesList.get(slot) == null).forEach(slot -> emptyParkingSlotList.add(slot));
        return emptyParkingSlotList;
    }

    public boolean unPark(Object vehicle) {
        parkingSlot = new ParkingSlot(vehicle);
        if (this.vehiclesList.contains(parkingSlot)) {
            this.vehiclesList.set(this.vehiclesList.indexOf(parkingSlot), null);
            return true;
        }
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public int findVehicle(Object vehicle) throws ParkingLotException {
        parkingSlot = new ParkingSlot(vehicle);
        if (this.vehiclesList.contains(parkingSlot))
            return this.vehiclesList.indexOf(parkingSlot);
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public int getVehicleParkingTime(Object vehicle) {
        parkingSlot = new ParkingSlot(vehicle);
        return parkingSlot.parkedTime;
    }

    public void registerObserver(ParkingLotObservers observer) {
        observersList.add(observer);
    }
}