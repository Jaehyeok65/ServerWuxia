package net.skhu.controller;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.skhu.entity.Likes;
import net.skhu.entity.Rate;
import net.skhu.entity.Wuxia;
import net.skhu.model.Pagination;
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
	@RequestMapping("page")
	public List<Wuxia> getPage(
	    @RequestParam int pg,
	    @RequestParam int sz,
	    @RequestParam(required = false) String title
	) {
	    Pagination pagination = new Pagination(pg, sz);
	    List<Wuxia> wuxiaList = new ArrayList<>();
	    if(title.equals("조회순")) {
	    	wuxiaList = wuxiaRepository.findAllByViewDesc(pagination);
	    }
	    else if(title.equals("좋아요순")){
	    	wuxiaList = wuxiaRepository.findAllByLikesDesc(pagination);
	    }
	    else if(title.equals("별점순")) {
	    	wuxiaList = wuxiaRepository.findAllByRateDesc(pagination);
	    }
	    return wuxiaList;
	}



	@ResponseBody
	@RequestMapping("rate")
	public Wuxia rateWuxia(@RequestBody Wuxia wuxia, HttpServletRequest request) {
		if(request.getSession(false) == null)  { //세션이 없다면 false리턴
			return new Wuxia();
		}

		String userEmail = (String) request.getSession(false).getAttribute(request.getSession(false).getId());
	    Wuxia wuxiaFromDb = wuxiaRepository.findByTitle(wuxia.getTitle());

	    if (wuxiaFromDb == null) {
	        return new Wuxia();
	    }

	    Rate prevRate = rateRepository.findByUserEmailAndTitle(userEmail, wuxia.getTitle());

	    if (prevRate != null) {
	        // 사용자가 이미 평가한 별점이 있는 경우
	        double newRate = ((wuxiaFromDb.getRate() * wuxiaFromDb.getPeople()) + wuxia.getRate() - prevRate.getRate()) / wuxiaFromDb.getPeople();
	        prevRate.setRate(wuxia.getRate());
	        rateRepository.save(prevRate);

	        // 작품의 별점 업데이트
	        wuxiaFromDb.setRate(newRate);
	    } else {
	        // 사용자가 처음으로 평가한 경우
	        double newRate = ((wuxiaFromDb.getRate() * wuxiaFromDb.getPeople()) + wuxia.getRate()) / (wuxiaFromDb.getPeople() + 1);
	        Rate newRateObject = new Rate(userEmail, wuxia.getTitle(), wuxia.getRate());
	        rateRepository.save(newRateObject);

	        // 작품의 별점 업데이트
	        wuxiaFromDb.setRate(newRate);
	        wuxiaFromDb.setPeople(wuxiaFromDb.getPeople() + 1);
	    }

	    // 작품 정보 저장
	    wuxiaRepository.save(wuxiaFromDb);

	    return wuxiaFromDb;
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
	@RequestMapping("search")
	public List<Wuxia> Search(@RequestBody Wuxia wuxia) {
		System.out.println(wuxia);
		List<Wuxia> wuxialist = wuxiaRepository.findAllByTitleContainingOrWriterContainingOrderByViewDesc(wuxia.getTitle(), wuxia.getTitle());
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
	public Wuxia View(@RequestBody Wuxia wuxia) {
		wuxia.setView(wuxia.getView() + 1);
		wuxiaRepository.save(wuxia);
		return wuxia;
	}


	@ResponseBody
	@RequestMapping("likes")
	public Wuxia Likes(@RequestBody Wuxia wuxia, HttpServletRequest request) {
	    if (request.getSession(false) == null) {
	        return new Wuxia(); // 혹은 다른 적절한 값으로 처리
	    }


	    // 세션에 저장된 userEmail 가져오기
	    String userEmail = (String) request.getSession(false).getAttribute(request.getSession(false).getId());

	    // userEmail과 제목(title)으로 좋아요 정보 조회
	    Likes existingLike = likesRepository.findByUserEmailAndTitle(userEmail, wuxia.getTitle());

	    // 이미 좋아요를 한 상태라면 좋아요 취소
	    if (existingLike != null) {
	        likesRepository.delete(existingLike); // 좋아요 정보 삭제
	        wuxia.setLikes(wuxia.getLikes() - 1); // 해당 제품의 좋아요 수 감소
	        wuxiaRepository.save(wuxia); // 제품 정보 저장
	        return wuxia; // 현재 좋아요 수 반환
	    }

	    // 좋아요를 하지 않은 상태라면 새로운 좋아요 추가
	    Likes newLike = new Likes();
	    newLike.setUserEmail(userEmail); // 사용자 이메일 설정
	    newLike.setTitle(wuxia.getTitle()); // 제목 설정
	    likesRepository.save(newLike); // 새로운 좋아요 정보 저장
	    wuxia.setLikes(wuxia.getLikes() + 1); // 해당 제품의 좋아요 수 증가
	    wuxiaRepository.save(wuxia); // 제품 정보 저장

	    return wuxia; // 현재 좋아요 수 반환
	}

	@ResponseBody
	@RequestMapping("mylike")
	public List<Wuxia> MyLikes(HttpServletRequest request) {
		if(request.getSession(false) == null)  { //세션이 없다면 false리턴
			return new ArrayList<>();
		}
		// 마이페이지에서 세션체크를 하고 페이지를 보여주므로 가능하며, 로그인된 세션에서 유저 이메일을 반환함
		String userEmail = (String) request.getSession(false).getAttribute(request.getSession(false).getId());
		List<Wuxia> list = new ArrayList<>();
		for(Likes like : likesRepository.findAllByUserEmailOrderByTitle(userEmail)) {
			Wuxia wuxia = wuxiaRepository.findByTitle(like.getTitle());
			list.add(wuxia);
		}
		return list;
	};

	@ResponseBody
	@RequestMapping("myrate")
	public List<Wuxia> MyRate(HttpServletRequest request) {
		if(request.getSession(false) == null)  { //세션이 없다면 false리턴
			return new ArrayList<>();
		} //세션이 있다는 뜻이므로 세션에 저장된 userEmail을 가져옴
		// 마이페이지에서 세션체크를 하고 페이지를 보여주므로 가능하며, 로그인된 세션에서 유저 이메일을 반환함
		String userEmail = (String) request.getSession(false).getAttribute(request.getSession(false).getId());
		List<Wuxia> list = new ArrayList<>();
		for(Rate rate : rateRepository.findAllByUserEmailOrderByTitle(userEmail)) {
			Wuxia wuxia = wuxiaRepository.findByTitle(rate.getTitle());
			list.add(wuxia);
		}
		return list;
	};

	@ResponseBody
	@RequestMapping("pagebyview")
	public List<Wuxia> PageByView(@RequestBody Pagination pagination) {
		//System.out.println(pagination.getPg());
		List<Wuxia> wuxialist = wuxiaRepository.findAllByViewDesc(pagination);
		return wuxialist;
	}

	@ResponseBody
	@RequestMapping("pagebyrate")
	public List<Wuxia> PageByRate(@RequestBody Pagination pagination) {
		List<Wuxia> wuxialist = wuxiaRepository.findAllByRateDesc(pagination);
		return wuxialist;
	}

	@ResponseBody
	@RequestMapping("pagebylikes")
	public List<Wuxia> PageByLikes(@RequestBody Pagination pagination) {
		List<Wuxia> wuxialist = wuxiaRepository.findAllByLikesDesc(pagination);
		return wuxialist;
	}

	@ResponseBody
    @RequestMapping("total")
    public int TotalByLikes(@RequestBody Pagination pagination) {
		List<Wuxia> wuxialist = wuxiaRepository.findAllByLikesDesc(pagination);
		System.out.println(pagination.getRecordCount());
        return pagination.getRecordCount();
    }

	@ResponseBody
    @RequestMapping("wuxiasave")
    public void WuxiaSave(@RequestBody Wuxia wuxia) {
		System.out.println(wuxia);
		//wuxiaRepository.save(wuxia);
        return;
    }











}
