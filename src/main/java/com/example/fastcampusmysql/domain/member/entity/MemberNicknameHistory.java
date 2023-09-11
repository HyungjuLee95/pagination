package com.example.fastcampusmysql.domain.member.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class MemberNicknameHistory {
    final private Long id;
    final private LocalDateTime createdat;
    final private String nickname;
    final private Long memberId;

    @Builder
    public MemberNicknameHistory(Long id, LocalDateTime createdat, String nickname, Long memberId) {
        this.id = id;
        this.nickname = Objects.requireNonNull(nickname);
        this.memberId = Objects.requireNonNull(memberId);
//        this.memberId = memberId;
        this.createdat = createdat == null? LocalDateTime.now():createdat;

    }
}
