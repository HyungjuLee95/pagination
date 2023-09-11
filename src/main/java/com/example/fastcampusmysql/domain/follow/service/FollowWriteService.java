package com.example.fastcampusmysql.domain.follow.service;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.repository.FollowRepository;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FollowWriteService {

    final private FollowRepository followRepository;
    public void create(MemberDto fromMember, MemberDto toMember) {
        /**
         * from, to 회정 벙보를 받아서 저장할 예정..
         * --> from to에 회원 정보를 받을건데, 식별자를 받을 것인지
         * from < - > to validate
         */
        // 받은 식별자를 통해서 이게 진짜 존재하는 검증을 여기서 진행을 해야한다.
        // 이 domain 영역은 mas로 간다고 했을 때 하나의 서버로 쪼게질 정도의 사이즈로 유지하는게 좋다.
        // 최대한 domain간의 결합도를 낮추는 것도 굉장히 좋은 것이다.

        Assert.isTrue(!(fromMember.id().equals(toMember.id())), "From, To 회원이 동일합니다" );

        var follow = Follow.builder()
                .fromMemberId(fromMember.id())
                .toMemberId(toMember.id())
                .build();
        followRepository.save(follow);

    }
}
