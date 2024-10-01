package net.skhu.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.entity.WuxiaComment;


public interface WuxiaCommentRepository extends JpaRepository<WuxiaComment, Integer> {

	List<WuxiaComment> findByWuxiaIdOrderByCreatedAtDesc(Integer wuxiaId);

}
