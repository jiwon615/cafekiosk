package com.study.cafekiosk.spring.api.service.order;

import com.study.cafekiosk.spring.client.mail.MailSendClient;
import com.study.cafekiosk.spring.domain.history.MailSendHistory;
import com.study.cafekiosk.spring.domain.history.MailSendHistoryRepository;
import com.study.cafekiosk.spring.domain.order.Order;
import com.study.cafekiosk.spring.domain.order.OrderRepository;
import com.study.cafekiosk.spring.domain.order.OrderStatus;
import com.study.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import com.study.cafekiosk.spring.domain.product.Product;
import com.study.cafekiosk.spring.domain.product.ProductRepository;
import com.study.cafekiosk.spring.domain.product.ProductType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.study.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static com.study.cafekiosk.spring.domain.product.ProductType.HANDMADE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

// 메일전송 같이 긴 작업이 소요되는 작업의 경우 transactional 안거는 것이 좋다!!
@SpringBootTest
class OrderStatisticsServiceTest {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

    // 실제 메일전송은 네트워크를 타는 것으로 테스트에 불필요한 과정이므로, 이런 경우 mockBean으로 가짜 객체 주입하여 사용
    @MockBean
    private MailSendClient mailSendClient;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatisticsMail() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 5, 5, 0, 0);
        Product product1 = createProduct(HANDMADE, "001", 1000);
        Product product2 = createProduct(HANDMADE, "002", 2000);
        Product product3 = createProduct(HANDMADE, "003", 3000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        Order order1 = createPaymentCompletedOrder(LocalDateTime.of(2023, 5, 4, 23, 59, 59), products);
        Order order2 = createPaymentCompletedOrder(now, products);
        Order order3 = createPaymentCompletedOrder(LocalDateTime.of(2023, 5, 5, 23, 59, 59), products);
        Order order4 = createPaymentCompletedOrder(LocalDateTime.of(2023, 5, 6, 0, 0, 0), products);

        // stubbing (mock 객체에 원하는 행위를 정의하는 것): sendMail() 호출 시 any(String.class) 즉 String값 아무거나 상관없고, 결과는 true로 반환해줘!
        Mockito.when(mailSendClient.sendMail(any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(true);

        // when
        boolean result = orderStatisticsService.sendOrderStatisticsMaiil(LocalDate.of(2023, 5, 5), "seg615@naver.com");

        // then
        assertThat(result).isTrue();

        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
                .extracting("content")
                .contains("총 매출 합계는 12000원입니다.");
    }

    private Order createPaymentCompletedOrder(LocalDateTime now, List<Product> products) {
        Order order = Order.builder()
                .products(products)
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .registeredDateTime(now)
                .build();
        return orderRepository.save(order);
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(SELLING)
                .name("메뉴 이름")
                .price(price)
                .build();
    }
}