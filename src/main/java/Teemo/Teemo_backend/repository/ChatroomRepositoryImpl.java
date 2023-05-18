package Teemo.Teemo_backend.repository;

import Teemo.Teemo_backend.domain.Chatroom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ChatroomRepositoryImpl implements ChatroomRepository{
    @PersistenceContext EntityManager em;

    @Override
    public Long save(Chatroom chatroom) {em.persist(chatroom); return chatroom.getId();}

    @Override
    public Chatroom findById(Long chatroomId) {return em.find(Chatroom.class, chatroomId);}

    @Override
    public Long delete(Chatroom chatroom) {
        Long result = chatroom.getId();
        em.remove(chatroom);
        return result;
    }
}
