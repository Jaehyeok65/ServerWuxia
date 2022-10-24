package net.skhu.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import net.skhu.entity.Rate;



public interface RateRepository extends JpaRepository<Rate,Integer> {

	Rate findByUserEmailAndTitle(String userEmail, String title);

}
