package com.parkinglot;

import com.parkinglot.Observers.ParkingAvailabilityInformer;
import com.parkinglot.dao.ParkingSlot;
import com.parkinglot.dao.Vehicle;
import com.parkinglot.enums.DriverTypes;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exceptions.ParkingLotException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.parkinglot.Predicates.VehiclePredicates.initializePredicate;

public class ParkingLots {

    private int parkingSlotCapacity;
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

    public List<Integer> getEmptyParkingSlotBasedOnVehicleType(VehicleType vehicleType) {
        return vehicleType.getParkingLotsList(getListOfEmptyParkingSlots());
    }

    public List<Integer> getListOfEmptyParkingSlots() {
        return IntStream.range(0, this.parkingSlotCapacity).filter(slot -> vehiclesList.get(slot) == null).boxed().collect(Collectors.toList());
    }

    public boolean unPark(Vehicle vehicle) {
        return vehiclesList.stream().filter(slot -> slot != null && slot.getVehicle() == vehicle && vehiclesList.set(vehiclesList.indexOf(new ParkingSlot(vehicle)), null) != null).map(slot -> true).findFirst()
                .orElseThrow(() -> new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND));
    }

    public int findVehicle(Vehicle vehicle) throws ParkingLotException {
        return vehiclesList.stream().filter(slot -> slot != null && slot.getVehicle() == vehicle).mapToInt(slot -> vehiclesList.indexOf(new ParkingSlot(vehicle))).findFirst()
                .orElseThrow(() -> new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND));
    }

    public int getVehicleParkingTime(Vehicle vehicle) {
        return vehiclesList.stream().filter(slot -> slot != null && slot.getVehicle() == vehicle).mapToInt(ParkingSlot::getParkedTime).findFirst()
                .orElseThrow(() -> new ParkingLotException("VEHICLE IS NOT AVAILABLE", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND));
    }

    public List<String> filterByPredicate(IntPredicate intPredicate) {
        initializePredicate(vehiclesList);
        return IntStream.range(0, vehiclesList.size())
                .filter(slot -> vehiclesList.get(slot) != null).filter(intPredicate)
                .mapToObj(slot -> ("SlotNumber=" + slot + " " + vehiclesList.get(slot).getVehicle()))
                .collect(Collectors.toList());
    }
}