package com.example.fastcampusmysql.domain.follow.repository;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class FollowRepository {
    final private String Table = "follow";
    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    RowMapper<Follow> rowMapper = (ResultSet resuleSet, int rowNum) ->Follow.builder()
            .id(resuleSet.getLong("id"))
            .toMemberId(resuleSet.getLong("toMemberId"))
            .fromMemberId(resuleSet.getLong("fromMemberId"))
            .createdat(resuleSet.getObject("createdat", LocalDateTime.class))
            .build();

    public List<Follow> findAllByFromMemberId(Long fromMemberId){
        var sql = String.format( "SELECT * FROM %s WHERE fromMemberId = :fromMemberId", Table);
        var params = new MapSqlParameterSource().addValue("fromMemberId", fromMemberId);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }
    public Follow save(Follow follow){
        if(follow.getId()==null)
            return insert(follow);
            
            throw new UnsupportedOperationException("Folow는 갱신을 지원하지 않습니다");

    }
    private Follow insert(Follow follow) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(Table)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(follow);
        var id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Follow.builder()
                .id(id)
                .toMemberId(follow.getToMemberId())
                .fromMemberId(follow.getFromMemberId())
                .createdat(follow.getCreatedat())
                .build();

    }

    }




