package api.service;

import api.domain.Chatroom;
import api.domain.Member;
import api.domain.Role;
import api.domain.Tag;
import api.repository.ChatroomRepository;
import api.repository.MemberRepository;
import api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatroomService {
    private final TagRepository tagRepository;
    private final ChatroomRepository chatroomRepository;
    private final MemberRepository memberRepository;


    /** 채팅방 만들기
     *
     * (Tag 는 Host 가 만들고, 채팅방은 Guest 가 만든다.)
     *
     *  1. { chatroom, tagId, memberId }
     *  2. tageId 와 memberId 로 Tag 정보와 Member 정보 가져온다.
     *  3. 전달 받은 chatroom 에 Tag와 Member 정보를 넣고, 연관 관계를 설정한다.
     *  4. chatroom 저장
     */
    @Transactional
    public Long createChatroom(Chatroom chatroom, Long tagId, Long memberId){
        Member guest = memberRepository.find(memberId);
        Tag tag = tagRepository.find(tagId);

        chatroom.setGuest(guest);
        chatroom.setTag(tag);

        return chatroomRepository.save(chatroom);
    }


    /** 참여 가능한 채팅방 조회
     *
     *  { memberId }
     *
     *  1. memberId로 Member 정보를 가져온다.
     *  2. Member 와 매핑된, Tag 조회 (Null 일 경우, 조회되지 않는다.)
     *  3. Tag 의 host 가 Member 일 경우, 모든 Chatroom 정보를 반환한다.
     *  4. host 가 아니라면, 연관된 Chatroom 정보를 조회하여
     *      guest 정보와 일치하는 Chatroom 정보를 반환한다.
     */
    public List<Chatroom> searchMyChatrooms(Long memberId){

        Member member = memberRepository.find(memberId);
//        Tag tag = member.getTag();

        // 1. 회원의 현재 역할이 GUEST 일 경우
        if(member.getRole()== Role.GUEST){
            Tag tag = member.getTag();
            return chatroomRepository.findGuestChatroom(tag.getId(), member.getId());
        }

        // 2. 회원의 현재 역할이 HOST 일 경우
        else if(member.getRole()== Role.HOST){
            Tag tag = member.getTag();
            return tag.getChatrooms();
        }

        // 3. 회원의 현재 역할이 HOST, GUEST 둘다 아닐 경우
        else
            return new ArrayList<>();

    }


}
