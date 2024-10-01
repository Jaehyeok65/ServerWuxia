package net.skhu.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.entity.Likes;



public interface LikesRepository extends JpaRepository<Likes,Integer> {

	Likes findByUserEmailAndTitle(String userEmail, String title);

	List<Likes> findAllByUserEmailOrderByTitle(String userEmail);

}
