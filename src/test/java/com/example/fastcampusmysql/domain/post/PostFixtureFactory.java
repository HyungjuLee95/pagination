package com.example.fastcampusmysql.domain.post;

import com.example.fastcampusmysql.domain.post.entitiy.Post;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.time.LocalDate;

import static org.jeasy.random.FieldPredicates.*;

public class PostFixtureFactory {

    static public EasyRandom get(Long memberId, LocalDate firstDate, LocalDate lastDate){
        var memberPredicate = named("memberId").and(ofType(Long.class))
                .and(inClass(Post.class));

        var idPredicate = named("id").and(ofType(Long.class))
                .and(inClass(Post.class));
        //EasyRandom parameter를 new로 만들어내고 있다. 아니면 seed를 받아서 만들어 내고 있다.
        //나는 백만건을 만들 것이기 때문에 새로운 객체를 계속 만들어 내는게 부화가 더 많이 걸릴거임
        // EasyRandom 같은 경우, seed가 같은 경우, 항상 같은 객체를 반환하게 되는데, seed를 고정으로 갖는 easyRandom 객체가 필요하다.
        var param = new EasyRandomParameters()
                .excludeField(idPredicate)
                .dateRange(firstDate, lastDate)
                .randomize(memberPredicate, ()->memberId);
        return new EasyRandom(param);


    }
}
