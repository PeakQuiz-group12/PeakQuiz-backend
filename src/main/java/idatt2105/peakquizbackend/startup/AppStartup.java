package idatt2105.peakquizbackend.startup;

import idatt2105.peakquizbackend.model.Category;
import idatt2105.peakquizbackend.model.Question;
import idatt2105.peakquizbackend.model.Quiz;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.model.embedded.Answer;
import idatt2105.peakquizbackend.model.enums.QuestionType;
import idatt2105.peakquizbackend.service.AuthService;
import idatt2105.peakquizbackend.service.CategoryService;
import idatt2105.peakquizbackend.service.QuizService;
import idatt2105.peakquizbackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Class that populates the database with testdata
 */
@Component
@AllArgsConstructor
public class AppStartup implements CommandLineRunner {

    private CategoryService categoryService;

    private QuizService quizService;

    private UserService userService;

    private AuthService authService;

    /**
     * Runs on application startup.
     *
     * @param args
     *            Command line arguments
     */
    @Override
    public void run(String... args) {
        prepareUser();
        prepareCategories();
        prepareTemplates();
    }

    /**
     * Adds categories that can be used for quizzes
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
     * Add test user with username "test"
     *
     */
    private void prepareUser() {
        if (userService.usernameExists("test"))
            return;

        User user = new User();
        user.setUsername("test");
        user.setPassword(authService.encryptPassword("Aa12345!"));
        user.setEmail("test@test.com");
        userService.saveUser(user);
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
        quiz.setImageUrl("https://zelda.nintendo.com/breath-of-the-wild/assets/icons/BOTW-Share_icon.jpg");
        quiz.setDescription("This is a quiz");

        Question question = new Question();
        question.setText("How many in a dozen?");
        question.setQuestionType(QuestionType.MULTIPLE_CHOICE);
        question.setDifficulty((byte) 5);
        question.setAnswers(Set.of(new Answer("76", false), new Answer("12", true)));

        Question question1 = new Question();
        question1.setText("When did WWII end?");
        question1.setMedia(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6c/Matilda_tanks_on_the_move_outside_the_perimeter_of_Tobruk%2C_Libya%2C_18_November_1941._E6600.jpg/173px-Matilda_tanks_on_the_move_outside_the_perimeter_of_Tobruk%2C_Libya%2C_18_November_1941._E6600.jpg");
        question1.setQuestionType(QuestionType.FILL_IN_BLANK);
        question1.setDifficulty((byte) 5);
        question1.setAnswers(Set.of(new Answer("1945", true)));
        quiz.addQuestion(question1);
        quizService.saveQuiz(quiz);
    }
}
