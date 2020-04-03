package com.parkinglot;

import com.parkinglot.Observers.ParkingAvailabilityInformer;
import com.parkinglot.dao.ParkingSlot;
import com.parkinglot.dao.Vehicle;
import com.parkinglot.enums.DriverTypes;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exceptions.ParkingLotException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.parkinglot.Predicates.VehiclePredicates.initializePredicate;

public class ParkingLots {

    private int parkingSlotCapacity;
    private static final AtomicInteger count = new AtomicInteger(0);
    ArrayList<String> filteredVehicleDetailsList;
    ParkingAvailabilityInformer informer;
    public List<ParkingSlot> vehiclesList;

    public ParkingLots(int parkingSlotCapacity) {
        informer = ParkingAvailabilityInformer.getInstance();
        setParkingLotCapacity(parkingSlotCapacity);
        filteredVehicleDetailsList = new ArrayList<>();
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
        if (isVehicleParked(vehicle))
            throw new ParkingLotException("vehicle already parked", ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED);
        if (vehiclesList.size() == parkingSlotCapacity && !vehiclesList.contains(null)) {
            informer.notifyParkingFull();
            throw new ParkingLotException("packing lot is full", ParkingLotException.ExceptionTypes.PARKING_LOT_FULL);
        }
        ParkingSlot parkingSlot = new ParkingSlot(vehicle, vehicleType, driverType);
        int emptyParkingSlot = getEmptyParkingSlotBasedOnDriverType(driverType, vehicleType);
        this.vehiclesList.set(emptyParkingSlot, parkingSlot);
        return true;
    }

    public boolean isVehicleParked(Vehicle vehicle) {
        return this.vehiclesList.contains(new ParkingSlot(vehicle));
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
            this.vehiclesList.set(this.vehiclesList.indexOf(new ParkingSlot(vehicle)), null);
            informer.notifyParkingAvailable();
            return true;
        }
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public int findVehicle(Vehicle vehicle) throws ParkingLotException {
        if (isVehicleParked(vehicle))
            return this.vehiclesList.indexOf(new ParkingSlot(vehicle));
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public int getVehicleParkingTime(Vehicle vehicle) {
        if (isVehicleParked(vehicle)) {
            return new ParkingSlot(vehicle).getParkedTime();
        }
        throw new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
    }

    public List<String> filterByPredicate(IntPredicate intPredicate) {
        initializePredicate(vehiclesList);
        IntStream.range(0, vehiclesList.size())
                .filter(slot -> vehiclesList.get(slot) != null)
                .filter(intPredicate)
                .mapToObj(slot -> (slot + " " + vehiclesList.get(slot).getVehicle().getNumberPlate() + " " + vehiclesList.get(slot).getVehicle().getModelName() + " " + vehiclesList.get(slot).getVehicle().getColor()))
                .forEach(filteredVehicleDetailsList::add);
        return filteredVehicleDetailsList;
    }
}