package com.example.demo.product;


import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;

class ProductServiceTest {

    private ProductService productService;
    private ProductPort productPort;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository =new ProductRepository();
        productPort = new ProductAdapter(productRepository);
        productService = new ProductService(productPort);
    }

    @Test
    void 상품등록() {
        final String name = "상품명";
        final int price = 1000;
        final DiscountPolicy discountPolicy = DiscountPolicy.NONE;
        final AddProductRequest rRequest = new AddProductRequest(name, price, discountPolicy);
        productService.addProduct(rRequest);
    }

    private class ProductService {

        private final ProductPort productPort;

        private ProductService(final ProductPort productPort) {
            this.productPort = productPort;
        }

        public void addProduct(final AddProductRequest request) {
            final Product product = new Product(request.name(), request.price(),
                    request.discountPolicy);

            productPort.save(product);
        }
    }

    private record AddProductRequest(String name, int price, DiscountPolicy discountPolicy) {

        private AddProductRequest {
            Assert.hasText(name, "상품명은 필수입니다.");
            Assert.isTrue(price > 0, "상품 가격은 0 이상이어야 합니다.");
            Assert.notNull(discountPolicy, "할인 정책은 필수입니다.");
        }
    }

    private class Product {

        private final String name;
        private final int price;
        private final DiscountPolicy discountPolicy;
        private Long id;

        public Product(final String name, final int price, final DiscountPolicy discountPolicy) {
            Assert.hasText(name, "상품명은 필수입니다.");
            Assert.isTrue(price > 0, "상품 가격은 0 이상이어야 합니다.");
            Assert.notNull(discountPolicy, "할인 정책은 필수입니다.");
            this.name = name;
            this.price = price;
            this.discountPolicy = discountPolicy;
        }

        public void assignId(final Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }

    private interface ProductPort {

        void save(final Product poduct);
    }

    private class ProductAdapter implements ProductPort {

        private final ProductRepository productRepository;

        public ProductAdapter(final ProductRepository productRepository) {
            this.productRepository = productRepository;
        }

        @Override
        public void save(final Product poduct) {
            ProductRepository.save(poduct);
        }
    }

    private class ProductRepository {

        private Map<Long, Product> persistent = new HashMap<>();
        private Long sequence = 0L;

        public void save(final Product product) {
            product.assignId(++sequence);
            persistent.put(product.getId(), product);
        }
    }


    private enum DiscountPolicy {NONE}
}