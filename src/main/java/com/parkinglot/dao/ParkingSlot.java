package com.parkinglot.dao;

import com.parkinglot.enums.DriverTypes;
import com.parkinglot.enums.VehicleType;

import java.util.concurrent.TimeUnit;

public class ParkingSlot {
    private Vehicle vehicle;
    private int parkedTime;
    private VehicleType vehicleType;
    private DriverTypes driverType;

    public ParkingSlot(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.parkedTime = (int) TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
    }

    public ParkingSlot(Vehicle vehicle, VehicleType vehicleType, DriverTypes driverType) {
        this.vehicle = vehicle;
        this.parkedTime = (int) TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
        this.driverType = DriverTypes.HANDICAP;
        this.vehicleType = vehicleType;
        this.driverType=driverType;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getParkedTime() {
        return parkedTime;
    }

    public DriverTypes getDriverType() {
        return driverType;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSlot that = (ParkingSlot) o;
        return vehicle.equals(that.vehicle);
    }
}
