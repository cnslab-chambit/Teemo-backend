package Teemo.Teemo_backend.repository;

import Teemo.Teemo_backend.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
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
        return em.find(Member.class, email);
    }

    @Override
    public Member findByNickname(String nickname) {
        return em.find(Member.class, nickname);
    }

    @Override
    public void delete(Member member) {
        em.remove(member);
    }
}
