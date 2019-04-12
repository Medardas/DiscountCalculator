package com.devtop.discountcalculator.discount;

import com.devtop.discountcalculator.discount.abstraction.IDiscountRule;
import com.devtop.discountcalculator.model.Order;

import java.util.ArrayList;
import java.util.List;

public class DiscountContext {

    private final double monthlyDiscountLimit;
    private final double lowestSPackagePrice;
    private IDiscountRule ruleChain;
    private List<Order> orders = new ArrayList<>();
    private double monthlyDiscountLimitLeft;

    public DiscountContext(IDiscountRule ruleChain, double lowestSPackagePrice, double monthlyDiscountLimit) {
        this.ruleChain = ruleChain;
        this.lowestSPackagePrice = lowestSPackagePrice;
        this.monthlyDiscountLimit = monthlyDiscountLimit;
        this.monthlyDiscountLimitLeft = monthlyDiscountLimit;
    }

    public void applyDiscount(Order order) {
        this.addOrder(order);
        ruleChain.apply(this);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public double getMonthlyDiscountLimitLeft() {
        return monthlyDiscountLimitLeft;
    }

    public void setMonthlyDiscountLimitLeft(double monthlyDiscountLimitLeft) {
        this.monthlyDiscountLimitLeft = monthlyDiscountLimitLeft;
    }

    public double getMonthlyDiscountLimit() {
        return monthlyDiscountLimit;
    }

    void setRemainingAndOrderDiscount(double discount) {
        if (discount > this.getMonthlyDiscountLimitLeft()) {
            this.getNewestOrder().setDiscount(this.getMonthlyDiscountLimitLeft());
            this.setMonthlyDiscountLimitLeft(0);
        } else if (discount > 0) {
            this.getNewestOrder().setDiscount(discount);
            this.setMonthlyDiscountLimitLeft(this.getMonthlyDiscountLimitLeft() - discount);
        }
    }

    Order getNewestOrder() {
        return orders.get(orders.size() - 1);
    }

    double getLowestSPackagePrice() {
        return lowestSPackagePrice;
    }

    private void addOrder(Order order) {
        orders.add(order);
    }
}
