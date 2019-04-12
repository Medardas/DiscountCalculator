package com.devtop.discountcalculator.model;

public class Courier {
    private final String name;
    private final double sPackagePrice;
    private final double mPackagePrice;
    private final double lPackagePrice;

    public Courier(String name, double sPackagePrice, double mPackagePrice, double lPackagePrice) {
        this.name = name;
        this.sPackagePrice = sPackagePrice;
        this.mPackagePrice = mPackagePrice;
        this.lPackagePrice = lPackagePrice;
    }

    public String getName() {
        return name;
    }

    public double getSPackagePrice() {
        return sPackagePrice;
    }

    public double getMPackagePrice() {
        return mPackagePrice;
    }

    public double getLPackagePrice() {
        return lPackagePrice;
    }

    public double getPriceForSize(PackageSize size) {
        switch (size) {
            case S:
                return this.getSPackagePrice();
            case M:
                return this.getMPackagePrice();
            case L:
                return this.getLPackagePrice();
            default:
                return -1;
        }
    }

}
