package com.example.demo.product;

import java.util.HashMap;
import java.util.Map;

class ProductRepository {

    private Map<Long, Product> persistent = new HashMap<>();
    private Long sequence = 0L;

    public void save(final Product product) {
        product.assignId(++sequence);
        persistent.put(product.getId(), product);
    }
}
