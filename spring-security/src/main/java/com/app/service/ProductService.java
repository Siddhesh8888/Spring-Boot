package com.app.service;

import com.app.entity.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProductService {

    private List<Product> productList=null;

    @PostConstruct
    public void loadProductsFromDB()
    {
        productList = IntStream.rangeClosed(1,100)
                .mapToObj(i->Product.builder()
                        .id(i)
                        .name("product "+i)
                        .quantity(new Random().nextInt(10))
                        .price(new Random().nextInt(5000))
                        .build())
                .collect(Collectors.toList());
    }

    public List<Product> getProducts()
    {
        return productList;
    }

    public Product getProduct(int id)
    {
        return productList.stream()
                .filter(product -> product.getId()==id)
                .findAny()
                .orElseThrow(()->new RuntimeException("product with id "+id+" not found!!"));
    }
}
