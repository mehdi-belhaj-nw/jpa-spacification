package com.example.jpaspecifications.specifications;

import com.example.jpaspecifications.entities.Category;
import com.example.jpaspecifications.entities.Category_;
import com.example.jpaspecifications.entities.Product;
import com.example.jpaspecifications.entities.Product_;
import com.example.jpaspecifications.specifications.criteria.ProductSearchCriteria;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductSpecification {
    private static List<Predicate> predicates;

    public static Specification<Product> combinedSpecV2(
            ProductSearchCriteria criteria
    ) {
        return (root, query, cb) -> {
            predicates = new ArrayList<>();
            likeName(criteria.getName(), root, cb);
            likeCategory(criteria.getCategoryName(), root, cb);
            lessThanPrice(criteria.getPrice(), root, cb);
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void likeName(
            String str,
            Root<Product> root,
            CriteriaBuilder cb
    ) {
        if (StringUtils.hasText(str)) {
            String formattedStr = "%" + str.toLowerCase() + "%";
            predicates.add(cb.like(
                    cb.lower(root.get(Product_.name)),
                    formattedStr
            ));
        }
    }

    private static void likeCategory(
            String str,
            Root<Product> root,
            CriteriaBuilder cb
    ) {
        if (StringUtils.hasText(str)) {
            Join<Product, Category> prodCatJoin = root.join("category");
            String formattedStr = "%" + str.toLowerCase() + "%";
            predicates.add(cb.like(
                    cb.lower(prodCatJoin.get("name")),
                    formattedStr
            ));
        }
    }

    private static void lessThanPrice(
            Double value,
            Root<Product> root,
            CriteriaBuilder cb
    ) {
            if (!Objects.isNull(value) && value > 0) {
                predicates.add(cb.lessThan(root.get("price"), value));
            }
    }

    public static Specification<Product> combinedSpec(ProductSearchCriteria criteria) {
        return Specification
                .where(likeName(criteria.getName()))
                .and(likeCategory(criteria.getCategoryName()))
                .and(lessThanPrice(criteria.getPrice()));
    }
    public static Specification<Product> likeName(String str) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(str)) return null;
            String formattedStr = "%" + str.toLowerCase() + "%";
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Product_.name)),
                    formattedStr
            );
        };
    }
    public static Specification<Product> likeCategory(String str) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(str)) return null;
            String formattedCategoryStr = "%" + str.toLowerCase() + "%";
            Join<Product, Category> prodCatJoin = root.join(Product_.category, JoinType.INNER);
            return criteriaBuilder.like(
                    criteriaBuilder.lower(prodCatJoin.get(Category_.name)),
                    formattedCategoryStr
            );
        };
    }
    public static Specification<Product> lessThanPrice(Double value) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(value) || value <= 0) return null;
            return criteriaBuilder.lessThan(root.get(Product_.price), value);
        };
    }
    // Add more methods for additional criteria
}
