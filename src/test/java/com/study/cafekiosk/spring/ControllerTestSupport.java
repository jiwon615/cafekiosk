package com.study.cafekiosk.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.cafekiosk.spring.api.controller.order.OrderController;
import com.study.cafekiosk.spring.api.controller.product.ProductController;
import com.study.cafekiosk.spring.api.service.order.OrderService;
import com.study.cafekiosk.spring.api.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        OrderController.class,
        ProductController.class
})
public abstract class ControllerTestSupport {


    @Autowired
    protected MockMvc mockMvc; // service layer 하위를 모두 mocking 처리하도록 도와주는 MockMvc 프레임워크

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean // 컨테이너에 mockito로 만든 mock 객체를 넣어줌
    protected OrderService orderService;

    @MockBean // 컨테이너에 mockito로 만든 mock 객체를 넣어줌
    protected ProductService productService;
}
