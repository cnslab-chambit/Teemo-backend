package api.repository;

import api.domain.Member;
import api.domain.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class TagRepository {
    @PersistenceContext EntityManager em;

    public Long save(Tag tag){
        em.persist(tag);
        return tag.getId();
    }

    public Tag find(Long id) {
        return em.find(Tag.class, id);
    }


}
