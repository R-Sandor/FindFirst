package dev.findfirst.core.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.findfirst.core.model.BookmarkTag;
import dev.findfirst.core.model.Tag;
import dev.findfirst.core.model.TagJDBC;
import dev.findfirst.core.repository.BookmarkTagRepository;
import dev.findfirst.core.repository.TagJDBCRepository;
import dev.findfirst.core.repository.TagRepository;
import dev.findfirst.security.userAuth.tenant.contexts.TenantContext;
import dev.findfirst.security.userAuth.tenant.repository.TenantRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {

  private final TagJDBCRepository tagRepositoryJDBC;

  private final BookmarkTagRepository bookmarkTagRepository;

  private final TagRepository tagRepository;

  private final TenantContext tenantContext;

  private final TenantRepository tRepository;

  private Tag addTag(String title) {
    return tagRepository.saveAndFlush(new Tag(title));
  }

  private TagJDBC addTagJDBC(String title) {
    var tenantId = tenantContext.getTenantId();
    var tenant = tRepository.findById(tenantId).orElseThrow();

    var tag = new TagJDBC(null, tenantId, tenant.getCreatedDate(), tenant.getCreatedBy(),
        tenant.getLastModifiedBy(), tenant.getLastModifiedDate(), title,
        new HashSet<BookmarkTag>());
    tag = tagRepositoryJDBC.save(tag);
    return tag;
  }

  /**
   * Simple checks if there is a tag with given tag title if not creates one and
   * returns the Tag.
   *
   * @param title
   * @return Tag existing tag with ID or a new Tag with the given title.
   */
  public Tag findOrCreateTag(String title) {
    return findByTagTitle(title).orElseGet(() -> addTag(title));
  }

  /**
   * Simple checks if there is a tag with given tag title if not creates one and
   * returns the Tag.
   *
   * @param title
   * @return Tag existing tag with ID or a new Tag with the given title.
   */
  public TagJDBC findOrCreateTagJDBC(String title) {
    return findByTagTitleJDBC(title).orElseGet(() -> addTagJDBC(title));
  }

  public TagJDBC getTagWithBookmarks(Long tagId) {
    TagJDBC tag = tagRepositoryJDBC.findById(tagId).orElseThrow();
    List<BookmarkTag> bookmarks = bookmarkTagRepository.findByTagId(tagId);
    tag.setBookmarks(new HashSet<>(bookmarks));
    return tag;
  }

  /**
   * Create List of tags by titles. Creating a new tags for ones that do not exist
   * and returning
   * list of existing tags.
   *
   * @param titles List of strings
   */
  public List<Tag> addAll(String... titles) {
    return Arrays.stream(titles).map(t -> findOrCreateTag(t)).toList();
  }

  /**
   * Create List of tags by titles. Creating a new tags for ones that do not exist
   * and returning
   * list of existing tags.
   *
   * @param titles List of strings
   */
  public List<TagJDBC> addAllJDBC(String... titles) {
    return Arrays.stream(titles).map(t -> findOrCreateTagJDBC(t)).toList();
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

  public Optional<TagJDBC> findByTagTitleJDBC(@NonNull String title) {
    return tagRepositoryJDBC.findByTitle(title, tenantContext.getTenantId());
  }

  public Optional<Tag> findByTagTitle(@NonNull String tag_title) {
    return tagRepository.findByTagTitle(tag_title);
  }
}
