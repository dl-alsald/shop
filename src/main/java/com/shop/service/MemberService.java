package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional //에러가 발생하다면 수행하기 이전 상태로 콜백 시켜줌
@RequiredArgsConstructor //final이나 @NonNull이 붙은 필드에 생성자를 생성
public class MemberService { //MemberService는 사용자 인증과 관련된 로직을 수행

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);

    }//saveMember

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }//validateDuplicateMember



}
