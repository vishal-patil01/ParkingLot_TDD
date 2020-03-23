package com.parkinglot;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestParkingLot {
    ParkingLotSystem parkingLotSystem;
    ParkingLotOwner owner;
    Object vehicle;

    @Before
    public void setup() {
        vehicle = new Object();
        parkingLotSystem = new ParkingLotSystem(2);
        owner = new ParkingLotOwner();
        parkingLotSystem.registerObserver(owner);
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

    //UC6
    @Test
    public void givenParkingLotCapacity_WhenInitialize_ShouldReturnParkingCapacity() {
        parkingLotSystem.setActualCapacity(10);
        int parkingLotCapacity = parkingLotSystem.initializeParkingLot();
        assertEquals(10, parkingLotCapacity);
    }

    @Test
    public void givenParkingLot_ShouldReturnAvailableSlots() {
        List expectedList = new ArrayList();
        expectedList.add(0);
        expectedList.add(1);
        parkingLotSystem.setActualCapacity(2);
        parkingLotSystem.initializeParkingLot();
        ArrayList emptySlotList = parkingLotSystem.getEmptyParkingSlot();
        assertEquals(expectedList, emptySlotList);
    }

    @Test
    public void AfterParkingAndUnParkingVehicles_ShouldReturnAvailableSlots() {
        List expectedList = new ArrayList();
        expectedList.add(0);
        expectedList.add(2);
        parkingLotSystem.setActualCapacity(3);
        parkingLotSystem.park(vehicle, 0);
        parkingLotSystem.park(new Object(), 1);
        parkingLotSystem.unPark(vehicle);
        ArrayList emptySlotList = parkingLotSystem.getEmptyParkingSlot();
        assertEquals(expectedList, emptySlotList);
    }

    @Test
    public void givenVehicleForParkingOnEmptySlot_WhenParkWithProvidedEmptySlot_ShouldReturnTrue() {
        parkingLotSystem.setActualCapacity(10);
        parkingLotSystem.initializeParkingLot();
        ArrayList<Integer> emptySlotList = parkingLotSystem.getEmptyParkingSlot();
        parkingLotSystem.park(vehicle, emptySlotList.get(0));
        boolean vehiclePark = parkingLotSystem.isVehicleParked(vehicle);
        assertTrue(vehiclePark);
    }

    //UC7
    @Test
    public void givenVehicle_WhenVehicleFound_ShouldReturnVehicleParkingSlotNumber() {
        parkingLotSystem.park(new Object());
        parkingLotSystem.park(vehicle);
        int slotNumber = parkingLotSystem.findVehicle(vehicle);
        assertEquals(1, slotNumber);
    }

    @Test
    public void givenVehicle_WhenVehicleNotFound_ShouldThrowVehicleNotFoundException() {
        parkingLotSystem.park(vehicle);
        try {
            parkingLotSystem.findVehicle(new Object());
        } catch (ParkingLotException e) {
            assertEquals(ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND, e.exceptionTypes);
        }
    }

    //UC8
    @Test
    public void givenVehicleForParking_WhenVehicleParkedTimeIsSet_ShouldReturnParkingTime() {

        int parkingTime = parkingLotSystem.park(vehicle);
        assertEquals(owner.getParkingTime(), parkingTime);
    }

    //UC9
    @Test
    public void givenMultipleCarsLessThanActualCapacity_WhenParkEvenly_shouldReturnLastIndexEmpty() {
        parkingLotSystem.setActualCapacity(5);
        parkingLotSystem.park(vehicle);
        parkingLotSystem.park(new Object());
        parkingLotSystem.park(new Object());
        parkingLotSystem.park(new Object());
        parkingLotSystem.unPark(vehicle);
        parkingLotSystem.park(new Object());
        Object lastEmptySlot = parkingLotSystem.getEmptyParkingSlot().get(0);
        System.out.println(parkingLotSystem.vehicles);
        assertEquals(4, lastEmptySlot);
    }
}