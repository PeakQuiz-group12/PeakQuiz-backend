package idatt2105.peakquizbackend.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("A")
public class Administrator extends User {

}
