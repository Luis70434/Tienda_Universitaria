package edu.unimagdalena.ecommerce.services.servicesImpl;



import edu.unimagdalena.ecommerce.api.dto.CategoryDtos.CategoryResponse;
import edu.unimagdalena.ecommerce.api.dto.CategoryDtos.CreateCategoryRequest;
import edu.unimagdalena.ecommerce.entities.Category;
import edu.unimagdalena.ecommerce.repositories.CategoryRepository;
import edu.unimagdalena.ecommerce.services.CategoryService;
import edu.unimagdalena.ecommerce.services.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;
    private final CategoryMapper mapper;

    @Override
    public CategoryResponse create(CreateCategoryRequest req) {


        Category category = mapper.toEntity(req);
        Category savedCategory = categoryRepo.save(category);
        return mapper.toResponse(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse get(UUID id) {
        return categoryRepo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Categoría con ID " + id + " no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> list() {
        return categoryRepo.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        categoryRepo.deleteById(id);
    }
}