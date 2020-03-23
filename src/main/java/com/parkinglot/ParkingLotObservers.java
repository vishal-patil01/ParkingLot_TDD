package com.parkinglot;

public interface ParkingLotObservers {
    void setCapacityFull();

    boolean isCapacityFull();

    void setParkingTime(int time);

    int getParkingTime();
}
