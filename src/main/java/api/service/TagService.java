package api.service;

import api.domain.Gender;
import api.domain.Member;
import api.domain.Tag;
import api.domain.dtos.FindTagsResponse;
import api.repository.ChatroomRepository;
import api.repository.MemberRepository;
import api.repository.TagRepository;
import api.util.AgeParse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 조회
public class TagService {

    private final TagRepository tagRepository;
    private final ChatroomRepository chatroomRepository;
    private final MemberRepository memberRepository;

    /** 태그 업로드
     *
     * (Tag 는 Host 가 만들고, 채팅방은 Guest 가 만든다.)
     *
     *  1. member_id로 host 정보를 찾는다.
     *  2. member와 host 에 서로의 정보를 저장
     */
    @Transactional
    public Long uploadTag(Long hostId, Tag tag){

        Member host = memberRepository.find(hostId);

        /**
         * host 가 있다면? setHost()
         */
        tag.setHost(host);

        return tagRepository.save(tag);
    }

    /** 내 근처 태그들 찾기
     *
     * 1. member_id로 회원을 찾는다.
     * 2. 회원의 성별, 나이를 조사한다.
     * 3. 나이, 위도, 경도 조건에 맞는 Tag 를 취한다.
     * 4. 성별 조건을 검사(tag 의 성별조건이 조회자의 성별과 일치하거나, 'NOMATTER')하고, DTO 리스트로 변환한다.
     */
    public List<FindTagsResponse> findTags(Long memberID, Double latitude, Double longitude){
        // 1
        Member member = memberRepository.find(memberID);
        // 2
        Gender gender = member.getGender();
        int age = AgeParse.calculateAge(member.getBirthday());
        // 3
        List<Tag> findTags = tagRepository.findAll(latitude, longitude, age);
        // 4
        List<FindTagsResponse> result = new ArrayList<>();
        for (Tag tag : findTags)
            if (tag.getTargetGender() == gender || tag.getTargetGender() == Gender.NOMATTER)
                result.add(new FindTagsResponse(tag));
        return result;
    }


}
