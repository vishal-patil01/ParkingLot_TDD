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
        parkingSlot = mock(ParkingSlot.class);
        parkingLots = new ParkingLots(3);
        vehicle = new Vehicle();
    }

    @Test
    public void testParkFunction() {
        boolean park = parkingLots.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        assertTrue(park);
    }
}
