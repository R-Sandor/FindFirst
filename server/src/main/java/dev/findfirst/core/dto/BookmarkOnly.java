package dev.findfirst.core.dto;

import java.util.Date;

public record BookmarkOnly(long id, String title, String url,  String screenshotUrl, boolean scrapable, Date createdDate, Date lastModifiedOn) {
}
