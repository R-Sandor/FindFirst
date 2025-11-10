package dev.findfirst.core.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true, chain = false)
@Getter(onMethod = @__(@JsonProperty))
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkDTO {

  private long id;
  private String title;
  private String url;
  private String screenshotUrl;
  private boolean scrapable;
  private Date createdDate;
  private Date lastModifiedOn;
  private List<TagOnly> tags;
  private String textHighlight;

  public BookmarkDTO(long id, String title, String url, String screenshotUrl, boolean scrapable,
      Date createdDate, Date lastModifiedOn, List<TagOnly> tags) {
    // we don't have any highlighted text.
    this(id, title, url, screenshotUrl, scrapable, createdDate, lastModifiedOn, tags, null);
  }
}
