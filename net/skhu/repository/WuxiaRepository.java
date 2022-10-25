package net.skhu.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.entity.Wuxia;



public interface WuxiaRepository extends JpaRepository<Wuxia,Integer> {

	Wuxia findByTitle(String title);

	List<Wuxia> findAllByTitleStartsWith(String title);

	List<Wuxia> findAllByTitleContaining(String title);

	List<Wuxia> findAllByTitleContainingOrderByViewDesc(String title);

	List<Wuxia> findAllByOrderByViewDesc();

	List<Wuxia> findAllByOrderByRateDesc();

	List<Wuxia> findAllByOrderByLikesDesc();

}
