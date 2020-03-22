package com.parkinglot;

import java.util.ArrayList;
import java.util.List;

public class ParkingLotSystem {
    private int actualCapacity;
    private ParkingLotOwner owner;
    private AirportSecurity security;
    private List vehicles;

    public ParkingLotSystem(int actualCapacity) {
        this.actualCapacity = actualCapacity;
        this.vehicles = new ArrayList();
    }

    public void registerOwner(ParkingLotOwner owner) {
        this.owner = owner;
    }
    public void registerSecurity(AirportSecurity airportSecurity) {
        this.security =airportSecurity;
    }

    public void setActualCapacity(int capacity) {
        this.actualCapacity = capacity;
    }

    public void park(Object vehicle) throws ParkingLotException {
        if (this.vehicles.size() == this.actualCapacity) {
            owner.capacityIsFull();
            throw new ParkingLotException("parkinglot is full");
        }
        if (isVehicalParked(vehicle))
            throw new ParkingLotException("vehicle already parked");
        this.vehicles.add(vehicle);
    }

    public boolean unPark(Object vehicle) {
        if (this.vehicles.contains(vehicle)) {
            this.vehicles.remove(vehicle);
            return true;
        }
        return false;
    }

    public boolean isVehicalParked(Object vehicle) {
        if (this.vehicles.contains(vehicle))
            return true;
        return false;
    }

}
