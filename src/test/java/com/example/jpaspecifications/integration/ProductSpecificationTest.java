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
import java.util.Objects;

@SpringBootTest
public class ProductSpecificationTest {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    private List<Product> allProducts;

    @BeforeEach
    void init() {
        productService.deleteALl();
        categoryService.deleteALl();

        Category formalAttire = categoryService.save(Category.builder().name("Formal Attire").build());
        Category casualWear = categoryService.save(Category.builder().name("Casual Wear").build());
        Category sportsWear = categoryService.save(Category.builder().name("Sports Wear").build());

        productService.save(Product.builder()
                .name("Fit Shirt Midnight")
                .category(formalAttire)
                .price(249.99)
                .build());

        productService.save(Product.builder()
                .name("Light Blue Regular Fit Jeans")
                .category(casualWear)
                .price(499.99)
                .build());

        productService.save(Product.builder()
                .name("Classic Black Dress Shoes")
                .category(formalAttire)
                .price(199.99)
                .build());

        productService.save(Product.builder()
                .name("Running Shoes - Neon Green")
                .category(sportsWear)
                .price(79.99)
                .build());

        productService.save(Product.builder()
                .name("Cotton White T-Shirt")
                .category(casualWear)
                .price(19.99)
                .build());

        productService.save(Product.builder()
                .name("Athletic Shorts - Navy Blue")
                .category(sportsWear)
                .price(29.99)
                .build());

        allProducts = productService.findAll();
    }

    @Test
    void find_product_by_single_specification() {
        // Given
        Product jeans = allProducts.stream().filter(
                product -> Objects.equals(product.getName(), "Light Blue Regular Fit Jeans")
        ).findFirst().orElse(null);
        // When
        List<Product> filteredProducts = productService.findProductsByNameLike("Jeans");
        Product filteredProduct = filteredProducts.stream().findFirst().orElse(null);

        // Then
        assertEquals(1, filteredProducts.size());
        assertEquals(jeans, filteredProduct);
    }

    @Test
    void find_Products_by_Criteria_combined_in_Service() {
        // Given
        Product shirt = allProducts.stream().filter(
                product -> Objects.equals(product.getName(), "Fit Shirt Midnight")
                        && Objects.equals(product.getCategory().getName(), "Formal Attire")
        ).findFirst().orElse(null);

        ProductSearchCriteria criteria = ProductSearchCriteria.builder()
                .name("Shirt")
                .categoryName("Formal")
                .price(500d)
                .build();
        // When
        List<Product> filteredProducts = productService.findProductsByCriteriaCombinedInService(criteria);
        // Then
        assertEquals(1, filteredProducts.size());
        Product firstFilteredProduct = filteredProducts.stream().findFirst().orElse(null);
        assertEquals(shirt, firstFilteredProduct);
    }

    @Test
    void find_Products_By_Combined_Specifications_v1() {
        // GIVEN
        Product shirt = allProducts.stream().filter(
                product -> Objects.equals(product.getCategory().getName(), "Casual Wear")
                        && product.getPrice() < 20d)
                .findFirst().orElse(null);
        ProductSearchCriteria criteria = ProductSearchCriteria.builder()
                .categoryName("casual")
                .price(20d)
                .build();
        // WHEN
        List<Product> filteredProducts = productService.findProductsByCombinedSpec(criteria);
        // THEN
        assertEquals(1, filteredProducts.size(), "Expected one product to match the criteria");
        Product firstFilteredProduct = filteredProducts.stream().findFirst().orElse(null);
        assertEquals(shirt, firstFilteredProduct, "Expected the filtered product to match " + shirt);
    }

    @Test
    void find_Products_By_Combined_Specifications_v2() {
        ProductSearchCriteria criteria = ProductSearchCriteria.builder()
                .name("fit")
                .categoryName("formal")
                .price(500d)
                .build();

        List<Product> filteredProducts = productService.findProductsByCombinedSpecV2(criteria);

        assertEquals(1, filteredProducts.size());
    }

    @Test
    void shouldFindProductsMatchingCriteriaV2() {
        // Given
        ProductSearchCriteria criteria = ProductSearchCriteria.builder()
                .name("fit shirt")
                .categoryName("formal")
                .price(500d)
                .build();
        // When
        List<Product> allProducts = productService.findProductsByCombinedSpecV2(criteria);
        // Then
        assertNotNull(allProducts, "Filtered products should not be null");
        assertEquals(1, allProducts.size(), "Expected one product to match the criteria");
        Product firstFilteredProduct = allProducts.stream().findFirst().orElse(null);
        assertNotNull(firstFilteredProduct, "First filtered product should not be null");
    }
}
