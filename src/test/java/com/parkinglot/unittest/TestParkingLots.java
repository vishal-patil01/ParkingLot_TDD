package com.parkinglot.unittest;

import com.parkinglot.dao.Vehicle;
import com.parkinglot.enums.DriverTypes;
import com.parkinglot.ParkingLots;
import com.parkinglot.ParkingLotsManagementSystem;
import com.parkinglot.enums.VehicleType;
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
import static org.mockito.Mockito.*;

public class TestParkingLots {
    @Mock
    ParkingLotsManagementSystem parkingLotsManagementSystem;
    ParkingLots parkingLots;
    DriverTypes driverTypes;
    Vehicle vehicle;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {
        parkingLotsManagementSystem = mock(ParkingLotsManagementSystem.class);
        vehicle = new Vehicle();
        parkingLots = new ParkingLots(2);
    }

    @Test
    public void testParkFunction() {
        doAnswer((Answer<Boolean>) invocationOnMock -> {
            parkingLots.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
            return true;
        }).when(parkingLotsManagementSystem).park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        boolean isParked = parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        assertTrue(isParked);
    }

    @Test
    public void testUnParkFunction() {
        when(parkingLotsManagementSystem.unPark(vehicle)).thenReturn(true);
        parkingLotsManagementSystem.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        boolean isParked = parkingLotsManagementSystem.unPark(vehicle);
        assertTrue(isParked);
    }

    @Test
    public void testFindVehicleFunction() {
        doAnswer((Answer<Void>) invocationOnMock -> {
            parkingLots.findVehicle(vehicle);
            return null;
        }).when(parkingLotsManagementSystem).findVehicle(vehicle);
        try {
            parkingLotsManagementSystem.findVehicle(vehicle);
        } catch (ParkingLotException e) {
            assertSame(ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND, e.exceptionTypes);
        }
    }
}
