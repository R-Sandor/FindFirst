package dev.findfirst.bookmarkit.service;

import dev.findfirst.bookmarkit.model.Tag;
import dev.findfirst.bookmarkit.model.TagCntRecord;
import dev.findfirst.bookmarkit.repository.TagRepository;
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

  public void addAll(List<Tag> tags) {
    tagRepository.saveAllAndFlush(tags);
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
