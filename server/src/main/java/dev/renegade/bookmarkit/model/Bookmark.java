package dev.renegade.bookmarkit.model;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Table(name = "bookmark")
@Getter
@Setter
public class Bookmark {

    public Bookmark(){
        System.out.println("HEREEEEEEE!!!!!!");

    }

    public Bookmark(String title, String url) {
        System.out.println("HEREEEEEEE");
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
    //private List<String> tags = new ArrayList<>();

}
