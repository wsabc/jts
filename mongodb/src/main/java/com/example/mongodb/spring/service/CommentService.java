package com.example.mongodb.spring.service;

import com.example.mongodb.spring.domain.Comment;
import com.example.mongodb.spring.domain.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    public Optional<Comment> getComment(String id) {
        return commentRepository.findById(id);
    }

    public Page<Comment> getCommentsByParentId(String parentId, int page) {
        return commentRepository.findByParentId(parentId, PageRequest.of(page -1, 2));
    }

    public void addLike(String id) {
        Query query = Query.query(Criteria.where("articleId").is("1001").and("parentId").is(id));
        Update update = new Update();
        update.inc("like", 1);
        mongoTemplate.updateFirst(query, update, Comment.class);
    }

}
