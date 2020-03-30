package com.parkinglot.unittest;

import com.parkinglot.enums.DriverTypes;
import com.parkinglot.ParkingLots;
import com.parkinglot.ParkingLotsManagementSystem;
import com.parkinglot.exceptions.ParkingLotException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class TestParkingLots {
    @Mock
    ParkingLotsManagementSystem parkingLotsManagementSystem;
    ParkingLots parkingLots;
    DriverTypes driverTypes;
    Object vehicle;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {
        parkingLotsManagementSystem = mock(ParkingLotsManagementSystem.class);
        vehicle = new Object();
        parkingLots = new ParkingLots(2);
    }

    @Test
    public void testParkFunction() {
        doAnswer((Answer<Void>) invocationOnMock -> {
            parkingLots.park(vehicle, DriverTypes.NORMAL);
            return null;
        }).when(parkingLotsManagementSystem).park(vehicle, DriverTypes.NORMAL);
        boolean isParked = parkingLots.park(vehicle, DriverTypes.NORMAL);
        assertTrue(isParked);
    }

    @Test
    public void testUnParkFunction() {
        doAnswer((Answer<Void>) invocationOnMock -> {
            parkingLots.unPark(vehicle);
            return null;
        }).when(parkingLotsManagementSystem).unPark(vehicle);
        parkingLots.park(vehicle, DriverTypes.NORMAL);
        boolean isParked = parkingLots.unPark(vehicle);
        assertTrue(isParked);
    }

    @Test
    public void testFindVehicleFunction() {
        doAnswer((Answer<Void>) invocationOnMock -> {
            parkingLots.findVehicle(vehicle);
            return null;
        }).when(parkingLotsManagementSystem).findVehicle(vehicle);
        try {
            parkingLots.findVehicle(vehicle);
        } catch (ParkingLotException e) {
            assertSame(ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND, e.exceptionTypes);
        }
    }
}
