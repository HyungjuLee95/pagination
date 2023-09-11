package com.example.fastcampusmysql.domain.member.repository;

import com.example.fastcampusmysql.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    static final private String TABLE = "member";

    RowMapper<Member> rowMapper = (ResultSet resuleSet, int rowNum) ->Member.builder()
            .id(resuleSet.getLong("id"))
            .email(resuleSet.getString("email"))
            .nickname(resuleSet.getString("nickname"))
            .birthday(resuleSet.getObject("birthday", LocalDate.class))
            //getObject 지네릭 타입을 이용하여 우리가 추출하기 원하는 클래스 레퍼런스를 넣어주면 변환해주는 메서드
            .createdat(resuleSet.getObject("createdat", LocalDateTime.class))
            .build();
    public Optional<Member> findById(Long id){
    var sql = String.format("SELECT * FROM %s WHERE id = :id",TABLE);
    var param = new MapSqlParameterSource()
            .addValue("id", id);
        //BeanPropertyRowMapper를 사용하지 않은 이유 : 물론 이것을 사용한다면 mapping 로직을 지워줄 수 있다.
        // 사용하지 않은 이유는 setter를 모두 다 열어주어야하는데, 조금 고민이 되었다.
        // setter를 다 열어버리는 것은 이 데이터를 어디서든 변경이 가능해진다는 관점을 생각해 볼 수 있는데, 이렇게 되면 사이드 이펙트를 생각하기에 어려움이 있다.

       var member= namedParameterJdbcTemplate.queryForObject(sql, param, rowMapper);
        return Optional.ofNullable(member);
    }

    public List<Member> findAllByIdIn(List<Long> ids){
        if(ids.isEmpty())
            return List.of();
        var sql = String.format( "SELECT * FROM %s WHERE id in(:ids)", TABLE);
        var params = new MapSqlParameterSource().addValue("ids", ids);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
    public Member save(Member member){
        /*
        Member id를 갱신 또는 삽일을 정함
        반환값은 id를 담아서 반환한다.

        */
        if(member.getId() == null){
            return insert(member);
        }
            return update(member);
        }


    public Member insert(Member member){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
                //한번 검색해서 공부하기
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        var id =simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return Member.builder().id(id).email(member.getEmail()).nickname(member.getNickname()).birthday(member.getBirthday()).build();
    }
    public Member update(Member member){
        var sql = String.format("UPDATE %s set email = :email, nickname =:nickname, birthday=:birthday WHERE id = :id", TABLE);
        System.out.println("eeeeeeeeeeeemmmmmmmmail : "+member.getEmail());
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        namedParameterJdbcTemplate.update(sql,params);


    return member;
    }

}
