package com.devtop.discountcalculator.discount;

import com.devtop.discountcalculator.discount.abstraction.AbstractDiscountRule;
import com.devtop.discountcalculator.model.Order;

import java.time.LocalDate;
import java.time.ZoneId;

/*
Should go first in chain because other rules depends on this one
 */
public class RuleAccumulatedDiscount extends AbstractDiscountRule {
    private String currentMonthAndYear;

    @Override
    public boolean apply(DiscountContext discountContext) {
        if (accumulatedDiscount(discountContext)) {
            return applyNextRuleIfExist(discountContext);
        }
        return false;
    }

    private boolean accumulatedDiscount(DiscountContext discountContext) {
        String monthAndYear = getMonthAndYear(discountContext.getNewestOrder());
        if (!monthAndYear.equals(currentMonthAndYear)) {
            currentMonthAndYear = monthAndYear;
            discountContext.setMonthlyDiscountLimitLeft(discountContext.getMonthlyDiscountLimit());
        }
        return discountContext.getMonthlyDiscountLimitLeft() > 0;
    }

    private String getMonthAndYear(Order order) {
        LocalDate localDate = order.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getMonthValue() + "/" + localDate.getYear();
    }


}
