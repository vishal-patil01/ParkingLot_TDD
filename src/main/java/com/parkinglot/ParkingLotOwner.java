package com.parkinglot;

public class ParkingLotOwner {
    private boolean isFullCapacity;

    public void capacityIsFull() {
        isFullCapacity = true;
    }
    public boolean isCapacityFull() {
        return isFullCapacity;
    }
}
