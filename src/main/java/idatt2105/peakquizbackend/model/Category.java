package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the category of quizzes. Categories are used to classify quizzes into different groups. A quiz can belong
 * to multiple categories, and a category can contain multiple quizzes.
 */
@Entity
@Table(name = "CATEGORY")
@Data
public class Category {

    @Id
    @GeneratedValue(generator = "category_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "category_id_seq", sequenceName = "category_id_seq")
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    @Size(min = 2, max = 20, message = "Name is required, maximum 20 characters.")
    private String name;

    // Bidirectional mapping between Category and Quiz
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "CATEGORY_QUIZ", joinColumns = @JoinColumn(name = "CATEGORY_ID"), inverseJoinColumns = @JoinColumn(name = "QUIZ_ID"))
    private Set<Quiz> quizzes = new HashSet<>();

    /**
     * Adds a quiz to the category.
     * 
     * @param quiz
     *            The quiz to be added to the category.
     */
    public void addQuiz(Quiz quiz) {
        this.quizzes.add(quiz);
        quiz.getCategories().add(this);
    }

    /**
     * Removes a quiz from the category.
     * 
     * @param quiz
     *            The quiz to be removed from the category.
     */
    public void removeQuiz(Quiz quiz) {
        this.quizzes.remove(quiz);
        quiz.getCategories().remove(this);
    }

    /**
     * Constructs a new Category instance.
     */
    public Category() {
    }

    /**
     * Constructs a new Category instance with the specified name.
     * 
     * @param name
     *            The name of the category.
     */
    public Category(String name) {
        this.name = name;
    }
}
