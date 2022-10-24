package net.skhu.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.entity.Likes;



public interface LikesRepository extends JpaRepository<Likes,Integer> {

	Likes findByUserEmailAndTitle(String userEmail, String title);

}
