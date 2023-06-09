package Teemo.Teemo_backend.repository;

import Teemo.Teemo_backend.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class MemberRepositoryImpl implements MemberRepository
{
    @PersistenceContext EntityManager em;

    @Override
    public void save(Member member) {
        em.persist(member);
    }

    @Override
    public Member findById(Long id) {
        return em.find(Member.class,id);
    }

    @Override
    public Member findByEmail(String email) {
        log.info("repo email={}",email);
        String query = "SELECT m FROM Member m WHERE m.email = :email";
        TypedQuery<Member> typedQuery = em.createQuery(query, Member.class).setParameter("email", email);

        List<Member> results = typedQuery.getResultList();
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }
    @Override
    public Member findByNickname(String nickname) {
        String query = "SELECT m FROM Member m WHERE m.nickname = :nickname";
        TypedQuery<Member> typedQuery = em.createQuery(query, Member.class).setParameter("nickname", nickname);

        List<Member> results = typedQuery.getResultList();
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }
    @Override
    public Member findByEmailOrNickname(String email, String nickname) {
        String query = "SELECT m FROM Member m WHERE m.email = :email OR m.nickname = :nickname";
        TypedQuery<Member> typedQuery = em.createQuery(query, Member.class)
                .setParameter("email", email)
                .setParameter("nickname", nickname);

        List<Member> results = typedQuery.getResultList();
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }
    @Override
    public void delete(Member member) {
        em.remove(member);
    }
}
