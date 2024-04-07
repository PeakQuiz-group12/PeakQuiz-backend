package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.exceptions.CategoryNotFoundException;
import idatt2105.peakquizbackend.model.Category;
import idatt2105.peakquizbackend.repository.CategoryRepository;
import java.util.Set;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for managing categories.
 */
@AllArgsConstructor
@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    /**
     * Finds categories by quiz ID.
     * 
     * @param quizId
     *            The ID of the quiz to find categories for
     * @return Set of categories associated with the specified quiz
     */
    public Set<Category> findCategoriesByQuizId(Long quizId) {
        return categoryRepository.findCategoriesByQuizId(quizId);
    }

    /**
     * Finds a category by its name.
     * 
     * @param name
     *            The name of the category to find
     * @return The category with the specified name
     * @throws CategoryNotFoundException
     *             if no category with the specified name is found
     */
    public Category findCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name).orElseThrow(CategoryNotFoundException::new);
    }

    /**
     * Checks if a category exists by name.
     * 
     * @param name
     *            The name of the category to check
     * @return true if the category exists, false otherwise
     */
    public boolean categoryExists(String name) {
        return categoryRepository.findCategoryByName(name).isPresent();
    }

    /**
     * Saves a category.
     * 
     * @param category
     *            The category to save
     * @return The saved category
     */
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }
}
