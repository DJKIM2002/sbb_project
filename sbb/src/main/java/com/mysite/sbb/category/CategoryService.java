package com.mysite.sbb.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // 전체 카테고리 조회
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 특정 카테고리 조회
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
    }

    // 카테고리 추가
    public Category createCategory(String name) {
        if (categoryRepository.findByName(name).isPresent()) {
            throw new RuntimeException("이미 존재하는 카테고리입니다.");
        }
        Category category = new Category(name);
        return categoryRepository.save(category);
    }

    // 카테고리 제거
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID의 카테고리를 찾을 수 없습니다."));
        categoryRepository.delete(category);
    }


    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 카테고리를 찾을 수 없습니다."));
    }
}
