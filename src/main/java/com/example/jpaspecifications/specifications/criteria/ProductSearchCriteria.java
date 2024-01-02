package com.example.jpaspecifications.specifications.criteria;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductSearchCriteria {
    private String name;
    private String categoryName;
    private Double price;
    private Boolean isAvailable;

}
