package idatt2105.peakquizbackend.model.embedded;

import idatt2105.peakquizbackend.model.Collaboration;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Parent;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * An embedded class representing a comment associated with a collaboration.
 */
@Embeddable
@Data
public class Comment {

    /**
     * The text content of the comment.
     */
    @Column(nullable = false)
    private String text;

    /**
     * The collaboration instance associated with the comment.
     */
    @Parent
    private Collaboration collaboration;

    /**
     * The timestamp when the comment was created.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private ZonedDateTime createdOn;

}
