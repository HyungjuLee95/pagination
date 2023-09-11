package com.example.fastcampusmysql.application.Controller;


import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.dto.MemberNicknameHistoryDto;
import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;
import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.service.MemberReadService;
import com.example.fastcampusmysql.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController {
    final private MemberWriteService memberWriteService;
    final private MemberReadService memberReadService;

//    @PostMapping("/members")
//    public Member register(@RequestBody RegisterMemberCommand command) {
//        return memberWriteService.create(command);
//    }

    @PostMapping("/members")
    public MemberDto register(@RequestBody RegisterMemberCommand command) {
        var member =  memberWriteService.register(command);
        return memberReadService.toDto(member);
    }


//    @GetMapping("/members/{id}")
//    public Member getMember(@PathVariable Long id) {
//        return memberReadService.getMember(id);
        //여기서 고민해 볼만한것
        // Member라는 domain entity를 반환하고 있음. 지금은 우리가 jpa를 사용하고있지 않지만 jpa에서 컨트롤러까지 entity 객체가 나오게된다면
        // 이슈가 발생할 수 있음. 가장 깊은 곳에 있는 entity가 컨트롤러까지(프로젝트 레이어)까지 나가버리면 프로젠테이션 레이어의 요구 사항에 엔티티가
        // 변경이 되는 상황이 생길 수 있음. 예를 들어 Dto 내려줘야할 데이터가 추가되었다든가, 프로젝트 레이어에 내려줘야할 데이터가 추가되었다는가 등등 이러한 경우에 이슈가 발생할 수 있음.
//    }
        @GetMapping("/members/{id}")
         public MemberDto getMember(@PathVariable Long id) {
         return memberReadService.getMember(id);
        }

        @PostMapping ("/{id}/name")
    public MemberDto getMember(@PathVariable long id, @RequestBody String nickname){
        memberWriteService.changeNickname(id, nickname);
        return memberReadService.getMember(id);
        }
        @GetMapping("/{memberId}/history")
        public List<MemberNicknameHistoryDto> getNicknameHistories(@PathVariable Long memberId){
        return memberReadService.getNicknameHistories(memberId);
    }
}

