package com.devtop.discountcalculator.view;

import com.devtop.discountcalculator.model.Order;

import java.util.List;

public class DiscountedOrdersPrinter {

    private final List<String> inputLines;
    private final List<Order> orders;

    public DiscountedOrdersPrinter(List<String> inputLines, List<Order> orders) {
        this.inputLines = inputLines;
        this.orders = orders;
    }

    public void printResult() {
        for (int i = 0; i < inputLines.size(); i++) {
            Order discountedOrder = getOrderById(i);
            if (discountedOrder != null) {
                String priceAfterDiscount = String.format("%.2f", discountedOrder.getDeliveryPriceWithDiscount());
                String discount = discountedOrder.getDiscount() > 0 ? String.format("%.2f", discountedOrder.getDiscount()) : "-";
                System.out.println(inputLines.get(i) + " " + priceAfterDiscount + " " + discount);
            } else {
                System.out.println(inputLines.get(i) + " Ignored");
            }
        }
    }

    private Order getOrderById(int id) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId() == id) {
                return orders.get(i);
            }
        }
        return null;
    }

}
