package com.devtop.discountcalculator.discount;

import com.devtop.discountcalculator.RuleReturnsTrue;
import com.devtop.discountcalculator.model.Courier;
import com.devtop.discountcalculator.model.Order;
import com.devtop.discountcalculator.model.PackageSize;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RuleAccumulatedDiscountTest {

    @InjectMocks
    RuleAccumulatedDiscount rule;

    @Mock
    DiscountContext context;

    private static Courier createCourier() {
        return new Courier("name", 1, 2, 3);
    }

    @Test
    public void testApply_limitNotReached_discountsAvailable() {
        Order order = new Order(0, new Date(), PackageSize.L, createCourier());
        when(context.getNewestOrder()).thenReturn(order);
        when(context.getMonthlyDiscountLimitLeft()).thenReturn(10.0);
        rule.setNextRule(new RuleReturnsTrue()); //we need this to check if next rule application line is reached

        boolean actual = rule.apply(context);

        assertTrue(actual);
    }

    @Test
    public void testApply_limitReached_discountsNotApplied() {
        Order order = new Order(0, new Date(), PackageSize.L, createCourier());
        when(context.getNewestOrder()).thenReturn(order);
        when(context.getMonthlyDiscountLimitLeft()).thenReturn(0.0);

        boolean actual = rule.apply(context);

        assertFalse(actual);
    }
}
