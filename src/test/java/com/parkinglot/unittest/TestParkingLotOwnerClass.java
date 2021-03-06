package com.parkinglot.unittest;

import com.parkinglot.Observers.ParkingAvailabilityInformer;
import com.parkinglot.Observers.ParkingLotOwner;
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

public class TestParkingLotOwnerClass {
    @Mock
    ParkingAvailabilityInformer informer;
    ParkingLotOwner owner;
    Object vehicle;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {
        informer = mock(ParkingAvailabilityInformer.class);
        owner = new ParkingLotOwner();
        vehicle = new Object();
    }

    @Test
    public void testSetParkingLotFull_And_isCapacityFullFunction() {
        informer.register(owner);
        doAnswer((Answer<Void>) invocationOnMock -> {
            owner.setParkingLotFull();
            return null;
        }).when(informer).notifyParkingFull();
        informer.notifyParkingFull();
        assertTrue(owner.isParkingLotFull());
    }

    @Test
    public void testSetParkingAvailable_And_isCapacityFullFunction() {
        informer.register(owner);
        doAnswer((Answer<Void>) invocationOnMock -> {
            owner.setParkingAvailable();
            return null;
        }).when(informer).notifyParkingAvailable();
        informer.notifyParkingAvailable();
        assertFalse(owner.isParkingLotFull());
    }
}
