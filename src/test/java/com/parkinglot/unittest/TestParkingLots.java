package com.parkinglot.unittest;

import com.parkinglot.ParkingLots;
import com.parkinglot.ParkingLotsManagementSystem;
import com.parkinglot.dao.ParkingSlot;
import com.parkinglot.dao.Vehicle;
import com.parkinglot.enums.DriverTypes;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exceptions.ParkingLotException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import java.time.LocalDateTime;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TestParkingLots {
    @Mock
    ParkingSlot parkingSlot;
    ParkingLots parkingLots;
    Vehicle vehicle;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {
        parkingLots = new ParkingLots(3);
        parkingSlot = mock(ParkingSlot.class);
        vehicle = new Vehicle();
    }

//    @Test
//    public void testParkFunction() {
//        when(new ParkingSlot(vehicle)).thenReturn(new ParkingSlot(vehicle, VehicleType.SMALL, DriverTypes.NORMAL));
//        boolean park = parkingLots.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
//        assertTrue(park);
//    }
//
//    @Test
//    public void testUnParkFunction() {
//        when(parkingLotsManagementSystem.getParkingLotHavingMaxSpace()).thenReturn(new ParkingLots(2));
//        parkingLots = parkingLotsManagementSystem.getParkingLotHavingMaxSpace();
//        parkingLots.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
//        boolean unPark = parkingLots.unPark(vehicle);
//        assertTrue(unPark);
//    }
//
//    @Test
//    public void testFindVehicleFunction() {
//        when(parkingLotsManagementSystem.getParkingLotHavingMaxSpace()).thenReturn(new ParkingLots(2));
//        parkingLots = parkingLotsManagementSystem.getParkingLotHavingMaxSpace();
//        parkingLots.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
//        int vehicle = parkingLots.findVehicle(this.vehicle);
//        assertSame(1, vehicle);
//    }
}
