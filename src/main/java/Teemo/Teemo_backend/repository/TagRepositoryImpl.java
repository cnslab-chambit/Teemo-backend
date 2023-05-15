package Teemo.Teemo_backend.repository;

import Teemo.Teemo_backend.domain.Gender;
import Teemo.Teemo_backend.domain.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository{
    @PersistenceContext EntityManager em;
    @Override
    public void save(Tag tag) {
        em.persist(tag);
    }
    @Override
    public Tag findById(Long tagId) {
        return em.find(Tag.class, tagId);
    }
    @Override
    public List<Tag> findByCriteria(Double latitude, Double longitude, Integer age, Gender gender) {
        return em.createQuery(
                        "SELECT t FROM Tag t " +
                                "WHERE t.lowerAge <= :age " + // 최저 나이보다 크거나 같고
                                "AND t.upperAge >= :age " + // 최고 나이보다 작거나 같고
                                "AND (t.targetGender = :gender OR t.targetGender = 'N') " + // targetGender 와 같거나 targetGender 가 "N" 인 경우
                                "AND ABS(t.latitude - :latitude) < 0.009 " + // 남북으로 각각 1 km 이내
                                "AND ABS(t.longitude - :longitude) < 0.012", // 동서로 각각 1 km 이내
                        Tag.class)
                .setParameter("age", age)
                .setParameter("gender", gender)
                .setParameter("latitude", latitude)
                .setParameter("longitude", longitude)
                .getResultList();
    }
    @Override
    public void delete(Tag tag) {
        em.remove(tag);
    }
}
