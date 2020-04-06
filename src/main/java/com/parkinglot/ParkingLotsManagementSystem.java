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
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

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
        return parkingLotsList.stream().filter(lots -> lots.isVehicleParked(vehicle)).map(slot -> true).collect(Collectors.toList()).get(0);
    }

    public int findVehicle(Vehicle vehicle) {
        return parkingLotsList.stream().filter(lots -> lots.findVehicle(vehicle) != -1).map(lots -> lots.findVehicle(vehicle)).collect(Collectors.toList()).get(0);
    }

    public boolean unPark(Vehicle vehicle) {
        return parkingLotsList.stream().filter(lots -> lots.unPark(vehicle)).map(slot -> true).collect(Collectors.toList()).get(0);
    }

    public int getVehicleParkingTime(Vehicle vehicle) {
        return parkingLotsList.stream().filter(lots -> lots.getVehicleParkingTime(vehicle) != -1).map(lots -> lots.getVehicleParkingTime(vehicle)).collect(Collectors.toList()).get(0);
    }

    public void register(ParkingLotObservers observer) {
        informer.register(observer);
    }

    public List<String> filterByPredicate(IntPredicate intPredicate) {
        List<String> filteredVehicleList= parkingLotsList.stream().map(lots -> lots.filterByPredicate(intPredicate)).collect(Collectors.toList()).get(0);
        if(filteredVehicleList.size()==0)
            throw new ParkingLotException("Vehicle Not Found",ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
        return filteredVehicleList;
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
}