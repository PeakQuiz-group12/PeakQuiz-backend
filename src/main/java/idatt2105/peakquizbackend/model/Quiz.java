package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.validator.constraints.URL;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a quiz in the system.
 */
@Entity
@Audited
@Data
@Indexed
public class Quiz {
    @Id
    @GeneratedValue(generator = "quiz_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "quiz_id_seq", sequenceName = "quiz_id_seq")
    private Long id;

    @FullTextField(analyzer = "english")
    private String title;

    @FullTextField(analyzer = "english")
    private String description;

    private boolean isTemplate = false;

    @Column(columnDefinition = "TEXT", length = 2048)
    @URL(regexp = "(?i)^(http|https):\\/\\/.+\\.(jpg|jpeg|png|gif)$", message = "Invalid image URL format. Must start with http/https and be of type .jpg, .jpeg, .png or .gif")
    private String imageUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private ZonedDateTime createdOn;

    @NotAudited
    @Formula("(SELECT COUNT(*) FROM GAME G WHERE G.QUIZ_ID = ID)")
    private Integer playCount;

    @NotAudited
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "quiz", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
    private Set<Game> games = new HashSet<>();

    // Bidirectional mapping between quizzes and questions
    @IndexedEmbedded
    @OneToMany(cascade = { CascadeType.PERSIST,
            CascadeType.REFRESH }, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "quiz")
    private Set<Question> questions = new HashSet<>();

    @NotAudited
    @IndexedEmbedded
    @ManyToMany(mappedBy = "quizzes", fetch = FetchType.EAGER)
    private Set<Category> categories = new HashSet<>();

    @NotAudited
    @OneToMany(mappedBy = "quiz", fetch = FetchType.EAGER)
    private Set<Collaboration> collaborators = new HashSet<>();

    public void addQuestion(Question question) {
        this.questions.add(question);
        question.setQuiz(this);
    }

    public void removeQuestion(Question question) {
        this.questions.remove(question);
    }

    /**
     * Removes all associating categories from the quiz before deleting it to prevent integrity errors
     */
    @PreRemove
    private void removeCategoryAssociations() {
        for (Category category : this.categories) {
            category.getQuizzes().remove(this);
        }
    }
}