package com.devtop.discountcalculator;

import com.devtop.discountcalculator.discount.DiscountContext;
import com.devtop.discountcalculator.discount.abstraction.AbstractDiscountRule;

public class RuleReturnsFalse extends AbstractDiscountRule {
    @Override
    public boolean apply(DiscountContext discountContext) {
        return false;
    }
}
