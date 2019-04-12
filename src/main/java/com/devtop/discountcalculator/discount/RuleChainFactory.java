package com.devtop.discountcalculator.discount;

import com.devtop.discountcalculator.discount.abstraction.IDiscountRule;

import java.util.Arrays;
import java.util.List;

public class RuleChainFactory {

    private static RuleChainFactory instance;

    private RuleChainFactory() {
    }

    public static RuleChainFactory getInstance() {
        if (instance == null) {
            instance = new RuleChainFactory();
            return instance;
        }
        return instance;
    }

    public IDiscountRule chainRules(IDiscountRule... rules) {

        if (rules.length < 2) {
            throw new IllegalArgumentException("There must be at least 2 rules");
        }

        List<IDiscountRule> listOrdered = Arrays.asList(rules);

        IDiscountRule prevRule = listOrdered.get(0);
        for (int i = 1; i < listOrdered.size(); i++) {
            IDiscountRule currentRule = listOrdered.get(i);
            prevRule.setNextRule(currentRule);
            prevRule = currentRule;
        }

        return listOrdered.get(0);
    }

}
