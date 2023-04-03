package api;

import api.domain.Gender;
import api.domain.Member;
import api.domain.Role;
import api.domain.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
        initService.dbInit3();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Member member = new Member("test1@email.com",
                    "0000",
                    "nickNam1",
                    LocalDate.of(1998, 12, 25),
                    Role.VIEWER,
                    Gender.MAN);
            em.persist(member);

            Tag tag = new Tag(
                    "testTagTitle1",
                    "testTagDetail1",
                    1,
                    Gender.WOMAN,
                    25,
                    20,
                    37.6190,
                    127.0597,
                    member
            );
            em.persist(tag);
        }

        public void dbInit2(){
            Member member = new Member("test2@email.com",
                    "1234",
                    "nickNam2",
                    LocalDate.of(2000, 12, 01),
                    Role.VIEWER,
                    Gender.WOMAN);
            em.persist(member);

            Tag tag = new Tag(
                    "testTagTitle2",
                    "testTagDetail2",
                    3,
                    Gender.MAN,
                    25,
                    20,
                    37.6190,
                    127.0597,
                    member
            );
            em.persist(tag);
        }

        public void dbInit3() {
            Member member = new Member("test3@email.com",
                    "password3",
                    "nickNam3",
                    LocalDate.of(1998, 02, 25),
                    Role.VIEWER,
                    Gender.WOMAN);
            em.persist(member);
        }
    }

}
