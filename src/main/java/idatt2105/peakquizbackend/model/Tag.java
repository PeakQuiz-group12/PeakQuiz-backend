package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a tag associated with a user in the system.
 */
@Entity
@Table(name = "TAG", uniqueConstraints = { @UniqueConstraint(columnNames = { "USER_ID", "TITLE" }) })
@Data
public class Tag {

    @Id
    @GeneratedValue(generator = "tag_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "tag_id_seq", sequenceName = "tag_id_seq")
    private Long id;

    @NotNull
    @Column(nullable = false)
    @Size(min = 2, max = 20, message = "Title is required, maximum 20 characters.")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    // Unidirectional mapping between tag and question

    @ManyToMany(cascade = { CascadeType.DETACH, })
    @JoinTable(name = "TAG_QUESTION", joinColumns = @JoinColumn(name = "TAG_ID"), inverseJoinColumns = @JoinColumn(name = "QUESTION_ID"))
    private Set<Question> questions = new HashSet<>();
}
