package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@DiscriminatorValue("M")
public class Member extends User {

  @NotNull
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  protected MemberType memberType;

}
