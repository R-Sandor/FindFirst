package dev.findfirst.bookmarkit.repository;

import dev.findfirst.bookmarkit.model.Bookmark;
import dev.findfirst.bookmarkit.model.Tag;
import dev.findfirst.bookmarkit.model.TagCntRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TagRepoCustomImpl implements TagRepoCustom {

  @Autowired EntityManager em;

  @Override
  public List<TagCntRecord> customTagsWithCnt() {
    CriteriaBuilder qb = em.getCriteriaBuilder();
    CriteriaQuery<TagCntRecord> cq = qb.createQuery(TagCntRecord.class);
    Root<Bookmark> bkmk = cq.from(Bookmark.class);
    Join<Bookmark, Tag> tag_bookmark = bkmk.join("tags");
    cq.multiselect(bkmk.get("tags"), qb.count(bkmk)).groupBy(tag_bookmark, bkmk.get("tags"));
    return em.createQuery(cq).getResultList();
  }


  
}
