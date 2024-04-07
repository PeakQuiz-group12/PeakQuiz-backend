package idatt2105.peakquizbackend.startup;

import idatt2105.peakquizbackend.model.Category;
import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.embedded.Answer;
import idatt2105.peakquizbackend.model.enums.QuestionType;
import idatt2105.peakquizbackend.service.CategoryService;
import idatt2105.peakquizbackend.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Startup component for initializing data on application startup.
 */
@Component
public class AppStartup implements CommandLineRunner {

    @Autowired
    CategoryService categoryService;

    @Autowired
    QuizService quizService;

    /**
     * Runs on application startup.
     *
     * @param args
     *            Command line arguments
     */
    @Override
    public void run(String... args) {
        prepareCategories();
        prepareTemplates();
    }

    /**
     * Prepares categories if they do not exist already.
     */
    private void prepareCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("History");
        categories.add("Music");
        categories.add("Math");

        categories.stream().filter(category -> !categoryService.categoryExists(category)).forEach(category -> {
            Category newCategory = new Category();
            newCategory.setName(category);
            categoryService.saveCategory(newCategory);
        });
    }

    /**
     * Prepares quiz templates if they do not exist already.
     */
    private void prepareTemplates() {
        final int nrTemplates = 1;
        if (quizService.findAllTemplates(PageRequest.of(0, 3)).getTotalElements() == nrTemplates)
            return;

        Quiz quiz = new Quiz();
        quiz.setTemplate(true);

        Category category = categoryService.findCategoryByName("History");
        quiz.setCategories(Set.of(category));
        quiz.setTitle("Smash");
        quiz.setDescription("This is a quiz");

        Question question = new Question();
        question.setText("How many");
        question.setQuestionType(QuestionType.MULTIPLE_CHOICE);
        question.setDifficulty((byte) 5);
        question.setAnswers(Set.of(new Answer("76", false), new Answer("55", true)));

        quiz.addQuestion(question);
        quizService.saveQuiz(quiz);
    }
}
