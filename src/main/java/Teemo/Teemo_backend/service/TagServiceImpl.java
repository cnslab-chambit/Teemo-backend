package Teemo.Teemo_backend.service;

import Teemo.Teemo_backend.domain.*;
import Teemo.Teemo_backend.domain.dtos.TagCreateRequest;
import Teemo.Teemo_backend.domain.dtos.TagFindResponse;
import Teemo.Teemo_backend.domain.dtos.TagSubscribeResponse;
import Teemo.Teemo_backend.error.InvalidRangeException;
import Teemo.Teemo_backend.error.InvalidStateException;
import Teemo.Teemo_backend.repository.TagRepository;
import Teemo.Teemo_backend.repository.MemberRepository;
import Teemo.Teemo_backend.util.DateTimeParse;
import Teemo.Teemo_backend.validator.CommonValidator;
import Teemo.Teemo_backend.validator.MemberValidator;
import Teemo.Teemo_backend.validator.TagValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{
    private final TagValidator tagValidator;
    private final MemberValidator memberValidator;
    private final CommonValidator commonValidator;

    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;
    @Override
    @Transactional
    public void upload(TagCreateRequest request) {
        /**
         * [전제조건]
         * 1. 유효한 사용자인지 확인
         *  1-1. 사용자의 역할이 "VIEWER" 여야 한다.
         *  1-2. 사용자에 이미 설정된 Tag 가 없어야 한다.
         *
         *  [체크리스트]
         * 1. title 길이 조건 확인
         * 2. detail 길이 조건 확인
         * 3. maxNum 범위 조건 확인
         * 4. upperAge 범위 조건 확인
         * 5. lowerAge 범위 조건 확인
         * 6. latitude 범위 조건 확인
         * 7. longitude 범위 조건 확인
         * 8. targetGender 상태 확인
         */
        Long memberId = request.getMemberId();
        Member host = memberRepository.findById(memberId);
        Double latitude = request.getLatitude();
        Double longitude = request.getLongitude();
        String title = request.getTitle();
        String detail = request.getDetail();
        Integer maxNum = request.getMaxNum();
        Gender targetGender = request.getTargetGender();
        Integer upperAge = request.getUpperAge();
        Integer lowerAge = request.getLowerAge();

        // [전제조건 1]
        if(!commonValidator.found(host))
            throw new InvalidStateException("memberId","회원이 식별되지 않습니다.");
        // [전제조건 1-1]
        if(!memberValidator.checkRole(host.getRole(), Role.VIEWER))
            throw new InvalidStateException("memberId","Tag 를 게시 할 수 있는 역할이 아닙니다.");
        // [전제조건 1-2]
        if(commonValidator.found(host.getTag())) // 없어야 한다.
            throw new InvalidStateException("memberId","Tag 를 게시하거나 구독 중인 사용자입니다.");

        // [체크리스트 1]
        if(!tagValidator.checkTitleLength(title.length()))
            throw new InvalidRangeException("title","제목은 1자에서 15자 이내로 입력되어야 합니다.");
        // [체크리스트 2]
        if(!tagValidator.checkDetailLength(detail.length()))
            throw new InvalidRangeException("detail","상세내용은 최대 40자까지 입력할 수 있습니다.");
        // [체크리스트 3]
        if(!tagValidator.checkMaxNum(maxNum))
            throw new InvalidRangeException("maxNum","모집인원은 1에서 5 사이의 값이어야 합니다.");
        // [체크리스트 4]
        if(!tagValidator.checkUpperAge(upperAge,DateTimeParse.calculateAge(host.getBirthday())))
            throw new InvalidRangeException("title","모집 나이대의 상한은 자신의 나이 이상, 100 이하여야 합니다.");
        // [체크리스트 5]
        if(!tagValidator.checkLowerAge(lowerAge,DateTimeParse.calculateAge(host.getBirthday())))
            throw new InvalidRangeException("title","모집 나이대의 하한은 0 이상, 자신의 나이 이하여야 합니다.");
        // [체크리스트 6]
        if(!tagValidator.checkLatitude(latitude))
            throw new InvalidRangeException("latitude","위도는 북위 33.11 이상, 북위 38.61 이하여야 합니다.");
        // [체크리스트 7]
        if(!tagValidator.checkLongitude(longitude))
            throw new InvalidRangeException("longitude","경도는 동경 124.60 이상, 동경 131.87 이하여야 합니다.");
        // [체크리스트 8]
        if(!tagValidator.checkTargetGender(targetGender))
            throw new InvalidStateException("targetGender","모집성별이 적절하지 않습니다.");
        Tag tag = new Tag(title,detail,maxNum,targetGender,upperAge,lowerAge,latitude,longitude,host);
        tagRepository.save(tag);
    }

    @Override
    public List<Tag> search(Long memberId, Double latitude, Double longitude) {
        /**
         * [전제조건]
         * 1. 유효한 사용자인지 확인
         *  1-1. 사용자의 역할이 "VIEWER" 여야 한다.
         *  1-2. 사용자에 이미 설정된 Tag 가 없어야 한다.
         *
         *  [체크리스트]
         * 1. latitude 범위 조건 확인
         * 2. longitude 범위 조건 확인
         *
         * [과정]
         * 1. memberId 로 조회자을 찾는다.
         * 2. 조회자의 성별, 나이를 찾는다.
         * 3. 나이, 위도, 경도 조건에 맞는 Tag 를 찾는다.
         */

        // [체크리스트 1]
        if(!tagValidator.checkLatitude(latitude))
            throw new InvalidRangeException("latitude","위도는 북위 33.11 이상, 북위 38.61 이하여야 합니다.");
        // [체크리스트 2]
        if(!tagValidator.checkLongitude(longitude))
            throw new InvalidRangeException("longitude","경도는 동경 124.60 이상, 동경 131.87 이하여야 합니다.");

        // [과정 1]
        Member member = memberRepository.findById(memberId);

        // [전제조건 1]
        if(!commonValidator.found(member))
            throw new InvalidStateException("memberId","회원이 식별되지 않습니다.");
        // [전제조건 1-1]
        if(!memberValidator.checkRole(member.getRole(), Role.VIEWER))
            throw new InvalidStateException("memberId","Tag 를 게시 할 수 있는 역할이 아닙니다.");
        // [전제조건 1-2]
        if(commonValidator.found(member.getTag())) // 없어야 한다.
            throw new InvalidStateException("memberId","Tag 를 게시하거나 구독 중인 사용자입니다.");

        // [과정 2]
        Gender gender = member.getGender();
        Integer age = DateTimeParse.calculateAge(member.getBirthday());
        // [과정 3]
        List<Tag> findTags = tagRepository.findByCriteria(latitude, longitude, age, gender);
        return findTags;
    }

    @Override
    public TagFindResponse find(Long tagId) {
        /**
         * [전제조건]
         * 1. 유효한 Tag 인지 확인
         *      1-1. Db에 저장되어 있나?
         *
         * [과정]
         * 1. tagId로 tag 을 찾는다.
         * 2. tag 의 host 를 검색 후 host 정보 추출.
         * 3. DTO 변환 이후 반환
         */

        // [과정 1]
        Tag tag = tagRepository.findById(tagId);

        // [전제조건 1-1]
        if(!commonValidator.found(tag))
            throw new InvalidStateException("tagId","Tag 가 식별되지 않습니다.");

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
         * [전제조건]
         * 1. 유효한 Tag 인지 확인
         *      1-1. Db에 저장되어 있나?
         * 2. 유효한 사용자인지 확인
         *      2-1. Db에 저장되어 있나?
         *      2-2. 사용자의 역할이 "VIEWER" 여야 한다.
         *      2-3. 사용자에 이미 설정된 Tag 가 없어야 한다.
         *
         * [과정]
         * 1. tagId로 Tag 정보를 가져온다.
         * 2. memberId로 사용자 정보를 가져온다.
         * 3. Tag 에 Guest 추가 & 연관된 사용자 정보에서 Tag 를 등록 & 연관된 사용자 정보에서 역할을 Guest 로 변경
         * 4. DTO 변환 이후 반환
         *
         */
        // [과정 1]
        Tag tag = tagRepository.findById(tagId);
        // [과정 2]
        Member member = memberRepository.findById(memberId);

        // [전제조건 1-1]
        if(!commonValidator.found(tag))
            throw new InvalidStateException("tagId","Tag 가 식별되지 않습니다.");
        // [전제조건 2-1]
        if(!commonValidator.found(member))
            throw new InvalidStateException("memberId","회원이 식별되지 않습니다.");
        // [전제조건 2-2, 2-3]
        if(!memberValidator.checkRole(member.getRole(), Role.VIEWER))
            throw new InvalidStateException("memberId","Tag 를 구독 할 수 있는 역할이 아닙니다.");
        // [전제조건 2-3]
        if(commonValidator.found(member.getTag())) //  있으면 안되는 거다.
            throw new InvalidStateException("memberId","Tag 를 게시하거나 이미 구독 중인 사용자입니다.");

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
         * [전제조건]
         * 1. 유효한 Tag 인지 확인
         *      1-1. Db에 저장되어 있나?
         * 2. 유효한 사용자인지 확인
         *      2-1. Db에 저장되어 있나?
         *      2-2. 사용자의 역할이 "GUEST" 여야 한다.
         *      2-3. 사용자에 이미 설정된 Tag 가 있어야 한다.
         *
         * [과정]
         * 1. tagId로 Tag 정보를 가져온다.
         * 2. memberId로 사용자 정보를 가져온다.
         * 3. Tag 에 Guest 제거 & 연관된 사용자 정보에서 Tag 를 해제 & 연관된 사용자 정보에서 역할을 GUEST 로 변경
         * 4. 만약 Host 와의 채팅이 있다면, 해당 채팅방을 제거한다.
         *
         */

        // [과정 1]
        Tag tag = tagRepository.findById(tagId);
        // [과정 2]
        Member member = memberRepository.findById(memberId);

        // [전제조건 1-1]
        if(!commonValidator.found(tag))
            throw new InvalidStateException("tagId","Tag 가 식별되지 않습니다.");
        // [전제조건 2-1]
        if(!commonValidator.found(member))
            throw new InvalidStateException("memberId","회원이 식별되지 않습니다.");
        // [전제조건 2-2]
        if(!memberValidator.checkRole(member.getRole(), Role.GUEST))
            throw new InvalidStateException("memberId","Tag 를 구독 해제 할 수 있는 역할이 아닙니다.");
        // [전제조건 2-3]
        if(!commonValidator.found(member.getTag())) // 있어야한다.
            throw new InvalidStateException("memberId","구독 해제 할 Tag 가 없습니다.");


        // [과정 3]
        tag.removeGuest(member);
        // [과정 4]
        if(member.getChatroom() != null){
            tag.removeChatroom(member.getChatroom());
        }
    }

    @Override
    @Transactional
    public void remove(Long memberId, Long tagId) {
        /**
         * [전제조건]
         * 1. 유효한 Tag 인지 확인
         *      1-1. Db에 저장되어 있나?
         * 2. 유효한 사용자인지 확인
         *      2-1. Db에 저장되어 있나?
         *      2-2. 사용자의 역할이 "HOST" 여야 한다.
         *      2-3. 사용자에 이미 설정된 Tag 가 있어야 한다.
         *
         * [과정]
         * 1. tagId로 Tag 정보를 가져온다.
         * 2. memberId로 사용자 정보를 가져온다.
         * 3. Tag 에서 모든 사용자 정보 제거 & 연관된 사용자 정보에서 Tag 를 해제 & 연관된 사용자 정보에서 역할을 GUEST 로 변경
         * 4. Tag 에서 모든 채팅방 정보 제거 & 제거되는 채팅방과 연관된 사용자 정보에서 채팅방과의 매핑 해제
         * 5. tag 를 제거한다.
         *
         */

        // [과정 1]
        Tag tag = tagRepository.findById(tagId);
        // [과정 2]
        Member member = memberRepository.findById(memberId);

        // [전제조건 1-1]
        if(!commonValidator.found(tag))
            throw new InvalidStateException("tagId","Tag 가 식별되지 않습니다.");
        // [전제조건 2-1]
        if(!commonValidator.found(member))
            throw new InvalidStateException("memberId","회원이 식별되지 않습니다.");
        // [전제조건 2-2]
        if(!memberValidator.checkRole(member.getRole(), Role.HOST))
            throw new InvalidStateException("memberId","Tag 를 삭제 할 수 있는 역할이 아닙니다.");
        // [전제조건 2-3]
        if(!commonValidator.found(member.getTag())) // 있어야 한다.
            throw new InvalidStateException("memberId","삭제 할 Tag 가 없습니다.");

        // [과정 3]
        tag.removeAllMembers();
        // [과정 4]
        tag.removeAllChatrooms();
        // [과정 5]
        tagRepository.delete(tag);
    }
}
