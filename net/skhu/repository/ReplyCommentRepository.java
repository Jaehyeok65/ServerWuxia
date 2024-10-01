package net.skhu.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.entity.ReplyComment;



public interface ReplyCommentRepository extends JpaRepository<ReplyComment,Integer> {

	 List<ReplyComment> findByWuxiaComment_WuxiaCommentIdOrderByCreatedAtDesc(int commentId);
}
