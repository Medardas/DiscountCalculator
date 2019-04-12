package com.devtop.discountcalculator.service;

import com.devtop.discountcalculator.model.Courier;
import com.devtop.discountcalculator.model.Order;
import com.devtop.discountcalculator.model.PackageSize;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderCreatorService {

    private final List<Courier> couriers;

    public OrderCreatorService(List<Courier> couriers) {
        this.couriers = couriers;
    }

    public List<Order> createOrders(List<String> orderLines) {
        List<Order> orders = new ArrayList<>();
        long idInList = -1l;
        for (String orderLine : orderLines) {
            String[] orderInfo = orderLine.split(" ");
            idInList++;
            if (isOrderLineCorrect(orderInfo)) {
                orders.add(new Order(idInList,
                        getDateFromOrderInfo(orderInfo[0]),
                        getPackageSizeFromOrderInfo(orderInfo[1]),
                        getCourierFromOrderInfo(orderInfo[2])));
            }
        }
        return orders;
    }

    private boolean isOrderLineCorrect(String[] orderInfo) {
        return orderInfo.length == 3 &&
                getDateFromOrderInfo(orderInfo[0]) != null &&
                getPackageSizeFromOrderInfo(orderInfo[1]) != null &&
                getCourierFromOrderInfo(orderInfo[2]) != null;
    }

    private PackageSize getPackageSizeFromOrderInfo(String size) {
        try {
            return PackageSize.valueOf(size);
        } catch (IllegalArgumentException iae) {
            System.err.println("Could not recognize package size: " + size);
            return null;
        }
    }

    private Courier getCourierFromOrderInfo(String courierName) {
        return couriers.stream()
                .filter(courier -> courier.getName().equals(courierName))
                .findFirst()
                .orElseGet(() -> {
                    System.err.println("Could not recognize courier: " + courierName);
                    return null;
                });
    }

    private Date getDateFromOrderInfo(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(date);
        } catch (ParseException e) {
            System.err.println("Could not parse date: " + date);
            return null;
        }
    }

}
