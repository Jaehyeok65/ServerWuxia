package net.skhu.controller;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.skhu.entity.User;
import net.skhu.repository.UserRepository;

@RestController
@RequestMapping("")
public class UserController {

	@Autowired UserRepository userRepository;

	private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>(); //중복 로그인 관리







	@ResponseBody
	@RequestMapping("login")
	public String login(HttpServletRequest request, @RequestBody User user, HttpServletResponse response) {
		User targetUser = userRepository.findByUserEmail(user.getUserEmail()); //userEmail로 찾음
		if(targetUser != null) { //null이 아니라면 로그인 비밀번호 체크
			if(!targetUser.getUserPassword().equals(user.getUserPassword())) { //비밀번호가 다르다면
				return "비밀번호가 다릅니다!"; //리턴
			}
			if(sessions.containsKey(user.getUserEmail())) {
				return "overlap"; //중복 로그인임을 알림
			}
			//중복 로그인이 아니며 비밀번호가 같으니 로그인 성공 == 닉네임 반환
			LoginSuccess(request, targetUser, response);
			return targetUser.getUserNickname();
		}
		HttpSession sessions = request.getSession();
		sessions.setMaxInactiveInterval(0);
		return "존재하지 않는 아이디입니다!";
	}

	@ResponseBody
	@RequestMapping("overlap")
	public String OverLap(HttpServletRequest request, @RequestBody User user, HttpServletResponse response) { //중복 로그인 처리
		sessions.get(user.getUserEmail()).invalidate(); //세션맵에서 세션을 가져온 후 세션 만료
		sessions.remove(user.getUserEmail());
		User targetUser = userRepository.findByUserEmail(user.getUserEmail()); //userEmail로 찾음
		LoginSuccess(request, targetUser, response);
		return targetUser.getUserNickname();
	}

	@ResponseBody
	@RequestMapping("sessioncheck")
	public String SessionCheck(HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = "login", required = false) Cookie cookie) {
		if(request.getSession(false) == null) { //세션이 null이라면 요청 만료
			if(cookie != null) { //쿠키가 null이 아니라면 == 쿠키가 있다면
				cookie.setMaxAge(0); //쿠키 만료
				response.addCookie(cookie); //응답에 담아서 보냄 == 쿠키 삭제
				return "true";
			}
		}
		else { //Session이 null이 아니라면 로그인 상태 갱신
			String userEmail = (String) request.getSession(false).getAttribute(request.getSession(false).getId());
			User targetUser = userRepository.findByUserEmail(userEmail); //userEmail로 찾음
			if(cookie == null) { //세션이 유지되어있는데도 쿠키가 없다면 쿠키를 만들어서 전달해줌
			Cookie cookies = new Cookie("login", "true"); //로그인 되었음을 알려주는 쿠키 생성
			cookies.setMaxAge(1800);
			response.addCookie(cookies); //응답에 담아서 보냄 == 쿠키 삭제
			}
			return targetUser.getUserNickname();
		}
		return "false";
	}

	@ResponseBody
	@RequestMapping("signup")
	public String SignUp(@RequestBody User user) {
		User targetUser = userRepository.findByUserEmail(user.getUserEmail()); //userEmail로 찾음
		User targetUser2 = userRepository.findByUserNickname(user.getUserNickname());
		if(targetUser != null) {
			return "이미 등록된 아이디입니다.";
		}
		else if(targetUser2 != null) {
			return "이미 등록된 닉네임입니다.";
		}
		else {
			userRepository.save(user);
			return "회원가입 완료";
		}
	}

	@ResponseBody
	@RequestMapping("emailcheck")
	public Boolean EmailCheck(@RequestBody User user) {
		User targetUser = userRepository.findByUserEmail(user.getUserEmail()); //userEmail로 찾음
		if(targetUser != null) {
			return false;
		}
		return true;
	}

	@ResponseBody
	@RequestMapping("nicknamecheck")
	public Boolean NicknameCheck(@RequestBody User user) {
		User targetUser = userRepository.findByUserNickname(user.getUserNickname()); //userEmail로 찾음
		if(targetUser != null) {
			return false;
		}
		return true;
	}

	@ResponseBody
	@RequestMapping("logout")
	public Boolean Logout(HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = "login", required = false) Cookie cookie, @CookieValue(value = "Admin", required = false) Cookie cookie2) { //로그인 쿠키를 가져오고
		if(request.getSession(false) != null) { //null이 아니라면 세션이 있다는 뜻이므로
			String userEmail = (String) request.getSession(false).getAttribute(request.getSession(false).getId());
			sessions.remove(userEmail);
			cookie.setMaxAge(0); //쿠키의 유효기간을 0으로 설정 후
			response.addCookie(cookie); //응답에 담아서 보냄 == 쿠키 삭제
			cookie2.setMaxAge(0);
			response.addCookie(cookie2);
			request.getSession(false).invalidate(); //로그인 세션 삭제
			return true; //로그아웃 성공
		}
		return false;
	}




	public static void LoginSuccess(HttpServletRequest request, User user, HttpServletResponse response) {
		HttpSession session = request.getSession(); //세션을 발급하고
		session.setAttribute(session.getId(), user.getUserEmail()); //세션에 값 설정
		sessions.put(user.getUserEmail(), session); //세션 맵에 추가
		Cookie cookie = new Cookie("login",String.valueOf(user.getId())); //로그인 되었음을 알려주는 쿠키 생성
		cookie.setMaxAge(1800);
		response.addCookie(cookie); //응답에 쿠키 추가
		if(user.isAdmin()) {
			Cookie cookie2 = new Cookie("Admin", "true");
			cookie2.setMaxAge(1800);
			response.addCookie(cookie2);
		}
	}

}
