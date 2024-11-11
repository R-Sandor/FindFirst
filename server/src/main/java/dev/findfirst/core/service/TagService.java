package dev.findfirst.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.NotBlank;

import dev.findfirst.core.dto.BookmarkOnly;
import dev.findfirst.core.dto.TagDTO;
import dev.findfirst.core.model.jdbc.TagJDBC;
import dev.findfirst.core.repository.jdbc.BookmarkJDBCRepository;
import dev.findfirst.core.repository.jdbc.BookmarkTagRepository;
import dev.findfirst.core.repository.jdbc.TagJDBCRepository;
import dev.findfirst.security.userAuth.tenant.contexts.TenantContext;
import dev.findfirst.security.userAuth.tenant.repository.TenantRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

  private final TagJDBCRepository tagRepositoryJDBC;

  private final BookmarkTagRepository bookmarkTagRepository;


  private final TenantContext tenantContext;

  private final TenantRepository tRepository;

  private final BookmarkJDBCRepository bookmarkRepo;

  private TagJDBC addTagJDBC(String title) {
    var tenantId = tenantContext.getTenantId();
    var tenant = tRepository.findById(tenantId).orElseThrow();

    var tag = new TagJDBC(null, tenantId, tenant.getCreatedDate(), tenant.getCreatedBy(),
        tenant.getLastModifiedBy(), tenant.getLastModifiedDate(), title);
    tag = tagRepositoryJDBC.save(tag);
    return tag;
  }

  /**
   * Simple checks if there is a tag with given tag title if not creates one and returns the Tag.
   *
   * @param title
   * @return Tag existing tag with ID or a new Tag with the given title.
   */
  public TagDTO findOrCreateTag(String title) {
    var tagExisting = findByTagTitleJDBC(title);
    TagJDBC t = tagExisting.orElseGet(() -> addTagJDBC(title));
    return convertTagJDBCToDTO(List.of(t), tenantContext.getTenantId(), true).get(0);
  }

  public List<TagJDBC> findAllById(List<Long> ids) {
    var tags = new ArrayList<TagJDBC>();
    tagRepositoryJDBC.findAllById(ids).forEach(tags::add);
    return tags;
  }

  public List<TagDTO> convertTagJDBCToDTO(List<TagJDBC> tagEntities, int tenantId,
      boolean withBookmarks) {

    // Get the bookmarks that are associated to the Tag.
    return tagEntities.stream().map(ent -> {
      var bkmkIds = bookmarkTagRepository.getAllBookmarkIdsForTag(ent.getId(), tenantId);
      var bkmkEnts = bookmarkRepo.findAllById(bkmkIds);

      List<BookmarkOnly> bookmarkDTOs = new ArrayList<>();

      if (withBookmarks) {
        for (var b : bkmkEnts) {
          bookmarkDTOs.add(new BookmarkOnly(b.getId(), b.getTitle(), b.getUrl(),
              b.getScreenshotUrl(), b.getScrapable(), b.getCreatedDate(), b.getLastModifiedDate()));
        }
      }

      return new TagDTO(ent.getId(), ent.getTitle(), bookmarkDTOs);
    }).toList();
  }

  /**
   * Simple checks if there is a tag with given tag title if not creates one and returns the Tag.
   *
   * @param title
   * @return Tag existing tag with ID or a new Tag with the given title.
   */
  public TagJDBC findOrCreateTagJDBC(String title) {
    return findByTagTitleJDBC(title).orElseGet(() -> addTagJDBC(title));
  }

  public TagDTO getTagWithBookmarks(Long tagId) {
    TagJDBC tag = tagRepositoryJDBC.findById(tagId).orElseThrow();
    return convertTagJDBCToDTO(List.of(tag), tenantContext.getTenantId(), true).get(0);
  }

  /**
   * Create List of tags by titles. Creating a new tags for ones that do not exist and returning
   * list of existing tags.
   *
   * @param titles List of strings
   */
  public List<TagDTO> addAll(String... titles) {
    return Arrays.stream(titles).map(t -> findOrCreateTag(t)).toList();
  }

  /**
   * Create List of tags by titles. Creating a new tags for ones that do not exist and returning
   * list of existing tags.
   *
   * @param titles List of strings
   */
  public List<TagJDBC> addAllJDBC(String... titles) {
    return Arrays.stream(titles).map(t -> findOrCreateTagJDBC(t)).toList();
  }

  public List<TagDTO> deleteAllTags() {
    var userTags = tagRepositoryJDBC.findAllByTenantId(tenantContext.getTenantId());
    var deleted = convertTagJDBCToDTO(userTags, tenantContext.getTenantId(), true);
    bookmarkTagRepository.deleteAllTagsByUser();
    tagRepositoryJDBC.deleteAll(userTags);
    return deleted;
  }

  public List<TagDTO> getTags() {
    var tagsJDBC = tagRepositoryJDBC.findAllByTenantId(tenantContext.getTenantId());
    return convertTagJDBCToDTO(tagsJDBC, tenantContext.getTenantId(), true);
  }

  public List<TagDTO> findAllTags(List<Long> tags, long tenantId, boolean withBookmarks) {
    var tagsJDBC = tagRepositoryJDBC.findAllById(tags);
    List<TagJDBC> tagEnts = new ArrayList<>();
    tagsJDBC.forEach(tagEnts::add);
    return convertTagJDBCToDTO(tagEnts, tenantContext.getTenantId(), withBookmarks);
  }

  public List<TagDTO> getTagsByBookmarkId(long id) {
    List<Long> tags =
        bookmarkTagRepository.findByBookmarkId(id).stream().map(bt -> bt.getTagId()).toList();
    List<TagJDBC> tagEnts = new ArrayList<>();
    tagRepositoryJDBC.findAllById(tags).forEach(tagEnts::add);
    return convertTagJDBCToDTO(tagEnts, tenantContext.getTenantId(), true);
  }

  public Optional<TagJDBC> findByIdJDBC(long id) {
    return tagRepositoryJDBC.findById(id);
  }

  public Optional<TagJDBC> findByTagTitleJDBC(@NonNull String title) {
    var tag = tagRepositoryJDBC.findByTitle(title, tenantContext.getTenantId());
    if (tag.isPresent()) {
      return Optional.of(tag.get());
    } else {
      return Optional.ofNullable(null);
    }
  }

  public Optional<Long> findIdByTagTitleJDBC(@NotBlank String title) {
    var tag = tagRepositoryJDBC.findIdByTitle(title, tenantContext.getTenantId());
    if (tag.isPresent()) {
      return Optional.of(tag.get());
    } else {
      return Optional.ofNullable(null);
    }
  }
}
