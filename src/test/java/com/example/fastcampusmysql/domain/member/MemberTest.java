package com.example.fastcampusmysql.domain.member;

import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.util.MemberFixtureFactory;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;
import java.util.stream.LongStream;


public class MemberTest {
    @Test
    @DisplayName("회원은 닉네임을 변경할 수 있다.")
    public void testChangeName(){
       var member = MemberFixtureFactory.create();
       var expected = "pnu";

       member.changeNickname(expected);

       Assertions.assertEquals(expected, member.getNickname());
    }


    @Test
    @DisplayName("회원의 닉네임은 10자를 초과할 수 없다.")
    public void testNicknameMaxLength(){
        var member = MemberFixtureFactory.create();
        var overMaxLengthNmae = "pnupnipinininininininin";

       Assertions.assertThrows(IllegalArgumentException.class,()->member.changeNickname(overMaxLengthNmae));


    }

}
