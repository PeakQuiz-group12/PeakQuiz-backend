package idatt2105.peakquizbackend.startup;

import idatt2105.peakquizbackend.model.Category;
import idatt2105.peakquizbackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AppStartup implements CommandLineRunner {

    @Autowired
    CategoryService categoryService;

    @Override
    public void run(String... args) {
        prepareCategories();
    }

    private void prepareCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("History");
        categories.add("Music");
        categories.add("Math");

        categories
                .stream()
                .filter(category -> !categoryService.categoryExists(category))
                .forEach(category -> {
                    Category newCategory = new Category();
                    newCategory.setName(category);
                    categoryService.saveCategory(newCategory);
                });
    }
}