package com.example.fastcampusmysql.domain.post.service;


import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.entitiy.Post;
import com.example.fastcampusmysql.domain.post.respository.PostRepsitory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostWriteService {
    final private PostRepsitory postRepsitory;

    public Long create(PostCommand command){
        var post = Post
                .builder()
                .memberId(command.memberId())
                .contents(command.contents())
                .build();
        System.out.println("Post : " + post.toString());
        return postRepsitory.save(post).getId();
    }
}
