package dev.findfirst.core.model;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import dev.findfirst.security.userAuth.tenant.model.Tenantable;

@Entity
@Table(name = "tag")
@Getter
@Setter
@AllArgsConstructor
public class Tag extends Tenantable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 255)
  @NonNull
  private String tag_title;

  public Tag() {
  }

  public Tag(String tagVal) {
    this.tag_title = tagVal;
  }

  @ManyToMany(mappedBy = "tags")
  @JsonIgnoreProperties("tags")
  Set<Bookmark> bookmarks = new HashSet<>();

  public Set<Bookmark> getBookmarks() {
    return this.bookmarks;
  }

  @PreRemove
  private void removeBookAssociations() {
    for (var bkmk : this.bookmarks) {
      bkmk.removeTag(this);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Tag) {
      Tag t = (Tag) obj;
      return t.tag_title.equals(this.tag_title);
    } else
      return false;
  }

  @Override
  public int hashCode() {
    return id.intValue() * tag_title.hashCode();
  }

  @Override
  public String toString() {
    return this.tag_title + " " + this.id;
  }
}
