package com.parkinglot;

import com.parkinglot.Observers.ParkingAvailabilityInformer;
import com.parkinglot.Observers.ParkingLotObservers;
import com.parkinglot.enums.DriverTypes;
import com.parkinglot.exceptions.ParkingLotException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ParkingLotsManagementSystem {
    List<ParkingLots> parkingLotsList;
    ParkingAvailabilityInformer informer;

    public ParkingLotsManagementSystem() {
        informer = new ParkingAvailabilityInformer();
        this.parkingLotsList = new ArrayList<>();
    }

    public void addLot(ParkingLots parkingLot) {
        this.parkingLotsList.add(parkingLot);
    }

    public boolean isLotAdded(ParkingLots parkingLot) {
        return this.parkingLotsList.contains(parkingLot);
    }

    public boolean park(Object vehicle, DriverTypes driverType) {
        ParkingLots lot = getParkingLotHavingMaxSpace();
        return lot.park(vehicle, driverType);
    }

    public boolean isVehicleParked(Object vehicle) {
        for (ParkingLots parkingLots : this.parkingLotsList) {
            if (parkingLots.isVehicleParked(vehicle))
                return true;
        }
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public int findVehicle(Object vehicle) {
        for (ParkingLots parkingLots : this.parkingLotsList)
            return parkingLots.findVehicle(vehicle);
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public boolean unPark(Object vehicle) {
        for (ParkingLots parkingLots : this.parkingLotsList) {
            return parkingLots.unPark(vehicle);
        }
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public int getVehicleParkingTime(Object vehicle) {
        for (ParkingLots parkingLots : this.parkingLotsList)
            return parkingLots.getVehicleParkingTime(vehicle);
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public ParkingLots getParkingLotHavingMaxSpace() {
        return parkingLotsList.stream().sorted(Comparator.comparing(list -> list.getListOfEmptyParkingSlots().size(), Comparator.reverseOrder())).collect(Collectors.toList()).get(0);
    }

    public void register(ParkingLotObservers observer) {
        informer.register(observer);
    }
}