package com.example.fastcampusmysql.domain.member.service;

import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;
import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;
import com.example.fastcampusmysql.domain.member.repository.MemberRepository;
import com.example.fastcampusmysql.domain.member.repository.NicknameHistoryRepository;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberWriteService {
    final private MemberRepository memberRepository;
    final private NicknameHistoryRepository nicknameHistoryRepository;


    public Member register(RegisterMemberCommand command){
/*
    목표 : 회원정보(이메일, 닉네임, 생년월일)를 등록한다.
    parameter로 받을 것.
     memberRegisterCommand
     val member = Member.of(memberRegisterCommand)
     memberRepository.save(member)
    */
        var member= Member.builder()
                .nickname(command.nickname())
                .email(command.email())
                .birthday(command.birthday())
                .build();
       var savedMember =  memberRepository.save(member);
       return savedMember;
}
    public void changeNickname(Long id, String nickname){
            /*
            1. 회원의 이름을 변경
            2. 변경 내역을 저장한다.
            */
        var member = memberRepository.findById(id).orElseThrow();
        member.changeNickname(nickname);
        memberRepository.save(member);


        saveMemberNicknameHistory(member);
    }

    private void saveMemberNicknameHistory(Member member) {
        var history = MemberNicknameHistory.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();
        //to do : 변경 내역을 히스토리에 저장한다.
        nicknameHistoryRepository.save(history);
    }
}
