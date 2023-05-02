package com.study.cafekiosk.spring.api.service.order;

import com.study.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import com.study.cafekiosk.spring.domain.order.Order;
import com.study.cafekiosk.spring.domain.order.OrderRepository;
import com.study.cafekiosk.spring.domain.order.response.OrderResponse;
import com.study.cafekiosk.spring.domain.product.Product;
import com.study.cafekiosk.spring.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {
        List<String> productNumbers = request.getProductNumbers();
        List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

        Order order = Order.create(products, registeredDateTime);
        Order savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder);
    }
}
