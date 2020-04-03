package com.parkinglot.enums;

import java.util.ArrayList;
import java.util.stream.IntStream;

public enum VehicleType {
    LARGE {
        @Override
        public ArrayList<Integer> getParkingLotsList(ArrayList<Integer> list) {
            ArrayList<Integer> modifiedEmptySlotsList = new ArrayList<>();
            IntStream.rangeClosed(0,list.size()-3).filter(i->(list.get(i + 2) - list.get(i) + 1) == 3)
                    .forEach(i->modifiedEmptySlotsList.add(list.get(i+1)));
            return (modifiedEmptySlotsList.size() > 0) ? modifiedEmptySlotsList : SMALL.getParkingLotsList(list);
        }
    },
    SMALL {
        @Override
        public ArrayList<Integer> getParkingLotsList(ArrayList<Integer> emptySlotsList) {
            return emptySlotsList;
        }
    };

    public abstract ArrayList<Integer> getParkingLotsList(ArrayList<Integer> list);
}

