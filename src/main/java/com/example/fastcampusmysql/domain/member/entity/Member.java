package com.example.fastcampusmysql.domain.member.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Member {
    final private Long id;
    private String nickname;
    final private LocalDate birthday;
    final private String email;
    final private LocalDateTime createdat;
    final private static Long NAME_MAX_LENGTH=10L;

    @Builder
    public Member(Long id, String nickname, LocalDate birthday, String email, LocalDateTime createdat) {
        this.id = id;
        this.nickname = Objects.requireNonNull(nickname);
        this.birthday = Objects.requireNonNull(birthday);
        validateNickName(nickname);

        this.email = Objects.requireNonNull(email);
        this.createdat = createdat == null? LocalDateTime.now() : createdat;
    }

    public void changeNickname(String to){
        Objects.requireNonNull(to);
        validateNickName(to);
        nickname=to;
    }
    //member라는 객체에 넣은 이유는 단일 테스트에 용이해지기에 이렇게 선택했다.


    private void validateNickName(String nickname){
        Assert.isTrue(nickname.length()<= NAME_MAX_LENGTH, "최대 길이를 초과했습니다.");
    }

}
