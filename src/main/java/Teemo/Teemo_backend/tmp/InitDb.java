package Teemo.Teemo_backend.tmp;

import Teemo.Teemo_backend.domain.Gender;
import Teemo.Teemo_backend.domain.Tag;
import Teemo.Teemo_backend.domain.Member;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit0();
        initService.dbInit1();
        initService.dbInit2();
        initService.dbInit3();
        initService.dbInit4();
        initService.dbInit5();
        initService.dbInit6();
        initService.dbInit7();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit0() {
            Member member = new Member("test0@domain.0","password0","nickname0","2001-01-01", Gender.M);
            em.persist(member);
        }
        public void dbInit1() {
            Member member = new Member("test1@domain.1","password1","nickname1","1991-01-01", Gender.M);
            em.persist(member);
        }
        public void dbInit2(){
            Member member = new Member("test2@domain.2","password2","nickname2","1993-01-01", Gender.W);
            em.persist(member);
        }
        public void dbInit3(){
            Member member = new Member("test3@domain.3","password3","nickname3","1995-01-01", Gender.W);
            em.persist(member);
            Tag tag = new Tag("title3","detail3",3,Gender.W,35,25,37.623662704, 127.061441277, member);
            em.persist(tag);
        }
        public void dbInit4(){ // Tag 3 과 대체로 동일하나 targetGender 를 M 으로 설정
            Member member = new Member("test4@domain.4","password4","nickname4","1995-01-01", Gender.W);
            em.persist(member);

            Tag tag = new Tag("title4","detail4",3,Gender.M,35,25,37.623662704, 127.061441277, member);
            em.persist(tag);
        }

        public void dbInit5(){ // Tag 3 과 대체로 동일하나 targetGender 를 N 으로 설정
            Member member = new Member("test5@domain.5","password5","nickname5","1995-01-01", Gender.W);
            em.persist(member);

            Tag tag = new Tag("title5","detail5",3,Gender.N,35,25,37.623662704, 127.061441277, member);
            em.persist(tag);
        }

        public void dbInit6(){ // Tag 3 과 대체로 동일하나 나이 범위 를 좁혔다.
            Member member = new Member("test6@domain.6","password6","nickname6","1995-01-01", Gender.W);
            em.persist(member);

            Tag tag = new Tag("title6","detail6",3,Gender.W,32,27,37.623662704, 127.061441277, member);
            em.persist(tag);
        }

        public void dbInit7(){ // Tag 3 과 대체로 동일하나 위, 경도를 변경
            Member member = new Member("test7@domain.7","password7","nickname7","1995-01-01", Gender.W);
            em.persist(member);

            Tag tag = new Tag("title7","detail7",3,Gender.W,32,27,37.723662704, 127.161441277, member);
            em.persist(tag);
        }
    }
}
