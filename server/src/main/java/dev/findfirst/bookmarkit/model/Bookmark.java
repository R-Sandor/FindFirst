package dev.findfirst.bookmarkit.model;

import dev.findfirst.bookmarkit.security.model.Tenantable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
  private Long id;

  @NonNull @Column(length = 50)
  private String title;

  @NonNull @Column(length = 255)
  private String url;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "bookmark_tag",
      joinColumns = @JoinColumn(name = "bookmark_id"),
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
