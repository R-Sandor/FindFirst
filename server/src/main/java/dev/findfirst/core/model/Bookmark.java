package dev.findfirst.core.model;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;

import dev.findfirst.security.userAuth.tenant.model.Tenantable;

@Entity
@Table(name = "bookmark")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bookmark extends Tenantable {

  public Bookmark(String title, String url) {
    this.title = title;
    this.url = url;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;

  @NonNull @Column(length = 512)
  private String title;

  @NonNull @Column(length = 2048)
  private String url;

  @Column(name = "screenshot_url", nullable = true)
  private String screenshotUrl;

  @PreRemove
  private void removeTagsFromBookmark() {
    tags.clear();
  }

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(name = "bookmark_tag", joinColumns = @JoinColumn(name = "bookmark_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
  private Set<Tag> tags = new HashSet<>();

  public void addTag(Tag tag) {
    this.tags.add(tag);
    tag.getBookmarks().add(this);
  }

  public void removeTag(Tag tag) {
    this.tags.remove(tag);
    tag.getBookmarks().remove(this);
  }
}
