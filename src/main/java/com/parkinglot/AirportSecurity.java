package com.parkinglot;

public class AirportSecurity implements ParkingLotObservers {
    private boolean isFullCapacity;

    @Override
    public void capacityIsFull() {
        isFullCapacity = true;
    }

    @Override
    public boolean isCapacityFull() {
        return this.isFullCapacity;
    }

}