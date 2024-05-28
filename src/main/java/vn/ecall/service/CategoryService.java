package vn.ecall.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ecall.domain.businessmodel.Category;
import vn.ecall.repository.CategoryRepository;
import vn.ecall.web.rest.errors.BadRequestAlertException;

import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public CategoryService (CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<Category> getAllCategories(Pageable pageable) {
        Page<Category> category = categoryRepository.findAll(pageable);
        return category;
    }

    @Transactional(readOnly = true)
    public Optional<Category> getCategoryByName(String name) {
        Optional<Category> category = categoryRepository.findOneByName(name);
        return category;
    }

    @Transactional(readOnly = true)
    public Optional<Category> getCategoryById(Integer id) {
        Optional<Category> category = categoryRepository.findOneByCategoryId(id);
        return category;
    }

    public Category createCategory(Category newCategory) {
        categoryRepository
                .findOneByName(newCategory.getName())
                .ifPresent(existingCategory -> {
                    throw new BadRequestAlertException("Category name already exists", "categoryManagement", "categoryexists");
                });
        Category category = categoryRepository.save(newCategory);
        return category;
    }

    public Optional<Category> updateCategory(Category category) {
        return Optional
                .of(categoryRepository.findOneByCategoryId(category.getCategory_id()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(
                        updateCategory -> {
                            updateCategory.setName(category.getName());
                            log.debug("Changed Information for Category: {}", updateCategory);
                            return updateCategory;
                        }
                );

    }

    public void deleteCategory(Integer id) {
        categoryRepository
                .findOneByCategoryId(id)
                .ifPresent(category -> {
                    categoryRepository.delete(category);
                    log.debug("Delete category: {}", category);
                });
    }
}
