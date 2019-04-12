package com.devtop.discountcalculator.service;

import com.devtop.discountcalculator.discount.DiscountContext;
import com.devtop.discountcalculator.model.Courier;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountServiceTest {

    DiscountService service;

    @Test
    public void testDiscountService_severalCouriers_correctLowestSPackagePrice() {
        service = new DiscountService(null, createCouriers(), 0);
        DiscountContext context = (DiscountContext) ReflectionTestUtils.getField(service, "discountContext");

        assertEquals(0.5, ReflectionTestUtils.getField(context, "lowestSPackagePrice"));
    }

    private List<Courier> createCouriers() {
        return List.of(
                new Courier("LP", 1.5, 4.9, 6.9),
                new Courier("MR", 3.5, 4.9, 6.9),
                new Courier("SS", 1.75, 4.9, 6.9),
                new Courier("TR", 1.3, 4.9, 6.9),
                new Courier("EW", 0.5, 4.9, 6.9),
                new Courier("WE", 1.5, 4.9, 6.9)
        );
    }

}
