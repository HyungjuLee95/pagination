package com.example.fastcampusmysql.domain.member.dto;

import java.time.LocalDate;

public record RegisterMemberCommand(
        // record를 선언하게 되면 getter setter를 자동으로 만들어주며 property 형식으로 사용할 수 있다.
        // 사용은 get---을 사용하는 것이 아닌 .email .nickname으로 사용할 수 있다.
    String email,
    String id,
    String nickname,
    LocalDate birthday
){
    }
