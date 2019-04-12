package com.devtop.discountcalculator.service;

import com.devtop.discountcalculator.model.Courier;

import java.util.List;

public class CourierService {
    /*
        This would normally be service to get couriers from database or remote/3rd party service.
        In this case I figured it would be easiest to hard code it this way and have `couriersRepository` object in main class represent this whole list of available couriers
     */
    public static List<Courier> establishCouriers() {
        return List.of(
                new Courier("LP", 1.5, 4.9, 6.9),
                new Courier("MR", 2, 3, 4)
        );
    }
}
