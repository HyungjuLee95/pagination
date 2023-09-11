package com.example.fastcampusmysql.domain.follow.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
@Getter
public class Follow {

    //항상 보면 createdat이 중복으로 있는데 이것을 어떻게 중복 제거를 할 것인지 생각해보자.
final private Long id;
final private Long fromMemberId;
final private Long toMemberId;
final private LocalDateTime createdat;

//follow는 데이터 최신성이 보장되어야함. 즉, 닉네임의 최신성! 그리하여 정규화를 해보도록할 예정
// 데이터 중복을 제거하는 것이 유리함.
// 쉽게 방법을 생각해보자.
/*
1. JOIN을 사용하는 방법
-> FOLLOW에서 멤버를 join하게 된다면 follow Service에 member가 repository 레이어까지 침투를 하게 되며, 엄청난 강결합이 이루어진다.
-> 그래서 프로젝트 초기에서 강결합을 하는 것은 유연한 아키텍처가 되기에는 힘든 부분이 있다.
-> 그리고 결합이 강하기 때문에 아키텍처 적으로 성능도 문제를 풀기 어렵다. 리팩토링도 어려움이 있다.
2. 쿼리문을 한번 더 날리는 방법
3. 다른 테이블을 이용하는 방법

* */

    public Follow(Long id, Long fromMemberId, Long toMemberId, LocalDateTime createdat) {
        this.id = id;
        this.fromMemberId = Objects.requireNonNull(fromMemberId);
        this.toMemberId = Objects.requireNonNull(toMemberId);
        this.createdat = createdat == null ? LocalDateTime.now() : createdat;
    }
}
