package dev.renegade.bookmarkit.model;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "bookmark")
@Data
@NoArgsConstructor
public class Bookmark {

  public Bookmark(String title, String url) {
    this.title = title;
    this.url = url;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NonNull
  @Column
  private String title;

  @NonNull
  @Column
  private String url;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "bookmark_tag",
    joinColumns = @JoinColumn(name = "bookmark_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private Set<Tag> tags;

  public void addTag(Tag tag) {
    this.tags.add(tag);
    tag.getBookmarks().add(this);
  }

  public void removeTag(Tag tag) {
    this.tags.remove(tag);
    tag.getBookmarks().remove(this);
  }
}
