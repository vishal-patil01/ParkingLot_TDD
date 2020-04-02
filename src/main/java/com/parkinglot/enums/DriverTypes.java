package com.parkinglot.enums;

import java.util.Comparator;
import static java.util.Comparator.*;

public enum  DriverTypes{
    NORMAL(reverseOrder()), HANDICAP(naturalOrder());

    public Comparator<Integer> order;

    DriverTypes(Comparator<Integer> order) {
        this.order = order;
    }
}
