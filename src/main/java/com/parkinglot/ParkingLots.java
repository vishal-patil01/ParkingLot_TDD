package com.parkinglot;

import com.parkinglot.enums.DriverTypes;
import com.parkinglot.Observers.ParkingAvailabilityInformer;
import com.parkinglot.dao.ParkingSlot;
import com.parkinglot.exceptions.ParkingLotException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParkingLots {

    private int parkingSlotCapacity;
    private ParkingSlot parkingSlot;
    ParkingAvailabilityInformer informer;
    public List<ParkingSlot> vehiclesList;

    public ParkingLots(int parkingSlotCapacity) {
        informer = new ParkingAvailabilityInformer();
        setParkingLotCapacity(parkingSlotCapacity);
    }

    public void setParkingLotCapacity(int capacity) {
        this.parkingSlotCapacity = capacity;
        initializeParkingLot();
    }

    public int initializeParkingLot() {
        this.vehiclesList = new ArrayList<>();
        IntStream.range(0, this.parkingSlotCapacity).forEach(slots -> vehiclesList.add(null));
        return vehiclesList.size();
    }

    public boolean park(Object vehicle, DriverTypes driverType) throws ParkingLotException {
        parkingSlot = new ParkingSlot(vehicle);
        if (isVehicleParked(vehicle))
            throw new ParkingLotException("vehicle already parked", ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED);
        if (vehiclesList.size() == parkingSlotCapacity && !vehiclesList.contains(null)) {
            informer.notifyParkingFull();
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

    public Integer getEmptyParkingSlotBasedOnDriverType(DriverTypes driverType) {
        return getListOfEmptyParkingSlots().stream().sorted(driverType.order).collect(Collectors.toList()).get(0);
    }

    public ArrayList<Integer> getListOfEmptyParkingSlots() {
        ArrayList<Integer> emptyParkingSlotList = new ArrayList<>();
        IntStream.range(0, this.parkingSlotCapacity).filter(slot -> vehiclesList.get(slot) == null).forEach(emptyParkingSlotList::add);
        return emptyParkingSlotList;
    }

    public boolean unPark(Object vehicle) {
        if (isVehicleParked(vehicle)) {
            this.vehiclesList.set(this.vehiclesList.indexOf(parkingSlot), null);
            informer.notifyParkingAvailable();
            return true;
        }
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public int findVehicle(Object vehicle) throws ParkingLotException {
        if (isVehicleParked(vehicle))
            return this.vehiclesList.indexOf(parkingSlot);
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);

    }

    public int getVehicleParkingTime(Object vehicle) {
        if (isVehicleParked(vehicle))
            return parkingSlot.parkedTime;
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }
}