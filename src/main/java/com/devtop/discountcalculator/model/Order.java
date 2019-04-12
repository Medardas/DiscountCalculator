package com.devtop.discountcalculator.model;

import java.util.Date;

public class Order {
    private final long id;
    private final Date date;
    private final PackageSize packageSize;
    private final Courier courier;
    private double discount = 0;

    public Order(long id, Date date, PackageSize packageSize, Courier courier) {
        this.id = id;
        this.date = date;
        this.packageSize = packageSize;
        this.courier = courier;
    }

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public PackageSize getPackageSize() {
        return packageSize;
    }

    public Courier getCourier() {
        return courier;
    }

    public double getDeliveryPrice() {
        return this.getCourier().getPriceForSize(this.getPackageSize());
    }

    public double getDeliveryPriceWithDiscount() {
        return this.getDeliveryPrice() - this.getDiscount();
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
