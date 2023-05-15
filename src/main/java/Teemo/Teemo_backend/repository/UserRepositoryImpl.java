package Teemo.Teemo_backend.repository;

import Teemo.Teemo_backend.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository
{
    @PersistenceContext EntityManager em;

    @Override
    public void save(User user) {
        em.persist(user);
    }

    @Override
    public User findById(Long id) {
        return em.find(User.class,id);
    }

    @Override
    public User findByEmail(String email) {
        return em.find(User.class, email);
    }

    @Override
    public User findByNickname(String nickname) {
        return em.find(User.class, nickname);
    }

    @Override
    public void delete(User user) {
        em.remove(user);
    }
}
