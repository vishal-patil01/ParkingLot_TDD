package com.parkinglot.Observers;

public class ParkingLotOwner implements ParkingLotObservers {
    private boolean isFullCapacity;

    @Override
    public void setParkingLotFull() {
        isFullCapacity = true;
    }

    @Override
    public void setParkingAvailable() {
      isFullCapacity=false;
    }

    @Override
    public boolean isParkingLotFull() {
        return isFullCapacity;
    }
}
