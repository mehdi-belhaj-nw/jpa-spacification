package com.example.jpaspecifications.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.example.jpaspecifications.entities.Category;
import com.example.jpaspecifications.entities.Product;
import com.example.jpaspecifications.services.CategoryService;
import com.example.jpaspecifications.services.ProductService;
import com.example.jpaspecifications.specifications.criteria.ProductSearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProductSpecificationTest {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    private Product shirt, jeans;

    @BeforeEach
    void init() {
        productService.deleteALl();
        categoryService.deleteALl();

        Category formalAttire = categoryService.save(Category.builder().name("Formal Attire").build());
        Category casualWear = categoryService.save(Category.builder().name("Casual Wear").build());
        Category sportsWear = categoryService.save(Category.builder().name("Sports Wear").build());

        shirt = productService.save(Product.builder()
                .name("Fit Shirt Midnight")
                .category(formalAttire)
                .price(249.99)
                .isAvailable(true)
                .build());

        jeans = productService.save(Product.builder()
                .name("Light Blue Regular Fit Jeans")
                .category(casualWear)
                .price(499.99)
                .isAvailable(false)
                .build());

        productService.save(Product.builder()
                .name("Classic Black Dress Shoes")
                .category(formalAttire)
                .price(199.99)
                .isAvailable(true)
                .build());

        productService.save(Product.builder()
                .name("Running Shoes - Neon Green")
                .category(sportsWear)
                .price(79.99)
                .isAvailable(true)
                .build());

        productService.save(Product.builder()
                .name("Cotton White T-Shirt")
                .category(casualWear)
                .price(19.99)
                .isAvailable(true)
                .build());

        productService.save(Product.builder()
                .name("Athletic Shorts - Navy Blue")
                .category(sportsWear)
                .price(29.99)
                .isAvailable(true)
                .build());
    }

    @Test
    void shouldFind2productWithPriceLessThan300AndCategoryFormal() {
        // Given
        ProductSearchCriteria criteria = ProductSearchCriteria.builder()
                .price(250d)
                .categoryName("formal")
                .build();

        // When
        List<Product> allProducts = productService.findProductsByCombinedSpec(criteria);

        // Then
        assertNotNull(allProducts, "Filtered products should not be null");
        assertEquals(
                2,
                allProducts.size(),
                "Expected 3 product to match the criteria"
        );
    }

    @Test
    void find_product_by_single_specification() {
        List<Product> filteredProducts = productService.findProductsByNameLike("Jeans");
        Product filteredProduct = filteredProducts.stream().findFirst().orElse(null);
        assertEquals(jeans, filteredProduct);
        assertNotEquals(shirt, filteredProduct);
    }

    @Test
    void find_product_by_criteria() {
        ProductSearchCriteria criteria = ProductSearchCriteria.builder()
                .name("Shirt")
                .categoryName("Formal")
                .price(500d)
                .build();

        List<Product> filteredProducts = productService.findProductsByCriteria(criteria);

        Product firstFilteredProduct = filteredProducts.stream().findFirst().orElse(null);
        System.out.println("\n\t - filtered product -> " + firstFilteredProduct + "\n");
        assertEquals(1, filteredProducts.size());
        assertEquals(shirt, firstFilteredProduct);
    }

    @Test
    void find_Products_By_Combined_Specifications_v2() {
        ProductSearchCriteria criteria = ProductSearchCriteria.builder()
                .name("fit shirt")
                .categoryName("formal")
                .price(500d)
                .build();

        List<Product> filteredProducts = productService.findProductsByCombinedSpecV2(criteria);

        Product firstFilteredProduct = filteredProducts.stream().findFirst().orElse(null);
        System.out.println("\n\t - filtered products -> " + filteredProducts + "\n");
        assertEquals(1, filteredProducts.size());
        assertEquals(shirt, firstFilteredProduct);
    }

    @Test
    void find_Products_By_Combined_Specifications_v1() {
        ProductSearchCriteria criteria = ProductSearchCriteria.builder()
                .name("fit")
                .categoryName("formal")
                .price(500d)
                .build();

        List<Product> filteredProducts = productService.findProductsByCombinedSpec(criteria);

        Product firstFilteredProduct = filteredProducts.stream().findFirst().orElse(null);
        System.out.println("\n\t - filtered products -> " + filteredProducts + "\n");
        assertEquals(1, filteredProducts.size());
        assertEquals(shirt, firstFilteredProduct);
    }

    @Test
    void shouldFindProductsMatchingCriteriaV1() {
        // Given
        ProductSearchCriteria criteria = ProductSearchCriteria.builder()
                .name("fit shirt")
                .categoryName("formal")
                .price(500d)
                .build();

        // When
        List<Product> allProducts = productService.findProductsByCombinedSpec(criteria);

        // Then
        assertNotNull(allProducts, "Filtered products should not be null");
        assertEquals(
                1,
                allProducts.size(),
                "Expected one product to match the criteria"
        );

        Product firstFilteredProduct = allProducts.stream().findFirst().orElse(null);
        assertNotNull(
                firstFilteredProduct,
                "First filtered product should not be null"
        );

        assertEquals(
                shirt.getName(),
                firstFilteredProduct.getName(),
                "Product name should match: " + shirt.getName()
        );
    }
}
