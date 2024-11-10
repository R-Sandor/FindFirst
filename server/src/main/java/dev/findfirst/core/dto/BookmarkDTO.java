package dev.findfirst.core.dto;

import java.util.Date;
import java.util.List;

public record BookmarkDTO(long id, String title, String url,  String screenshotUrl, boolean scrapable, Date createdDate, Date lastModifiedOn, List<TagDTO> tags) {
}
