package com.parkinglot;

import com.parkinglot.Observers.ParkingAvailabilityInformer;
import com.parkinglot.Observers.ParkingLotObservers;
import com.parkinglot.dao.ParkingSlot;
import com.parkinglot.dao.Vehicle;
import com.parkinglot.enums.DriverTypes;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exceptions.ParkingLotException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParkingLotsManagementSystem {
    List<ParkingLots> parkingLotsList;
    ParkingAvailabilityInformer informer;

    public ParkingLotsManagementSystem() {
        informer = ParkingAvailabilityInformer.getInstance();
        this.parkingLotsList = new ArrayList<>();
    }

    public void addLot(ParkingLots parkingLot) {
        this.parkingLotsList.add(parkingLot);
    }

    public boolean isLotAdded(ParkingLots parkingLot) {
        return this.parkingLotsList.contains(parkingLot);
    }

    public boolean park(Vehicle vehicle, DriverTypes driverType, VehicleType vehicleType) {
        ParkingLots lot = getParkingLotHavingMaxSpace();
        return lot.park(vehicle, driverType, vehicleType);
    }

    public boolean isVehicleParked(Vehicle vehicle) {
        for (ParkingLots parkingLots : this.parkingLotsList) {
            if (parkingLots.isVehicleParked(vehicle))
                return true;
        }
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public int findVehicle(Vehicle vehicle) {
        for (ParkingLots parkingLots : this.parkingLotsList)
            if (parkingLots.isVehicleParked(vehicle))
                return parkingLots.findVehicle(vehicle);
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public ArrayList<Integer> findVehicleByColor(String color) {
        for (ParkingLots lot : parkingLotsList)
         return lot.findOnField(color);
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public boolean unPark(Vehicle vehicle) {
        for (ParkingLots parkingLots : this.parkingLotsList) {
            return parkingLots.unPark(vehicle);
        }
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public int getVehicleParkingTime(Vehicle vehicle) {
        for (ParkingLots parkingLots : this.parkingLotsList)
            return parkingLots.getVehicleParkingTime(vehicle);
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public ParkingLots getParkingLotHavingMaxSpace() {
        ParkingLots parkingLots;
        try {
            parkingLots = parkingLotsList.stream()
                    .sorted(Comparator.comparing(list -> list.getListOfEmptyParkingSlots().size(), Comparator.reverseOrder()))
                    .filter(list -> list.getListOfEmptyParkingSlots().size() != 0)
                    .findFirst()
                    .orElseThrow(() -> new ParkingLotException("ParkingLot Full", ParkingLotException.ExceptionTypes.PARKING_LOT_FULL));
        } catch (ParkingLotException e) {
            parkingLots = parkingLotsList.get(0);
        }
        return parkingLots;
    }

    public void register(ParkingLotObservers observer) {
        informer.register(observer);
    }
}