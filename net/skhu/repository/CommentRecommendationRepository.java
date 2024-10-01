package net.skhu.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.entity.CommentRecommendation;



public interface CommentRecommendationRepository extends JpaRepository<CommentRecommendation,Integer> {

	 CommentRecommendation findByUserIdAndWuxiaCommentWuxiaCommentId(Integer userId, Integer commentId);

	 CommentRecommendation findByUserIdAndReplyCommentReplyId(Integer userId, Integer replyId);
}