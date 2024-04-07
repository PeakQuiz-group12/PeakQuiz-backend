package idatt2105.peakquizbackend.model.embedded;

import idatt2105.peakquizbackend.model.Collaboration;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Parent;

import java.time.ZonedDateTime;
import java.util.Objects;

@Embeddable
public class Comment {
    @Column(nullable = false)
    private String text;

    // TODO: Check up on the compile time error ('Basic' attribute type should not be 'Persistence Entity')
    @Parent
    private Collaboration collaboration;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private ZonedDateTime createdOn;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Comment comment)) return false;
    return text.equals(comment.text);
  }

    public Collaboration getCollaboration() {
        return collaboration;
    }

    public void setCollaboration(Collaboration collaboration) {
        this.collaboration = collaboration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
