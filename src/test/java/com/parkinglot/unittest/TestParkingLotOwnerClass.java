package com.parkinglot.unittest;

import com.parkinglot.ParkingLotOwner;
import com.parkinglot.ParkingLotSystem;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class TestParkingLotOwnerClass {
    @Mock
    ParkingLotSystem parkingLotSystem;
    ParkingLotOwner owner;
    Object vehicle;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {
        parkingLotSystem = mock(ParkingLotSystem.class);
        owner = new ParkingLotOwner();
        vehicle = new Object();
    }

    @Test
    public void testSetCapacityFull_And_isCapacityFullFunction() {
        doAnswer((Answer<Void>) invocationOnMock -> {
            owner.setCapacityFull();
            return null;
        }).when(parkingLotSystem).park(vehicle, ParkingLotSystem.DriverType.NORMAL);
        parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
        assertTrue(owner.isCapacityFull());
    }
}
