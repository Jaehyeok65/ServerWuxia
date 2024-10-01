package net.skhu.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "wuxiacomment")
public class WuxiaComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    public Integer wuxiaCommentId;

    @ManyToOne
    @JoinColumn(name = "wuxia_id", nullable = false)
    public Wuxia wuxia;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @Column(name = "content", nullable = false)
    public String ContentText;


    @Column(name = "created_at", nullable = false)
    public String createdAt;

    @Column(name = "recommendation_count", nullable = false)
    public int recommendationcount;

}
