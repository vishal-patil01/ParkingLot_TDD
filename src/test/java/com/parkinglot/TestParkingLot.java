package com.parkinglot;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestParkingLot {
    ParkingLotSystem parkingLotSystem;
    Object vehicle;

    @Before
    public void setup() {
        vehicle = new Object();
        parkingLotSystem = new ParkingLotSystem(2);
    }

    @Test
    public void givenVehicle_WhenParked_ShouldReturnTrue() {
        parkingLotSystem.park(vehicle);
        boolean isParked = parkingLotSystem.isVehicleParked(vehicle);
        assertTrue(isParked);
    }

    @Test
    public void givenVehicle_WhenAlReadyParked_ShouldReturnFalse() {
        try {
            parkingLotSystem.park(vehicle);
            parkingLotSystem.park(vehicle);
        } catch (ParkingLotException e) {
            assertEquals(ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED, e.exceptionTypes);
        }
    }

    @Test
    public void givenVehicle_WhenUnParked_ShouldReturnTrue() {
        parkingLotSystem.park(vehicle);
        boolean isUnParked = parkingLotSystem.unPark(vehicle);
        assertTrue(isUnParked);
    }

    @Test
    public void givenWhenParkingLotIsFull_ShouldInformTheOwner() {
        Object vehicle2 = new Object();
        ParkingLotOwner owner = new ParkingLotOwner();
        parkingLotSystem.registerObserver(owner);
        try {
            parkingLotSystem.park(vehicle);
            parkingLotSystem.park(new Object());
            parkingLotSystem.park(new Object());
        } catch (ParkingLotException e) {
            boolean capacityFull = owner.isCapacityFull();
            assertTrue(capacityFull);
        }
    }

    @Test
    public void givenCapacityIs2_ShouldAbleToPark2Vehicle() {
        Object vehicle2 = new Object();
        parkingLotSystem.setActualCapacity(2);
        parkingLotSystem.park(vehicle);
        parkingLotSystem.park(vehicle2);
        boolean isParked1 = parkingLotSystem.isVehicleParked(vehicle);
        boolean isParked2 = parkingLotSystem.isVehicleParked(vehicle2);
        assertTrue(isParked1 && isParked2);
    }

    @Test
    public void givenWhenLotIsFull_ShouldInformTheSecurity() {
        AirportSecurity airportSecurity = new AirportSecurity();
        parkingLotSystem.registerObserver(airportSecurity);
        try {
            parkingLotSystem.park(vehicle);
            parkingLotSystem.park(new Object());
            parkingLotSystem.park(new Object());
        } catch (ParkingLotException e) {
            boolean capacityFull = airportSecurity.isCapacityFull();
            assertTrue(capacityFull);
        }
    }

    @Test
    public void givenVehicle_WhenLotSpaceIsAvailableAfterFull_ShouldInformTheOwnerAndReturnFalse() {
        Object vehicle2 = new Object();
        ParkingLotOwner owner = new ParkingLotOwner();
        parkingLotSystem.registerObserver(owner);
        parkingLotSystem.park(vehicle);
        parkingLotSystem.park(vehicle2);
        parkingLotSystem.unPark(vehicle);
        assertFalse(owner.isCapacityFull());
    }

    @Test
    public void givenVehicle_WhenLotSpaceIsAvailableAfterFull_ShouldInformTheAirPortSecurityAndReturnFalse() {
        AirportSecurity airportSecurity = new AirportSecurity();
        parkingLotSystem.registerObserver(airportSecurity);
        parkingLotSystem.park(vehicle);
        parkingLotSystem.unPark(vehicle);
        assertFalse(airportSecurity.isCapacityFull());
    }
}