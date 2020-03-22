package com.parkinglot;

public class ParkingLotOwner implements ParkingLotObservers {
    private boolean isFullCapacity;

    @Override
    public void setCapacityFull() {
        isFullCapacity = true;
    }

    @Override
    public boolean isCapacityFull() {
        return isFullCapacity;
    }
}
