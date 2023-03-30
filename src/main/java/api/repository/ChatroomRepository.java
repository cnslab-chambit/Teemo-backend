package api.repository;

import api.domain.Chatroom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatroomRepository {
    @PersistenceContext EntityManager em;

    public Long save(Chatroom chatroom){
        em.persist(chatroom);
        return chatroom.getId();
    }

    public Chatroom find(Long id) {
        return em.find(Chatroom.class, id);
    }

    public List<Chatroom> findGuestChatroom(Long tagId, Long memberId) {
        return em.createQuery(
                "select cr from Chatroom cr " +
                        "join fetch cr.tag t " +
                        "join fetch cr.guest g " +
                        "where t.id = :tagId " +
                        "and g.id = :guestId"
                , Chatroom.class)
                .setParameter("tagId", tagId)
                .setParameter("guestId",memberId)
                .getResultList();
    }

}
