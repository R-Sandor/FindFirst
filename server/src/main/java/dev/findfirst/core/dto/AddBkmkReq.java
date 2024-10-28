package dev.findfirst.core.dto;

import java.util.List;

public record AddBkmkReq(String title, String url, List<Long> tagIds, boolean scrapable) {
}
