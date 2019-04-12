package com.devtop.discountcalculator.service;

import com.devtop.discountcalculator.discount.DiscountContext;
import com.devtop.discountcalculator.discount.abstraction.IDiscountRule;
import com.devtop.discountcalculator.model.Courier;
import com.devtop.discountcalculator.model.Order;

import java.util.Comparator;
import java.util.List;

public class DiscountService {
    private final DiscountContext discountContext;

    public DiscountService(IDiscountRule rulesChain, List<Courier> couriersRepository, double monthlyDiscountLimit) {
        discountContext = new DiscountContext(rulesChain,
                getLowestSPackagePrice(couriersRepository),
                monthlyDiscountLimit);
    }

    public List<Order> getDiscountedOrders() {
        return discountContext.getOrders();
    }

    public void applyDiscount(List<Order> orders) {
        for (Order order : orders) {
            this.applyDiscount(order);
        }
    }

    public void applyDiscount(Order order) {
        discountContext.applyDiscount(order);
    }

    private double getLowestSPackagePrice(List<Courier> couriers) {
        return couriers.stream()
                .map(courier -> courier.getSPackagePrice())
                .min(Comparator.comparing(Double::doubleValue))
                .get();
    }
}
