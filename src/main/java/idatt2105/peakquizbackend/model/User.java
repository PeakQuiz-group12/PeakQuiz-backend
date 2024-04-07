package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
@Data
public class User {
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(generator = "user_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq")
    private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false,
          updatable = false)
  @CreationTimestamp
  private ZonedDateTime createdOn;

  @NotNull
  @Column(nullable = false, unique = true)
  private String username;

    @Email(message = "Email should be valid")
    @NotNull
    @Column(nullable = false, unique = true)
    private String email;


    // Hash(password + salt) (probably not the appropriate datatype)
    @Getter
    @NotNull
    @Column(nullable = false)
    private String password;

    @Getter
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Game> games = new HashSet<>();

    @ToString.Include
  private String getGamesToString() {
    if (games == null) return "";
    return games.stream().map(Game::getId).toString();
  }

  @Getter
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Collaboration> collaborations = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = {
                  CascadeType.REMOVE,
                  CascadeType.PERSIST,
                  CascadeType.MERGE
          }
  )
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
    public Set<Tag> tags = new HashSet<>();

  @ToString.Include
  String getTagsToString() {
    if (tags == null) return "";
    return tags.stream().map(Tag::getId).toString();
  }
}
