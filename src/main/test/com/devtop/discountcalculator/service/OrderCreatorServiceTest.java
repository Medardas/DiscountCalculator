package com.devtop.discountcalculator.service;

import com.devtop.discountcalculator.model.Order;
import com.devtop.discountcalculator.model.PackageSize;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderCreatorServiceTest {
    private static OrderCreatorService service;

    @BeforeAll
    public static void setup(){
        service = new OrderCreatorService(CourierService.establishCouriers());
    }

    @Test
    public void testCreateOrders_correctLine_orderCreated() throws ParseException {
        List<String> orderLines = List.of("2015-03-01 S MR",
                "2015-03-01 M MR",
                "2015-03-01 L LP");

        List<Order> actual = service.createOrders(orderLines);

        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2015-03-01"), actual.get(0).getDate());
        assertEquals(PackageSize.S, actual.get(0).getPackageSize());
        assertEquals("MR", actual.get(0).getCourier().getName());
        assertEquals(PackageSize.M, actual.get(1).getPackageSize());
        assertEquals(PackageSize.L, actual.get(2).getPackageSize());
        assertEquals("LP", actual.get(2).getCourier().getName());
    }

    @Test
    public void testCreateOrders_incorrectLine_ordersNotCreated(){
        List<String> orderLines = List.of("2015/03-01 S MR",
                "2015-03-01 P MR",
                "2015-03-01 L ST",
                "2015-03-01 L LP");

        List<Order> actual = service.createOrders(orderLines);

        assertEquals(1, actual.size());
    }

}
