package dev.renegade.bookmarkit.model;


import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import javax.persistence.GeneratedValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Table(name = "bookmark")
@Getter
@Setter
public class Bookmark {

    public Bookmark(){
    }

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

    //@ElementCollection
    @ManyToMany
    @JoinTable(
        name = "bookmark_tag", 
        joinColumns = @JoinColumn(name = "bookmark_id"), 
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

}
