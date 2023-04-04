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

        public void dbInit1() {
            /** 호스트 전용 Data */
            Member member = new Member("test1@email.com",
                    "pass01",
                    "nickNam1",
                    LocalDate.of(1998, 12, 25),
                    Gender.MAN);
            em.persist(member);

            /**
             * 여성만 모집
             * 나이 범위: 20 ~ 25
             * 태그 생성 위치: (37.6190, 127.0597)
             */
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
            /** 호스트 전용 Data */
            Member member = new Member("test2@email.com",
                    "pass02",
                    "nickNam2",
                    LocalDate.of(2000, 12, 01),
                    Gender.WOMAN);
            em.persist(member);

            /**
             * 성별 상관 없음
             * 나이 범위: 20 ~ 25
             * 태그 생성 위치: (37.61901, 127.05971)
             */
            Tag tag = new Tag(
                    "testTagTitle2",
                    "testTagDetail2",
                    3,
                    Gender.NOMATTER,
                    25,
                    20,
                    37.6190,
                    127.0597,
                    member
            );
            em.persist(tag);
        }

        public void dbInit3() {
            /** 호스트 전용 Data */
            Member member = new Member("test3@email.com",
                    "pass03",
                    "nickNam3",
                    LocalDate.of(2000, 02, 02),
                    Gender.MAN);
            em.persist(member);

            /**
             * 남성만 모집
             * 나이 범위: 20 ~ 25
             * 태그 생성 위치: (37.6190, 127.0597)
             */
            Tag tag = new Tag(
                    "testTagTitle1",
                    "testTagDetail1",
                    1,
                    Gender.MAN,
                    25,
                    20,
                    37.6190,
                    127.0597,
                    member
            );
            em.persist(tag);
        }

        public void dbInit4() {
            Member member = new Member("test4@email.com",
                    "pass04",
                    "nickNam4",
                    LocalDate.of(2010, 10, 11),
                    Gender.MAN);
            em.persist(member);

            Tag tag = new Tag(
                    "testTagTitle2",
                    "testTagDetail2",
                    3,
                    Gender.NOMATTER,
                    25,
                    20,
                    37.61901,
                    127.05971,
                    member
            );
            em.persist(tag);
        }
    }

}
