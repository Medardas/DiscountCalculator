package com.devtop.discountcalculator.discount;

import com.devtop.discountcalculator.discount.abstraction.IDiscountRule;
import com.devtop.discountcalculator.model.Courier;
import com.devtop.discountcalculator.model.Order;
import com.devtop.discountcalculator.model.PackageSize;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountContextTest {

    DiscountContext context;

    @Test
    public void testApplyDiscount_firstWithSecondRuleNotEnoughMonthlyLimitLeft_partialDiscountApplied() {
        Order order = new Order(0, new Date(), PackageSize.S, new Courier("1", 2, 4, 5));
        IDiscountRule firstRule = new RuleAccumulatedDiscount();
        ReflectionTestUtils.setField(firstRule, "currentMonthAndYear", getMonthAndYear(order));
        IDiscountRule ruleChain = RuleChainFactory.getInstance().chainRules(firstRule,
                new RuleLowestSPkgPriceAmongCouriers());
        context = new DiscountContext(ruleChain, 1.5, 0.25);

        context.applyDiscount(order);

        assertEquals(0.25, context.getOrders().get(0).getDiscount());
    }

    @Test
    public void testApplyDiscount_firstWithSecondRuleNewMonth_correctDiscount() {
        Order order = new Order(0, new Date(0), PackageSize.S, new Courier("1", 2, 4, 5));
        Order newMonthOrder = new Order(0, new Date(), PackageSize.S, new Courier("1", 2, 4, 5));
        IDiscountRule firstRule = new RuleAccumulatedDiscount();
        ReflectionTestUtils.setField(firstRule, "currentMonthAndYear", getMonthAndYear(order));
        IDiscountRule ruleChain = RuleChainFactory.getInstance().chainRules(firstRule,
                new RuleLowestSPkgPriceAmongCouriers());
        context = new DiscountContext(ruleChain, 1.5, 10);
        context.setMonthlyDiscountLimitLeft(0.25);

        context.applyDiscount(order);
        context.applyDiscount(newMonthOrder);

        assertEquals(0.25, context.getOrders().get(0).getDiscount());
        assertEquals(0.5, context.getOrders().get(1).getDiscount());
    }

    @Test
    public void testApplyDiscount_firstWithThirdRuleNotEnoughMonthlyLimitLeft_partialDiscountApplied() {
        List<Order> orders = create3OrdersForLP();
        IDiscountRule firstRule = new RuleAccumulatedDiscount();
        ReflectionTestUtils.setField(firstRule, "currentMonthAndYear", getMonthAndYear(orders.get(0)));
        IDiscountRule ruleChain = RuleChainFactory.getInstance().chainRules(firstRule,
                new RuleThirdLargePkgToLPFreeOnceAMonth());
        context = new DiscountContext(ruleChain, 1.5, 0.25);

        context.applyDiscount(orders.get(0));
        context.applyDiscount(orders.get(1));
        context.applyDiscount(orders.get(2));

        assertEquals(0.0, context.getOrders().get(0).getDiscount());
        assertEquals(0.0, context.getOrders().get(1).getDiscount());
        assertEquals(0.25, context.getOrders().get(2).getDiscount());
    }

    @Test
    public void testApplyDiscount_secondRuleSingleOrder_discountCorrect() {
        context = new DiscountContext(new RuleLowestSPkgPriceAmongCouriers(), 1.5, Integer.MAX_VALUE);
        Order order = new Order(0, new Date(), PackageSize.S, new Courier("1", 2, 4, 5));

        context.applyDiscount(order);

        assertEquals(0.5, context.getOrders().get(0).getDiscount());
    }

    @Test
    public void testApplyDiscount_thirdRuleThreeOrders_discountCorrect() {
        context = new DiscountContext(new RuleThirdLargePkgToLPFreeOnceAMonth(), 1.5, Integer.MAX_VALUE);
        List<Order> orders = create3OrdersForLP();

        context.applyDiscount(orders.get(0));
        context.applyDiscount(orders.get(1));
        context.applyDiscount(orders.get(2));

        assertEquals(0.0, context.getOrders().get(0).getDiscount());
        assertEquals(0.0, context.getOrders().get(1).getDiscount());
        assertEquals(5.0, context.getOrders().get(2).getDiscount());
    }

    @Test
    public void testApplyDiscount_thirdRuleSixOrdersNewMonth_discountCorrect() {
        context = new DiscountContext(new RuleThirdLargePkgToLPFreeOnceAMonth(), 1.5, Integer.MAX_VALUE);
        Order order1 = new Order(0, new Date(0), PackageSize.L, new Courier("LP", 2, 4, 5));
        Order order2 = new Order(0, new Date(0), PackageSize.L, new Courier("LP", 2, 4, 5));
        Order order3 = new Order(0, new Date(0), PackageSize.L, new Courier("LP", 2, 4, 5));
        List<Order> orders = create3OrdersForLP();

        context.applyDiscount(order1);
        context.applyDiscount(order2);
        context.applyDiscount(order3);
        context.applyDiscount(orders.get(0));
        context.applyDiscount(orders.get(1));
        context.applyDiscount(orders.get(2));

        assertEquals(0.0, context.getOrders().get(0).getDiscount());
        assertEquals(0.0, context.getOrders().get(1).getDiscount());
        assertEquals(5.0, context.getOrders().get(2).getDiscount());
        assertEquals(0.0, context.getOrders().get(3).getDiscount());
        assertEquals(0.0, context.getOrders().get(4).getDiscount());
        assertEquals(5.0, context.getOrders().get(5).getDiscount());
    }

    @Test
    public void testApplyDiscount_thirdRuleFourOrdersOneMonth_discountCorrect() {
        context = new DiscountContext(new RuleThirdLargePkgToLPFreeOnceAMonth(), 1.5, Integer.MAX_VALUE);
        List<Order> orders = create3OrdersForLP();
        Order order = new Order(0, new Date(), PackageSize.L, new Courier("LP", 2, 4, 5));

        context.applyDiscount(orders.get(0));
        context.applyDiscount(orders.get(1));
        context.applyDiscount(orders.get(2));
        context.applyDiscount(order);

        assertEquals(0.0, context.getOrders().get(0).getDiscount());
        assertEquals(0.0, context.getOrders().get(1).getDiscount());
        assertEquals(5.0, context.getOrders().get(2).getDiscount());
        assertEquals(0.0, context.getOrders().get(3).getDiscount());
    }

    @Test
    public void testApplyDiscount_thirdRuleOnlyThirdOrderToLP_discountNotApplied() {
        context = new DiscountContext(new RuleThirdLargePkgToLPFreeOnceAMonth(), 1.5, Integer.MAX_VALUE);
        Order order1 = new Order(0, new Date(), PackageSize.L, new Courier("MR", 2, 4, 5));
        Order order2 = new Order(0, new Date(), PackageSize.L, new Courier("MR", 2, 4, 5));
        Order order3 = new Order(0, new Date(), PackageSize.L, new Courier("LP", 2, 4, 5));

        context.applyDiscount(order1);
        context.applyDiscount(order2);
        context.applyDiscount(order3);

        assertEquals(0.0, context.getOrders().get(0).getDiscount());
        assertEquals(0.0, context.getOrders().get(1).getDiscount());
        assertEquals(0.0, context.getOrders().get(2).getDiscount());
    }

    private List<Order> create3OrdersForLP() {
        return List.of(
                new Order(0, new Date(), PackageSize.L, new Courier("LP", 2, 4, 5)),
                new Order(0, new Date(), PackageSize.L, new Courier("LP", 2, 4, 5)),
                new Order(0, new Date(), PackageSize.L, new Courier("LP", 2, 4, 5))
        );
    }

    private String getMonthAndYear(Order order) {
        LocalDate localDate = order.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getMonthValue() + "/" + localDate.getYear();
    }
}
