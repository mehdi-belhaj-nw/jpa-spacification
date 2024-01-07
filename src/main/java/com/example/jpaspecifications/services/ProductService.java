package com.example.jpaspecifications.services;

import com.example.jpaspecifications.entities.Product;
import com.example.jpaspecifications.repositories.ProductRepository;
import com.example.jpaspecifications.specifications.ProductSpecification;
import com.example.jpaspecifications.specifications.criteria.ProductSearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class ProductService {
    private final ProductRepository repository;

    // Your business logic and interactions with the repository go here
    public void deleteALl() {
        repository.deleteAll();
    }
    public Product save(Product newProduct) {
        return repository.save(newProduct);
    }
    public List<Product> findAll() {
        return repository.findAll();
    }

    /** Using a single specification */
    public List<Product> findProductsByNameLike(String name) {
        Specification<Product> spec = ProductSpecification.likeName(name);
        return repository.findAll(spec);
    }

    /** Combining multiple specifications in the service */
    public List<Product> findProductsByCriteriaCombinedInService(
            ProductSearchCriteria criteria
    ) {
        Specification<Product> spec = Specification.where(null);
        String name = criteria.getName();
        if (name != null) {
            spec = spec.and(ProductSpecification.likeName(name));
        }
        String categoryName = criteria.getCategoryName();
        if (categoryName != null) {
            spec = spec.and(ProductSpecification.likeCategory(categoryName));
        }

        Double price = criteria.getPrice();
        if (!Objects.isNull(price) && price > 0) {
            spec = spec.and(ProductSpecification.lessThanPrice(price));
        }
        return repository.findAll(spec);
    }

    /** Combining multiple specifications in the service */
    public List<Product> findProductsByCombinedSpec(
            ProductSearchCriteria criteria) {
        Specification<Product> spec = ProductSpecification.combinedSpec(criteria);
        return repository.findAll(spec);
    }

    public List<Product> findProductsByCombinedSpecV2(
            ProductSearchCriteria criteria) {
        Specification<Product> spec = ProductSpecification.combinedSpecV2(criteria);
        return repository.findAll(spec);
    }


}
