package com.devtop.discountcalculator.discount;

import com.devtop.discountcalculator.RuleReturnsFalse;
import com.devtop.discountcalculator.model.Courier;
import com.devtop.discountcalculator.model.Order;
import com.devtop.discountcalculator.model.PackageSize;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RuleThirdLargePkgToLPFreeOnceAMonthTest {

    @InjectMocks
    RuleThirdLargePkgToLPFreeOnceAMonth rule;

    @Mock
    DiscountContext context;

    @Test
    public void testApply_thirdOrderInAMonth_discountApplied() {
        Order order = new Order(0, new Date(), PackageSize.L, new Courier("LP", 1, 1, 1));
        when(context.getNewestOrder()).thenReturn(order);
        when(context.getOrders()).thenReturn(createNumberOfOrdersWithSpecificLast(2, order));

        boolean actual = rule.apply(context);

        assertTrue(actual);
    }

    @Test
    public void testApply_thirdOrderInAMonthWithDifferentCourier_discountNotApplied() {
        Order order = new Order(0, new Date(), PackageSize.L, new Courier("x", 1, 1, 1));
        when(context.getNewestOrder()).thenReturn(order);
        when(context.getOrders()).thenReturn(createNumberOfOrdersWithSpecificLast(2, order));
        rule.setNextRule(new RuleReturnsFalse());

        boolean actual = rule.apply(context);

        assertFalse(actual);
    }

    @Test
    public void testApply_sixthOrderInAMonth_discountNotApplied() {
        Order order = new Order(0, new Date(), PackageSize.L, new Courier("LP", 1, 1, 1));
        when(context.getNewestOrder()).thenReturn(order);
        when(context.getOrders()).thenReturn(createNumberOfOrdersWithSpecificLast(5, order));
        rule.setNextRule(new RuleReturnsFalse());

        boolean actual = rule.apply(context);

        assertFalse(actual);
    }

    private List<Order> createNumberOfOrdersWithSpecificLast(int number, Order lastOrder) {
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            orders.add(new Order(0, new Date(), PackageSize.L, new Courier("LP", 1, 1, 1)));
        }
        orders.add(lastOrder);
        return orders;
    }
}
