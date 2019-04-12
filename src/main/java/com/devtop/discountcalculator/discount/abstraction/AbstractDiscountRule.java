package com.devtop.discountcalculator.discount.abstraction;

import com.devtop.discountcalculator.discount.DiscountContext;

public abstract class AbstractDiscountRule implements IDiscountRule {

    protected IDiscountRule nextRule;

    public void setNextRule(IDiscountRule nextRule) {
        this.nextRule = nextRule;
    }

    public boolean applyNextRuleIfExist(DiscountContext discountContext) {
        if (this.nextRule != null) {
            return this.nextRule.apply(discountContext);
        }
        return false;
    }

}