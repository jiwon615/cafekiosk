package com.study.cafekiosk.unit;

import com.study.cafekiosk.unit.beverages.Americano;
import com.study.cafekiosk.unit.beverages.Latte;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CafeKioskRunner {
    public static void main(String[] args) {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        log.info(">>> 아메리카노 추가");
        cafeKiosk.add(new Latte());
        log.info(">>> Latte 추가");

        int totalPrice = cafeKiosk.calculateTotalPrice();
        log.info("총 주문 가격:{}", totalPrice);
    }
}
