package net.skhu.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.entity.Comment;



public interface CommentRepository extends JpaRepository<Comment,Integer> {

	List<Comment> findAllByOrderByDateDesc();

	List<Comment> findAllByOrderByRecommendDesc();


}
