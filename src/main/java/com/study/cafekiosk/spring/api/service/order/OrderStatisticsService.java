package com.study.cafekiosk.spring.api.service.order;

import com.study.cafekiosk.spring.api.service.mail.MailService;
import com.study.cafekiosk.spring.domain.order.Order;
import com.study.cafekiosk.spring.domain.order.OrderRepository;
import com.study.cafekiosk.spring.domain.order.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderStatisticsService {

    private final OrderRepository orderRepository;
    private final MailService mailService;

    public void sendOrderStatisticsMaiil(LocalDate orderDate, String email) {
        // 해당 일자에 결제완료된 주문들을 가져와서
        List<Order> orders = orderRepository.findOrderBy(
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay(),
                OrderStatus.PAYMENT_COMPLETED
        );

        // 총 매출합계를 계산해서
        int totalAmount = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        // 메일 전송
        boolean result = mailService.sendMail(
                "seg615@naver.com",
                email,
                String.format("[매출 통계] %s", orderDate),
                String.format("총 매출 합계는 %원입니다.", totalAmount));

        if (!result) {
            throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
        }
    }
}
