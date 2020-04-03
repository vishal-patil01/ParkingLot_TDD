package com.parkinglot;

import com.parkinglot.Observers.AirportSecurity;
import com.parkinglot.Observers.ParkingLotOwner;
import com.parkinglot.dao.Vehicle;
import com.parkinglot.enums.DriverTypes;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exceptions.ParkingLotException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

public class IntegrationTestParkingLotSystem {
    ParkingLots parkingLot;
    ParkingLotsManagementSystem parkingLotsManagementSystem;
    ParkingLotOwner owner;
    AirportSecurity airportSecurity;
    Vehicle vehicle;

    @Before
    public void setup() {
        vehicle = new Vehicle();
        parkingLotsManagementSystem = new ParkingLotsManagementSystem();
        parkingLot = new ParkingLots(3);
        parkingLotsManagementSystem.addLot(parkingLot);
        owner = new ParkingLotOwner();
        airportSecurity = new AirportSecurity();
        parkingLotsManagementSystem.register(owner);
    }

    @Test
    public void givenVehicle_WhenParked_ShouldReturnTrue() {
        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        boolean isParked = parkingLot.isVehicleParked(vehicle);
        assertTrue(isParked);
    }

    @Test
    public void givenVehicle_WhenAlReadyParked_ShouldReturnFalse() {
        try {
            parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
            parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        } catch (ParkingLotException e) {
            assertEquals(ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED, e.exceptionTypes);
        }
    }

    @Test
    public void givenVehicle_WhenUnParked_ShouldReturnTrue() {
        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        boolean isUnParked = parkingLotsManagementSystem.unPark(vehicle);
        assertTrue(isUnParked);
    }

    @Test
    public void givenVehicle_WhenIsNotAlreadyParked_ShouldThrowVehicleNotFoundException() {
        try {
            parkingLotsManagementSystem.unPark(vehicle);
        } catch (ParkingLotException e) {
            assertEquals(ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND, e.exceptionTypes);
        }
    }

    @Test
    public void givenWhenParkingLotIsFull_ShouldInformTheOwner() {
        parkingLotsManagementSystem.register(owner);
        try {
            parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
            parkingLotsManagementSystem.park(new Vehicle(), DriverTypes.NORMAL, VehicleType.SMALL);
            parkingLotsManagementSystem.park(new Vehicle(), DriverTypes.NORMAL, VehicleType.SMALL);
        } catch (ParkingLotException e) {
            boolean capacityFull = owner.isParkingLotFull();
            assertTrue(capacityFull);
        }
    }

    @Test
    public void givenCapacityIs2_ShouldAbleToPark2Vehicle() {
        Vehicle vehicle2 = new Vehicle();
        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        parkingLotsManagementSystem.park(vehicle2, DriverTypes.NORMAL, VehicleType.SMALL);
        boolean isParked1 = parkingLotsManagementSystem.isVehicleParked(vehicle);
        boolean isParked2 = parkingLotsManagementSystem.isVehicleParked(vehicle2);
        assertTrue(isParked1 && isParked2);
    }

    @Test
    public void givenWhenLotIsFull_ShouldInformTheSecurity() {
        parkingLotsManagementSystem.register(airportSecurity);
        try {
            parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
            parkingLotsManagementSystem.park(new Vehicle(), DriverTypes.NORMAL, VehicleType.SMALL);
            parkingLotsManagementSystem.park(new Vehicle(), DriverTypes.NORMAL, VehicleType.SMALL);
        } catch (ParkingLotException e) {
            boolean capacityFull = airportSecurity.isParkingLotFull();
            assertTrue(capacityFull);
        }
    }

    @Test
    public void givenVehicle_WhenLotSpaceIsAvailableAfterFull_ShouldInformTheOwnerAndReturnFalse() {
        Vehicle vehicle2 = new Vehicle();
        parkingLotsManagementSystem.register(owner);
        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        parkingLotsManagementSystem.park(vehicle2, DriverTypes.NORMAL, VehicleType.SMALL);
        parkingLotsManagementSystem.unPark(vehicle);
        assertFalse(owner.isParkingLotFull());
    }

    @Test
    public void givenVehicle_WhenLotSpaceIsAvailableAfterFull_ShouldInformTheAirPortSecurityAndReturnFalse() {
        parkingLotsManagementSystem.register(airportSecurity);
        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        parkingLotsManagementSystem.unPark(vehicle);
        assertFalse(airportSecurity.isParkingLotFull());
    }

    //UC6
    @Test
    public void givenParkingLotCapacity_WhenInitialize_ShouldReturnParkingCapacity() {
        parkingLot.setParkingLotCapacity(10);
        int parkingLotCapacity = parkingLot.initializeParkingLot();
        assertEquals(10, parkingLotCapacity);
    }

