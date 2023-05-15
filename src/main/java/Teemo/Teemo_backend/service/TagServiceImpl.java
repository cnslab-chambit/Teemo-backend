package Teemo.Teemo_backend.service;

import Teemo.Teemo_backend.domain.Gender;
import Teemo.Teemo_backend.domain.Tag;
import Teemo.Teemo_backend.domain.Member;
import Teemo.Teemo_backend.domain.dtos.TagCreateRequest;
import Teemo.Teemo_backend.domain.dtos.TagFindResponse;
import Teemo.Teemo_backend.domain.dtos.TagSubscribeResponse;
import Teemo.Teemo_backend.repository.TagRepository;
import Teemo.Teemo_backend.repository.MemberRepository;
import Teemo.Teemo_backend.util.DateTimeParse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{
    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;
    @Override
    @Transactional
    public void upload(TagCreateRequest request) {
        /**
         * [제약조건]
         * 1. 빈 값인지 확인
         * 2. 유효한 사용자인지 확인
         *  2-1. 사용자의 역할이 "Viewer" 여야 한다.
         *  2-2. 사용자에 이미 설정된 Tag 가 없어야 한다.
         * 3. title 길이 조건 확인
         * 4. detail 길이 조건 확인
         * 5. maxNum 범위 조건 확인
         * 6. upperAge 범위 조건 확인
         * 7. lowerAge 범위 조건 확인
         * 8. latitude 범위 조건 확인
         * 9. longitude 범위 조건 확인
         */
        Long memberId = request.getMemberId();
        Double latitude = request.getLatitude();
        Double longitude = request.getLongitude();
        String title = request.getTitle();
        String detail = request.getDetail();
        Integer maxNum = request.getMaxNum();
        Gender targetGender = request.getTargetGender();
        Integer upperAge = request.getUpperAge();
        Integer lowerAge = request.getLowerAge();

        Member host = memberRepository.findById(memberId);

        Tag tag = new Tag(title,detail,maxNum,targetGender,upperAge,lowerAge,latitude,longitude,host);
        tagRepository.save(tag);


    }

    @Override
    public List<Tag> search(Long memberId, Double latitude, Double longitude) {
        /**
         * [과정]
         * 1. memberId 로 조회자을 찾는다.
         * 2. 조회자의 성별, 나이를 찾는다.
         * 3. 나이, 위도, 경도 조건에 맞는 Tag 를 찾는다.
         * 4. 성별 조건을 검사(Tag 의 성별조건이 조회자의 성별과 일치하거나, Tag 의 성별조건이 'N')
         */

        Member member = memberRepository.findById(memberId);
        Gender gender = member.getGender();
        Integer age = DateTimeParse.calculateAge(member.getBirthday());
        List<Tag> findTags = tagRepository.findByCriteria(latitude, longitude, age, gender);
        return findTags;
    }

    @Override
    public TagFindResponse find(Long tagId) {
        /**
         * [과정]
         * 1. tagId로 tag 을 찾는다.
         * 2. tag 의 host 를 검색 후 host 정보 추출.
         * 3. DTO 변환 이후 반환
         */

        // [과정 1]
        Tag tag = tagRepository.findById(tagId);

        // [과정 2]
        List<Member> members = tag.getMembers();
        Member host = members.get(0); // 호스트는 0번

        // [과정 3]
        Integer remainingMinutes = DateTimeParse.getRemainingInMinutes(tag.getDeletedAt());
        Integer hostAge = DateTimeParse.calculateAge(host.getBirthday());
        TagFindResponse response = new TagFindResponse(
                tag.getId(),
                tag.getTitle(),
                tag.getMaxNum(),
                tag.getTargetGender(),
                tag.getUpperAge(),
                tag.getLowerAge(),
                remainingMinutes,
                host.getNickname(),
                host.getGender(),
                hostAge
                );

        return response;
    }

    @Override
    @Transactional
    public TagSubscribeResponse subscribe(Long memberId, Long tagId) {
        /**
         * [과정]
         * 1. tagId로 Tag 정보를 가져온다.
         * 2. memberId로 사용자 정보를 가져온다.
         * 3. Tag 에 Guest 추가 & 연관된 사용자 정보에서 Tag 를 등록 & 연관된 사용자 정보에서 역할을 Guest 로 변경
         * 4. DTO 변환 이후 반환
         */

        // [과정 1]
        Tag tag = tagRepository.findById(tagId);
        // [과정 2]
        Member member = memberRepository.findById(memberId);
        // [과정 3]
        tag.addGuest(member);
        //[과정 4]
        TagSubscribeResponse response = new TagSubscribeResponse(tag.getId(), tag.getLatitude(), tag.getLongitude());
        return response;
    }

    @Override
    @Transactional
    public void unsubscribe(Long memberId, Long tagId) {
        /**
         * [과정]
         * 1. tagId로 Tag 정보를 가져온다.
         * 2. memberId로 사용자 정보를 가져온다.
         * 3. Tag 에 Guest 제거 & 연관된 사용자 정보에서 Tag 를 해제 & 연관된 사용자 정보에서 역할을 GUEST 로 변경
         * 4. 만약 host 와의 Chatroom 이 있다면, 해당 Chatroom 을 삭제해준다.
         */

        // [과정 1]
        Tag tag = tagRepository.findById(tagId);
        // [과정 2]
        Member member = memberRepository.findById(memberId);
        // [과정 3]
        tag.removeGuest(member);
        // [과정 4]
        // 추후에 구현
    }

    @Override
    @Transactional
    public void remove(Long memberId, Long tagId) {
        /**
         * [과정]
         * 1. tagId로 Tag 정보를 가져온다.
         * 2. memberId로 사용자 정보를 가져온다.
         * 3. Tag 에서 모든 사용자 정보 제거 & 연관된 사용자 정보에서 Tag 를 해제 & 연관된 사용자 정보에서 역할을 GUEST 로 변경
         * 4. tag 를 제거한다.
         *
         * [제약조건]
         * Tag 에 저장된 호스트 정보와 memberId 로 찾은 사용자의 정보가 일치하는 지 확인한다.
         */

        // [과정 1]
        Tag tag = tagRepository.findById(tagId);
        // [과정 2]
        Member member = memberRepository.findById(memberId);
        // [과정 3]
        tag.removeAllMembers();
        // [과정 4]
        tagRepository.delete(tag);
    }
}
