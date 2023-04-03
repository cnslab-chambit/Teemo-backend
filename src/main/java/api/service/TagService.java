package api.service;

import api.domain.*;
import api.domain.dtos.CreateTagRequest;
import api.domain.dtos.FindTagsResponse;
import api.domain.dtos.SubscribeTagResponse;
import api.domain.dtos.SearchTagResponse;
import api.repository.ChatroomRepository;
import api.repository.MemberRepository;
import api.repository.TagRepository;
import api.util.DateTimeParse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 조회
@Slf4j
public class TagService {

    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;
    private final ChatroomRepository chatroomRepository;

    @Transactional
    public Long uploadTag(CreateTagRequest request){
        /** 태그 업로드
         *
         * (Tag 는 Host 가 만들고, 채팅방은 Guest 가 만든다.)
         *
         *  1. member_id로 host 정보를 찾는다.
         *  2. member와 host 에 서로의 정보를 저장
         */

        Member host = memberRepository.find(request.getHostId());
        Tag tag = new Tag(
                request.getTitle(),
                request.getDetail(),
                request.getMaxNum(),
                request.getTargetGender(),
                request.getTargetAgeUpper(),
                request.getTargetAgeLower(),
                request.getLatitude(),
                request.getLongitude(),
                host
        );

        return tagRepository.save(tag);
    }
    public List<FindTagsResponse> findTags(Long memberId, Double latitude, Double longitude){
        /** 내 근처 태그들 찾기
         *
         * 1. memberID로 조회자을 찾는다.
         * 2. 조회자의 성별, 나이를 찾는다.
         * 3. 나이, 위도, 경도 조건에 맞는 Tag 를 찾는다.
         * 4. 성별 조건을 검사(tag 의 성별조건이 조회자의 성별과 일치하거나, 'NOMATTER')하고, DTO 리스트로 변환한다.
         */
        // 1
        Member member = memberRepository.find(memberId);
        // 2
        Gender gender = member.getGender();
        int age = DateTimeParse.calculateAge(member.getBirthday());
        // 3
        List<Tag> findTags = tagRepository.findAll(latitude, longitude, age);
        // 4
        List<FindTagsResponse> result = new ArrayList<>();
        for (Tag tag : findTags)
            if (tag.getTargetGender() == gender || tag.getTargetGender() == Gender.NOMATTER)
                result.add(new FindTagsResponse(tag));
        return result;
    }
    public SearchTagResponse searchTag(Long tagId){
        /** 특정 Tag 정보 검색
         *
         * 1. tagId로 tag 을 찾는다.
         * 2. tag 의 host 를 검색 후 host 정보 추출.
         * 3. tag 를 DTO 로 변환 후 반환
         */
        Tag tag = tagRepository.find(tagId);
        Member host = tag.getHost();
        return new SearchTagResponse(tag,host);
    }

    @Transactional
    public SubscribeTagResponse subscribeTag(Long tagId, Long memberId){
        /** 특정 Tag 로 목적지 설정
         *
         * 1. tagId로 tag 을 찾는다.
         * 2. memberId로 조회자 정보를 가져온다.
         * 3. 조회자 멤버변수에 Tag 등록
         * 4. Tag의 멤버변수에 조회자 등록
         * 5. 조회자의 역할을 GUEST 로 바꾼다.
         * 6. Tag 의 위도 경도 반환
         */

        // 1
        Tag tag = tagRepository.find(tagId);
        // 2
        Member guest = memberRepository.find(memberId);
        // 3
        guest.setTag(tag);
        // 4
        tag.getGuests().add(guest);
        // 5
        guest.setRole(Role.GUEST);
        // 6
        return new SubscribeTagResponse(tag);
    }

    @Transactional
    public void unsubscribeTag(Long tagId, Long memberId){
        /** 목적지 설정된거 취소
         *
         * 1. tagId로 tag 을 찾는다.
         * 2. memberId로 Guest 정보를 찾는다.
         * 3. 조회자 멤버변수에 Tag 등록 해제
         * 4. Tag 멤버변수에 Guest 정보 삭제
         * 5. 조회자의 역할을 VIEWER 로 바꾼다.
         * 6. 만약 host 와의 Chatroom 이 있다면, 해당 Chatroom 을 삭제해준다.
         *      6-1. guest 의 Chatroom 정보를 가져온다.
         *      6-2. tag 를 통해 host 정보를 가져온다.
         *      6-3. host 의 HostChatroom List 에 접근하여 해당 Chatroom 정보를 제거한다.
         *      6-4. guest 의 GuestChatroom 에서 해당 Chatroom 정보를 제거한다.
         *      6-5. chatroom 에도 저장된 host, guest 매핑관계를 제거한다.
         *      6-6. chatroom 을 제거한다.
         */

        // 1
        Tag tag = tagRepository.find(tagId);
        // 2
        Member guest = memberRepository.find(memberId);
        // 3
        guest.setTag(null);
        // 4
        tag.getGuests().remove(guest);
        // 5
        guest.setRole(Role.VIEWER);
        // 6
        if(guest.getGuestChatroom() != null){
            // 6-1
            Chatroom chatroom = guest.getGuestChatroom();
            // 6-2
            Member host = tag.getHost();
            if (chatroom != null) {
                // 6-3
                host.getHostedChatrooms().remove(chatroom);
                // 6-4
                guest.setGuestChatroom(null);
                // 6-5
                chatroom.setHost(null);
                chatroom.setGuest(null);
                // 6-6
                chatroomRepository.delete(chatroom);
            }
        }
    }

    @Transactional
    public void deleteTag(Long tagId){
        /** 태그 내리기
         *
         * 1. tagId 로 tag 정보를 찾는다.
         * 2. tag 를 통해 host 정보를 찾는다.
         * 3. tag 를 통해 guests 정보를 찾는다.
         * 4. guests 의 멤버변수에서 tag 정보를 제거한다.
         * 5. host 의 멤버 변수에서 tag 정보를 제거한다.
         * 6. tag 를 제거한다.
         * 7. Host 의 역할을 HOST 에서 VIEWER 로 바꿔준다.
         */
        // 1
        Tag tag = tagRepository.find(tagId);
        // 2
        Member host = tag.getHost();
        // 3
        List<Member> guests = tag.getGuests();
        // 4
        for (Member guest : guests)
            if(guest != null && guest.getTag() == tag)
                guest.setTag(null);
        // 5
        if (host.getTag() == tag)
            host.setTag(null);
        // 6
        host.setRole(Role.VIEWER);
    }
    public boolean vaildateTag(Tag tag){
        /**
         * tag 유효성 검사
         *
         * tag 에 기록된 생성시간으로부터 남은시간이 0이면 false 반환
         */
        if(DateTimeParse.getRemainingInMinutes(tag.getCreateAt()) == 0){
            return false;
        }
        return true;
    }

}
