package com.parkinglot.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum VehicleType {
    LARGE {
        @Override
        public List<Integer> getParkingLotsList(List<Integer> list) {
            List<Integer> modifiedEmptySlotsList = IntStream.rangeClosed(0, list.size() - 3)
                    .filter(i -> (list.get(i + 2) - list.get(i) + 1) == 3)
                    .mapToObj(i -> list.get(i + 1)).collect(Collectors.toList());
            return (modifiedEmptySlotsList.size() > 0) ? modifiedEmptySlotsList : SMALL.getParkingLotsList(list);
        }
    },
    SMALL {
        @Override
        public List<Integer> getParkingLotsList(List<Integer> emptySlotsList) {
            return emptySlotsList;
        }
    };

    public abstract List<Integer> getParkingLotsList(List<Integer> list);
}

