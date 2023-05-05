package com.study.cafekiosk.spring.api.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import com.study.cafekiosk.spring.api.service.product.ProductService;
import com.study.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.study.cafekiosk.spring.domain.product.ProductSellingStatus;
import com.study.cafekiosk.spring.domain.product.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        mockMvc.perform(post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request)) // objectMapper는 내부적으로 디폴트 생성자 사용하므로  ProductCreateRequest는 @NoArgsConstructor 추가해야함
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("신규 상품을 등록할 때 상품 타입은 필수값이다.")
    @Test
    void createProductWithoutType() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(4000)
                .build();

        // when / then
        mockMvc.perform(post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request)) // objectMapper는 내부적으로 디폴트 생성자 사용하므로  ProductCreateRequest는 @NoArgsConstructor 추가해야함
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 상품을 등록할 때 상품 판매상태는 필수값이다.")
    @Test
    void createProductWithoutSellingStatus() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .name("아메리카노")
                .price(4000)
                .build();

        // when / then
        mockMvc.perform(post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request)) // objectMapper는 내부적으로 디폴트 생성자 사용하므로  ProductCreateRequest는 @NoArgsConstructor 추가해야함
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 상품을 등록할 때 상품 이름은 필수값이다.")
    @Test
    void createProductWithoutName() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .price(4000)
                .build();

        // when / then
        mockMvc.perform(post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request)) // objectMapper는 내부적으로 디폴트 생성자 사용하므로  ProductCreateRequest는 @NoArgsConstructor 추가해야함
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 이름은 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("신규 상품을 등록할 때 상품 가격은 앙수여야 한다.")
    @Test
    void createProductWithoutPrice() throws Exception {
        // given
        ProductCreateRequest request = ProductCreateRequest.builder()
                .type(ProductType.HANDMADE)
                .sellingStatus(ProductSellingStatus.SELLING)
                .name("아메리카노")
                .price(0)
                .build();

        // when / then
        mockMvc.perform(post("/api/v1/products/new")
                        .content(objectMapper.writeValueAsString(request)) // objectMapper는 내부적으로 디폴트 생성자 사용하므로  ProductCreateRequest는 @NoArgsConstructor 추가해야함
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품 가격은 양수여야 합나다."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("판매 상품을 조회한다.")
    @Test
    void getSellingProducts() throws Exception {
        // given
        List<ProductResponse> result = List.of();

        when(productService.getSellingProducts()).thenReturn(result);

        // when / then
        mockMvc.perform(get("/api/v1/products/selling")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isArray());
    }
}