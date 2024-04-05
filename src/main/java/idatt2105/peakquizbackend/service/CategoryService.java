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

  Category findCategoryByName(String name) {
    return categoryRepository.findCategoryByName(name).orElseThrow(CategoryNotFoundException::new);
  }
}
