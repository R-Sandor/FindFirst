package dev.findfirst.core.repository;

import dev.findfirst.core.model.TagCntRecord;
import java.util.List;

public interface TagRepoCustom {
  List<TagCntRecord> customTagsWithCnt();
}
