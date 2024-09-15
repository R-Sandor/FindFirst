package dev.findfirst.core.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import dev.findfirst.core.model.Tag;
import dev.findfirst.core.repository.TagRepository;

@Service
@RequiredArgsConstructor
public class TagService {

  private final TagRepository tagRepository;

  public Tag addTag(String title) {
    return tagRepository.saveAndFlush(new Tag(title));
  }

  /**
   * Simple checks if there is a tag with given tag title if not creates one and returns the Tag.
   *
   * @param title
   * @return Tag existing tag with ID or a new Tag with the given title.
   */
  public Tag findOrCreateTag(String title) {
    return findByTagTitle(title).orElseGet(() -> addTag(title));
  }

  /**
   * Create List of tags by titles. Creating a new tags for ones that do not exist and returning
   * list of existing tags.
   *
   * @param titles List of strings
   */
  public List<Tag> addAll(String... titles) {
    return Arrays.stream(titles).map(t -> findOrCreateTag(t)).toList();
  }

  public List<Tag> deleteAllTags() {
    var userTags = tagRepository.findAll();
    tagRepository.deleteAll(userTags);
    return userTags;
  }

  public List<Tag> getTags() {
    return tagRepository.findAll();
  }

  public List<Tag> getTagsByBookmarkId(long id) {
    return tagRepository.findTagsByBookmarkId(id);
  }

  public Optional<Tag> getTagByTitle(String title) {
    return tagRepository.findByTagTitle(title);
  }

  public Optional<Tag> findById(Long id) {
    return tagRepository.findById(id);
  }

  public Optional<Tag> findByTagTitle(@NonNull String tag_title) {
    return tagRepository.findByTagTitle(tag_title);
  }
}
