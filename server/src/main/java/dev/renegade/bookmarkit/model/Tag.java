package dev.renegade.bookmarkit.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "tag")
@Getter
@Setter
@AllArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NonNull
    private String tag_title;

      @ManyToMany(fetch = FetchType.EAGER,
      cascade = {
          CascadeType.PERSIST,
          CascadeType.MERGE
      },
      mappedBy = "tags")
    @JsonIgnore
    Set<Bookmark> bookmarks;

    public Tag(String tagVal){
        this.tag_title = tagVal;
    }

    public Set<Bookmark> getBookmarks(){
        return this.bookmarks;
    }

    public Tag(){
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof Tag){
            Tag t = (Tag) obj;
            return t.tag_title.equals(this.tag_title);
        } else
        return false;
    }

}
