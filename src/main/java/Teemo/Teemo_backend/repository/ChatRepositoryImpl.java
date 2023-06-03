package Teemo.Teemo_backend.repository;

import Teemo.Teemo_backend.domain.Chat;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatRepositoryImpl implements ChatRepository
{
    @PersistenceContext EntityManager em;
    @Override
    public Long save(Chat chat) {
        em.persist(chat);
        return chat.getId();
    }
}
