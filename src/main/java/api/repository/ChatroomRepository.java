package api.repository;

import api.domain.Chatroom;
import api.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatroomRepository {
    @PersistenceContext
    EntityManager em;

    public Long save(Chatroom chatroom){
        em.persist(chatroom);
        return chatroom.getId();
    }

    public List<Chatroom> findHostChatroom(Long id)
    {
        /*
         * 호스트의 id를 입력받아 관련 Chatroom List를 반환합니다.
         */
        return em.createQuery("select c from Chatroom c where c.host.id = :id", Chatroom.class)
                .setParameter("id", id)
                .getResultList();
    }

    public Chatroom findGuestChatroom(Long id)
    {
        /*
         * 게스트 id를 입력받아 관련 Chatroom 객체 을 반환합니다.
         */
        return em.createQuery("select c from Chatroom c where c.guest.id = :id", Chatroom.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public void remove(Chatroom chatroom){
        /*
         * 전달 받은 chatroom 객체를 db 상에서 삭제합니다.
         */
        em.remove(chatroom);
    }

}
