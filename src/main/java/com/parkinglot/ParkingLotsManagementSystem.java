package com.parkinglot;

import com.parkinglot.Observers.ParkingAvailabilityInformer;
import com.parkinglot.Observers.ParkingLotObservers;
import com.parkinglot.dao.Vehicle;
import com.parkinglot.enums.DriverTypes;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exceptions.ParkingLotException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    public ArrayList<String> findVehicleByColour(String colour) {
        for (ParkingLots lot : parkingLotsList)
            return lot.findVehicleByColor(colour);
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public ArrayList<String> findVehicleByModelNumber(String modelNumber) {
        for (ParkingLots lot : parkingLotsList)
            return lot.findVehicleByModelNumber(modelNumber);
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public ArrayList<String> findVehicleParkedInLast30Minutes() {
        for (ParkingLots lot : parkingLotsList)
            return lot.findVehicleParkedInLast30Minutes();
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public ArrayList<String> findVehicleByMultipleFieldNames(String color, String model) {
        for (ParkingLots lot : parkingLotsList)
            return lot.findByFieldNames(color, model);
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public ArrayList<String> findVehicleByVehicleTypeAndDriverType(VehicleType vehicleType, DriverTypes driverType) {
        for (ParkingLots lot : parkingLotsList)
            return lot.findByFieldByVehicleTypeAndDriverType(vehicleType, driverType);
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public ArrayList<String> findAllParkedVehicles() {
        for (ParkingLots lot : parkingLotsList)
            return lot.findAllParkedVehicles();
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }
}