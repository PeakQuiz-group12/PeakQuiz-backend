package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
public class User {
  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  @Id
  @GeneratedValue(generator = "user_id_seq", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(
          name = "user_id_seq",
          sequenceName = "user_id_seq"
  )
  protected Long id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false,
          updatable = false)
  @CreationTimestamp
  protected ZonedDateTime createdOn;

  @Size(
          min = 2,
          max = 20,
          message = "Username is required, maximum 20 characters."
  )
  @NotNull
  @Column(nullable = false)
  protected String username;

  @Email(message = "Email should be valid")
  @NotNull
  @Column(nullable = false)
  protected String email;

  // Hash(password + salt) (probably not the appropriate datatype)
  @Getter
  @NotNull
  @Column(nullable = false)
  protected String password;

  @OneToMany(mappedBy = "user")
  protected Set<Game> games = new HashSet<>();

  @OneToMany(mappedBy = "user")
  protected Set<Collaboration> collaborations = new HashSet<>();

  @OneToMany(
          mappedBy = "user",
          fetch = FetchType.LAZY,
          cascade = CascadeType.REMOVE
  )
  public Set<Tag> tags = new HashSet<>();

  public String getUsername() {
    return username;
  }

  public String getEmail () {
    return email;
  }
  public Set<Game> getGames() {
    return games;
  }

  public Set<Collaboration> getCollaborations() {
    return collaborations;
  }

}
