package idatt2105.peakquizbackend.startup;

import idatt2105.peakquizbackend.model.Category;
import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.embedded.Answer;
import idatt2105.peakquizbackend.service.CategoryService;
import idatt2105.peakquizbackend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class AppStartup implements CommandLineRunner {

    @Autowired
    CategoryService categoryService;

    @Autowired QuizService quizService;

    @Override
    public void run(String... args) {
        prepareCategories();
        prepareTemplates();
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

    private void prepareTemplates() {
        // todo: if test for whether the template exists
        // todo: set id to 1L
        Quiz quiz = new Quiz();
        quiz.setTemplate(true);

        // todo: might cause duplicate category issues
        quiz.setCategories(Set.of(new Category("History")));
        quiz.setTitle("Smash");
        quiz.setDescription("This is a quiz about smash");

        Question question = new Question();
        question.setText("How many characters are there in Smash ultimate");
        question.getAnswers().add(new Answer());
        question.getAnswers().add(new Answer());

        quizService.saveQuiz(quiz);
        //quiz.setQuestions(Set.ofquestion);
        }
}