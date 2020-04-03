package com.parkinglot;

import com.parkinglot.Observers.ParkingAvailabilityInformer;
import com.parkinglot.dao.ParkingSlot;
import com.parkinglot.dao.Vehicle;
import com.parkinglot.enums.DriverTypes;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exceptions.ParkingLotException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParkingLots {

    private int parkingSlotCapacity;
    private ParkingSlot parkingSlot;
    ParkingAvailabilityInformer informer;
    public List<ParkingSlot> vehiclesList;

    public ParkingLots(int parkingSlotCapacity) {
        informer = ParkingAvailabilityInformer.getInstance();
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

    public boolean park(Vehicle vehicle, DriverTypes driverType, VehicleType vehicleType) throws ParkingLotException {
        parkingSlot = new ParkingSlot(vehicle);
        if (isVehicleParked(vehicle))
            throw new ParkingLotException("vehicle already parked", ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED);
        if (vehiclesList.size() == parkingSlotCapacity && !vehiclesList.contains(null)) {
            informer.notifyParkingFull();
            throw new ParkingLotException("packing lot is full", ParkingLotException.ExceptionTypes.PARKING_LOT_FULL);
        }
        int emptyParkingSlot = getEmptyParkingSlotBasedOnDriverType(driverType, vehicleType);
        this.vehiclesList.set(emptyParkingSlot, parkingSlot);
        return true;
    }

    public boolean isVehicleParked(Vehicle vehicle) {
        parkingSlot = new ParkingSlot(vehicle);
        return this.vehiclesList.contains(parkingSlot);
    }

    public Integer getEmptyParkingSlotBasedOnDriverType(DriverTypes driverType, VehicleType vehicleType) {
        return getEmptyParkingSlotBasedOnVehicleType(vehicleType).stream().sorted(driverType.order).collect(Collectors.toList()).get(0);
    }

    public ArrayList<Integer> getEmptyParkingSlotBasedOnVehicleType(VehicleType vehicleType) {
        return vehicleType.getParkingLotsList(getListOfEmptyParkingSlots());
    }

    public ArrayList<Integer> getListOfEmptyParkingSlots() {
        ArrayList<Integer> emptyParkingSlotList = new ArrayList<>();
        IntStream.range(0, this.parkingSlotCapacity).filter(slot -> vehiclesList.get(slot) == null).forEach(emptyParkingSlotList::add);
        return emptyParkingSlotList;
    }

    public boolean unPark(Vehicle vehicle) {
        if (isVehicleParked(vehicle)) {
            this.vehiclesList.set(this.vehiclesList.indexOf(parkingSlot), null);
            informer.notifyParkingAvailable();
            return true;
        }
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public int findVehicle(Vehicle vehicle) throws ParkingLotException {
        ParkingSlot parkingSlot = new ParkingSlot(vehicle);
        if (isVehicleParked(vehicle))
            return this.vehiclesList.indexOf(parkingSlot);
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public int getVehicleParkingTime(Vehicle vehicle) {
        if (isVehicleParked(vehicle))
            return parkingSlot.parkedTime;
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public ArrayList<Integer> findVehicleByColor(String colour) {
        ArrayList<Integer> filteredVehicleDetailsList = new ArrayList<>();
        IntStream.range(0, vehiclesList.size())
                .filter(slot -> vehiclesList.get(slot) != null)
                .filter(slot -> Objects.equals(vehiclesList.get(slot).vehicle.getColor(), colour))
                .forEach(filteredVehicleDetailsList::add);
        return filteredVehicleDetailsList;
    }

    public ArrayList<Integer> findVehicleByModelNumber(String modelNumber) {
        ArrayList<Integer> filteredVehicleDetailsList = new ArrayList<>();
        IntStream.range(0, vehiclesList.size())
                .filter(slot -> vehiclesList.get(slot) != null)
                .filter(slot -> Objects.equals(vehiclesList.get(slot).vehicle.getModelName(), modelNumber))
                .forEach(filteredVehicleDetailsList::add);
        return filteredVehicleDetailsList;
    }

    public ArrayList<String> findByFieldNames(String color, String modelName) {
        ArrayList<String> filteredVehicleDetailsList = new ArrayList<>();
        IntStream.range(0, vehiclesList.size())
                .filter(slot -> vehiclesList.get(slot) != null)
                .filter(slot -> Objects.equals(vehiclesList.get(slot).vehicle.getColor(), color))
                .filter(slot -> Objects.equals(vehiclesList.get(slot).vehicle.getModelName(), modelName))
                .mapToObj(slot->(slot+ " "+vehiclesList.get(slot).vehicle.getNumberPlate()))
                .forEach(filteredVehicleDetailsList::add);
        return filteredVehicleDetailsList;
    }
}