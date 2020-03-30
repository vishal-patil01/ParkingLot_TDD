package com.parkinglot.unittest;

import com.parkinglot.Observers.AirportSecurity;
import com.parkinglot.Observers.ParkingAvailabilityInformer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class TestAirportSecurityClass {
    @Mock
    ParkingAvailabilityInformer informer;
    AirportSecurity security;
    Object vehicle;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {
        informer = mock(ParkingAvailabilityInformer.class);
        security = new AirportSecurity();
        vehicle = new Object();
    }
    @Test
    public void testSetParkingLotFull_And_isCapacityFullFunction() {
        informer.register(security);
        doAnswer((Answer<Void>) invocationOnMock -> {
            security.setParkingLotFull();
            return null;
        }).when(informer).notifyParkingFull();
        informer.notifyParkingFull();
        assertTrue(security.isParkingLotFull());
    }

    @Test
    public void testSetParkingAvailable_And_isCapacityFullFunction() {
        informer.register(security);
        doAnswer((Answer<Void>) invocationOnMock -> {
            security.setParkingAvailable();
            return null;
        }).when(informer).notifyParkingAvailable();
        informer.notifyParkingAvailable();
        assertFalse(security.isParkingLotFull());
    }
}
