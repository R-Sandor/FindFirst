package dev.findfirst.core.service;

import dev.findfirst.core.model.Tag;
import dev.findfirst.core.model.TagCntRecord;
import dev.findfirst.core.repository.TagRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagService {

  @Autowired TagRepository tagRepository;

  public Tag addTag(Tag tag) {
    return tagRepository.saveAndFlush(tag);
  }

  /**
   * Simple checks if there is a tag with given tag title if not creates one and returns the Tag.
   *
   * @param title
   * @return Tag existing tag with ID or a new Tag with the given title.
   */
  public Tag findOrCreateTag(String title) {
    return findByTagTitle(title).orElseGet(() -> addTag(new Tag(title)));
  }

  public List<Tag> addAll(List<Tag> tags) {
    return tagRepository.saveAllAndFlush(tags);
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

  public void deleteAllTags() {
    tagRepository.deleteAll();
  }

  public void deleteTag(Tag tag) {
    tagRepository.delete(tag);
  }

  public List<Tag> getTags() {
    return tagRepository.findAll();
  }

  public List<Tag> getTagsByBookmarkId(long id) {
    return tagRepository.findTagsByBookmarkId(id);
  }

  public List<TagCntRecord> getTagsWithCnt() {
    return tagRepository.customTagsWithCnt();
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
