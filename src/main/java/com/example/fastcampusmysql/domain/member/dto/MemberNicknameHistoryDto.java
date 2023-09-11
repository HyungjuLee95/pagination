package com.example.fastcampusmysql.domain.member.dto;

import java.time.LocalDateTime;

public record MemberNicknameHistoryDto(
        Long Id,
        Long memberId,
        String nickname,
        LocalDateTime createdat
) {
}
