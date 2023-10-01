package com.example.demo.product;

class ProductAdapter implements ProductPort {

    private final ProductRepository productRepository;

    public ProductAdapter(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void save(final Product poduct) {
        ProductRepository.save(poduct);
    }
}
