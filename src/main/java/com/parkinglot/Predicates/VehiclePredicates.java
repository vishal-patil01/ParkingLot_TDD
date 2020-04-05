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

    public static IntPredicate getPredicates(String type, String... args) {
        switch (type) {
            case "colour":
                return slot -> Objects.equals(vehiclesList.get(slot).getVehicle().getColor(), args[0]);
            case "modelNumber&color":
                return slot -> Objects.equals(vehiclesList.get(slot).getVehicle().getModelName(), args[0])
                        && Objects.equals(vehiclesList.get(slot).getVehicle().getColor(), args[1]);
            case "modelNumber":
                return slot -> Objects.equals(vehiclesList.get(slot).getVehicle().getModelName(), args[0]);
            case "parkingTime":
                long currentTimeInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis());
                return slot -> currentTimeInMinutes - vehiclesList.get(slot).getParkedTime() <= Integer.parseInt(args[0]);
            case "vehicleType&DriverType":
                return slot -> Objects.equals(vehiclesList.get(slot).getVehicleType(), VehicleType.valueOf(args[0])) &&
                        Objects.equals(vehiclesList.get(slot).getDriverType(), DriverTypes.valueOf(args[1]));
            default:
                return slot -> vehiclesList.get(slot) != null;
        }
    }
}
