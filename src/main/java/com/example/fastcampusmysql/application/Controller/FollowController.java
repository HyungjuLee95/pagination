package com.example.fastcampusmysql.application.Controller;


import com.example.fastcampusmysql.application.usecase.CreateFollowMemberUsecase;
import com.example.fastcampusmysql.application.usecase.GetFollowingMemberUsecase;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {
    final private CreateFollowMemberUsecase createFollowMemberUsecase;
    final private GetFollowingMemberUsecase getFollowingMemberUsecase;

    @PostMapping("/{fromId}/{toId}") // 식별자를 받는 것이니까 path로 받는게 자연스러움
    public void register(@PathVariable Long fromId, @PathVariable Long toId){
        createFollowMemberUsecase.execute(fromId, toId);
    }
    @GetMapping("/members/{fromId}") // 식별자를 받는 것이니까 path로 받는게 자연스러움
    public List<MemberDto> create(@PathVariable Long fromId){
        return getFollowingMemberUsecase.excute(fromId);
    }
}
