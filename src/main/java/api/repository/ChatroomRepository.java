package api.repository;

import api.domain.Chatroom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ChatroomRepository {
    @PersistenceContext
    EntityManager em;

    public Long save(Chatroom chatroom){
        em.persist(chatroom);
        return chatroom.getId();
    }

    public Chatroom find(Long id){
        return em.find(Chatroom.class, id);
    }

    public void delete(Chatroom chatroom){
        em.remove(chatroom);
    }
}
