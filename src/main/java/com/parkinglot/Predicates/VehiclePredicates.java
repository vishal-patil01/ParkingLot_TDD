package com.parkinglot.Predicates;

import com.parkinglot.dao.ParkingSlot;
import com.parkinglot.enums.DriverTypes;
import com.parkinglot.enums.VehicleType;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.IntPredicate;

public class VehiclePredicates {
    public static List<ParkingSlot> vehiclesList;

    public static void initializePredicate(List<ParkingSlot> vehicles) {
        vehiclesList = vehicles;
    }

    public static IntPredicate filterByParkedVehicles() {
        return slot -> vehiclesList.get(slot) != null;
    }

    public static IntPredicate colorFilter(String color) {
        return slot -> Objects.equals(vehiclesList.get(slot).getVehicle().getColor(), color);
    }

    public static IntPredicate modelNumberAndColorFilter(String modelNumber, String color) {
        return slot -> Objects.equals(vehiclesList.get(slot).getVehicle().getModelName(), modelNumber)
                && Objects.equals(vehiclesList.get(slot).getVehicle().getColor(), color);
    }

    public static IntPredicate modelNumberFilter(String modelNumber) {
        return slot -> Objects.equals(vehiclesList.get(slot).getVehicle().getModelName(), modelNumber);
    }

    public static IntPredicate parkingTimeFilter(int timeInMinutes) {
        long currentTimeInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
        return slot -> currentTimeInMinutes - vehiclesList.get(slot).getParkedTime() <= timeInMinutes;
    }

    public static IntPredicate filterByVehicleTypeAndDriverType(VehicleType vehicleType, DriverTypes driverType) {
        return slot -> Objects.equals(vehiclesList.get(slot).getVehicleType(), vehicleType) &&
                Objects.equals(vehiclesList.get(slot).getDriverType(), driverType);
    }
}
