package dev.renegade.bookmarkit.model;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table(name = "bookmark_tag")
@Getter
@Setter
public class Tag {
    @Id
    @GeneratedValue
    private long id;

    @Column
    @NonNull
    private String tag_title;

    @ManyToMany
    @JoinColumn(name = "bookmark_id", nullable = false)
    Set<Bookmark> bookmark;

    public Tag(long id, String tagVal, Set<Bookmark> bookmarks){
        this.id = id;
        this.tag_title = tagVal;
        this.bookmark = bookmarks;
    }
    public Tag(){
    }
}
