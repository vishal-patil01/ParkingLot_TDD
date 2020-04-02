package com.parkinglot.unittest;

import com.parkinglot.Observers.ParkingLotOwner;
import com.parkinglot.ParkingLots;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TestParkingLotExceptionClass {
    @Mock
    ParkingLots parkingLots;
    ParkingLotOwner owner;
    Vehicle vehicle;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {
        parkingLots = mock(ParkingLots.class);
        owner = new ParkingLotOwner();
        vehicle = new Vehicle();
    }

    @Test(expected = ParkingLotException.class)
    public void testParkingLotExceptionClass_ThrowParkingLotException_WhenCallingParkFunction() {
        doThrow(ParkingLotException.class)
                .when(parkingLots).park(any(), any(DriverTypes.class),any(VehicleType.class));
        parkingLots.park(new Vehicle(), DriverTypes.NORMAL,VehicleType.SMALL);
    }

    @Test
    public void testVehicleAlReadyParkedException_WhenVehicleVehiclePassedToParkFunction_ThrowAnException() {
        when(parkingLots.park(any(), any(),any())).thenAnswer(
                (Answer) invocation -> {
                    if (invocation.getArgument(0).equals(vehicle)) {
                        throw new ParkingLotException("", ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED);
                    }
                    throw new ParkingLotException("", ParkingLotException.ExceptionTypes.PARKING_LOT_FULL);
                });
        try {
            parkingLots.park(vehicle, DriverTypes.NORMAL, VehicleType.SMALL);
        } catch (ParkingLotException e) {
            assertEquals(e.exceptionTypes, ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED);
        }
    }

    @Test
    public void testParkingLotFullException_WhenAnotherVehiclePassedToParkFunction_ThrowAnException() {
        when(parkingLots.park(any(), any(),any())).thenAnswer(
                (Answer) invocation -> {
                    if (invocation.getArgument(0) == vehicle) {
                        throw new ParkingLotException("", ParkingLotException.ExceptionTypes.VEHICLE_ALREADY_PARKED);
                    }
                    throw new ParkingLotException("", ParkingLotException.ExceptionTypes.PARKING_LOT_FULL);
                });
        try {
            parkingLots.park(new Vehicle(), DriverTypes.NORMAL,VehicleType.SMALL);
        } catch (ParkingLotException e) {
            assertEquals(e.exceptionTypes, ParkingLotException.ExceptionTypes.PARKING_LOT_FULL);
        }
    }

    @Test
    public void testVehicleNotFoundException_WhenNewVehiclePassedToUnParkFunctionNotMatchesVehicle_ThrowVehicleNotFoundException() {
        when(parkingLots.unPark(any())).thenAnswer(
                (Answer) invocation -> {
                    if (invocation.getArgument(0) == vehicle) {
                        return "unParked";
                    }
                    throw new ParkingLotException("", ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
                });
        try {
            parkingLots.unPark(new Vehicle());
        } catch (ParkingLotException e) {
            assertEquals(e.exceptionTypes, ParkingLotException.ExceptionTypes.VEHICLE_NOT_FOUND);
        }
    }
}
