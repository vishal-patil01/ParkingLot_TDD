package com.parkinglot;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestParkingLot {
        ParkingLotSystem parkingLotSystem;

        @Before
        public void setup() {
            parkingLotSystem = new ParkingLotSystem();
        }

        @Test
        public void givenVehicle_WhenParked_ShouldReturnTrue() {
            boolean isParked = parkingLotSystem.parkVehicle(new Object());
            assertTrue(isParked);
        }
    }


