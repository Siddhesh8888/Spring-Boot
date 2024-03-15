package com.app.config;

import com.app.model.Product;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomItemProcessor implements ItemProcessor<Product,Product> {
    @Override
    public Product process(Product item) throws Exception {
        return item;
    }
}
