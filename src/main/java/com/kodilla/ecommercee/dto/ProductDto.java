package com.kodilla.ecommercee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductDto {

    private Long productId;
    private String productName;
    private String productDescription;
    private int productQuantity;
    private BigDecimal productPrice;

}
