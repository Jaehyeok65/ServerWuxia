package net.skhu.controller;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.skhu.entity.Likes;
import net.skhu.entity.Rate;
import net.skhu.entity.Wuxia;
import net.skhu.repository.LikesRepository;
import net.skhu.repository.RateRepository;
import net.skhu.repository.WuxiaRepository;

@RestController
@RequestMapping("")
public class HomeController {

	@Autowired WuxiaRepository wuxiaRepository;
	@Autowired RateRepository rateRepository;
	@Autowired LikesRepository likesRepository;

	@ResponseBody
	@RequestMapping("main")
	public List<List<Wuxia>> Main() { //중복 로그인 처리
		List<Wuxia> wuxialist1 = wuxiaRepository.findAllByOrderByViewDesc().subList(0, 12);
		List<Wuxia> wuxialist2 = wuxiaRepository.findAllByOrderByLikesDesc().subList(0, 12);
		List<Wuxia> wuxialist3 = wuxiaRepository.findAllByOrderByRateDesc().subList(0, 12);
		List<List<Wuxia>> list = new ArrayList<>();
		list.add(wuxialist1);
		list.add(wuxialist2);
		list.add(wuxialist3);
		return list;
	}

	@ResponseBody
	@RequestMapping("save")
	public String Save(@RequestBody Wuxia wuxia) { //중복 로그인 처리
		wuxiaRepository.save(wuxia);
		return "true";

	}

	@ResponseBody
	@RequestMapping("list")
	public List<Wuxia> List() {
		List<Wuxia> wuxialist = wuxiaRepository.findAll();
		return wuxialist;
	}

	@ResponseBody
	@RequestMapping("listbylikes")
	public List<Wuxia> ListByLikes() {
		List<Wuxia> wuxialist = wuxiaRepository.findAllByOrderByLikesDesc();
		return wuxialist;
	}

	@ResponseBody
	@RequestMapping("listbyview")
	public List<Wuxia> ListByView() {
		List<Wuxia> wuxialist = wuxiaRepository.findAllByOrderByViewDesc();
		return wuxialist;
	}

	@ResponseBody
	@RequestMapping("listbyrate")
	public List<Wuxia> ListByRate() {
		List<Wuxia> wuxialist = wuxiaRepository.findAllByOrderByRateDesc();
		return wuxialist;
	}


	@ResponseBody
	@RequestMapping("product")
	public Wuxia Product(@RequestBody Wuxia wuxia) {
		Wuxia wuxias = wuxiaRepository.findByTitle(wuxia.getTitle());
		return wuxias;
	}

	@ResponseBody
	@RequestMapping("view")
	public void View(@RequestBody Wuxia wuxia) {
		wuxiaRepository.save(wuxia);
		return;
	}

	@ResponseBody
	@RequestMapping("likes")
	public Boolean Likes(@RequestBody Wuxia wuxia, HttpServletRequest request) {
		if(request.getSession(false) == null)  { //세션이 없다면 false리턴
			return false;
		} //세션이 있다는 뜻이므로 세션에 저장된 userEmail을 가져옴
		String userEmail = (String) request.getSession(false).getAttribute(request.getSession(false).getId());
		if(likesRepository.findByUserEmailAndTitle(userEmail, wuxia.getTitle()) != null) {
			likesRepository.delete(likesRepository.findByUserEmailAndTitle(userEmail, wuxia.getTitle()));
			wuxia.setLikes(wuxia.getLikes()-1);
			wuxiaRepository.save(wuxia);
			return false;
		}
		Likes likes = new Likes();
		likes.setUserEmail(userEmail);
		likes.setTitle(wuxia.getTitle());
		likesRepository.save(likes);
		wuxia.setLikes(wuxia.getLikes()+1);
		wuxiaRepository.save(wuxia);
		return true;
	}

	@ResponseBody
	@RequestMapping("rate")
	public Boolean Rate(@RequestBody Wuxia wuxia, HttpServletRequest request) {
		if(request.getSession(false) == null)  { //세션이 없다면 false리턴
			return false;
		} //세션이 있다는 뜻이므로 세션에 저장된 userEmail을 가져옴
		String userEmail = (String) request.getSession(false).getAttribute(request.getSession(false).getId());
		if(rateRepository.findByUserEmailAndTitle(userEmail, wuxia.getTitle()) != null) {
			return false;
		}
		Rate rate = new Rate();
		rate.setUserEmail(userEmail);
		rate.setTitle(wuxia.getTitle());
		rateRepository.save(rate);
		wuxiaRepository.save(wuxia);
		return true;
	}






}
