package com.devtop.discountcalculator.discount;

import com.devtop.discountcalculator.discount.abstraction.AbstractDiscountRule;
import com.devtop.discountcalculator.model.Order;
import com.devtop.discountcalculator.model.PackageSize;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class RuleThirdLargePkgToLPFreeOnceAMonth extends AbstractDiscountRule {

    @Override
    public boolean apply(DiscountContext discountContext) {
        if (applyDiscountOnThirdOrderOnceAMonth(discountContext)) {
            return true;
        }
        return applyNextRuleIfExist(discountContext);
    }

    private boolean applyDiscountOnThirdOrderOnceAMonth(DiscountContext discountContext) {
        if (isThirdLargePackageInLPOnceAMonth(discountContext)) {
            Order orderToApplyDiscountFor = discountContext.getNewestOrder();
            double discount = orderToApplyDiscountFor.getDeliveryPrice();
            discountContext.setRemainingAndOrderDiscount(discount);
            return true;
        }
        return false;
    }

    private boolean isThirdLargePackageInLPOnceAMonth(DiscountContext discountContext) {
        Order newestOrder = discountContext.getNewestOrder();
        List<Order> allLargeOrdersToLPWithCurrentOrderMonthAndYear = discountContext.getOrders().stream()
                .filter(order -> isLargeOrderForLP(order))
                .filter(order -> isSameYearAndMonth(order.getDate(), newestOrder.getDate()))
                .sorted(Comparator.comparing(Order::getDate))
                .collect(Collectors.toList());
        return allLargeOrdersToLPWithCurrentOrderMonthAndYear.size() == 3;
    }

    private boolean isLargeOrderForLP(Order order) {
        return PackageSize.L.equals(order.getPackageSize()) && "LP".equals(order.getCourier().getName());
    }

    private boolean isSameYearAndMonth(Date date1, Date date2) {
        LocalDate localDate = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        localDate = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return year == localDate.getYear() && month == localDate.getMonthValue();
    }
}
