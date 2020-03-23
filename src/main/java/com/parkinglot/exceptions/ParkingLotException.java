package com.parkinglot.exceptions;

public class ParkingLotException extends RuntimeException {
    String exceptionMessage;

    public enum ExceptionTypes {
        PARKING_LOT_FULL, VEHICLE_NOT_FOUND ,VEHICLE_ALREADY_PARKED
    }

    public ExceptionTypes exceptionTypes;

    public ParkingLotException(String message, ExceptionTypes types) {
        this.exceptionMessage = message;
        this.exceptionTypes = types;
    }
}
