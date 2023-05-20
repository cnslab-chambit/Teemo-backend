package Teemo.Teemo_backend.service;

import Teemo.Teemo_backend.domain.*;
import Teemo.Teemo_backend.domain.dtos.ChatroomCreateRequest;
import Teemo.Teemo_backend.domain.dtos.ChatroomSearchResponse;
import Teemo.Teemo_backend.error.InvalidRangeException;
import Teemo.Teemo_backend.error.InvalidStateException;
import Teemo.Teemo_backend.repository.ChatroomRepository;
import Teemo.Teemo_backend.repository.MemberRepository;
import Teemo.Teemo_backend.repository.TagRepository;
import Teemo.Teemo_backend.util.DateTimeParse;
import Teemo.Teemo_backend.validator.MemberValidator;
import Teemo.Teemo_backend.validator.TagValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatroomServiceImpl implements ChatroomService{
    private final TagValidator tagValidator;
    private final MemberValidator memberValidator;

    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;
    private final ChatroomRepository chatroomRepository;


    @Override
    @Transactional
    public ChatroomSearchResponse create(ChatroomCreateRequest request) {
        /**
         * [선제조건]
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
         *
         * [과정]
         * 1. tagId로 Tag 정보를 가져온다.
         * 2. memberId로 사용자 정보를 가져온다.
         *
         *
         *
         */
        Long tagId = request.getTagId();
        Long memberId = request.getMemberId();
        Double latitude = request.getLatitude();
        Double longitude = request.getLongitude();

        Tag tag = tagRepository.findById(tagId);
        Member member = memberRepository.findById(memberId);

        // [전제조건 1-1]
        if(!tagValidator.found(tag))
            throw new InvalidStateException("tagId","Tag 가 식별되지 않습니다.");
        // [전제조건 2-1]
        if(!memberValidator.found(member))
            throw new InvalidStateException("memberId","회원이 식별되지 않습니다.");
        // [전제조건 2-2]
        if(!memberValidator.checkRole(member.getRole(), Role.GUEST))
            throw new InvalidStateException("memberId","Chatroom 을 생성 할 수 있는 역할이 아닙니다.");
        // [전제조건 2-3]
        if(!memberValidator.found(member.getTag())) // 없으면 안된다.
            throw new InvalidStateException("memberId","Chatroom 을 생성 할 수 있는 상태가 아닙니다.");
        // [전제조건 3-1]
        if( Math.abs(tag.getLatitude() - latitude) > 0.0045 )
            throw new InvalidRangeException("latitude","Tag 의 위치로부터 500m 외각에 있습니다.");
        // [전제조건 3-2]
        if( Math.abs(tag.getLongitude() - longitude) > 0.006 )
            throw new InvalidRangeException("longitude","Tag 의 위치로부터 500m 외각에 있습니다.");




        Chatroom chatroom = Chatroom.createChatroom(member,tag);
        Long chatroomId = chatroomRepository.save(chatroom);

        Member host = tag.getMembers().get(0);
        String nickname = host.getNickname();
        Integer age = DateTimeParse.calculateAge(host.getBirthday());
        Gender gender = host.getGender();
        String title = tag.getTitle();

        ChatroomSearchResponse response
                = new ChatroomSearchResponse( chatroomId, nickname, age, gender, title);

        return response;
    }

    @Override
    public List<ChatroomSearchResponse> search(Long memberId, Long tagId) {
        /**
         * [과정]
         * 1. tagId로 Tag 정보를 가져온다.
         * 2. 조회자 의 역할이 Host 인지, Guest 인지 판단한다.
         *  2-1. 조회자가 Guest 라면
         *      2-1-1. Tag 와 관련된 모든 Chatroom 정보를 가져온다.
         *      2-1-2. 모든 Chatroom 정보를 순회하며, Chatroom 을 만든 게스트 정보를 확인하고, 필요한 정보만 추려 DTO 로 변환.
         *  2-2. 조회자가 Host 라면
         *      2-2-1. Tag 의 저장된 Host 정보를 가져온다.
         *      2-2-2. 본인이 개설한 Chatroom 정보, Host 의 정보 중 필요한 정보만 추려 DTO 로 변환.
         * [제약조건]
         *
         */
        List<ChatroomSearchResponse> responses = new ArrayList<>();

        Tag tag = tagRepository.findById(tagId);
        Member member = memberRepository.findById(memberId);
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

        } else {
          // VIEWER 라면 뭔가 크게 잘못됨. 추후 에러처리 ㄱㄱ.
        }

        return responses;
    }

    @Override
    public List<Chat> load(Long chatroomId) {
        /**
         * [제약조건]
         * 해당 채팅방이 있는지 확인.
         */
        Chatroom chatroom = chatroomRepository.findById(chatroomId);
        List<Chat> response = chatroom.getChats();
        return response;
    }

    @Override
    @Transactional
    public void remove(Long memberId,Long chatroomId) {
        /**
         * [과정]
         * 1. memberId 로 Member 정보를 가져온다.
         * 2. chatroomId 로 Chatroom 정보를 가져온다.
         * 3. Chatroom 에 저장된 Guest 객체에 접근하여 Chatroom 과의 매핑을 끊는다.
         * 4. Chatroom 에 저장된 Tag 객체 접근하여 Chatroom 과의 매핑을 끊는다.
         * 5. Chatroom 을 db 상에서 삭제한다.
         *
         * [제약조건]
         *
         *
         *
         */
        //[과정 1]
        Member member = memberRepository.findById(memberId);
        //[과정 2]
        Chatroom chatroom = chatroomRepository.findById(chatroomId);
        //[과정 3]
        chatroom.removeGuest();
        //[과정 4]
        chatroom.unsetTag();
        //[과정 5]
        chatroomRepository.delete(chatroom);
    }
}
