package net.skhu.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "commentrecommend")
public class CommentRecommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String userEmail;
    int commentId;

}