    @Test
    public void givenParkingLot_ShouldReturnAvailableSlots() {
        ArrayList<Integer> expectedList = new ArrayList<>();
        expectedList.add(0);
        expectedList.add(1);
        parkingLot.setParkingLotCapacity(2);
        parkingLot.initializeParkingLot();
        List<Integer> emptySlotList = parkingLot.getListOfEmptyParkingSlots();
        assertEquals(expectedList, emptySlotList);
    }

    @Test
    public void AfterParkingAndUnParkingVehicles_ShouldReturnAvailableSlots() {
        ArrayList<Integer> expectedList = new ArrayList<>();
        expectedList.add(0);
        expectedList.add(2);
        parkingLot.setParkingLotCapacity(3);
        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        parkingLotsManagementSystem.park(new Vehicle(), DriverTypes.NORMAL, VehicleType.SMALL);
        parkingLotsManagementSystem.unPark(vehicle);
        List<Integer> emptySlotList = parkingLot.getListOfEmptyParkingSlots();
        assertEquals(expectedList, emptySlotList);
    }

    @Test
    public void givenVehicleForParkingOnEmptySlot_WhenParkWithProvidedEmptySlot_ShouldReturnTrue() {
        parkingLot.setParkingLotCapacity(10);
        parkingLot.initializeParkingLot();
        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        boolean vehiclePark = parkingLot.isVehicleParked(vehicle);
        assertTrue(vehiclePark);
    }

    //UC7
    @Test
    public void givenVehicle_WhenVehicleFound_ShouldReturnVehicleParkingSlotNumber() {
        parkingLotsManagementSystem.park(new Vehicle(), DriverTypes.NORMAL, VehicleType.SMALL);
        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        int slotNumber = parkingLotsManagementSystem.findVehicle(vehicle);
        assertEquals(1, slotNumber);
    }

    @Test
    public void givenVehicle_WhenVehicleNotFound_ShouldThrowVehicleNotFoundException() {
        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        try {
            parkingLotsManagementSystem.findVehicle(new Vehicle());
        } catch (ParkingLotException e) {
            assertEquals(ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND, e.exceptionTypes);
        }
    }

    //UC8
    @Test
    public void givenVehicleForParking_WhenVehicleParkedTimeIsSet_ShouldReturnParkingTime() {
        int parkingTime = (int) ((System.currentTimeMillis() / (1000 * 60)) % 60);
        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        int vehicleParkingTime = parkingLotsManagementSystem.getVehicleParkingTime(vehicle);
        assertEquals(parkingTime, vehicleParkingTime);
    }

    @Test
    public void givenVehicle_WhenVehicleNotParkedAndCheckingParkingTime_ShouldThrowVehicleNotFoundException() {
        try {
            parkingLotsManagementSystem.getVehicleParkingTime(vehicle);
        } catch (ParkingLotException e) {
            assertEquals(ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND, e.exceptionTypes);
        }
    }

    //UC9
    @Test
    public void givenMultipleCarParkingLots_WhenAdded_ShouldReturnTrue() {
        ParkingLots parkingLot1 = new ParkingLots(3);
        parkingLotsManagementSystem.addLot(parkingLot);
        parkingLotsManagementSystem.addLot(parkingLot1);
        boolean lotAdded = parkingLotsManagementSystem.isLotAdded(parkingLot);
        boolean lot1Added = parkingLotsManagementSystem.isLotAdded(parkingLot1);
        assertTrue(lotAdded && lot1Added);
    }

    @Test
    public void givenParkingLotSystem_WhenVehicleShouldParkInEvenlyInDistributedLots_ShouldReturnTrue() {
        parkingLot.setParkingLotCapacity(5);
        parkingLot.initializeParkingLot();
        //   parkingLotsManagementSystem.addLot(parkingLot);

        ParkingLots parkingLot1 = new ParkingLots(3);
        parkingLot1.setParkingLotCapacity(3);
        parkingLot1.initializeParkingLot();

        Vehicle vehicle2 = new Vehicle();
        Vehicle vehicle3 = new Vehicle();
        Vehicle vehicle4 = new Vehicle();
        parkingLotsManagementSystem.addLot(parkingLot1);

        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        boolean isVehiclePark1 = parkingLotsManagementSystem.isVehicleParked(vehicle);
        parkingLotsManagementSystem.park(vehicle2, DriverTypes.NORMAL, VehicleType.SMALL);
        boolean isVehiclePark2 = parkingLotsManagementSystem.isVehicleParked(vehicle2);
        parkingLotsManagementSystem.park(vehicle3, DriverTypes.NORMAL, VehicleType.SMALL);
        boolean isVehiclePark3 = parkingLotsManagementSystem.isVehicleParked(vehicle3);
        parkingLotsManagementSystem.park(vehicle4, DriverTypes.NORMAL, VehicleType.SMALL);
        boolean isVehiclePark4 = parkingLotsManagementSystem.isVehicleParked(vehicle4);
        assertTrue(isVehiclePark1 && isVehiclePark2 && isVehiclePark3 && isVehiclePark4);
    }

