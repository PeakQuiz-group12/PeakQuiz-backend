package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE")
public abstract class User {

  @Id
  @GenericGenerator(
          name = "USER_ID_GENERATOR",
          strategy = "enhanced-sequence",
          parameters = {
                 @Parameter(
                         name = "sequence_name",
                         value = "USERS_SEQUENCE"
                 ),
                 @Parameter(
                         name = "initial_value",
                         value = "1000"
                 )
          })
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

  @Size(
          min = 6,
          message = "Email is requied, minimum 6 characters."
  )
  @NotNull
  @Column(nullable = false)
  protected String email;

  // Hash(password + salt) (probably not the appropriate datatype)
  @NotNull
  @Column(nullable = false)
  protected String password;

  // Probably not the appropriate datatype
  @NotNull
  @Column(nullable = false)
  protected String salt;

  @OneToMany(mappedBy = "user")
  protected Set<Game> games = new HashSet<>();

  public Set<Game> getGames() {
    return games;
  }
}
