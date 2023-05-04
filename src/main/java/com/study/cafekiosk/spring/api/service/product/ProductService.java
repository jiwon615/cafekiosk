package com.study.cafekiosk.spring.api.service.product;

import com.study.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import com.study.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.study.cafekiosk.spring.domain.product.Product;
import com.study.cafekiosk.spring.domain.product.ProductRepository;
import com.study.cafekiosk.spring.domain.product.ProductSellingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * readOnly = true: 읽기전용
 * CRUD 에서 CUD 동작 X / only Read
 * JPA: CUD 에서 스냅샷 저장, 변경감지 등을 안해도됨 (성능 향상에 이점 있음)
 *
 * CQRS - COMMAND 작업(CUD) 빈도보다 Query(Read) 작업 빈도가 훨씬 많음
 * -> 두 개는 성격이 다르기 때문에 잘 나눠서 작성하는 것이 좋음
 * -> 아래처럼 기본값은 readOnly = true로 주고 필요한 COMMAND 작업에 @Transactional 걸어주거나,
 *    아니면, COMMAND용 서비스와 Query용 서비스를 분리할 수 있음
 */
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 동시성 이슈
     * - 해결 방법: productNumber 디비의 필드에다가 unique index 걸고 재시도 로직 추가 / 혹운 UUID 값으로 한다면 동시성 이슈 없이 가능
     */
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        String nextProductNumber = createNextProductNumber();
        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if (latestProductNumber == null) {
            return "001";
        }
        int latestProductNumberInt = Integer.valueOf(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;
        return String.format("%03d", nextProductNumberInt);
    }

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
