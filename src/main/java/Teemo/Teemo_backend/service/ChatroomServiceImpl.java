package Teemo.Teemo_backend.service;

import Teemo.Teemo_backend.domain.*;
import Teemo.Teemo_backend.domain.dtos.ChatroomCreateRequest;
import Teemo.Teemo_backend.domain.dtos.ChatroomSearchResponse;
import Teemo.Teemo_backend.error.CustomInvalidValueException;
import Teemo.Teemo_backend.repository.ChatroomRepository;
import Teemo.Teemo_backend.repository.MemberRepository;
import Teemo.Teemo_backend.repository.TagRepository;
import Teemo.Teemo_backend.util.DateTimeParse;
import Teemo.Teemo_backend.validator.CommonValidator;
import Teemo.Teemo_backend.validator.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatroomServiceImpl implements ChatroomService{
    private final MemberValidator memberValidator;
    private final CommonValidator commonValidator;

    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;
    private final ChatroomRepository chatroomRepository;

    @Override
    @Transactional
    public ChatroomSearchResponse create(ChatroomCreateRequest request) {
        /**
         * [전제조건]
         * 1. 유효한 Tag 인지 확인
         *      1-1. Db에 저장되어 있나?
         * 2. 유효한 사용자인지 확인
         *      2-1. Db에 저장되어 있나?
         *      2-2. 사용자의 역할이 "GUEST" 여야 한다.
         *      2-3. 사용자에 이미 설정된 Tag 가 있어야 한다.
         * 3. 현재 위치가 tag 위치로부터 500m 이내 인지를 판단
         *      3-1. 위도 검사
         *      3-2. 경도 검사
         *
         * [과정]
         * 1. tagId로 Tag 정보를 가져온다.
         * 2. memberId로 사용자 정보를 가져온다.
         * 3. guest 의 이름으로 Chatroom 객체 생성 및 저장
         * 4. 불러온 태그 정보와 그것이 관리하는 host의 정보(닉네임, 나이, 성별)를 가져온다.
         * 5. DTO 로 변환 및 반환
         */
        Long tagId = request.getTagId();
        Long memberId = request.getMemberId();
        Double latitude = request.getLatitude();
        Double longitude = request.getLongitude();

        // [과정 1]
        Tag tag = tagRepository.findById(tagId);
        // [과정 2]
        Member guest = memberRepository.findById(memberId);

        // [전제조건 1-1]
        if(!commonValidator.found(tag))
            throw new CustomInvalidValueException("tagId","Tag 가 식별되지 않습니다.");
        // [전제조건 2-1]
        if(!commonValidator.found(guest))
            throw new CustomInvalidValueException("memberId","회원이 식별되지 않습니다.");
        // [전제조건 2-2]
        if(!memberValidator.checkRole(guest.getRole(), Role.GUEST))
            throw new CustomInvalidValueException("memberId","Chatroom 을 생성 할 수 있는 역할이 아닙니다.");
        // [전제조건 2-3]
        if(!commonValidator.found(guest.getTag())) // 없으면 안된다.
            throw new CustomInvalidValueException("memberId","Chatroom 을 생성 할 수 있는 상태가 아닙니다.");
        // [전제조건 3-1]
        if( Math.abs(tag.getLatitude() - latitude) > 0.0045 )
            throw new CustomInvalidValueException("latitude","Tag 의 위치로부터 500m 외각에 있습니다.");
        // [전제조건 3-2]
        if( Math.abs(tag.getLongitude() - longitude) > 0.006 )
            throw new CustomInvalidValueException("longitude","Tag 의 위치로부터 500m 외각에 있습니다.");

        // [과정 3]
        Chatroom chatroom = Chatroom.createChatroom(guest,tag);
        Long chatroomId = chatroomRepository.save(chatroom);

        // [과정 4]
        Member host = tag.getMembers().get(0);
        String nickname = host.getNickname();
        Integer age = DateTimeParse.calculateAge(host.getBirthday());
        Gender gender = host.getGender();
        String title = tag.getTitle();

        // [과정 5]
        ChatroomSearchResponse response
                = new ChatroomSearchResponse( chatroomId, nickname, age, gender, title);
        return response;
    }

    @Override
    public List<ChatroomSearchResponse> search(Long memberId, Long tagId) {
        /**
         * [전제조건]
         * 1. 유효한 Tag 인지 확인
         *      1-1. Db에 저장되어 있나?
         * 2. 유효한 사용자인지 확인
         *      2-1. Db에 저장되어 있나?
         *
         * [과정]
         * 1. tagId로 Tag 정보를 가져온다.
         * 2. 조회자 의 역할이 Host 인지, Guest 인지 판단한다.
         *  2-1. 조회자가 Guest 라면
         *      2-1-1. Tag 와 관련된 모든 Chatroom 정보를 가져온다.
         *      2-1-2. 모든 Chatroom 정보를 순회하며, Chatroom 을 만든 게스트 정보를 확인하고, 필요한 정보만 추려 DTO 로 변환.
         *  2-2. 조회자가 Host 라면
         *      2-2-1. Tag 의 저장된 Host 정보를 가져온다.
         *      2-2-2. 본인이 개설한 Chatroom 정보, Host 의 정보 중 필요한 정보만 추려 DTO 로 변환.
         *
         */
        List<ChatroomSearchResponse> responses = new ArrayList<>();

        // [과정 1]
        Tag tag = tagRepository.findById(tagId);
        // [과정 2]
        Member member = memberRepository.findById(memberId);

        // [전제조건 1-1]
        if(!commonValidator.found(tag))
            throw new CustomInvalidValueException("tagId","Tag 가 식별되지 않습니다.");
        // [전제조건 2-1]
        if(!commonValidator.found(member))
            throw new CustomInvalidValueException("memberId","회원이 식별되지 않습니다.");

        if(member.getRole() == Role.HOST){ // 조회자가 Host 라면
            List<Chatroom> chatrooms = tag.getChatrooms();
            for(Chatroom chatroom: chatrooms){
                Member guest = chatroom.getGuest();
                responses.add(new ChatroomSearchResponse(
                        chatroom.getId(), // 채팅방 ID
                        guest.getNickname(), // 게스트 닉네임
                        DateTimeParse.calculateAge(guest.getBirthday()), // 게스트 나이
                        guest.getGender(), // 게스트 성별
                        tag.getTitle() // Tag 제목
                        )
                );
            }
        } else if (member.getRole() == Role.GUEST) { // 조회자가 Guest 라면
            Member host = tag.getMembers().get(0);
            Chatroom chatroom = member.getChatroom();

            responses.add(new ChatroomSearchResponse(
                    chatroom.getId(), // 채팅방 ID
                    host.getNickname(), // 호스트 닉네임
                    DateTimeParse.calculateAge(host.getBirthday()), // 호스트 나이
                    host.getGender(), // 호스트 성별
                    tag.getTitle() // Tag 제목
                )
            );
        }
        else{} // 조회자가 Viewer 라면 반환 값 없음.
        return responses;
    }

    @Override
    public List<Chat> load(Long chatroomId) {
        /**
         * [전제조건]
         * 1. 유효한 채팅방 인지 확인
         */
        Chatroom chatroom = chatroomRepository.findById(chatroomId);
        if(commonValidator.found(chatroom))
            throw new CustomInvalidValueException("chatroomId","채팅방 이 식별되지 않습니다.");
        List<Chat> response = chatroom.getChats();
        return response;
    }

    @Override
    @Transactional
    public void remove(Long memberId,Long chatroomId) {
        /**
         * [전제조건]
         * 1. 유효한 Member 인지 확인
         *      1-1. Db에 저장되어 있나?
         *      1-2. 사용자의 역할이 "VIEWER" 이면 안된다.
         * 2. 유효한 사용자인지 확인
         *      2-1. Db에 저장되어 있나?
         *
         * [과정]
         * 1. memberId 로 Member 정보를 가져온다.
         * 2. chatroomId 로 Chatroom 정보를 가져온다.
         * 3. Chatroom 에 저장된 Guest 객체에 접근하여 Chatroom 과의 매핑을 끊는다.
         * 4. Chatroom 에 저장된 Tag 객체 접근하여 Chatroom 과의 매핑을 끊는다.
         * 5. Chatroom 을 db 상에서 삭제한다.
         *
         */
        //[과정 1]
        Member member = memberRepository.findById(memberId);
        //[과정 2]
        Chatroom chatroom = chatroomRepository.findById(chatroomId);

        if(commonValidator.found(member))
            throw new CustomInvalidValueException("memberId","회원이 식별되지 않습니다.");
        if(commonValidator.found(chatroom))
            throw new CustomInvalidValueException("chatroomId","채팅방 이 식별되지 않습니다.");

        //[과정 3]
        chatroom.removeGuest();
        //[과정 4]
        chatroom.unsetTag();
        //[과정 5]
        chatroomRepository.delete(chatroom);
    }
}
