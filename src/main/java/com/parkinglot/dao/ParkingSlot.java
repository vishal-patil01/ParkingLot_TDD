package com.parkinglot.dao;

public class ParkingSlot {
    public Vehicle vehicle;
    public int parkedTime;

    public ParkingSlot(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.parkedTime = (int) ((System.currentTimeMillis() / (1000 * 60)) % 60);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSlot that = (ParkingSlot) o;
        return vehicle.equals(that.vehicle);
    }
}
