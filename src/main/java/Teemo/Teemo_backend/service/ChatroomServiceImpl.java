package Teemo.Teemo_backend.service;


import Teemo.Teemo_backend.domain.*;
import Teemo.Teemo_backend.domain.dtos.ChatroomCreateRequest;
import Teemo.Teemo_backend.domain.dtos.ChatroomSearchResponse;
import Teemo.Teemo_backend.repository.ChatRepository;
import Teemo.Teemo_backend.repository.ChatroomRepository;
import Teemo.Teemo_backend.repository.MemberRepository;
import Teemo.Teemo_backend.repository.TagRepository;
import Teemo.Teemo_backend.util.DateTimeParse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatroomServiceImpl implements ChatroomService{
    private final TagRepository tagRepository;
    private final MemberRepository memberRepository;
    private final ChatroomRepository chatroomRepository;


    @Override
    @Transactional
    public ChatroomSearchResponse create(ChatroomCreateRequest request) {
        /**
         * [과정]
         * 1. tagId로 Tag 정보를 가져온다.
         * 2. memberId로 사용자 정보를 가져온다.
         *
         * [제약조건]
         * 1. 빈 값인지 확인
         * 2. 현재 위치가 tag 위치로부터 500m 이내 인지를 판단
         * 3. 게스트인지 확인
         *
         *
         */
        Long tagId = request.getTagId();
        Long memberId = request.getMemberId();
        Double latitude = request.getLatitude();
        Double longitude = request.getLongitude();

        Tag tag = tagRepository.findById(tagId);
        Member guest = memberRepository.findById(memberId);
        Chatroom chatroom = Chatroom.createChatroom(guest,tag);
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
        chatroom.removeTag();
        //[과정 5]
        chatroomRepository.delete(chatroom);
    }
}
