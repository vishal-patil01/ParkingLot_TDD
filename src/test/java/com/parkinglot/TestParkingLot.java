package com.parkinglot;

import com.parkinglot.exceptions.ParkingLotException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestParkingLot {
    ParkingLotSystem parkingLotSystem;
    ParkingLotOwner owner;
    AirportSecurity airportSecurity;
    Object vehicle;

    @Before
    public void setup() {
        vehicle = new Object();
        parkingLotSystem = new ParkingLotSystem(2);
        owner = new ParkingLotOwner();
        airportSecurity = new AirportSecurity();
        parkingLotSystem.registerObserver(owner);
    }

    @Test
    public void givenVehicle_WhenParked_ShouldReturnTrue() {
        parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
        boolean isParked = parkingLotSystem.isVehicleParked(vehicle);
        assertTrue(isParked);
    }

    @Test
    public void givenVehicle_WhenAlReadyParked_ShouldReturnFalse() {
        try {
            parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
            parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
        } catch (ParkingLotException e) {
            assertEquals(ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED, e.exceptionTypes);
        }
    }

    @Test
    public void givenVehicle_WhenUnParked_ShouldReturnTrue() {
        parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
        boolean isUnParked = parkingLotSystem.unPark(vehicle);
        assertTrue(isUnParked);
    }

    @Test
    public void givenWhenParkingLotIsFull_ShouldInformTheOwner() {
        parkingLotSystem.registerObserver(owner);
        try {
            parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
            parkingLotSystem.park(new Object(), ParkingLotSystem.DriverType.NORMAL);
            parkingLotSystem.park(new Object(), ParkingLotSystem.DriverType.NORMAL);
        } catch (ParkingLotException e) {
            boolean capacityFull = owner.isCapacityFull();
            assertTrue(capacityFull);
        }
    }

    @Test
    public void givenCapacityIs2_ShouldAbleToPark2Vehicle() {
        Object vehicle2 = new Object();
        parkingLotSystem.setActualCapacity(2);
        parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
        parkingLotSystem.park(vehicle2, ParkingLotSystem.DriverType.NORMAL);
        boolean isParked1 = parkingLotSystem.isVehicleParked(vehicle);
        boolean isParked2 = parkingLotSystem.isVehicleParked(vehicle2);
        assertTrue(isParked1 && isParked2);
    }

    @Test
    public void givenWhenLotIsFull_ShouldInformTheSecurity() {
        parkingLotSystem.registerObserver(airportSecurity);
        try {
            parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
            parkingLotSystem.park(new Object(), ParkingLotSystem.DriverType.NORMAL);
            parkingLotSystem.park(new Object(), ParkingLotSystem.DriverType.NORMAL);
        } catch (ParkingLotException e) {
            boolean capacityFull = airportSecurity.isCapacityFull();
            assertTrue(capacityFull);
        }
    }

    @Test
    public void givenVehicle_WhenLotSpaceIsAvailableAfterFull_ShouldInformTheOwnerAndReturnFalse() {
        Object vehicle2 = new Object();
        parkingLotSystem.registerObserver(owner);
        parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
        parkingLotSystem.park(vehicle2, ParkingLotSystem.DriverType.NORMAL);
        parkingLotSystem.unPark(vehicle);
        assertFalse(owner.isCapacityFull());
    }

    @Test
    public void givenVehicle_WhenLotSpaceIsAvailableAfterFull_ShouldInformTheAirPortSecurityAndReturnFalse() {
        parkingLotSystem.registerObserver(airportSecurity);
        parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
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
        parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL, 0);
        parkingLotSystem.park(new Object(), ParkingLotSystem.DriverType.NORMAL, 1);
        parkingLotSystem.unPark(vehicle);
        ArrayList emptySlotList = parkingLotSystem.getEmptyParkingSlot();
        assertEquals(expectedList, emptySlotList);
    }

    @Test
    public void givenVehicleForParkingOnEmptySlot_WhenParkWithProvidedEmptySlot_ShouldReturnTrue() {
        parkingLotSystem.setActualCapacity(10);
        parkingLotSystem.initializeParkingLot();
        ArrayList<Integer> emptySlotList = parkingLotSystem.getEmptyParkingSlot();
        parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL, emptySlotList.get(0));
        boolean vehiclePark = parkingLotSystem.isVehicleParked(vehicle);
        assertTrue(vehiclePark);
    }

    //UC7
    @Test
    public void givenVehicle_WhenVehicleFound_ShouldReturnVehicleParkingSlotNumber() {
        parkingLotSystem.park(new Object(), ParkingLotSystem.DriverType.NORMAL);
        parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
        int slotNumber = parkingLotSystem.findVehicle(vehicle);
        assertEquals(0, slotNumber);
    }

    @Test
    public void givenVehicle_WhenVehicleNotFound_ShouldThrowVehicleNotFoundException() {
        parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
        try {
            parkingLotSystem.findVehicle(new Object());
        } catch (ParkingLotException e) {
            assertEquals(ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND, e.exceptionTypes);
        }
    }

    //UC8
    @Test
    public void givenVehicleForParking_WhenVehicleParkedTimeIsSet_ShouldReturnParkingTime() {

        int parkingTime = parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
        assertEquals(owner.getParkingTime(), parkingTime);
    }

    //UC9
    @Test
    public void givenMultipleCarsLessThanActualCapacity_WhenParkEvenly_shouldReturnFirstIndexEmpty() {
        parkingLotSystem.setActualCapacity(5);
        parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
        parkingLotSystem.park(new Object(), ParkingLotSystem.DriverType.NORMAL);
        parkingLotSystem.park(new Object(), ParkingLotSystem.DriverType.NORMAL);
        parkingLotSystem.park(new Object(), ParkingLotSystem.DriverType.NORMAL);
        parkingLotSystem.unPark(vehicle);
        parkingLotSystem.park(new Object(), ParkingLotSystem.DriverType.NORMAL);
        Object lastEmptySlot = parkingLotSystem.getEmptyParkingSlot().get(0);
        assertEquals(0, lastEmptySlot);
    }

    //UC10
    @Test
    public void givenCarToPark_whenDriverIsHandicap_shouldParkedAtNearestSpot() {
        parkingLotSystem.setActualCapacity(5);
        Object vehicle2 = new Object();
        parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
        parkingLotSystem.park(new Object(), ParkingLotSystem.DriverType.NORMAL);
        parkingLotSystem.park(vehicle2, ParkingLotSystem.DriverType.NORMAL);
        parkingLotSystem.park(new Object(), ParkingLotSystem.DriverType.HANDICAP);
        parkingLotSystem.unPark(vehicle2);
        parkingLotSystem.unPark(vehicle);
        parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
        parkingLotSystem.park(vehicle2, ParkingLotSystem.DriverType.HANDICAP);
        int vehicleParkedLocation = parkingLotSystem.findVehicle(vehicle2);
        assertEquals(1, vehicleParkedLocation);
    }
}