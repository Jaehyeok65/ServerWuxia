package net.skhu.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.entity.User;



public interface UserRepository extends JpaRepository<User,Integer> {

	User findByUserEmail(String userEmail);

	User findByUserNickname(String nickname);

}
