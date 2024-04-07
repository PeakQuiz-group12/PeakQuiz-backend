package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.exceptions.TagNotFoundException;
import idatt2105.peakquizbackend.model.Tag;
import idatt2105.peakquizbackend.repository.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class for managing tag-related operations.
 */
@Service
@AllArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    /**
     * Finds a tag by ID.
     * 
     * @param id
     *            The ID of the tag to find
     * @return The found tag
     * @throws TagNotFoundException
     *             if the tag is not found
     */
    public Tag findTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(TagNotFoundException::new);
    }

    /**
     * Saves a tag.
     * 
     * @param tag
     *            The tag to save
     * @return The saved tag
     */
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    /**
     * Checks if a tag exists.
     * 
     * @param userId
     *            The ID of the user associated with the tag
     * @param title
     *            The title of the tag to check
     * @return true if the tag exists, false otherwise
     */
    public boolean tagExists(Long userId, String title) {
        return tagRepository.findTagByUserIdAndTitle(userId, title).isPresent();
    }
}
