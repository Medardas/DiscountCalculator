package com.devtop.discountcalculator.discount;

import com.devtop.discountcalculator.discount.abstraction.AbstractDiscountRule;
import com.devtop.discountcalculator.model.Order;
import com.devtop.discountcalculator.model.PackageSize;

public class RuleLowestSPkgPriceAmongCouriers extends AbstractDiscountRule {

    @Override
    public boolean apply(DiscountContext discountContext) {
        if (applyDiscountForLowestSPackagePriceAmongCouriers(discountContext)) {
            return true;
        }
        return applyNextRuleIfExist(discountContext);
    }

    private boolean applyDiscountForLowestSPackagePriceAmongCouriers(DiscountContext discountContext) {
        Order orderToApplyDiscountFor = discountContext.getNewestOrder();
        if (PackageSize.S.equals(orderToApplyDiscountFor.getPackageSize())) {
            double discount = orderToApplyDiscountFor.getDeliveryPrice() - discountContext.getLowestSPackagePrice();
            if (discount > 0) {
                discountContext.setRemainingAndOrderDiscount(discount);
                return true;
            }
        }
        return false;
    }
}
