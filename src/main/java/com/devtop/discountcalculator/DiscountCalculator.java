package com.devtop.discountcalculator;

import com.devtop.discountcalculator.discount.RuleAccumulatedDiscount;
import com.devtop.discountcalculator.discount.RuleChainFactory;
import com.devtop.discountcalculator.discount.RuleLowestSPkgPriceAmongCouriers;
import com.devtop.discountcalculator.discount.RuleThirdLargePkgToLPFreeOnceAMonth;
import com.devtop.discountcalculator.discount.abstraction.IDiscountRule;
import com.devtop.discountcalculator.model.Courier;
import com.devtop.discountcalculator.model.Order;
import com.devtop.discountcalculator.service.CourierService;
import com.devtop.discountcalculator.service.DiscountService;
import com.devtop.discountcalculator.service.FileLineReaderService;
import com.devtop.discountcalculator.service.OrderCreatorService;
import com.devtop.discountcalculator.view.DiscountedOrdersPrinter;

import java.util.List;

public class DiscountCalculator {
    private static final String DEFAULT_FILE = "input.txt";
    private static int MONTHLY_DISCOUNT_LIMIT = 10;

    public static void main(String[] args) {
        List<Courier> couriersRepository = CourierService.establishCouriers();

        OrderCreatorService orderCreator = new OrderCreatorService(couriersRepository);
        FileLineReaderService reader = new FileLineReaderService(args.length == 1 ? args[0] : null, DEFAULT_FILE);
        List<String> inputLines = reader.readLines();
        List<Order> orders = orderCreator.createOrders(inputLines);

        DiscountService discountService = new DiscountService(createChainOfRules(), couriersRepository, MONTHLY_DISCOUNT_LIMIT);
        discountService.applyDiscount(orders);

        DiscountedOrdersPrinter printer = new DiscountedOrdersPrinter(inputLines, discountService.getDiscountedOrders());
        printer.printResult();
    }

    private static IDiscountRule createChainOfRules() {
        IDiscountRule accumulatedDiscount = new RuleAccumulatedDiscount();
        IDiscountRule lowestSPackagePrice = new RuleLowestSPkgPriceAmongCouriers();
        IDiscountRule thirdPackageToLPFree = new RuleThirdLargePkgToLPFreeOnceAMonth();

        return RuleChainFactory.getInstance().chainRules(accumulatedDiscount,
                thirdPackageToLPFree,
                lowestSPackagePrice);
    }
}
