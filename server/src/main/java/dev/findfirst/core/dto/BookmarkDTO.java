package dev.findfirst.core.dto;

import java.util.List;

public record BookmarkDTO(long id, String title, String url, String screenshotUrl, boolean scrapable, List<TagDTO> tags) {
}
