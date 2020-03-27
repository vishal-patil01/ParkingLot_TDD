package com.parkinglot.unittest;

import com.parkinglot.AirportSecurity;
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

public class TestAirportSecurityClass {
    @Mock
    ParkingLotSystem parkingLotSystem;
    AirportSecurity airportSecurity;
    Object vehicle;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {
        parkingLotSystem = mock(ParkingLotSystem.class);
        airportSecurity = new AirportSecurity();
        vehicle = new Object();
    }

    @Test
    public void testSetCapacityFull_And_isCapacityFullFunction() {
        doAnswer((Answer<Void>) invocationOnMock -> {
            airportSecurity.setCapacityFull();
            return null;
        }).when(parkingLotSystem).park(vehicle, ParkingLotSystem.DriverType.NORMAL);
        parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
        assertTrue(airportSecurity.isCapacityFull());
    }
}
