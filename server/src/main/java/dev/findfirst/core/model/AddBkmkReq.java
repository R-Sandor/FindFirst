package dev.findfirst.core.model;

import java.util.List;

public record AddBkmkReq(String title, String url, List<Long> tagIds) {}
