package dev.findfirst.core.model;

import java.sql.Date;
import java.util.List;

import dev.findfirst.core.model.jpa.Bookmark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Screenshot {

  private Long id;
  private String path;
  private int captureAttemptsFailed;
  private boolean disabled;
  private boolean manuallyCaptured;
  private Date lastUpdated;
  private List<Bookmark> bookmarksWithScreenshot;
}
