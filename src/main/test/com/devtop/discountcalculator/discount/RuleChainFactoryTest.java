package com.devtop.discountcalculator.discount;

import com.devtop.discountcalculator.RuleReturnsTrue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RuleChainFactoryTest {

    @Test
    public void testChainRules_oneRule_exception() {
        assertThrows(IllegalArgumentException.class,
                () -> RuleChainFactory.getInstance().chainRules(new RuleReturnsTrue()));
    }

}
