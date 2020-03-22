package com.parkinglot;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        boolean isParked = parkingLotSystem.isVehicalParked(vehicle);
        assertTrue(isParked);
    }

    @Test
    public void givenVechical_WhenAlReadyParked_ShouldReturnFalse() {
        try {
            parkingLotSystem.park(vehicle);
            parkingLotSystem.park(new Object());
        } catch (ParkingLotException e) {
            assertEquals("Parkinglot Is Full", e.getMessage());
        }
    }

    @Test
    public void givenVehicle_WhenUnParked_ShouldReturnTrue() {
        parkingLotSystem.park(vehicle);
        boolean isUnParked = parkingLotSystem.unPark(vehicle);
        assertTrue(isUnParked);
    }

    @Test
    public void givenWhenParkingLotIsFull_ShoulInformTheOwner() {
        ParkingLotOwner owner = new ParkingLotOwner();
        parkingLotSystem.registerOwner(owner);
        try {
            parkingLotSystem.park(vehicle);
            parkingLotSystem.park(new Object());
        } catch (ParkingLotException e) {
            boolean capacityFull = owner.isCapacityFull();
            assertTrue(capacityFull);
        }
    }

    @Test
    public void givenCapacityIs2_ShouldAbleToPark2Vechile() {
        Object vehicle2 = new Object();
        parkingLotSystem.setActualCapacity(2);
        parkingLotSystem.park(vehicle);
        parkingLotSystem.park(vehicle2);
        boolean isParked1 = parkingLotSystem.isVehicalParked(vehicle);
        boolean isParked2 = parkingLotSystem.isVehicalParked(vehicle2);
        assertTrue(isParked1 && isParked2);
    }
}

