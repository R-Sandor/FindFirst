package dev.renegade.bookmarkit;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
public class Bookmark {

    public Bookmark(String title, String url) {
        this.title = title;
        this.url = url.replaceAll(" ", "") + ".com";
    }

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String title;

    @NonNull
    private String url;

}
