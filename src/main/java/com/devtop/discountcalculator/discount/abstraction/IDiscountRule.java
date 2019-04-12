package com.devtop.discountcalculator.discount.abstraction;

import com.devtop.discountcalculator.discount.DiscountContext;

public interface IDiscountRule {

    void setNextRule(IDiscountRule nextRule);

    boolean apply(DiscountContext discountContext);

}
