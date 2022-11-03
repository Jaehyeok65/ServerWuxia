package net.skhu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.entity.CommentRecommend;



public interface CommentRecommendRepository extends JpaRepository<CommentRecommend,Integer> {

	CommentRecommend findByUserEmailAndCommentId(String email, int id);


}