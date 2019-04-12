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

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RuleLowestSPkgPriceAmongCouriersTest {

    @InjectMocks
    RuleLowestSPkgPriceAmongCouriers rule;

    @Mock
    DiscountContext context;

    @Test
    public void testApply_currentSPackagePriceNotLowest_discountApplied() {
        Order order = new Order(0, new Date(), PackageSize.S, new Courier("1", 3, 4, 5));
        when(context.getNewestOrder()).thenReturn(order);
        when(context.getLowestSPackagePrice()).thenReturn(1.0);

        boolean actual = rule.apply(context);

        assertTrue(actual);
    }

    @Test
    public void testApply_currentSPackagePriceIsLowest_discountNotApplied() {
        Order order = new Order(0, new Date(), PackageSize.S, new Courier("1", 3, 4, 5));
        when(context.getNewestOrder()).thenReturn(order);
        when(context.getLowestSPackagePrice()).thenReturn(3.0);
        rule.setNextRule(new RuleReturnsFalse());

        boolean actual = rule.apply(context);

        assertFalse(actual);
    }

}
