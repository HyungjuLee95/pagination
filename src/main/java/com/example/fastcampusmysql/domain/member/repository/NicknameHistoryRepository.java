package com.example.fastcampusmysql.domain.member.repository;

import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;
import lombok.RequiredArgsConstructor;
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
public class NicknameHistoryRepository {
    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    static final String TABLE = "MemberNicknameHistory";

    static final RowMapper<MemberNicknameHistory> rowMapper = (ResultSet resuleSet, int rowNum) ->MemberNicknameHistory.builder()
            .id(resuleSet.getLong("id"))
            .memberId(resuleSet.getLong("memberId"))
            .nickname(resuleSet.getString("nickname"))
            //getObject 지네릭 타입을 이용하여 우리가 추출하기 원하는 클래스 레퍼런스를 넣어주면 변환해주는 메서드
            .createdat(resuleSet.getObject("createdat", LocalDateTime.class))
            .build();

    public List<MemberNicknameHistory> findAllByMemberId(Long memberId){
        var sql = String.format("SELECT * FROM %s WHERE memberId = :memberId", TABLE);
        var params = new MapSqlParameterSource().addValue("memberId", memberId);
        return namedParameterJdbcTemplate.query(sql,params,rowMapper);
    }

    public MemberNicknameHistory save(MemberNicknameHistory history){
        /*
        Member id를 갱신 또는 삽일을 정함
        반환값은 id를 담아서 반환한다.

        */
        if(history.getId() == null){
            return insert(history);
        }
            throw new UnsupportedOperationException("MemberNicknameHistory는 갱신을 지원하지 않습니다.");
        }
//
//
    public MemberNicknameHistory insert(MemberNicknameHistory history){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");
                //한번 검색해서 공부하기
        SqlParameterSource params = new BeanPropertySqlParameterSource(history);
        var id =simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return MemberNicknameHistory
                .builder()
                .id(id)
                .memberId(history.getMemberId())
                .nickname(history.getNickname())
                .createdat(history.getCreatedat())
                .build();
    }
//

}
