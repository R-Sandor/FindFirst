package dev.findfirst.core.dto;


public record UpdateBookmarkReq(long id,  String title,  String url, Boolean isScrapable) {
}