    // UC10
    @Test
    public void givenCarToPark_whenDriverIsHandicap_shouldParkedAtNearestSpot() {
        parkingLot.setParkingLotCapacity(5);
        Vehicle vehicle2 = new Vehicle();
        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        parkingLotsManagementSystem.park(new Vehicle(), DriverTypes.NORMAL, VehicleType.SMALL);
        parkingLotsManagementSystem.park(vehicle2, DriverTypes.NORMAL, VehicleType.SMALL);
        parkingLotsManagementSystem.park(new Vehicle(), DriverTypes.HANDICAP, VehicleType.SMALL);
        parkingLotsManagementSystem.unPark(vehicle2);
        parkingLotsManagementSystem.unPark(vehicle);
        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        parkingLotsManagementSystem.park(vehicle2, DriverTypes.HANDICAP, VehicleType.SMALL);
        int vehicleParkedLocation = parkingLotsManagementSystem.findVehicle(vehicle2);
        assertEquals(1, vehicleParkedLocation);
    }

    //uc11
    @Test
    public void givenLargeVehicle_WhenVehicleShouldParked_ShouldReturnMiddleSlotNumber() {
        parkingLot.setParkingLotCapacity(4);
        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.LARGE);
        int expectedSlot = 1;
        int actualSlot = parkingLotsManagementSystem.findVehicle(vehicle);
        assertSame(expectedSlot, actualSlot);
    }

    @Test
    public void givenVehicles_WhenVehicleShouldParkedAccordinglyType_ShouldReturnExpectedSlotNumbers() {
        parkingLot.setParkingLotCapacity(6);
        ArrayList<Integer> expectedVehicles = new ArrayList<>();
        expectedVehicles.add(2);
        expectedVehicles.add(3);
        expectedVehicles.add(4);
        parkingLotsManagementSystem.park(vehicle, DriverTypes.HANDICAP, VehicleType.LARGE);
        parkingLotsManagementSystem.park(new Vehicle(), DriverTypes.HANDICAP, VehicleType.SMALL);
        parkingLotsManagementSystem.park(new Vehicle(), DriverTypes.NORMAL, VehicleType.SMALL);
        ArrayList<Integer> emptyParkingSlots = parkingLot.getListOfEmptyParkingSlots();
        assertEquals(expectedVehicles, emptyParkingSlots);
    }

    //uc12
    @Test
    public void givenVehicleColour_WhenFindVehicleAccordinglyColour_ShouldReturnVehicleSlotNumber() {
        Vehicle vehicle2 = new Vehicle("White");
        Vehicle vehicle3 = new Vehicle("White");
        parkingLot.setParkingLotCapacity(5);
        ArrayList<Integer> expectedVehicles = new ArrayList<>();
        expectedVehicles.add(2);
        expectedVehicles.add(4);
        parkingLotsManagementSystem.park(vehicle2, DriverTypes.NORMAL, VehicleType.LARGE);
        parkingLotsManagementSystem.park(vehicle3, DriverTypes.NORMAL, VehicleType.LARGE);
        parkingLotsManagementSystem.park(new Vehicle(), DriverTypes.NORMAL, VehicleType.SMALL);
        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        ArrayList<Integer> vehicleDetailsListBasedOnFilters = parkingLotsManagementSystem.findVehicleByColor("White");
        assertEquals(expectedVehicles, vehicleDetailsListBasedOnFilters);
    }

    //uc13
    @Test
    public void givenVehicleModelNumberAndColor_WhenFindVehicleAccordinglyModelNumberAndColor_ShouldReturnFilteredVehicleInformation() {
        parkingLot.setParkingLotCapacity(8);
        ArrayList<String> expectedVehicles = new ArrayList<>();
        expectedVehicles.add(1+" " +"MH-12-V123");
        Vehicle vehicle1 = new Vehicle("white", "MH-19", "toyota");
        Vehicle vehicle2 = new Vehicle("blue", "MH-12", "BMW");
        Vehicle vehicle3 = new Vehicle("blue","MH-12-V123", "toyota");
        Vehicle vehicle4 = new Vehicle("white", "xy123", "BMW");

        parkingLotsManagementSystem.park(vehicle1, DriverTypes.NORMAL, VehicleType.LARGE);
        parkingLotsManagementSystem.park(vehicle2, DriverTypes.NORMAL, VehicleType.LARGE);
        parkingLotsManagementSystem.park(vehicle3, DriverTypes.NORMAL, VehicleType.LARGE);
        parkingLotsManagementSystem.park(vehicle4, DriverTypes.NORMAL, VehicleType.LARGE);
        parkingLotsManagementSystem.park(new Vehicle(), DriverTypes.NORMAL, VehicleType.SMALL);
        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        ArrayList<String> vehicleDetailsListBasedOnFilters = parkingLotsManagementSystem.findVehicleByMultipleValue("blue","toyota");
        assertEquals(expectedVehicles, vehicleDetailsListBasedOnFilters);
    }
}