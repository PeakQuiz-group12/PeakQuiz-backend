package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.exceptions.CategoryNotFoundException;
import idatt2105.peakquizbackend.model.Category;
import idatt2105.peakquizbackend.repository.CategoryRepository;
import java.util.Set;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CategoryService {

  private CategoryRepository categoryRepository;

  public Set<Category> findCategoriesByQuizId(Long quizId) {
    return categoryRepository.findCategoriesByQuizId(quizId);
  }

  public Category findCategoryByName(String name) {
    return categoryRepository.findCategoryByName(name).orElseThrow(CategoryNotFoundException::new);
  }

  public boolean categoryExists(String name) {
    return categoryRepository.findCategoryByName(name).isPresent();
  }

  public Category saveCategory(Category category) {
    return categoryRepository.save(category);
  }
}
