package vn.ecall.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vn.ecall.domain.businessmodel.Category;
import vn.ecall.domain.businessmodel.Product;
import vn.ecall.repository.CategoryRepository;
import vn.ecall.security.AuthoritiesConstants;
import vn.ecall.service.CategoryService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CategoryResource {

    private static class CategoryResourceException extends RuntimeException {
        private CategoryResourceException(String message) { super(message); }
    }

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
            Arrays.asList("categoryId", "name")
    );

    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories(Pageable pageable) {
        log.debug("REST request to get all categories");
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }
        final Page<Category> page = categoryService.getAllCategories(pageable);
        return new ResponseEntity<>(page.getContent(), HttpStatus.OK);
    }

    @GetMapping("/provider/categories/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.PROVIDER + "\")")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        Category category = categoryService
                .getCategoryById(id)
                .orElseThrow(() -> new CategoryResourceException("Category can not be found"));
        return new ResponseEntity<>(category, HttpStatus.FOUND);
    }

    @PostMapping("/provider/categories")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.PROVIDER + "\")")
    public ResponseEntity<Category> createCatogory(@Valid @RequestBody Category newCategory) throws URISyntaxException {
        log.debug("REST request to create category");
        final Category category = categoryService.createCategory(newCategory);
        return ResponseEntity
                .created(new URI("api/provider/category/" + category.getCategory_id()))
                .body(category);
    }

    @PutMapping("/provider/categories")
    @PreAuthorize(("hasAuthority(\"" + AuthoritiesConstants.PROVIDER + "\")"))
    public ResponseEntity<Category> updateProduct(@Valid @RequestBody Category category) {
        log.debug("REST request to update Category: {}", category);
        Optional<Category> updateCategory = categoryService.updateCategory(category);
        //Need to check this.
        return updateCategory
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private boolean onlyContainsAllowedProperties(org.springframework.data.domain.Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    @DeleteMapping("/provider/categories/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.PROVIDER + "\")")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        log.debug("REST request to delete Product: {}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
