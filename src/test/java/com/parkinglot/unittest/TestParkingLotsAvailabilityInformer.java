package com.parkinglot.unittest;

import com.parkinglot.Observers.AirportSecurity;
import com.parkinglot.dao.Vehicle;
import com.parkinglot.enums.DriverTypes;
import com.parkinglot.Observers.ParkingAvailabilityInformer;
import com.parkinglot.Observers.ParkingLotOwner;
import com.parkinglot.ParkingLots;
import com.parkinglot.enums.VehicleType;
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

public class TestParkingLotsAvailabilityInformer {
    @Mock
    ParkingLots parkingLots;
    ParkingAvailabilityInformer informer;
    ParkingLotOwner owner;
    AirportSecurity security;
    Vehicle vehicle;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {
        parkingLots = mock(ParkingLots.class);
        security = new AirportSecurity();
        owner = new ParkingLotOwner();
        informer = ParkingAvailabilityInformer.getInstance();
        vehicle = new Vehicle();
    }

    @Test
    public void testNotifyParkingAvailable() {
        informer.register(owner);
        informer.register(security);
        doAnswer((Answer<Void>) invocationOnMock -> {
            informer.notifyParkingAvailable();
            return null;
        }).when(parkingLots).park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        parkingLots.park(vehicle, DriverTypes.NORMAL,VehicleType.SMALL);
        assertFalse(owner.isParkingLotFull() && security.isParkingLotFull());
    }

    @Test
    public void testNotifyParkingFull() {
        informer.register(owner);
        informer.register(security);
        doAnswer((Answer<Void>) invocationOnMock -> {
            informer.notifyParkingFull();
            return null;
        }).when(parkingLots).park(vehicle, DriverTypes.NORMAL,VehicleType.SMALL);
        parkingLots.park(vehicle, DriverTypes.NORMAL,VehicleType.SMALL);
        assertTrue(owner.isParkingLotFull() && security.isParkingLotFull());
    }
}
