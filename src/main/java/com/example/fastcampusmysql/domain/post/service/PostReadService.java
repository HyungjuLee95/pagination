package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.entitiy.Post;
import com.example.fastcampusmysql.domain.post.respository.PostRepsitory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReadService {
    //일자별 게시물 등록수를 반환하는 것
    final private PostRepsitory postRepsitory;

    public List<DailyPostCount> getDailyPostCounts(DailyPostCountRequest request){
        /*
        반환 값 -> 리스트[작성일자, 작성회원, 작성 게시물 갯수]
        select * from Post where memberId = : memberId and createdDate between firstDate and lastDate
        group by createdDate memberId
        */
    return postRepsitory.groupBycreatedDate(request);
    }

    public Page<Post> getPosts(Long memberId, Pageable pageRequest){
        return postRepsitory.findAllByMemberId(memberId, pageRequest);

    }


}
