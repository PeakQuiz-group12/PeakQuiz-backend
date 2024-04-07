package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.exceptions.TagNotFoundException;
import idatt2105.peakquizbackend.model.Tag;
import idatt2105.peakquizbackend.repository.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Tag findTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(TagNotFoundException::new);
    }

    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public boolean tagExists(Long userId, String title) {
      return tagRepository.findTagByUserIdAndTitle(userId, title).isPresent();
    }
}
