package com.example.mongodb.spring.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "comment")
@Setter
@Getter
@CompoundIndex(def = "{userId:1, like:-1}")
public class Comment implements Serializable {
    @Id
    private String id; // for _id
    @Field("content")
    private String content;
    private Date publishDate;
    private String state;
    @Indexed
    private String userId;
    private String nickName;
    private String date;
    private Integer like;
    private Integer reply;
    private String articleId;
    private String parentId;
}
