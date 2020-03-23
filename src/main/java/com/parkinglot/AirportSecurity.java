package com.parkinglot;

public class AirportSecurity implements ParkingLotObservers {
    private boolean isFullCapacity;

    @Override
    public void setCapacityFull() {
        isFullCapacity = true;
    }

    @Override
    public boolean isCapacityFull() {
        return this.isFullCapacity;
    }

    @Override
    public void setParkingTime(int time) {}

    @Override
    public int getParkingTime() {
        return 0;
    }

}
