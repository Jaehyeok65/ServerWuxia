package net.skhu.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.entity.Wuxia;
import net.skhu.model.Pagination;



public interface WuxiaRepository extends JpaRepository<Wuxia,Integer> {

	Wuxia findByTitle(String title);

	List<Wuxia> findAllByTitleStartsWith(String title);

	List<Wuxia> findAllByTitleContainingOrWriterContainingOrderByViewDesc(String title, String writer);

	List<Wuxia> findAllByTitleContainingOrderByViewDesc(String title);

	List<Wuxia> findAllByOrderByViewDesc();

	List<Wuxia> findAllByOrderByRateDesc();

	List<Wuxia> findAllByOrderByLikesDesc();


	public default List<Wuxia> findAllByViewDesc(Pagination pagination) {

        Page<Wuxia> page = this.findAll(PageRequest.of(pagination.getPg() - 1, pagination.getSz(),
        		Sort.by("view").descending().and(Sort.by("title").ascending())));
        pagination.setRecordCount((int)page.getTotalElements());
        return page.getContent();
    }

	public default List<Wuxia> findAllByRateDesc(Pagination pagination) {

        Page<Wuxia> page = this.findAll(PageRequest.of(pagination.getPg() - 1, pagination.getSz(),
        		Sort.by("rate").descending().and(Sort.by("title").ascending())));
        pagination.setRecordCount((int)page.getTotalElements());
        return page.getContent();
    }

	public default List<Wuxia> findAllByLikesDesc(Pagination pagination) {

        Page<Wuxia> page = this.findAll(PageRequest.of(pagination.getPg() - 1, pagination.getSz(),
        		Sort.by("likes").descending().and(Sort.by("title").ascending())));
        pagination.setRecordCount((int)page.getTotalElements());
        return page.getContent();
    }




}
