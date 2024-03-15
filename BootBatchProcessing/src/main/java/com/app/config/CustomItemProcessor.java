package com.app.config;

import com.app.model.Product;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Product, Product> {
    @Override
    public Product process(Product item) throws Exception {


        if (!item.getProductId().equalsIgnoreCase("productid"))
        {
            double discount = Double.parseDouble(item.getDiscount());
            double price = Double.parseDouble(item.getPrice());
            double discountPrice = (discount/100)*price;
            double finalPrice = price - discountPrice;
            item.setDiscountedPrice(String.valueOf(finalPrice));
        }
        return item;
    }
}
