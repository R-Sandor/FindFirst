package dev.renegade.bookmarkit.model;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "tag")
@Getter
@Setter
public class Tag {
    @Id
    @GeneratedValue
    private long id;

    @Column
    @NonNull
    private String tag_title;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    Set<Bookmark> bookmarks;

    public Tag(long id, String tagVal, Set<Bookmark> bookmarks){
        this.id = id;
        this.tag_title = tagVal;
        this.bookmarks = bookmarks;
    }

    public Tag(){
    }
}
