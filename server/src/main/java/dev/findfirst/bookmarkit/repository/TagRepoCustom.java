package dev.findfirst.bookmarkit.repository;

import dev.findfirst.bookmarkit.model.TagCntRecord;
import java.util.List;

public interface TagRepoCustom {
  List<TagCntRecord> customTagsWithCnt();
}
