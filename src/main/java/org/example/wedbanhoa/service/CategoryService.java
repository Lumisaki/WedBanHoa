package org.example.wedbanhoa.service;

import org.example.wedbanhoa.dto.CategoryRequest;
import org.example.wedbanhoa.dto.CategoryResponse;
import org.example.wedbanhoa.entity.Category;
import org.example.wedbanhoa.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAllByOrderByCategoryIdAsc().stream()
                .map(this::toResponse) // Gọi hàm mapping nội bộ
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getById(Integer id) {
        return categoryRepository.findById(id)
                .map(this::toResponse) // Gọi hàm mapping nội bộ
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Category getEntityById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Transactional
    public boolean insertCategory(CategoryRequest request) {
        try {
            Category cat = toEntity(request); // Gọi hàm mapping nội bộ
            categoryRepository.save(cat);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean insertCategory(String name) {
        return insertCategory(CategoryRequest.builder().categoryName(name).build());
    }

    @Transactional
    public boolean updateCategory(CategoryRequest request) {
        try {
            Category cat = categoryRepository.findById(request.getCategoryId()).orElse(null);
            if (cat != null) {
                cat.setCategoryName(request.getCategoryName());
                categoryRepository.save(cat);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean updateCategory(Integer id, String name) {
        return updateCategory(CategoryRequest.builder().categoryId(id).categoryName(name).build());
    }

    @Transactional
    public boolean deleteCategory(Integer id) {
        try {
            categoryRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // =========================================================================
    // CÁC HÀM MAPPING ĐƯỢC CHUYỂN TRỰC TIẾP VÀO ĐÂY
    // =========================================================================

    private Category toEntity(CategoryRequest request) {
        if (request == null) return null;
        return Category.builder()
                .categoryId(request.getCategoryId())
                .categoryName(request.getCategoryName())
                .build();
    }

    private CategoryResponse toResponse(Category entity) {
        if (entity == null) return null;
        return CategoryResponse.builder()
                .categoryId(entity.getCategoryId())
                .categoryName(entity.getCategoryName())
                .build();
    }

    private CategoryRequest toRequest(Category entity) {
        if (entity == null) return null;
        return CategoryRequest.builder()
                .categoryId(entity.getCategoryId())
                .categoryName(entity.getCategoryName())
                .build();
    }
}