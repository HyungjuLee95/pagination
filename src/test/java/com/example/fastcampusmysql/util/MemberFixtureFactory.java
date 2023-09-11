package com.example.fastcampusmysql.util;

import com.example.fastcampusmysql.domain.member.entity.Member;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class MemberFixtureFactory {

    static  public Member create(){
        var param = new EasyRandomParameters();
        return  new EasyRandom(param).nextObject(Member.class);
    }
    static public Member create(Long seed){

        var param = new EasyRandomParameters().seed(seed);
          return  new EasyRandom(param).nextObject(Member.class);
        //easyrandom은 시드 기반으로 동작함. 시드가 동일한 상태에서 nextobj로 호출을 한다면 계속 같은 객체만 반환이 되게 한다.
        //테스트를 할 때에 임의로 객체를 만들어주는 것이다! fixture 라이브러리
    }
}
