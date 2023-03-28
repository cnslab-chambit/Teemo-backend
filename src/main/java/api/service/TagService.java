package api.service;

import api.domain.Member;
import api.domain.Tag;
import api.repository.ChatroomRepository;
import api.repository.MemberRepository;
import api.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        tag.setHost(host);

        return tagRepository.save(tag);
    }

}
