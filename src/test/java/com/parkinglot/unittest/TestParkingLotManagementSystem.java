package com.parkinglot.unittest;

import com.parkinglot.enums.DriverTypes;
import com.parkinglot.ParkingLots;
import com.parkinglot.ParkingLotsManagementSystem;
import com.parkinglot.enums.VehicleType;
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

public class TestParkingLotManagementSystem {
    @Mock
    ParkingLots parkingLots;
    ParkingLotsManagementSystem parkingLotsManagementSystem;
    Object vehicle;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {
        parkingLots = mock(ParkingLots.class);
        vehicle = new Object();
        parkingLotsManagementSystem = new ParkingLotsManagementSystem();
        parkingLotsManagementSystem.addLot(parkingLots);
    }

    @Test
    public void testParkFunction() {
        when(parkingLots.park(vehicle, DriverTypes.NORMAL,VehicleType.SMALL)).thenReturn(true);
        boolean check = parkingLotsManagementSystem.park(vehicle,DriverTypes.NORMAL, VehicleType.SMALL);
        assertTrue(check);
    }

    @Test
    public void testUnParkFunction() {
        when(parkingLots.unPark(vehicle)).thenReturn(true);
        boolean check = parkingLotsManagementSystem.unPark(vehicle);
        assertTrue(check);
    }

    @Test
    public void testIsVehicleParkedFunction() {
        parkingLotsManagementSystem.park(vehicle,DriverTypes.NORMAL,VehicleType.SMALL);
        when(parkingLots.isVehicleParked(vehicle)).thenReturn(true);
        boolean check = parkingLotsManagementSystem.isVehicleParked(vehicle);
        assertTrue(check);
    }

    @Test
    public void testGetVehicleParkingFunction() {
        parkingLotsManagementSystem.park(vehicle,DriverTypes.NORMAL,VehicleType.SMALL);
        when(parkingLots.getVehicleParkingTime(vehicle)).thenReturn(66);
        int check = parkingLotsManagementSystem.getVehicleParkingTime(vehicle);
        assertSame(66, check);
    }
}
