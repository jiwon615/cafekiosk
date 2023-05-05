package com.study.cafekiosk.spring.api.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import com.study.cafekiosk.spring.api.service.product.ProductService;
import com.study.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.study.cafekiosk.spring.domain.product.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = ProductController.class) // controller 관련 빈들만 올릴 수 있음
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc; // servce layer 하위를 모두 mocking 처리하도록 도와주는 MockMvc 프레임워크

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean // 컨테이너에 mockito로 만든 mock 객체를 넣어줌
    private ProductService productService;

    @DisplayName("신규 상품을 등록한다.")
    @Test
    void createProduct() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        // when / then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request)) // objectMapper는 내부적으로 디폴트 생성자 사용하므로  ProductCreateRequest는 @NoArgsConstructor 추가해야함
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getSellingProducts() {
    }
}