package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.exceptions.CategoryNotFoundException;
import idatt2105.peakquizbackend.model.Category;
import idatt2105.peakquizbackend.repository.CategoryRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

  @Autowired
  CategoryRepository categoryRepository;
  List<Category> findCategoriesByQuizId(Long quizId) {
    return categoryRepository.findCategoriesByQuizId(quizId);
  }

  Category findCategoryByName(String name) {
    return categoryRepository.findCategoryByName(name).orElseThrow(CategoryNotFoundException::new);
  }
}
