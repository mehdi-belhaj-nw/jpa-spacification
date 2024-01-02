package com.example.jpaspecifications.services;

import com.example.jpaspecifications.entities.Category;
import com.example.jpaspecifications.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository repository;

    public void deleteALl() {
        repository.deleteAll();
    }
    public Category save(Category newCategory) {
        return repository.save(newCategory);
    }

}
