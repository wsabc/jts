package com.example.mongodb.spring;

import com.example.mongodb.spring.domain.Comment;
import com.example.mongodb.spring.service.CommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Test
    public void testInsert() {
        Comment comment = new Comment();
        comment.setArticleId("1001");
        comment.setUserId("100101");
        comment.setContent("good post");
        comment.setLike(100);
        comment.setNickName("ws");
        comment.setReply(5);
        comment.setParentId("1");
        commentService.saveComment(comment);
    }

    @Test
    public void testGet() {
        String id = "1";
        Optional<Comment> comment = commentService.getComment(id);
        if (comment.isPresent()) {
            System.out.println(comment);
        }
    }

    @Test
    public void testGetByParentId() {
        String id = "1";
        Page<Comment> comment = commentService.getCommentsByParentId(id, 1);
        System.out.println(comment.getTotalElements());
    }

    @Test
    public void testAddLike() {
        String id = "1";
        commentService.addLike(id);
    }
}
