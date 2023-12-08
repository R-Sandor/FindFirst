package dev.findfirst.core.repository;

import dev.findfirst.core.model.Bookmark;
import dev.findfirst.core.model.TagCntRecord;
import dev.findfirst.security.contexts.TenantContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TagRepoCustomImpl implements TagRepoCustom {

  @Autowired EntityManager em;

  @Autowired private TenantContext tenantContext;

  @Override
  public List<TagCntRecord> customTagsWithCnt() {
    CriteriaBuilder qb = em.getCriteriaBuilder();
    CriteriaQuery<TagCntRecord> cq = qb.createQuery(TagCntRecord.class);
    Root<Bookmark> bkmk = cq.from(Bookmark.class);
    cq.multiselect(bkmk.get("tags"), qb.count(bkmk))
        .groupBy(bkmk.get("tags"))
        .where(qb.equal(bkmk.get("tenantId"), tenantContext.getTenantId()));
    return em.createQuery(cq).getResultList();
  }
}
