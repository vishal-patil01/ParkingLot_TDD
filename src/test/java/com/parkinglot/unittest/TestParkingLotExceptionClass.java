package com.parkinglot.unittest;

import com.parkinglot.ParkingLotOwner;
import com.parkinglot.ParkingLotSystem;
import com.parkinglot.exceptions.ParkingLotException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TestParkingLotExceptionClass {
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

    @Test(expected = ParkingLotException.class)
    public void testParkingLotExceptionClass_ThrowParkingLotException_WhenCallingParkFunction() {
        doThrow(ParkingLotException.class)
                .when(parkingLotSystem).park(any(), any(ParkingLotSystem.DriverType.class));
        parkingLotSystem.park(1, ParkingLotSystem.DriverType.NORMAL);
    }

    @Test
    public void testVehicleAlReadyParkedException_WhenVehicleObjectPassedToParkFunction_ThrowAnException() {
        when(parkingLotSystem.park(any(),any())).thenAnswer(
                (Answer) invocation -> {
                    if (invocation.getArgument(0).equals(vehicle)){
                        throw new ParkingLotException("",ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED);
                    }
                    throw new ParkingLotException("",ParkingLotException.ExceptionTypes.PARKING_LOT_FULL);
                });
        try {
            parkingLotSystem.park(vehicle, ParkingLotSystem.DriverType.NORMAL);
        }
        catch (ParkingLotException e)
        {
            assertEquals(e.exceptionTypes,ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED);
        }
    }
    @Test
    public void testParkingLotFullException_WhenAnotherObjectPassedToParkFunction_ThrowAnException() {
        when(parkingLotSystem.park(any(),any())).thenAnswer(
                (Answer) invocation -> {
                    if (invocation.getArgument(0)==vehicle){
                        throw new ParkingLotException("",ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED);
                    }
                    throw new ParkingLotException("",ParkingLotException.ExceptionTypes.PARKING_LOT_FULL);
                });
        try {
            parkingLotSystem.park(new Object(), ParkingLotSystem.DriverType.NORMAL);
        }
        catch (ParkingLotException e)
        {
            assertEquals(e.exceptionTypes,ParkingLotException.ExceptionTypes.PARKING_LOT_FULL);
        }
    }

    @Test
    public void testVehicleNotFoundException_WhenNewObjectPassedToUnParkFunctionNotMatchesVehicleObject_ThrowVehicleNotFoundException() {
        when(parkingLotSystem.unPark(any())).thenAnswer(
                (Answer) invocation -> {
                    if (invocation.getArgument(0)==vehicle){
                        return "unParked";
                    }
                    throw new ParkingLotException("",ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
                });
        try {
            parkingLotSystem.unPark(new Object());
        }
        catch (ParkingLotException e)
        {
            assertEquals(e.exceptionTypes,ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
        }
    }
}
