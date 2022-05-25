package dev.renegade.bookmarkit.model;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

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
  @GeneratedValue
  private Long id;

  @NonNull
  @Column
  private String title;

  @NonNull
  @Column
  private String url;

  @ManyToMany
  @JoinTable(
    name = "bookmark_tag",
    joinColumns = @JoinColumn(name = "bookmark_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private Set<Tag> tags;

  public void addTag(Tag tag){
      this.tags.add(tag);
      tag.getBookmarks().add(this);
  }

  public void removeTag(Tag tag) {
      this.tags.remove(tag);
      tag.getBookmarks().remove(this);
  }

}
